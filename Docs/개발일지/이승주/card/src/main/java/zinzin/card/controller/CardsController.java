package zinzin.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import zinzin.card.domain.Card;
import zinzin.card.service.CardService;

import java.util.List;
import java.util.Optional;

@Controller
public class CardsController {

    private final CardService cardService;

    @Autowired
    public CardsController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/api/mypage")
    public String cards(Model model) {
        List<Card> cards = cardService.findAll();
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

    @GetMapping("api/cards/1")
    public String detail(Long id, Model model) {
        Optional<Card> card = cardService.findById(id);
        model.addAttribute("card", card);
        return "api/cards/update";
    }

    @PostMapping("api/cards/update")
    public String update(CardForm form) {
        Card card = new Card();

        card.setName(form.getName());
        card.setIntro(form.getIntro());

        cardService.update(card);

        return "redirect:/api/mypage";
    }
}
