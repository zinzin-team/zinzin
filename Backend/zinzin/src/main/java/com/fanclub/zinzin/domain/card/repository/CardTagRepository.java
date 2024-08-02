package com.fanclub.zinzin.domain.card.repository;

import com.fanclub.zinzin.domain.card.entity.CardTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTagRepository extends JpaRepository<CardTag, Long> {
}
