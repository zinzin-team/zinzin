package com.fanclub.zinzin.domain.friend.repository;

import com.fanclub.zinzin.domain.friend.entity.TempFriend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TempFriendRepository extends JpaRepository<TempFriend, Long> {
    List<TempFriend> findAllByMySub(String mySub);
}
