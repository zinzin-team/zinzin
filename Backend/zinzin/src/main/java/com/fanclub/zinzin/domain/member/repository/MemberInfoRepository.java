package com.fanclub.zinzin.domain.member.repository;

import com.fanclub.zinzin.domain.member.entity.MatchingVisibility;
import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import com.fanclub.zinzin.domain.searcher.dto.SearchedMemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
    Optional<MemberInfo> findMemberInfoByMemberId(Long memberId);

    @Query("SELECT new com.fanclub.zinzin.domain.searcher.dto.SearchedMemberDto(m.id, m.name, mi.profileImage) " +
            "FROM MemberInfo mi " +
            "JOIN mi.member m " +
            "WHERE mi.searchId = :searchId")
    Optional<SearchedMemberDto> findSearcherResponseBySearchId(@Param("searchId") String searchId);

    boolean existsBySearchId(String searchId);

    @Modifying
    @Query("update MemberInfo i set i.matchingMode = :matchingMode, i.matchingModeLog = CURRENT_TIMESTAMP where i.member.id = :memberId")
    void updateMatchingMode(@Param("memberId") Long memberId, @Param("matchingMode") boolean matchingMode);

    @Modifying
    @Query("update MemberInfo i set i.matchingMode = :matchingMode, i.matchingVisibility = :matchingVisibility, i.matchingModeLog = CURRENT_TIMESTAMP where i.member.id = :memberId")
    void updateMatchingModeAndVisibility(@Param("memberId") Long memberId, @Param("matchingMode") boolean matchingMode, @Param("matchingVisibility") MatchingVisibility matchingVisibility);
}
