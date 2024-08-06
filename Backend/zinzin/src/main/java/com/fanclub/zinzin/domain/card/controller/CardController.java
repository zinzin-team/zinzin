package com.fanclub.zinzin.domain.card.controller;

import com.fanclub.zinzin.domain.card.dto.CardRequest;
import com.fanclub.zinzin.domain.card.dto.CardResponse;
import com.fanclub.zinzin.domain.card.service.CardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<?> createCard(HttpServletRequest request,
                                        @RequestPart CardRequest cardRequest,
                                        @RequestPart(required = false) List<MultipartFile> images) {
        cardService.createCard(new CardRequest(cardRequest.getInfo(), images, cardRequest.getTags()), (Long) request.getAttribute("memberId"));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> readCard(HttpServletRequest request) {
        CardResponse cardResponse = cardService.readCard((Long) request.getAttribute("memberId"));
        return ResponseEntity.ok(cardResponse);
    }

    @PutMapping("/{cardId}")
    public ResponseEntity<?> updateCard(HttpServletRequest request,
                                        @PathVariable("cardId") Long cardId,
                                        @RequestPart CardRequest cardRequest,
                                        @RequestPart(required = false) List<MultipartFile> images) {
        cardService.updateCard(cardId,
                new CardRequest(cardRequest.getInfo(), images, cardRequest.getTags()),
                (Long) request.getAttribute("memberId"));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
