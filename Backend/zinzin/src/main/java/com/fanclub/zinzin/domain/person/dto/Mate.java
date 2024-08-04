package com.fanclub.zinzin.domain.person.dto;

import lombok.Getter;

@Getter
public class Mate {
    private final Long memberId;
    private final String name;
    private final String profileImage;

    public Mate(Long memberId, String name, String profileImage){
        this.memberId = memberId;
        this.name = name;
        this.profileImage = profileImage;
    }
}
