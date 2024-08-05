package com.fanclub.zinzin.domain.card.repository;

import com.fanclub.zinzin.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findCardByMemberId(Long memberId);
}
