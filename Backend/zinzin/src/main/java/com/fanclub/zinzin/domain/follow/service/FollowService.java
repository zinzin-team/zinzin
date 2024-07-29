package com.fanclub.zinzin.domain.follow.service;

import com.fanclub.zinzin.domain.follow.dto.AnswerFollowRequest;
import com.fanclub.zinzin.domain.follow.dto.FollowRequest;
import com.fanclub.zinzin.domain.follow.repository.FollowRepository;
import com.fanclub.zinzin.global.error.code.CommonErrorCode;
import com.fanclub.zinzin.global.error.code.FollowErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void answerFollowRequest(AnswerFollowRequest request){
        if(request.getTargetMemberId() == null || request.getUserMemberId() == null){
            throw new BaseException(FollowErrorCode.FOLLOW_INFO_NOT_FOUND);
        }

        Long userMemberId = request.getUserMemberId();
        Long targetMemberId = request.getTargetMemberId();
        Integer followRequestCnt = followRepository.existsRequestFollowRelation(targetMemberId, userMemberId);

        if(followRequestCnt == null || followRequestCnt == 0){
            throw new BaseException(FollowErrorCode.FOLLOW_REQUEST_NOT_FOUND);
        }

        if(request.isAccepted()){
            Integer result = followRepository.acceptFollowRequest(targetMemberId, userMemberId);
            if(result == null || result == 0){
                throw new BaseException(FollowErrorCode.ACCEPT_FOLLOW_ERROR);
            }
        }
        else{
            Integer result = followRepository.rejectFollowRequest(targetMemberId, userMemberId);
            if(result == null || result == 0){
                throw new BaseException(FollowErrorCode.REJECT_FOLLOW_ERROR);
            }
        }
    }
}
