package com.fanclub.zinzin.domain.matching.service;

import com.fanclub.zinzin.domain.card.entity.Card;
import com.fanclub.zinzin.domain.card.repository.CardRepository;
import com.fanclub.zinzin.domain.matching.dto.CardInfo;
import com.fanclub.zinzin.domain.matching.dto.MatchingResponse;
import com.fanclub.zinzin.domain.matching.entity.RecommendedCard;
import com.fanclub.zinzin.domain.matching.repository.RecommendedCardRepository;
import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.domain.person.dto.MatchingPartner;
import com.fanclub.zinzin.domain.matching.dto.Matching;
import com.fanclub.zinzin.domain.person.dto.Mate;
import com.fanclub.zinzin.domain.person.repository.PersonRepository;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final MemberRepository memberRepository;
    private final PersonRepository personRepository;
    private final CardRepository cardRepository;
    private final RecommendedCardRepository recommendedCardRepository;

    @Transactional
    public MatchingResponse getMatchings(HttpServletRequest request) {
        if(request.getAttribute("memberId") == null){
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        Long memberId = (Long) request.getAttribute("memberId");
        Member member = memberRepository.getReferenceById(memberId);

        List<RecommendedCard> todayRecommendation = findTodayRecommendation(member);

        if(todayRecommendation.size() != 0){
            List<Matching> matchings = new ArrayList<>();

            for(RecommendedCard recommendedCard:todayRecommendation){
                MatchingPartner matchingPartner = personRepository.getPersonByCardId(recommendedCard.getCard().getId());
                List<Mate> mates = personRepository.getMatesByMatchingPartnerId(memberId, matchingPartner.getMemberId());
                matchings.add(recommendedCard.toMatching(matchingPartner, mates));
            }
            return MatchingResponse.of(matchings);
        }

        List<MatchingPartner> matchingPartners = personRepository.getMatchingPartners(memberId);

        List<Matching> matchings = new ArrayList<>();
        int position = 1;
        for(MatchingPartner matchingPartner:matchingPartners){
            CardInfo cardInfo = getCard(matchingPartner.getCardId());
            if(cardInfo == null){
                continue;
            }

            List<Mate> mates = personRepository.getMatesByMatchingPartnerId(memberId, matchingPartner.getMemberId());
            matchings.add(Matching.of(matchingPartner, cardInfo, mates, position, false));

            Card card = cardRepository.getReferenceById(cardInfo.getCardId());
            recommendedCardRepository.save(RecommendedCard.of(position,false,member,card));
            position++;
        }

        return MatchingResponse.of(matchings);
    }

    private List<RecommendedCard> findTodayRecommendation(Member member){
        return recommendedCardRepository.findRecommendedCardsByRecommendedDateAndMember(LocalDate.now(), member);
    }

    private CardInfo getCard(Long cardId){
        Card card = cardRepository.findCardById(cardId)
                .orElse(null);

        if(card == null){
            return null;
        }

        return CardInfo.of(card);
    }
}
