package com.fanclub.zinzin.domain.follow.service;

import com.fanclub.zinzin.domain.follow.dto.*;
import com.fanclub.zinzin.domain.follow.repository.FollowRepository;
import com.fanclub.zinzin.domain.friend.dto.Friend;
import com.fanclub.zinzin.domain.friend.repository.FriendRepository;
import com.fanclub.zinzin.global.error.code.CommonErrorCode;
import com.fanclub.zinzin.global.error.code.FollowErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final FriendRepository friendRepository;

    public List<FollowingResponse> getFollowList(Long memberId) {
        if(memberId == null){
            throw new BaseException(FollowErrorCode.FOLLOW_INFO_NOT_FOUND);
        }

        return followRepository.findPeopleByFollowRelation(memberId);
    }

    public void requestFollow(Long memberId, FollowRequest request){
        if(request.getTargetMemberId() == null || memberId == null){
            throw new BaseException(FollowErrorCode.FOLLOW_INFO_NOT_FOUND);
        }

        Integer created = followRepository.createFollowRelation(memberId, request.getTargetMemberId());
        if(created != null && created > 0){
            return;
        }

        throw new BaseException(FollowErrorCode.FOLLOW_RELATION_EXIST);
    }

    public void answerFollowRequest(Long memberId, AnswerFollowRequest request){
        if(request.getTargetMemberId() == null || memberId == null){
            throw new BaseException(FollowErrorCode.FOLLOW_INFO_NOT_FOUND);
        }

        Long targetMemberId = request.getTargetMemberId();
        Integer followRequestCnt = followRepository.existsRequestFollowRelation(targetMemberId, memberId);

        if(followRequestCnt == null || followRequestCnt == 0){
            throw new BaseException(FollowErrorCode.FOLLOW_REQUEST_NOT_FOUND);
        }

        if(request.isAccepted()){
            Integer result = followRepository.acceptFollowRequest(targetMemberId, memberId);
            if(result == null || result == 0){
                throw new BaseException(FollowErrorCode.ACCEPT_FOLLOW_ERROR);
            }
        }
        else{
            Integer result = followRepository.rejectFollowRequest(targetMemberId, memberId);
            if(result == null || result == 0){
                throw new BaseException(FollowErrorCode.REJECT_FOLLOW_ERROR);
            }
        }
    }

    public void unfollow(Long memberId, FollowRequest request) {
        if(request.getTargetMemberId() == null || memberId == null){
            throw new BaseException(FollowErrorCode.FOLLOW_INFO_NOT_FOUND);
        }

        Long targetMemberId = request.getTargetMemberId();

        Integer followRelationCnt = followRepository.countFollowRelation(memberId, targetMemberId);
        if(followRelationCnt == null || followRelationCnt != 2){
            throw new BaseException(FollowErrorCode.INVALID_FOLLOW_RELATION);
        }

        Integer a = followRepository.unfollow(memberId, targetMemberId);
        if(a == null || a == 0){
            throw new BaseException(FollowErrorCode.UNFOLLOW_ERROR);
        }
    }

    public List<FollowingRequestResponse> getFollowRequestList(Long memberId) {
        if(memberId == null){
            throw new BaseException(FollowErrorCode.FOLLOW_INFO_NOT_FOUND);
        }

        return followRepository.findPeopleByRequestFollowRelation(memberId);
    }

    public List<KakaoFriendResponse> getSocialFriendList(Long memberId){
        if(memberId == null){
            throw new BaseException(CommonErrorCode.BAD_REQUEST);
        }

        List<Friend> friends = friendRepository.getKakaoRelationships(memberId);
        return friends.stream().map(Friend::toKakaoFriendResponse).toList();
    }
}
