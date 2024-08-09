package com.fanclub.zinzin.domain.matchingstatus.repository;

import com.fanclub.zinzin.domain.matchingstatus.dto.MatchingStatus;

import java.util.List;

public interface MatchingStatusRepository {
    List<MatchingStatus> getMatchingStatusList(Long memberId);
}
