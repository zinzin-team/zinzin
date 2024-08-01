package com.fanclub.zinzin.domain.member.service;

import com.fanclub.zinzin.domain.member.dto.MemberRegisterDto;
import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import com.fanclub.zinzin.domain.member.repository.MemberInfoRepository;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.domain.member.repository.RandomNicknameRepository;
import com.fanclub.zinzin.domain.person.entity.Person;
import com.fanclub.zinzin.domain.person.repository.PersonRepository;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final PersonRepository personRepository;
    private final RandomNicknameRepository randomNicknameRepository;

    public void registerNewMember(MemberRegisterDto memberRegisterDto) {

        try {
            String randomNickname = randomNicknameRepository.getRandomNickname();
            memberRegisterDto.setNickname(randomNickname);

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
}
