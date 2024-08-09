package com.fanclub.zinzin.domain.card.service;

import com.fanclub.zinzin.domain.card.dto.CardRequest;
import com.fanclub.zinzin.domain.card.dto.CardResponse;
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
import com.fanclub.zinzin.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
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
    private final S3Service s3Service;

    @Transactional
    public void createCard(CardRequest cardRequest, Long memberId) {
        if (memberId == null) {
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        cardRepository.findCardByMemberId(memberId).ifPresent(existingCard -> {
            throw new BaseException(CardErrorCode.CARD_ALREADY_EXISTS);
        });

        if (cardRequest.getImages() == null || cardRequest.getImages().size() != 3) {
            throw new BaseException(CardErrorCode.INVALID_NUMBER_OF_IMAGES);
        }

        if (cardRequest.getTags() == null || cardRequest.getTags().size() != 5) {
            throw new BaseException(CardErrorCode.INVALID_NUMBER_OF_TAGS);
        }

        // card를 저장한다.
        Card card = Card.toCardEntity(member, cardRequest.getInfo());
        Card newCard = cardRepository.save(card);

        // cardTags를 저장한다.
        List<CardTag> cardTags = cardRequest.getTags().stream()
                .map(tagContent -> {
                    Tag tag = tagRepository.findByContent(tagContent)
                            .orElseThrow(() -> new BaseException(CardErrorCode.TAG_NOT_FOUND));
                    return CardTag.toCardTagEntity(newCard, tag);
                })
                .collect(Collectors.toList());
        cardTagRepository.saveAll(cardTags);

        // 요청으로 들어온 카드 이미지를 업로드하고 URL을 저장한다.
        List<CardImage> images = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            if (cardRequest.getImages().get(i) == null) {
                throw new BaseException(CardErrorCode.IMAGE_UPLOAD_NOT_REQUESTED);
            }

            String newImageURL = s3Service.uploadCard(cardRequest.getImages().get(i));
            images.add(CardImage.toCardImageEntity(card, newImageURL, i));
        }

        // cardImage를 저장한다.
        cardImageRepository.saveAll(images);
    }

    @Transactional(readOnly = true)
    public CardResponse readCard(Long memberId) {
        if (memberId == null) {
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        Card card = cardRepository.findCardByMemberId(memberId)
                .orElseThrow(() -> new BaseException(CardErrorCode.CARD_NOT_FOUND));

        return new CardResponse(card);
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

        if (cardRequest.getImages() == null || cardRequest.getImages().size() != 3) {
            throw new BaseException(CardErrorCode.INVALID_NUMBER_OF_IMAGES);
        }

        if (cardRequest.getTags() == null || cardRequest.getTags().size() != 5) {
            throw new BaseException(CardErrorCode.INVALID_NUMBER_OF_TAGS);
        }

        // card의 info를 업데이트한다.
        card.updateInfo(cardRequest.getInfo());

        // cardTags를 업데이트한다.
        List<CardTag> newCardTags = cardRequest.getTags().stream()
                .map(tagContent -> {
                    Tag tag = tagRepository.findByContent(tagContent)
                            .orElseThrow(() -> new BaseException(CardErrorCode.TAG_NOT_FOUND));
                    return CardTag.toCardTagEntity(card, tag);
                })
                .collect(Collectors.toList());
        cardTagRepository.deleteByCard(card);
        cardTagRepository.saveAll(newCardTags);

        // 요청으로 들어온 카드 이미지를 업로드하고 URL을 저장한다.
        // 요청으로 들어온 카드 이미지가 존재하지 않으면 기존 URL을 저장한다.
        List<String> imageURLs = card.getCardImages().stream()
                .sorted(Comparator.comparingInt(CardImage::getImageNum))
                .map(CardImage::getImage)
                .toList();
        List<CardImage> newCardImages = new ArrayList<>();

        for (int i = 0; i < imageURLs.size(); i++) {
            if (cardRequest.getImages().get(i) == null) {
                newCardImages.add(CardImage.toCardImageEntity(card, imageURLs.get(i), i));
            } else {
                String newImageURL = s3Service.uploadCard(cardRequest.getImages().get(i));
                newCardImages.add(CardImage.toCardImageEntity(card, newImageURL, i));
            }
        }

        // cardImages를 업데이트한다.
        cardImageRepository.deleteByCard(card);
        cardImageRepository.saveAll(newCardImages);

        // card를 저장한다.
        cardRepository.save(card);

        // 기존 URL에 해당하는 카드 이미지를 삭제한다.
        for (String imageURL : imageURLs) {
            s3Service.deleteS3(imageURL);
        }
    }
}
