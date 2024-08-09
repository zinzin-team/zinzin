package com.fanclub.zinzin.domain.member.entity;

import com.fanclub.zinzin.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
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
    @Getter
    private Long id;

    @Getter
    @Column(length = 320, nullable = false, unique = true)
    private String email;

    @Getter
    @Column(length = 50, nullable = false)
    private String name;

    @Getter
    @Column(length = 50, nullable = false, unique = true)
    private String sub;

    @Getter
    @Column(nullable = false)
    private LocalDate birth;

    @Getter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDateTime deletedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'USER'")
    private Role role;

    @Builder
    private Member(Long id, String email, String name, String sub, LocalDate birth, Gender gender, LocalDateTime deletedAt){
        this.id = id;
        this.email = email;
        this.name = name;
        this.sub = sub;
        this.birth = birth;
        this.gender = gender;
        this.deletedAt = deletedAt;
        this.status = Status.ACTIVE;
        this.role = Role.USER;
    }

    public void withdraw() {
        this.status = Status.DELETED;
        this.deletedAt = LocalDateTime.now();
    }
}