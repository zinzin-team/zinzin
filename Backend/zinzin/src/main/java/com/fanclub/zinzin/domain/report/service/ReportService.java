package com.fanclub.zinzin.domain.report.service;

import com.fanclub.zinzin.domain.card.entity.Card;
import com.fanclub.zinzin.domain.card.repository.CardRepository;
import com.fanclub.zinzin.domain.matching.entity.RecommendedCard;
import com.fanclub.zinzin.domain.matching.repository.RecommendedCardRepository;
import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.domain.person.repository.PersonRepository;
import com.fanclub.zinzin.domain.report.dto.ReportRequest;
import com.fanclub.zinzin.domain.report.entity.Report;
import com.fanclub.zinzin.domain.report.repository.ReportRepository;
import com.fanclub.zinzin.global.error.code.MatchingErrorCode;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.code.ReportErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final PersonRepository personRepository;
    private final CardRepository cardRepository;
    private final RecommendedCardRepository recommendedCardRepository;

    @Transactional
    public void report(Long memberId, Long cardId, ReportRequest reportRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        Member reportedMember = memberRepository.findById(reportRequest.getTargetId())
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        Card card = cardRepository.getReferenceById(cardId);
        if(Long.compare(card.getMember().getId(),reportedMember.getId()) != 0){
            throw new BaseException(ReportErrorCode.MISMATCH_REPORTED_CARD_MEMBER);
        }

        RecommendedCard recommendedCard = recommendedCardRepository
                .findByRecommendedDateAndMemberAndCard(LocalDate.now(), member, card)
                .orElseThrow(() -> new BaseException(MatchingErrorCode.INVALID_MATCHING_CARD));
        System.out.println(recommendedCard);

        recommendedCard.checkCard();

        Report report = reportRequest.toReportEntity(member, reportedMember);
        reportRepository.save(report);

        personRepository.report(memberId, reportRequest.getTargetId());
    }
}
