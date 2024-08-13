package com.fanclub.zinzin.domain.member.entity;

import com.fanclub.zinzin.domain.member.dto.MemberRegisterDto;
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
@Getter
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 320, nullable = false, unique = true)
    private String email;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false, unique = true)
    private String sub;

    @Column(nullable = false)
    private LocalDate birth;

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

    public void updateDeletedMember(MemberRegisterDto memberRegisterDto){
        this.email = memberRegisterDto.getEmail();
        this.name = memberRegisterDto.getName();
        this.birth = memberRegisterDto.getBirth();
        this.gender = memberRegisterDto.getGender();
        this.deletedAt = null;
        this.status = Status.ACTIVE;
        this.role = Role.USER;
    }

    public void withdraw() {
        this.status = Status.DELETED;
        this.deletedAt = LocalDateTime.now();
    }
}