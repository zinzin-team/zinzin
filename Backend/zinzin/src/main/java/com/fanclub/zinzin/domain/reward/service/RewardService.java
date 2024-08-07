package com.fanclub.zinzin.domain.reward.service;

import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import com.fanclub.zinzin.domain.member.repository.MemberInfoRepository;
import com.fanclub.zinzin.domain.reward.dto.SuccessCountResponse;
import com.fanclub.zinzin.domain.reward.repository.RewardRepository;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final MemberInfoRepository memberInfoRepository;

    public SuccessCountResponse getSuccessCount(Long memberId) {

        if (memberId == null) {
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        MemberInfo memberInfo = memberInfoRepository.findMemberInfoByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        return new SuccessCountResponse(memberInfo.getSuccessCount());
    }
}
