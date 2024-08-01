package com.fanclub.zinzin.domain.member.service;

import com.fanclub.zinzin.domain.member.dto.MatchingModeRequest;
import com.fanclub.zinzin.domain.member.dto.MemberRegisterDto;
import com.fanclub.zinzin.domain.member.entity.MatchingVisibility;
import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import com.fanclub.zinzin.domain.member.repository.MemberInfoRepository;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.domain.person.entity.Person;
import com.fanclub.zinzin.domain.person.repository.PersonRepository;
import com.fanclub.zinzin.global.error.code.CommonErrorCode;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final PersonRepository personRepository;

    public void registerNewMember(MemberRegisterDto memberRegisterDto) {

        try {
            Member member = memberRegisterDto.toMemberEntity();
            memberRepository.save(member);
            MemberInfo memberInfo = memberRegisterDto.toMemberInfoEntity(member);
            memberInfoRepository.save(memberInfo);

            Person person = memberRegisterDto.toPersonEntity(member, memberInfo);
            personRepository.save(person);
        } catch (Exception e) {
            throw new BaseException(MemberErrorCode.MEMBER_REGIST_FAILED);
        }
    }

    @Transactional
    public void changeMatchingMode(Long memberId, MatchingModeRequest matchingModeRequest){
        if(memberId == null){
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        boolean matchingMode = matchingModeRequest.isMatchingMode();
        MatchingVisibility matchingVisibility = matchingModeRequest.getMatchingVisibility();
        if(matchingMode && matchingVisibility == null){
            throw new BaseException(CommonErrorCode.BAD_REQUEST);
        }

        if(matchingMode){
            memberInfoRepository.updateMatchingModeAndVisibility(memberId, matchingMode, matchingVisibility);
            return;
        }

        memberInfoRepository.updateMatchingMode(memberId, matchingMode);
    }
}
