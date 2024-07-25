package zinzin.card.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zinzin.card.domain.Card;
import zinzin.card.repository.CardRepository;
import zinzin.card.repository.MemoryCardRepository;

import java.util.List;

@Service
public class CardService {
    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public void create(Card card) {
        cardRepository.save(card);
    }

    public List<Card> findCards() {
        return cardRepository.findAll();
    }
}
