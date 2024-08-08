package com.fanclub.zinzin.domain.reward.repository;

import com.fanclub.zinzin.domain.reward.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    boolean existsByMemberIdAndChatRoomId(Long MemberId, Long chatRoomId);

    @Query("SELECT COUNT(r) FROM Reward r WHERE r.selectedMember.id = :selectedMemberId AND r.chatRoomId = :chatRoomId")
    Long countBySelectedMemberIdAndChatRoomId(@Param("selectedMemberId") Long selectedMemberId,
                                              @Param("chatRoomId") Long chatRoomId);
}
