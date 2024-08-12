package com.fanclub.zinzin.domain.chatting.repository;

import com.fanclub.zinzin.domain.chatting.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ChatRoomMember c WHERE c.chatRoom.id = :chatRoomId AND c.member.id = :memberId")
    boolean existsByChatRoomIdAndMemberId(@Param("chatRoomId") Long chatRoomId, @Param("memberId") Long memberId);

    @Query("SELECT c FROM ChatRoomMember c WHERE c.chatRoom.id = :chatRoomId AND c.id = :memberId")
    Optional<ChatRoomMember> findByChatRoomIdAndMemberId(@Param("chatRoomId") Long chatRoomId, @Param("memberId") Long memberId);
}