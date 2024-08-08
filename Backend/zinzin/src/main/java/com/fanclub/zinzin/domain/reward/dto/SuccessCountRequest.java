package com.fanclub.zinzin.domain.reward.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuccessCountRequest {
    private Long targetId;
    private Long roomId;
}
