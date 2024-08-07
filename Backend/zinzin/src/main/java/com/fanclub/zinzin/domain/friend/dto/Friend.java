package com.fanclub.zinzin.domain.friend.dto;

import com.fanclub.zinzin.domain.follow.dto.KakaoFriendResponse;
import com.fanclub.zinzin.domain.person.entity.Person;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@RequiredArgsConstructor
public class Friend {
    private final Person person;
    private final String kakaoName;
    private final List<String> relationships;

    public KakaoFriendResponse toKakaoFriendResponse(){
        return KakaoFriendResponse.of(person.getMemberId(), person.getProfileImage(), kakaoName, (relationships.isEmpty())?null:relationships.get(0));
    }
}
