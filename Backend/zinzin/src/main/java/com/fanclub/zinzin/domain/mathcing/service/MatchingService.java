package com.fanclub.zinzin.domain.mathcing.service;

import com.fanclub.zinzin.domain.mathcing.dto.MatchingPartner;
import com.fanclub.zinzin.domain.mathcing.dto.MatchingResponse;
import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.domain.person.repository.PersonRepository;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final PersonRepository personRepository;
    private final MemberRepository memberRepository;

    public MatchingResponse getMatchings(HttpServletRequest request) {
        if(request.getAttribute("memberId") == null){
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        Long memberId = (Long) request.getAttribute("memberId");
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        List<MatchingPartner> list = personRepository.getMatchingPartner(memberId);
        return MatchingResponse.of(list);
    }
}
