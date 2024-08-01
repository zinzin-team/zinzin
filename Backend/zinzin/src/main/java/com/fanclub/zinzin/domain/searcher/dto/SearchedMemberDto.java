package com.fanclub.zinzin.domain.searcher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SearchedMemberDto {
    private Long id;
    private String name;
    private String profileImage;
}