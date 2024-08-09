package com.fanclub.zinzin.domain.card.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 300, nullable = false)
    private String image;

    @Column(nullable = false)
    private Integer imageNum = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card;

    @Builder
    private CardImage(String image, Card card, Integer imageNum) {
        this.card = card;
        this.image = image;
        this.imageNum = imageNum;
    }

    public static CardImage toCardImageEntity(Card card, String image, Integer imageNum) {
        return CardImage.builder()
                .card(card)
                .image(image)
                .imageNum(imageNum)
                .build();
    }
}
