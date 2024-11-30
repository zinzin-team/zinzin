package com.fanclub.zinzin.domain.reward.service;

import com.fanclub.zinzin.domain.chatting.entity.ChatRoom;
import com.fanclub.zinzin.domain.chatting.repository.ChatRoomMemberRepository;
import com.fanclub.zinzin.domain.chatting.repository.ChatRoomRepository;
import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import com.fanclub.zinzin.domain.member.repository.MemberInfoRepository;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.domain.reward.dto.SuccessCountResponse;
import com.fanclub.zinzin.domain.reward.entity.Reward;
import com.fanclub.zinzin.domain.reward.repository.RewardRepository;
import com.fanclub.zinzin.global.error.code.ChatRoomErrorCode;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.code.RewardErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final MemberInfoRepository memberInfoRepository;
    private final MemberRepository memberRepository;
    private final RewardRepository rewardRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Transactional(readOnly = true)
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

        if (memberId.equals(targetId)) {
            throw new BaseException(RewardErrorCode.SELF_SELECTED);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        Member selectedMember = memberRepository.findById(targetId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        MemberInfo selectedMemberInfo = memberInfoRepository.findMemberInfoByMemberId(targetId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BaseException(ChatRoomErrorCode.CHAT_ROOM_NOT_FOUND));

        if (!chatRoomMemberRepository.existsByChatRoomIdAndMemberId(roomId, memberId)) {
            throw new BaseException(RewardErrorCode.NOT_MEMBER_OF_CHATROOM);
        }

        boolean rewardExistsForMemberInChatRoom = rewardRepository.existsByMemberIdAndChatRoomId(memberId, roomId);
        Long rewardCountForTargetAndChatRoom = rewardRepository.countBySelectedMemberIdAndChatRoomId(targetId, roomId);

        if (rewardExistsForMemberInChatRoom) {
            throw new BaseException(RewardErrorCode.ALREADY_SELECTED);
        } else {
            if (rewardCountForTargetAndChatRoom > 1) {
                throw new BaseException(RewardErrorCode.DUPLICATED);
            }
            Reward reward = Reward.toRewardEntity(chatRoom, member, selectedMember);
            rewardRepository.save(reward);
        }

        if (rewardCountForTargetAndChatRoom == 0) {
            selectedMemberInfo.increaseSuccessCount();
            memberInfoRepository.save(selectedMemberInfo);
        }
    }
}
