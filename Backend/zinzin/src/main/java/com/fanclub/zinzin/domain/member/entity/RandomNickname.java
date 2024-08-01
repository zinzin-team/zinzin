package com.fanclub.zinzin.domain.member.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "random_nickname")
public class RandomNickname {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;
}
