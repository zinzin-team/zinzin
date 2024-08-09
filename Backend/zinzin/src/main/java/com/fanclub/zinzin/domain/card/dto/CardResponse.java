package com.fanclub.zinzin.domain.card.dto;

import com.fanclub.zinzin.domain.card.entity.Card;
import com.fanclub.zinzin.domain.card.entity.CardImage;
import com.fanclub.zinzin.domain.card.entity.CardTag;
import com.fanclub.zinzin.domain.card.entity.Tag;
import lombok.Getter;

import java.util.List;

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
                .map(CardImage::getImage)
                .toList();
        this.tags = card.getCardTags().stream()
                .map(CardTag::getTag)
                .map(Tag::getContent)
                .toList();
    }
}
