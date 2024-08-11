package com.fanclub.zinzin.domain.matching.repository;

import com.fanclub.zinzin.domain.card.entity.Card;
import com.fanclub.zinzin.domain.matching.entity.RecommendedCard;
import com.fanclub.zinzin.domain.member.entity.Member;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendedCardRepository extends JpaRepository<RecommendedCard, Long> {
    List<RecommendedCard> findRecommendedCardsByRecommendedDateAndMember(LocalDate recommendedDate, Member member);
    Optional<RecommendedCard> findByRecommendedDateAndMemberAndCard(LocalDate recommendedDate, Member member, Card card);
}
