package com.fanclub.zinzin.domain.member.repository;

import com.fanclub.zinzin.domain.member.entity.RandomNickname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RandomNicknameRepository extends JpaRepository<RandomNickname, Long> {
    @Query(value = "SELECT r.nickname FROM RandomNickname r ORDER BY RAND() LIMIT 1")
    String getRandomNickname();
}
