package com.fanclub.zinzin.domain.chatting.repository;

import com.fanclub.zinzin.domain.chatting.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
     List<ChatMessage> findAllByRoomId(Long roomId);
}
