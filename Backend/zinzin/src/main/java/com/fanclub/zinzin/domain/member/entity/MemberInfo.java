package com.fanclub.zinzin.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
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

    @Getter
    @Column(length = 30, nullable = false)
    private String nickname;

    @Getter
    @Column(length = 15, nullable = false)
    private String searchId;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'PRIVATE'")
    private MatchingVisibility matchingVisibility;

    @Getter
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean matchingMode;

    @Getter
    @LastModifiedDate
    private LocalDateTime matchingModeLog;

    @Getter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Getter
    @Column(name = "success_cnt")
    private Integer successCount;

    @Builder
    public MemberInfo(String profileImage, String nickname, String searchId, MatchingVisibility matchingVisibility, boolean matchingMode, Member member) {
        this.profileImage = (profileImage == null) ? "default.jpg" : profileImage;
        this.nickname = nickname;
        this.searchId = searchId;
        this.matchingVisibility = matchingVisibility;
        this.matchingMode = matchingMode;
        this.member = member;
        this.successCount = 0;
    }

    public void updateMemberInfo(String profileImage, String searchId) {
        this.profileImage = profileImage;
        this.searchId = searchId;
    }

    public void increaseSuccessCount() {
        this.successCount += 1;
    }

    @Transient
    public Long getMemberId() {
        return member != null ? member.getId() : null;
    }

    @Transient
    public String getMemberName() {
        return member != null ? member.getName() : null;
    }

    public String updateNickname(String nickname) {
        this.nickname = nickname;
        return nickname;
    }
}
