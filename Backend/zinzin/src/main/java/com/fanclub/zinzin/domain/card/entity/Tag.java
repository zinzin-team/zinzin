package com.fanclub.zinzin.domain.card.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, updatable = false)
    private String content;
}
