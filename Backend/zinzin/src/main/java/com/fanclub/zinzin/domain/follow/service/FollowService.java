package com.fanclub.zinzin.domain.follow.service;

import com.fanclub.zinzin.domain.follow.dto.FollowRequest;
import com.fanclub.zinzin.domain.follow.repository.FollowRepository;
import com.fanclub.zinzin.global.error.code.FollowErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    public void requestFollow(FollowRequest request){
        if(request.getTargetMemberId() == null || request.getUserMemberId() == null){
            throw new BaseException(FollowErrorCode.FOLLOW_INFO_NOT_FOUND);
        }

        Integer created = followRepository.createFollowRelation(request.getUserMemberId(), request.getTargetMemberId());
        if(created != null && created > 0){
            return;
        }

        throw new BaseException(FollowErrorCode.FOLLOW_RELATION_EXIST);
    }
}
