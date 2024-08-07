package com.fanclub.zinzin.domain.report.controller;

import com.fanclub.zinzin.domain.report.dto.ReportRequest;
import com.fanclub.zinzin.domain.report.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/matchings/{cardId}/report")
    public ResponseEntity<?> report(HttpServletRequest request, @RequestBody ReportRequest reportRequest) {
        reportService.report((Long) request.getAttribute("memberId"), reportRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
