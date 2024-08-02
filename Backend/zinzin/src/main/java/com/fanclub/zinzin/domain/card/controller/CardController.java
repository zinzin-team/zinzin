package com.fanclub.zinzin.domain.card.controller;

import com.fanclub.zinzin.domain.card.dto.CardRequest;
import com.fanclub.zinzin.domain.card.service.CardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<?> createCard(HttpServletRequest request, @RequestBody CardRequest cardRequest) {
        cardService.createCard(cardRequest, (Long) request.getAttribute("memberId"));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
