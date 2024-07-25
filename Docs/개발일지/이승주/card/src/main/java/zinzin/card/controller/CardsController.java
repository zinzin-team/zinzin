package zinzin.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import zinzin.card.domain.Card;
import zinzin.card.service.CardService;

import java.util.List;

@Controller
public class CardsController {

    private final CardService cardService;

    @Autowired
    public CardsController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/api/mypage")
    public String cards(Model model) {
        List<Card> cards = cardService.findCards();
        model.addAttribute("cards", cards);
        return "cards";
    }

    @GetMapping("api/cards")
    public String cardForm() {
        return "cardForm";
    }

    @PostMapping("api/cards")
    public String create(CardForm form) {
        Card card = new Card();

        card.setName(form.getName());
        card.setIntro(form.getIntro());

        cardService.create(card);

        return "redirect:/api/mypage";
    }
}
