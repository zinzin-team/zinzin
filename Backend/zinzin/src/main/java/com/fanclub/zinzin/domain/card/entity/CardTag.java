package com.fanclub.zinzin.domain.card.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="card_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public void setCard(Card card) {
        this.card = card;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public static CardTag createCard(Card card, Tag tag) {
        CardTag cardTag = new CardTag();
        cardTag.setCard(card);
        cardTag.setTag(tag);
        return cardTag;
    }
}
