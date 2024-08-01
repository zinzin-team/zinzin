package com.fanclub.zinzin.domain.searcher.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SearcherResponse {
    private boolean success;
    private SearchedMemberDto member;

    @Builder
    private SearcherResponse (boolean success, SearchedMemberDto member){
        this.success = success;
        this.member = member;
    }

    public static SearcherResponse of(boolean success, SearchedMemberDto member){
        return SearcherResponse.builder()
                .success(success)
                .member(member)
                .build();
    }
}
