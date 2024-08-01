package com.fanclub.zinzin.domain.member.dto;

import com.fanclub.zinzin.domain.member.entity.MatchingVisibility;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MatchingModeRequest {
    private final boolean matchingMode;
    private final MatchingVisibility matchingVisibility;
}
