package com.fanclub.zinzin.domain.mathcing.dto;

import com.fanclub.zinzin.domain.card.entity.Card;
import com.fanclub.zinzin.domain.card.entity.CardImage;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CardInfo {
    private final String info;
    private final List<String> images;
    private final List<String> tags;

    @Builder
    private CardInfo(String info, List<String> images, List<String> tags) {
        this.info = info;
        this.images = images;
        this.tags = tags;
    }

    public static CardInfo of(Card card) {
        List<String> images = card.getCardImages().stream()
                .map(CardImage::getImage)
                .toList();

        List<String> tags = card.getTags().stream()
                .map(cardTag -> cardTag.getTag().getContent())
                .toList();

        return CardInfo.builder()
                .info(card.getInfo())
                .images(images)
                .tags(tags)
                .build();
    }
}

