package com.fanclub.zinzin.domain.card.controller;

import com.fanclub.zinzin.domain.card.dto.CardRequest;
import com.fanclub.zinzin.domain.card.service.CardService;
import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.service.MemberService;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;
//    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> createCard(@RequestBody CardRequest request) {

//        Member member = memberService.findById(1L)
//                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

//        cardService.createCard(request, member);
        cardService.createCard(request, null);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
