package com.fanclub.zinzin.domain.report.entity;

import com.fanclub.zinzin.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    public void setContent(String content) {
        this.content = content;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setReportedMember(Member reportedMember) {
        this.reportedMember = reportedMember;
    }

    public static Report report(String content, Member member, Member reportedMember) {
        Report report = new Report();
        report.setContent(content);
        report.setMember(member);
        report.setReportedMember(reportedMember);
        return report;
    }
}
