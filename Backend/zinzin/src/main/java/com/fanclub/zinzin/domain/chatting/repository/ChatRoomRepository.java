package com.fanclub.zinzin.domain.chatting.repository;

import com.fanclub.zinzin.domain.chatting.entity.ChatRoom;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT cr FROM ChatRoom cr JOIN cr.members m WHERE m.memberInfo.member.id = :memberId AND cr.status = :status")
    List<ChatRoom> findAllByMemberIdAndStatus(Long memberId, ChatRoomStatus status);

    @Query("SELECT r FROM ChatRoom r " +
            "JOIN r.members m1 " +
            "JOIN r.members m2 " +
            "WHERE m1.memberInfo.member.id = :myMemberId " +
            "AND m2.memberInfo.member.id = :otherMemberId " +
            "AND r.status = :status " +
            "AND (SELECT COUNT(m) FROM ChatRoomMember m WHERE m.chatRoom.id = r.id) = 2")
    Optional<ChatRoom> findChatRoomByMemberIds(@Param("myMemberId") Long myMemberId,
                                               @Param("otherMemberId") Long otherMemberId,
                                               @Param("status") ChatRoomStatus status);

}

