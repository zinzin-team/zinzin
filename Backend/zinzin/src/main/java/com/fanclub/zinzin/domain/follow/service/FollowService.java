package com.fanclub.zinzin.domain.follow.service;

import com.fanclub.zinzin.domain.follow.dto.AnswerFollowRequest;
import com.fanclub.zinzin.domain.follow.dto.FollowingListRequest;
import com.fanclub.zinzin.domain.follow.dto.FollowRequest;
import com.fanclub.zinzin.domain.follow.dto.FollowingResponse;
import com.fanclub.zinzin.domain.follow.repository.FollowRepository;
import com.fanclub.zinzin.global.error.code.FollowErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    public List<FollowingResponse> getFollowList(FollowingListRequest request) {
        if(request.getUserMemberId() == null){
            throw new BaseException(FollowErrorCode.FOLLOW_INFO_NOT_FOUND);
        }

        return followRepository.findPeopleByFollowRelation(request.getUserMemberId());
    }

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

    public void unfollow(FollowRequest request) {
        if(request.getTargetMemberId() == null || request.getUserMemberId() == null){
            throw new BaseException(FollowErrorCode.FOLLOW_INFO_NOT_FOUND);
        }

        Long userMemberId = request.getUserMemberId();
        Long targetMemberId = request.getTargetMemberId();

        Integer followRelationCnt = followRepository.countFollowRelation(userMemberId, targetMemberId);
        if(followRelationCnt == null || followRelationCnt != 2){
            throw new BaseException(FollowErrorCode.INVALID_FOLLOW_RELATION);
        }

        Integer a = followRepository.unfollow(userMemberId, targetMemberId);
        if(a == null || a == 0){
            throw new BaseException(FollowErrorCode.UNFOLLOW_ERROR);
        }
    }
}
