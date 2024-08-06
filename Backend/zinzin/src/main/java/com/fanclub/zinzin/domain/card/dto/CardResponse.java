package com.fanclub.zinzin.domain.card.dto;

import com.fanclub.zinzin.domain.card.entity.Card;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CardResponse {
    private Long cardId;
    private String info;
    private List<String> images;
    private List<String> tags;

    public CardResponse(Card card) {
        this.cardId = card.getId();
        this.info = card.getInfo();
        this.images = card.getCardImages().stream()
                .map(cardImage -> cardImage.getImage())
                .collect(Collectors.toList());
        this.tags = card.getCardTags().stream()
                .map(ct -> ct.getTag().getContent())
                .collect(Collectors.toList());
    }
}
