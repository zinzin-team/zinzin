package com.fanclub.zinzin.domain.report.entity;

import com.fanclub.zinzin.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_member_id", referencedColumnName = "id")
    private Member reportedMember;

    @Builder
    private Report(String content, Member member, Member reportedMember) {
        this.content = content;
        this.member = member;
        this.reportedMember = reportedMember;
    }
}
