package com.fanclub.zinzin.domain.member.entity;

import com.fanclub.zinzin.domain.member.dto.MemberRegisterDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "member_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 300, nullable = false)
    @ColumnDefault("'default.jpg'")
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

    private LocalDateTime matchingModeLog;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Column(name = "success_cnt")
    private Integer successCount;

    @Builder
    public MemberInfo(String profileImage, String nickname, String searchId, MatchingVisibility matchingVisibility, boolean matchingMode, LocalDateTime matchingModeLog, Member member) {
        this.profileImage = (profileImage == null) ? "default.jpg" : profileImage;
        this.nickname = nickname;
        this.searchId = searchId;
        this.matchingVisibility = matchingVisibility;
        this.matchingMode = matchingMode;
        this.matchingModeLog = matchingModeLog;
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

    public void updateDeletedMemberInfo(MemberRegisterDto memberRegisterDto, String nickname){
        this.searchId = memberRegisterDto.getSearchId();
        this.matchingMode = memberRegisterDto.isMatchingMode();
        this.matchingVisibility = memberRegisterDto.getMatchingVisibility();
        this.matchingModeLog = LocalDateTime.now();
        this.nickname = nickname;
    }
}
