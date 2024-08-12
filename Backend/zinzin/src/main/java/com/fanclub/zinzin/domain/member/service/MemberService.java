package com.fanclub.zinzin.domain.member.service;

import com.fanclub.zinzin.domain.card.entity.Card;
import com.fanclub.zinzin.domain.card.repository.CardRepository;
import com.fanclub.zinzin.domain.friend.entity.TempFriend;
import com.fanclub.zinzin.domain.friend.repository.TempFriendRepository;
import com.fanclub.zinzin.domain.member.dto.CheckSearchIdResponse;
import com.fanclub.zinzin.domain.member.dto.MatchingModeRequest;
import com.fanclub.zinzin.domain.member.dto.MemberInfoResponse;
import com.fanclub.zinzin.domain.member.dto.MemberInfoUpdateRequest;
import com.fanclub.zinzin.domain.member.dto.MemberRegisterDto;
import com.fanclub.zinzin.domain.member.entity.MatchingVisibility;
import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import com.fanclub.zinzin.domain.member.entity.RandomNickname;
import com.fanclub.zinzin.domain.member.repository.MemberInfoRepository;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.domain.member.repository.RandomNicknameRepository;
import com.fanclub.zinzin.domain.person.entity.Person;
import com.fanclub.zinzin.domain.person.repository.PersonRepository;
import com.fanclub.zinzin.global.error.code.CommonErrorCode;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import com.fanclub.zinzin.global.s3.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final PersonRepository personRepository;
    private final RandomNicknameRepository randomNicknameRepository;
    private final CardRepository cardRepository;
    private final TempFriendRepository tempFriendRepository;
    private final S3Service s3Service;

    @Value("${random-nickname.size}")
    private int randomNicknameSize;

    @Transactional
    public void registerNewMember(MemberRegisterDto memberRegisterDto) {

        try {
            Member member = memberRegisterDto.toMemberEntity();
            memberRepository.save(member);
            MemberInfo memberInfo = memberRegisterDto.toMemberInfoEntity(member, getRandomNickname().getNickname());
            memberInfoRepository.save(memberInfo);

            Person person = memberRegisterDto.toPersonEntity(member, memberInfo);
            personRepository.save(person);

            List<TempFriend> tempFriends = tempFriendRepository.findAllByMySub(memberRegisterDto.getSub());
            for (TempFriend tempFriend : tempFriends) {
                personRepository.saveKakaoFriends(tempFriend.getMySub(), tempFriend.getFriendSub(), tempFriend.getFriendName());
            }

            tempFriendRepository.deleteAll(tempFriends);
        } catch (Exception e) {
            throw new BaseException(MemberErrorCode.MEMBER_REGIST_FAILED);
        }
    }

    public CheckSearchIdResponse checkDuplicatedSearchId(String searchId) {
        if (searchId == null) {
            throw new BaseException(MemberErrorCode.INVALID_SEARCHID);
        }

        boolean isDuplicated = memberInfoRepository.existsBySearchId(searchId);
        return CheckSearchIdResponse.of(isDuplicated);
    }

    @Transactional
    public void changeMatchingMode(Long memberId, MatchingModeRequest matchingModeRequest) {
        if (memberId == null) {
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        boolean matchingMode = matchingModeRequest.isMatchingMode();
        MatchingVisibility matchingVisibility = matchingModeRequest.getMatchingVisibility();
        if (matchingMode && matchingVisibility == null) {
            throw new BaseException(CommonErrorCode.BAD_REQUEST);
        }

        personRepository.updateMatchingMode(memberId, matchingMode);

        if (matchingMode) {
            memberInfoRepository.updateMatchingModeAndVisibility(memberId, matchingMode, matchingVisibility);
            return;
        }
        memberInfoRepository.updateMatchingMode(memberId, matchingMode);
    }

    public RandomNickname getRandomNickname() {
        Random random = new Random();
        int randomId = random.nextInt(randomNicknameSize);

        return randomNicknameRepository.findById((long) randomId)
                .orElseThrow(() -> new BaseException(CommonErrorCode.INTERNAL_SERVER_ERROR));
    }

    public MemberInfoResponse getMemberInfo(HttpServletRequest request) {
        if (request.getAttribute("memberId") == null) {
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        MemberInfo memberInfo = memberInfoRepository.findMemberInfoByMemberId((Long) request.getAttribute("memberId"))
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));
        Card card = cardRepository.findCardByMemberId((Long) request.getAttribute("memberId")).orElse(null);
        return MemberInfoResponse.of(memberInfo, card);
    }

    @Transactional
    public void updateMemberInfo(Long memberId, MemberInfoUpdateRequest memberInfoUpdateRequest) {
        if (memberId == null) {
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        MemberInfo memberInfo = memberInfoRepository.findMemberInfoByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 요청으로 들어온 프로필 이미지가 있다면, 새로운 이미지를 업로드하고 URL을 얻는다.
        String imageURL = memberInfo.getProfileImage();
        String newImageURL = imageURL;

        if (memberInfoUpdateRequest.getProfileImage() != null) {
            newImageURL = s3Service.uploadProfile(memberInfoUpdateRequest.getProfileImage());
        }

        // DB를 업데이트한다.
        memberInfo.updateMemberInfo(newImageURL, memberInfoUpdateRequest.getSearchId());
        memberInfoRepository.save(memberInfo);
        personRepository.updateProfileImage(memberId, newImageURL);

        // 새로운 이미지를 저장했다면, 기존 프로필 이미지는 삭제한다.
        if (!newImageURL.equals(imageURL)) {
            s3Service.deleteS3(imageURL);
        }
    }

    @Transactional
    public void withdraw(Long memberId) {
        if (memberId == null) {
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.withdraw();
        personRepository.withdraw(member.getSub());
    }

    @Transactional
    public String updateRandomNickname(Long memberId) {
        if (memberId == null) {
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        MemberInfo memberInfo = memberInfoRepository.findMemberInfoByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        String randomNickname = getRandomNickname().getNickname();
        memberInfo.updateNickname(randomNickname);

        personRepository.updateNicknameByMemberId(memberId,randomNickname);

        return randomNickname;
    }
}
