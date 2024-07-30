package com.fanclub.zinzin.domain.member.repository;

import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {

}
