package com.fanclub.zinzin.domain.report.dto;

import lombok.Getter;

@Getter
public class ReportRequest {
    private Long memberId;
    private Long targetId;
    private String content;
}
