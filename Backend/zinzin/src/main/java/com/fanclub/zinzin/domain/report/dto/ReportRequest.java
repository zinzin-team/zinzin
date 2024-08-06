package com.fanclub.zinzin.domain.report.dto;

import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.report.entity.Report;
import lombok.Getter;

@Getter
public class ReportRequest {
    private Long memberId;
    private Long targetId;
    private String content;

    public Report toReportEntity(Member member, Member reportedMember) {
        return Report.builder()
                .content(this.content)
                .member(member)
                .reportedMember(reportedMember)
                .build();
    }
}
