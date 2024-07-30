package com.fanclub.zinzin.domain.follow.dto;

import lombok.Getter;

@Getter
public class FollowRequest {
    private Long userMemberId;
    private Long targetMemberId;
}
