package com.fanclub.zinzin.domain.card.repository;

import com.fanclub.zinzin.domain.card.entity.Card;
import com.fanclub.zinzin.domain.card.entity.CardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardImageRepository extends JpaRepository<CardImage, Long> {
    void deleteByCard(Card card);
}
