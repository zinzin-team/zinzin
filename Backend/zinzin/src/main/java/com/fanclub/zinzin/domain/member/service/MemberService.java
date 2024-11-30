package com.fanclub.zinzin.domain.member.service;

import com.fanclub.zinzin.domain.card.entity.Card;
import com.fanclub.zinzin.domain.card.repository.CardRepository;
import com.fanclub.zinzin.domain.friend.entity.TempFriend;
import com.fanclub.zinzin.domain.friend.repository.TempFriendRepository;
import com.fanclub.zinzin.domain.member.dto.*;
import com.fanclub.zinzin.domain.member.entity.*;
import com.fanclub.zinzin.domain.member.repository.MemberInfoRepository;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.domain.member.repository.RandomNicknameRepository;
import com.fanclub.zinzin.domain.person.entity.Person;
import com.fanclub.zinzin.domain.person.repository.PersonRepository;
import com.fanclub.zinzin.global.auth.service.OAuth2Service;
import com.fanclub.zinzin.global.auth.dto.MemberAuthResponseDto;
import com.fanclub.zinzin.global.error.code.CommonErrorCode;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import com.fanclub.zinzin.global.s3.S3Service;
import com.fanclub.zinzin.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
    private final OAuth2Service oAuth2Service;
    private final JwtUtil jwtUtil;

    @Value("${random-nickname.size}")
    private int randomNicknameSize;

    @Transactional
    public MemberAuthResponseDto registerNewMember(HttpServletResponse response, MemberRegisterDto memberRegisterDto) {
        try {
            if(memberRegisterDto.getSub() == null) {
                throw new BaseException(MemberErrorCode.MEMBER_REGIST_FAILED);
            }
            Member member = memberRepository.findBySub(memberRegisterDto.getSub());
            MemberInfo memberInfo = null;

            if(member == null){
                member = memberRegisterDto.toMemberEntity();
                memberRepository.save(member);
                memberInfo = memberRegisterDto.toMemberInfoEntity(member, getRandomNickname().getNickname());
                memberInfoRepository.save(memberInfo);
            }
            else{
                member.updateDeletedMember(memberRegisterDto);
                memberInfo = memberInfoRepository.findMemberInfoByMemberId(member.getId())
                        .orElseThrow(() -> new BaseException(CommonErrorCode.BAD_REQUEST));
                memberInfo.updateDeletedMemberInfo(memberRegisterDto, getRandomNickname().getNickname());
            }

            Person person = memberRegisterDto.toPersonEntity(member, memberInfo);
            personRepository.save(person);

            List<TempFriend> tempFriends = tempFriendRepository.findAllByMySub(memberRegisterDto.getSub());
            for (TempFriend tempFriend : tempFriends) {
                personRepository.saveKakaoFriends(tempFriend.getMySub(), tempFriend.getFriendSub(), tempFriend.getFriendName());
            }

            tempFriendRepository.deleteAll(tempFriends);

            return generateRegisterTokens(response, member);
        } catch (Exception e) {
            throw new BaseException(MemberErrorCode.MEMBER_REGIST_FAILED);
        }
    }

    private MemberAuthResponseDto generateRegisterTokens(HttpServletResponse response, Member member) {
        if (member == null) {
            throw new BaseException (MemberErrorCode.MEMBER_REGIST_FAILED);
        }
        Long memberId = member.getId();
        Map<String, String> tokensMap = oAuth2Service.generateTokens(memberId, member.getSub(), Role.USER);
        String accessToken = tokensMap.get("accessToken");
        jwtUtil.addRefreshTokenToCookie(response, tokensMap.get("refreshToken"));

        return MemberAuthResponseDto.createTokenResponse(accessToken);
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
        personRepository.updateProfileImageAndSearchId(memberId, newImageURL, memberInfoUpdateRequest.getSearchId());

        // 새로운 이미지를 저장했다면, 기존 프로필 이미지는 삭제한다.
        if (!newImageURL.equals(imageURL) && !"default.jpg".equals(imageURL)) {
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
    }

    @Transactional
    public RandomNicknameResponse updateRandomNickname(Long memberId) {
        if (memberId == null) {
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        MemberInfo memberInfo = memberInfoRepository.findMemberInfoByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        String randomNickname = getRandomNickname().getNickname();
        memberInfo.updateNickname(randomNickname);

        personRepository.updateNicknameByMemberId(memberId,randomNickname);

        return new RandomNicknameResponse(randomNickname);
    }
}
