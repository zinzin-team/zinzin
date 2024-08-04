package com.fanclub.zinzin.domain.mathcing.service;

import com.fanclub.zinzin.domain.card.entity.Card;
import com.fanclub.zinzin.domain.card.repository.CardRepository;
import com.fanclub.zinzin.domain.mathcing.dto.CardInfo;
import com.fanclub.zinzin.domain.mathcing.dto.MatchingResponse;
import com.fanclub.zinzin.domain.person.dto.MatchingPartner;
import com.fanclub.zinzin.domain.mathcing.dto.Matching;
import com.fanclub.zinzin.domain.person.dto.Mate;
import com.fanclub.zinzin.domain.person.repository.PersonRepository;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final PersonRepository personRepository;
    private final CardRepository cardRepository;

    public MatchingResponse getMatchings(HttpServletRequest request) {
        if(request.getAttribute("memberId") == null){
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        Long memberId = (Long) request.getAttribute("memberId");
        List<MatchingPartner> matchingPartners = personRepository.getMatchingPartners(memberId);

        List<Matching> matchings = new ArrayList<>();
        int order = 1;
        for(MatchingPartner matchingPartner:matchingPartners){
            CardInfo card = getCard(matchingPartner.getCardId());
            if(card == null){
                continue;
            }

            List<Mate> mates = personRepository.getMates(memberId, matchingPartner.getMemberId());
            matchings.add(Matching.of(matchingPartner, card, mates, order++, false));
        }

        return MatchingResponse.of(matchings);
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
