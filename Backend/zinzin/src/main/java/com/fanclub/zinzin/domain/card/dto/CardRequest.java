package com.fanclub.zinzin.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CardRequest {
    private String info;
    private List<String> tags;
    private List<Integer> imageIndexes;
}
