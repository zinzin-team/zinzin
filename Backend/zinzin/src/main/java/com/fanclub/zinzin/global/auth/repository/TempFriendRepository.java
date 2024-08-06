package com.fanclub.zinzin.global.auth.repository;

import com.fanclub.zinzin.global.auth.entity.TempFriend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TempFriendRepository extends JpaRepository<TempFriend, Long> {
    List<TempFriend> findAllByMySub(String mySub);
}
