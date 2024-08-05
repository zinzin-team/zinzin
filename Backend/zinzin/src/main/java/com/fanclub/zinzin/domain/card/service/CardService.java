package com.fanclub.zinzin.domain.card.service;

import com.fanclub.zinzin.domain.card.dto.CardRequest;
import com.fanclub.zinzin.domain.card.entity.Card;
import com.fanclub.zinzin.domain.card.entity.CardImage;
import com.fanclub.zinzin.domain.card.entity.CardTag;
import com.fanclub.zinzin.domain.card.entity.Tag;
import com.fanclub.zinzin.domain.card.repository.CardImageRepository;
import com.fanclub.zinzin.domain.card.repository.CardRepository;
import com.fanclub.zinzin.domain.card.repository.CardTagRepository;
import com.fanclub.zinzin.domain.card.repository.TagRepository;
import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.global.error.code.CardErrorCode;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardImageRepository cardImageRepository;
    private final CardTagRepository cardTagRepository;
    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;
    private final ImageStorageService imageStorageService;

    @Transactional
    public Card createCard(CardRequest cardRequest, Long memberId) {

        if (memberId == null) {
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        cardRepository.findCardByMemberId(memberId).ifPresent(existingCard -> {
            throw new BaseException(CardErrorCode.CARD_ALREADY_EXISTS);
        });

        if (cardRequest.getImages().size() != 3) {
            throw new BaseException(CardErrorCode.INVALID_NUMBER_OF_IMAGES);
        }

        if (cardRequest.getTags().size() != 5) {
            throw new BaseException(CardErrorCode.INVALID_NUMBER_OF_TAGS);
        }

        Card card = Card.createCard(cardRequest.getInfo(), member);
        Card newCard = cardRepository.save(card);

        List<CardImage> images = cardRequest.getImages().stream()
                .map(image -> {
                    String imagePath = imageStorageService.storeFile(image);
                    return CardImage.createCard(newCard, imagePath);
                })
                .collect(Collectors.toList());
        cardImageRepository.saveAll(images);

        List<CardTag> cardTags = cardRequest.getTags().stream()
                .map(tagContent -> {
                    Tag tag = tagRepository.findByContent(tagContent)
                            .orElseThrow(() -> new BaseException(CardErrorCode.TAG_NOT_FOUND));
                    return CardTag.createCard(newCard, tag);
                })
                .collect(Collectors.toList());
        cardTagRepository.saveAll(cardTags);

        return newCard;
    }

    @Transactional
    public void updateCard(Long cardId, CardRequest cardRequest, Long memberId) {

        if (memberId == null) {
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new BaseException(CardErrorCode.CARD_NOT_FOUND));

        if (!card.getMember().equals(member)) {
            throw new BaseException(CardErrorCode.AUTHOR_MISMATCH);
        }

        if (cardRequest.getImages().size() != 3) {
            throw new BaseException(CardErrorCode.INVALID_NUMBER_OF_IMAGES);
        }

        if (cardRequest.getTags().size() != 5) {
            throw new BaseException(CardErrorCode.INVALID_NUMBER_OF_TAGS);
        }

        card.setInfo(cardRequest.getInfo());

        List<CardImage> newImages = cardRequest.getImages().stream()
                .map(image -> {
                    String imagePath = imageStorageService.storeFile(image);
                    return CardImage.createCard(card, imagePath);
                })
                .collect(Collectors.toList());
        cardImageRepository.deleteByCard(card);
        cardImageRepository.saveAll(newImages);

        List<CardTag> newCardTags = cardRequest.getTags().stream()
                .map(tagContent -> {
                    Tag tag = tagRepository.findByContent(tagContent)
                            .orElseThrow(() -> new BaseException(CardErrorCode.TAG_NOT_FOUND));
                    return CardTag.createCard(card, tag);
                })
                .collect(Collectors.toList());
        cardTagRepository.deleteByCard(card);
        cardTagRepository.saveAll(newCardTags);

        cardRepository.save(card);
    }
}
