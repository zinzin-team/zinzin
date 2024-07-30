package com.fanclub.zinzin.domain.member.service;

import com.fanclub.zinzin.domain.member.dto.MemberRegisterDto;
import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import com.fanclub.zinzin.domain.member.repository.MemberInfoRepository;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;

    public Member registerNewMember(MemberRegisterDto memberRegisterDto) {

        try {
            Member member = memberRegisterDto.toMemberEntity();
            memberRepository.save(member);
            MemberInfo memberInfo = memberRegisterDto.toMemberInfoEntity(member);
            memberInfoRepository.save(memberInfo);
            return member;
        } catch (Exception e) {
            throw new BaseException(MemberErrorCode.MEMBER_REGIST_FAILED);
        }
    }
}
