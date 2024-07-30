package com.fanclub.zinzin.domain.member.entity;

import com.fanclub.zinzin.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 320, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private Long sub;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'USER'")
    private Role role;

    @Builder
    public Member(Long id, String email, String name, Long sub, LocalDate birth, Gender gender, LocalDateTime deletedAt, Status status, Role role){
        this.id = id;
        this.email = email;
        this.name = name;
        this.sub = sub;
        this.birth = birth;
        this.gender = gender;
        this.deletedAt = deletedAt;
        this.status = status;
        this.role = role;
    }
}