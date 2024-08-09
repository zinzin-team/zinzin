package com.fanclub.zinzin.domain.matchingstatus.service;

import com.fanclub.zinzin.domain.matchingstatus.dto.MatchingStatus;
import com.fanclub.zinzin.domain.matchingstatus.dto.MatchingStatusResponse;
import com.fanclub.zinzin.domain.person.repository.PersonRepository;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class MatchingStatusService {
    private final PersonRepository personRepository;

    public List<MatchingStatusResponse> getMatchingStatusList(HttpServletRequest request){
        if(request.getAttribute("memberId") == null){
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        Long memberId = (Long)request.getAttribute("memberId");
        List<MatchingStatus> results = personRepository.getMatchingStatusList(memberId);
        return results.stream()
                .map(MatchingStatusResponse::of)
                .toList();
    }
}
