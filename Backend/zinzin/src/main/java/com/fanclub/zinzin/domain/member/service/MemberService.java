package com.fanclub.zinzin.domain.member.service;

import com.fanclub.zinzin.domain.member.dto.MemberRegisterDto;
import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import com.fanclub.zinzin.domain.member.repository.MemberInfoRepository;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;

    public Member registerNewMember(MemberRegisterDto memberRegisterDto) {
        Member member = Member.builder()
                .email(memberRegisterDto.getEmail())
                .name(memberRegisterDto.getName())
                .sub(memberRegisterDto.getSub())
                .birth(memberRegisterDto.getBirth())
                .gender(memberRegisterDto.getGender())
                .role(memberRegisterDto.getRole())
                .status(memberRegisterDto.getStatus())
                .build();
        memberRepository.save(member);

        MemberInfo memberInfo = MemberInfo.builder()
                .profileImage(memberRegisterDto.getProfileImage())
                .nickname(memberRegisterDto.getNickname())
                .searchId(memberRegisterDto.getSearchId())
                .matchingVisibility(memberRegisterDto.getMatchingVisibility())
                .matchingMode(memberRegisterDto.isMatchingMode())
                .member(member)
                .build();
        memberInfoRepository.save(memberInfo);

        return member;
    }
}
