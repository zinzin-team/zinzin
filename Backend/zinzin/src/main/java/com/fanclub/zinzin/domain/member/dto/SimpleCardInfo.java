package com.fanclub.zinzin.domain.member.dto;

import com.fanclub.zinzin.domain.card.entity.Card;
import com.fanclub.zinzin.domain.card.entity.CardImage;
import com.fanclub.zinzin.domain.card.entity.CardTag;
import com.fanclub.zinzin.domain.card.entity.Tag;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SimpleCardInfo {
    private final Long id;
    private final String info;
    private final List<String> tags;
    private final List<String> images;

    @Builder
    private SimpleCardInfo(Long id, String info, List<String> tags, List<String> images) {
        this.id = id;
        this.info = info;
        this.tags = tags;
        this.images = images;
    }

    public static SimpleCardInfo of(Card card){
        List<String> tags = card.getCardTags().stream()
                .map(CardTag::getTag)
                .map(Tag::getContent)
                .toList();

        List<String> images = card.getCardImages().stream()
                .map(CardImage::getImage)
                .collect(Collectors.toList());

        return SimpleCardInfo.builder()
                .id(card.getId())
                .info(card.getInfo())
                .tags(tags)
                .images(images)
                .build();
    }
}
