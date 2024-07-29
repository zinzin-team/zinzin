package com.fanclub.zinzin.domain.follow.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AnswerFollowRequest {
    private Long userMemberId;
    private Long targetMemberId;
    private boolean accepted;
}
