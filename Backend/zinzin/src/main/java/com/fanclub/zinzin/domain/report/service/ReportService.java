package com.fanclub.zinzin.domain.report.service;

import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.domain.report.dto.ReportRequest;
import com.fanclub.zinzin.domain.report.entity.Report;
import com.fanclub.zinzin.domain.report.repository.ReportRepository;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void report(ReportRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        Member reportedMember = memberRepository.findById(request.getTargetId())
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        Report report = request.toReportEntity(member, reportedMember);
        reportRepository.save(report);
    }
}
