package com.fanclub.zinzin.domain.member.repository;

import com.fanclub.zinzin.domain.member.entity.RandomNickname;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RandomNicknameRepository extends JpaRepository<RandomNickname, Long> {
}
