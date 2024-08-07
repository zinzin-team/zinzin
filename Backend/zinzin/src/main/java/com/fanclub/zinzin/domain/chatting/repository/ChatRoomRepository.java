package com.fanclub.zinzin.domain.chatting.repository;

import com.fanclub.zinzin.domain.chatting.entity.ChatRoom;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT cr FROM ChatRoom cr JOIN cr.members m WHERE m.memberInfo.member.id = :memberId AND cr.status = :status ORDER BY cr.lastMessageDateTime DESC")
    List<ChatRoom> findAllByMemberIdAndStatusOrderByLastMessageDateDesc(Long memberId, ChatRoomStatus status);
}