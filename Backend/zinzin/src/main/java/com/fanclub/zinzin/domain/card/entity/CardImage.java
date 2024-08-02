package com.fanclub.zinzin.domain.card.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(length = 300, nullable = false)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card;

    public void setCard(Card card) {
        this.card = card;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static CardImage createCard(Card card, String image) {
        CardImage cardImage = new CardImage();
        cardImage.setCard(card);
        cardImage.setImage(image);
        return cardImage;
    }
}
