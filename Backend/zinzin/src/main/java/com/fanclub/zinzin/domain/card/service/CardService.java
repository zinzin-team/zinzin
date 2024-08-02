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
import com.fanclub.zinzin.global.error.code.CardErrorCode;
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

    @Transactional
    public Card createCard(CardRequest request, Member member) {

        cardRepository.findByMember(member).ifPresent(existingCard -> {
            throw new BaseException(CardErrorCode.CARD_ALREADY_EXISTS);
        });

        if (request.getImages().size() != 3) {
            throw new BaseException(CardErrorCode.INVALID_NUMBER_OF_IMAGES);
        }

        if (request.getTags().size() != 5) {
            throw new BaseException(CardErrorCode.INVALID_NUMBER_OF_TAGS);
        }

        Card card = Card.createCard(request.getInfo(), member);
        Card newCard = cardRepository.save(card);

        List<CardImage> images = request.getImages().stream()
                .map(image -> CardImage.createCard(newCard, image))
                .collect(Collectors.toList());
        cardImageRepository.saveAll(images);

        List<CardTag> cardTags = request.getTags().stream()
                .map(tagContent -> {
                    Tag tag = tagRepository.findByContent(tagContent)
                            .orElseThrow(() -> new BaseException(CardErrorCode.TAG_NOT_FOUND));
                    return CardTag.createCard(newCard, tag);
                })
                .collect(Collectors.toList());
        cardTagRepository.saveAll(cardTags);

        return newCard;
    }
}
