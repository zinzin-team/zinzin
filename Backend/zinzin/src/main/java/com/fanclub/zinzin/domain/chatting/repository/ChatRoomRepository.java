package com.fanclub.zinzin.domain.chatting.repository;

import com.fanclub.zinzin.domain.chatting.entity.ChatRoom;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT cr FROM ChatRoom cr JOIN cr.members m WHERE m.member.id = :memberId AND cr.status = :status")
    List<ChatRoom> findAllByMemberIdAndStatus(Long memberId, ChatRoomStatus status);

    @Query("SELECT r FROM ChatRoom r " +
            "JOIN r.members m1 " +
            "JOIN r.members m2 " +
            "WHERE m1.member.id = :myMemberId " +
            "AND m2.member.id = :otherMemberId " +
            "AND r.status = :status ")
    List<ChatRoom> findChatRoomByMemberIds(@Param("myMemberId") Long myMemberId,
                                               @Param("otherMemberId") Long otherMemberId,
                                               @Param("status") ChatRoomStatus status);

}

