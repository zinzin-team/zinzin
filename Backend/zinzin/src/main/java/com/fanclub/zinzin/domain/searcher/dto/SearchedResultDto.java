package com.fanclub.zinzin.domain.searcher.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchedResultDto {
    private final Long id;
    private final String name;
    private final String profileImagePath;
    private final List<String> relationships;
}