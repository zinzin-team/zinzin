package com.fanclub.zinzin.domain.member.repository;

import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.entity.Role;
import com.fanclub.zinzin.domain.member.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);
    Member findBySub(String sub);

    @Query("SELECT m.role FROM Member m WHERE m.id = :memberId")
    Role findRoleByMemberId(@Param("memberId") Long memberId);

    List<Member> findAllByStatusAndDeletedAtBefore(Status status, LocalDateTime cutoffDate);
}
