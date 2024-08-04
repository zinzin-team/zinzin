package com.fanclub.zinzin.domain.chatting.entity;

import com.fanclub.zinzin.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Table(name = "chat_room")
@NoArgsConstructor
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatRoomType roomType;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatRoomStatus status;

    @Column(name = "last_message_date")
    private LocalDate lastMessageDate;

    @Column(name = "last_message_id")
    private String lastMessageId;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomMember> members;

    @Builder
    public ChatRoom(ChatRoomType roomType) {
        this.roomType = roomType;
        this.status = ChatRoomStatus.ACTIVE;
    }

    public void updateLastMessage(String lastMessageId, LocalDate lastMessageDate) {
        this.lastMessageId = lastMessageId;
        this.lastMessageDate = lastMessageDate;
    }
}
