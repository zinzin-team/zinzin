package com.fanclub.zinzin.domain.card.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="card_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CardTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    private Tag tag;

    @Builder
    private CardTag(Card card, Tag tag) {
        this.card = card;
        this.tag = tag;
    }

    public static CardTag toCardTagEntity(Card card, Tag tag) {
        return CardTag.builder()
                .card(card)
                .tag(tag)
                .build();
    }
}
