package com.fanclub.zinzin.domain.chatting.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chatting_content")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    private ObjectId id;
    private Long roomId;
    private String message;
    private Long memberId;

    public ChatMessage(Long roomId, Long memberId, String message) {
        this.roomId = roomId;
        this.message = message;
        this.memberId = memberId;
    }
}
