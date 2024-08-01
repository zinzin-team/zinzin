package com.fanclub.zinzin.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
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
    @Getter
    private String profileImage;

    @Column(length = 30, nullable = false)
    private String nickname;

    @Column(length = 15, nullable = false)
    private String searchId;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'PRIVATE'")
    private MatchingVisibility matchingVisibility;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean matchingMode;

    @LastModifiedDate
    private LocalDateTime matchingModeLog;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Builder
    public MemberInfo(String profileImage, String nickname, String searchId, MatchingVisibility matchingVisibility, boolean matchingMode, Member member) {
        this.profileImage = (profileImage==null)?"default.jpg":profileImage;
        this.nickname = nickname;
        this.searchId = searchId;
        this.matchingVisibility = matchingVisibility;
        this.matchingMode = matchingMode;
        this.member = member;
    }
}
