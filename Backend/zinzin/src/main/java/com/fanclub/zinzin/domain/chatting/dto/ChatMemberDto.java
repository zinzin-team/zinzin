package com.fanclub.zinzin.domain.chatting.dto;

import com.fanclub.zinzin.domain.person.entity.Person;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMemberDto {

    private final Long memberId;
    private final String name;
    private final String nickname;
    private final String profileImage;

    public static ChatMemberDto fromEntity(Person person) {
        return ChatMemberDto.builder()
                .memberId(person.getMemberId())
                .nickname(person.getNickname())
                .name(person.getName())
                .profileImage(person.getProfileImage())
                .build();
    }
}
