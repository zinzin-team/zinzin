package com.fanclub.zinzin.domain.card.repository;

import com.fanclub.zinzin.domain.card.entity.Card;
import com.fanclub.zinzin.domain.card.entity.CardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardImageRepository extends JpaRepository<CardImage, Long> {
    void deleteByCard(Card card);

    @Modifying
    @Query("UPDATE CardImage i SET i.image = :image WHERE i.card.id = :cardId AND i.imageNum = :imageNum")
    void updateImage(@Param("cardId") Long cardId, @Param("imageNum") Integer imageNum, @Param("image") String image);
}
