package com.fanclub.zinzin.domain.card.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CardRequest {
    private String info;
    private List<String> images;
    private List<String> tags;
}
