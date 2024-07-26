package com.fanclub.zinzin.domain.card.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    private Tag tag;
}
