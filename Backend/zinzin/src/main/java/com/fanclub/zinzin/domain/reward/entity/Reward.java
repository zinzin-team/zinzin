package com.fanclub.zinzin.domain.reward.entity;

import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reward")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reward extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_room_id", nullable = false)
    private Long chatRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_member_id", referencedColumnName = "id")
    private Member selectedMember;

    @Builder
    public Reward(Long chatRoomId, Member member, Member selectedMember) {
        this.chatRoomId = chatRoomId;
        this.member = member;
        this.selectedMember = selectedMember;
    }

    public static Reward toRewardEntity(Long chatRoomId, Member member, Member selectedMember) {
        return Reward.builder()
                .chatRoomId(chatRoomId)
                .member(member)
                .selectedMember(selectedMember)
                .build();
    }
}
