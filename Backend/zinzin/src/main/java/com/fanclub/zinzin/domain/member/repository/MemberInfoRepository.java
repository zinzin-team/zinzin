package com.fanclub.zinzin.domain.member.repository;

import com.fanclub.zinzin.domain.member.entity.MatchingVisibility;
import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
    @Query("SELECT mi FROM MemberInfo mi WHERE mi.member.id = :memberId")
    Optional<MemberInfo> findMemberInfoByMemberId(Long memberId);

    boolean existsBySearchId(String searchId);

    @Modifying
    @Query("update MemberInfo i set i.matchingMode = :matchingMode, i.matchingModeLog = CURRENT_TIMESTAMP where i.member.id = :memberId")
    void updateMatchingMode(@Param("memberId") Long memberId, @Param("matchingMode") boolean matchingMode);

    @Modifying
    @Query("update MemberInfo i set i.matchingMode = :matchingMode, i.matchingVisibility = :matchingVisibility, i.matchingModeLog = CURRENT_TIMESTAMP where i.member.id = :memberId")
    void updateMatchingModeAndVisibility(@Param("memberId") Long memberId, @Param("matchingMode") boolean matchingMode, @Param("matchingVisibility") MatchingVisibility matchingVisibility);
}
