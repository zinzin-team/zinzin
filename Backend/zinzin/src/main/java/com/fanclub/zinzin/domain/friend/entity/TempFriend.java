package com.fanclub.zinzin.domain.friend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "temp_friend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TempFriend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mySub;

    @Column(nullable = false)
    private String friendSub;

    @Column(length = 50, nullable = false)
    private String friendName;

    public TempFriend(String mySub, String friendSub, String friendName) {
        this.mySub = mySub;
        this.friendSub = friendSub;
        this.friendName = friendName;
    }
}
