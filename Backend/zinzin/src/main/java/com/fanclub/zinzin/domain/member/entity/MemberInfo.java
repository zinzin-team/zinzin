package com.fanclub.zinzin.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "member_info")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 300, nullable = false)
    @ColumnDefault("'default.jpg'")
    private String profile_image;

    @Column(length = 30, nullable = false)
    private String nickname;

    @Column(length = 15, nullable = false)
    private String searchId;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'PRIVATE'")
    private MatchingVisibility matchingVisibility;

    @Column(nullable = false)
    private boolean matchingMode;

    @LastModifiedDate
    private LocalDateTime matchingModeLog;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Builder
    public MemberInfo(String profileImage, String nickname, String searchId, MatchingVisibility matchingVisibility, boolean matchingMode, Member member) {
        this.profile_image = profileImage;
        this.nickname = nickname;
        this.searchId = searchId;
        this.matchingVisibility = matchingVisibility;
        this.matchingMode = matchingMode;
        this.member = member;
    }
}
