package com.fanclub.zinzin.domain.member.repository;

import com.fanclub.zinzin.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);
    Member findBySub(Long sub);
}
