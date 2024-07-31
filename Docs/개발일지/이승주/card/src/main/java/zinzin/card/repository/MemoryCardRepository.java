package zinzin.card.repository;

import org.springframework.stereotype.Repository;
import zinzin.card.domain.Card;

import java.math.BigInteger;
import java.util.*;

@Repository
public class MemoryCardRepository implements CardRepository {
    private static Map<Long, Card> store = new HashMap<>();
    private static Long autoId = 0L;

    @Override
    public Card save(Card card) {
        card.setId(++autoId);
        store.put(card.getId(), card);
        return card;
    }

    @Override
    public Optional<Card> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Card> findAll() {
        return new ArrayList<>(store.values());
    }
}
