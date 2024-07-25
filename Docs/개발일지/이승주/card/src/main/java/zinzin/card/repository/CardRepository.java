package zinzin.card.repository;

import zinzin.card.domain.Card;

import java.util.List;
import java.util.Optional;

public interface CardRepository {
    Card save(Card card);
    Optional<Card> find(Long id);
    List<Card> findAll();
}
