package com.fanclub.zinzin.domain.chatting.entity;

import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_room_member")
@Getter
@NoArgsConstructor
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private MemberInfo memberInfo;

    @Getter
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean heartToggle;

    @Builder
    public ChatRoomMember(ChatRoom chatRoom, MemberInfo memberInfo) {
        this.chatRoom = chatRoom;
        this.memberInfo = memberInfo;
        this.heartToggle = false;
    }
}
