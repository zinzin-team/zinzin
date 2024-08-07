package com.fanclub.zinzin.domain.chatting.repository;

import com.fanclub.zinzin.domain.chatting.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoom, Long> {
    }