package com.fanclub.zinzin.domain.chatting.entity;

import com.fanclub.zinzin.domain.chatting.dto.CreateChatRoomDto;
import com.fanclub.zinzin.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomMember> members;

    @Builder(builderClassName = "createRoomBuilder", builderMethodName = "createRoomBuilder")
    public ChatRoom(ChatRoomType roomType) {
        this.roomType = roomType;
        this.status = ChatRoomStatus.ACTIVE;
    }

    public static ChatRoom createRoom(CreateChatRoomDto createChatRoomDto) {
        return createRoomBuilder()
                .roomType(createChatRoomDto.getRoomType())
                .build();
    }
}
