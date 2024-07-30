package com.fanclub.zinzin.domain.follow.dto;

import lombok.Getter;

@Getter
public class AnswerFollowRequest {
    private Long userMemberId;
    private Long targetMemberId;
    private boolean accepted;
}
