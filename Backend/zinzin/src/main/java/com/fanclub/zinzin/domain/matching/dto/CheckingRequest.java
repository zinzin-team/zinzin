package com.fanclub.zinzin.domain.matching.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CheckingRequest {
    private final Long cardId;
    private final boolean like;
}
