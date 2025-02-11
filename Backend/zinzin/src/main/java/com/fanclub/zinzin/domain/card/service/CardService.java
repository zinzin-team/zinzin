package com.fanclub.zinzin.domain.card.service;

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
import com.fanclub.zinzin.domain.person.repository.PersonRepository;
import com.fanclub.zinzin.global.error.code.CardErrorCode;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import com.fanclub.zinzin.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final PersonRepository personRepository;

    @Transactional
    public void createCard(String info, List<MultipartFile> images, List<String> tags, Long memberId) {
        if (memberId == null) {
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        cardRepository.findCardByMemberId(memberId).ifPresent(existingCard -> {
            throw new BaseException(CardErrorCode.CARD_ALREADY_EXISTS);
        });

        if (images == null || images.size() != 3) {
            throw new BaseException(CardErrorCode.INVALID_NUMBER_OF_IMAGES);
        }

        if (tags == null || tags.size() != 5) {
            throw new BaseException(CardErrorCode.INVALID_NUMBER_OF_TAGS);
        }

        // card를 저장한다.
        Card card = Card.toCardEntity(member, info);
        Card newCard = cardRepository.save(card);

        // cardTags를 저장한다.
        List<CardTag> cardTags = tags.stream()
                .map(tagContent -> {
                    Tag tag = tagRepository.findByContent(tagContent)
                            .orElseThrow(() -> new BaseException(CardErrorCode.TAG_NOT_FOUND));
                    return CardTag.toCardTagEntity(newCard, tag);
                })
                .collect(Collectors.toList());
        cardTagRepository.saveAll(cardTags);

        // 요청으로 들어온 카드 이미지를 업로드하고 URL을 저장한다.
        List<CardImage> cardImages = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            if (images.get(i) == null) {
                throw new BaseException(CardErrorCode.IMAGE_UPLOAD_NOT_REQUESTED);
            }

            String newImageURL = s3Service.uploadCard(images.get(i));
            cardImages.add(CardImage.toCardImageEntity(card, newImageURL, i));
        }

        // cardImage를 저장한다.
        cardImageRepository.saveAll(cardImages);

        personRepository.saveCard(memberId, card.getId());
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
    public void updateCard(Long cardId, String info, List<MultipartFile> images, List<Integer> imageIndexes,
                           List<String> tags, Long memberId) {
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

        if (images == null) {
            images = new ArrayList<>();
        }

        if (images.size() != imageIndexes.size()) {
            throw new BaseException(CardErrorCode.INVALID_NUMBER_OF_IMAGES);
        }

        if (tags == null || tags.size() != 5) {
            throw new BaseException(CardErrorCode.INVALID_NUMBER_OF_TAGS);
        }

        // card의 info를 업데이트한다.
        card.updateInfo(info);

        // cardTags를 업데이트한다.
        List<CardTag> newCardTags = tags.stream()
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
        List<String> oldImageURLs = card.getCardImages().stream()
                .sorted(Comparator.comparingInt(CardImage::getImageNum))
                .map(CardImage::getImage)
                .toList();
        List<String> newImageURLs = new ArrayList<>(oldImageURLs.stream().toList());

        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            Integer imageIndex = imageIndexes.get(i);
            String newImageURL = s3Service.uploadCard(image);
            newImageURLs.set(imageIndex, newImageURL);
        }

        List<CardImage> newCardImages = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            newCardImages.add(CardImage.toCardImageEntity(card, newImageURLs.get(i), i));
        }

        // cardImages를 업데이트한다.
        cardImageRepository.deleteByCard(card);
        cardImageRepository.saveAll(newCardImages);

        // card를 저장한다.
        cardRepository.save(card);

        // 기존 URL에 해당하는 카드 이미지를 삭제한다.
        for (int i = 0; i < 3; i++) {
            if (oldImageURLs.get(i) != newImageURLs.get(i)) {
                s3Service.deleteS3(oldImageURLs.get(i));
            }
        }
    }
}
