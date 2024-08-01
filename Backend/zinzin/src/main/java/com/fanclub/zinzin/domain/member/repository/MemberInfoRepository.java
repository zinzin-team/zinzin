package com.fanclub.zinzin.domain.member.repository;

import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import com.fanclub.zinzin.domain.searcher.dto.SearchedMemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
    @Query("SELECT new com.fanclub.zinzin.domain.searcher.dto.SearchedMemberDto(m.id, m.name, mi.profile_image) " +
            "FROM MemberInfo mi " +
            "JOIN mi.member m " +
            "WHERE mi.searchId = :searchId")
    Optional<SearchedMemberDto> findSearcherResponseBySearchId(@Param("searchId") String searchId);
}