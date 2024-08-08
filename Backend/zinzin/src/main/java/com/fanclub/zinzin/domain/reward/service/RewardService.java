package com.fanclub.zinzin.domain.reward.service;

import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import com.fanclub.zinzin.domain.member.repository.MemberInfoRepository;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.domain.reward.dto.SuccessCountResponse;
import com.fanclub.zinzin.domain.reward.entity.Reward;
import com.fanclub.zinzin.domain.reward.repository.RewardRepository;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.code.RewardErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final MemberInfoRepository memberInfoRepository;
    private final MemberRepository memberRepository;
    private final RewardRepository rewardRepository;

    public SuccessCountResponse getSuccessCount(Long memberId) {

        if (memberId == null) {
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        MemberInfo memberInfo = memberInfoRepository.findMemberInfoByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        return new SuccessCountResponse(memberInfo.getSuccessCount());
    }

    @Transactional
    public void updateSuccessCount(Long memberId, Long targetId, Long roomId) {
        if (memberId == null) {
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        if (targetId == null) {
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        if (memberId == targetId) {
            throw new BaseException(RewardErrorCode.SELF_SELECTED);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        Member selectedMember = memberRepository.findById(targetId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        MemberInfo selectedMemberInfo = memberInfoRepository.findMemberInfoByMemberId(targetId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        boolean rewardExistsForMemberInChatRoom = rewardRepository.existsByMemberIdAndChatRoomId(memberId, roomId);

        if (rewardExistsForMemberInChatRoom) {
            throw new BaseException(RewardErrorCode.ALREADY_SELECTED);
        } else {
            Reward reward = Reward.toRewardEntity(roomId, member, selectedMember);
            rewardRepository.save(reward);
        }

        Long rewardCountForTargetAndChatRoom = rewardRepository.countBySelectedMemberIdAndChatRoomId(targetId, roomId);

        if (rewardCountForTargetAndChatRoom == 1) {
            selectedMemberInfo.increaseSuccessCount();
            memberInfoRepository.save(selectedMemberInfo);
        } else if (rewardCountForTargetAndChatRoom > 2) {
            throw new BaseException(RewardErrorCode.DUPLICATED);
        }
    }
}
