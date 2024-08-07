package com.fanclub.zinzin.domain.chatting.dto;

import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMemberDto {

    private final Long memberId;
    private final String name;
    private final String nickname;
    private final String profileImage;

    public static ChatMemberDto fromEntity(MemberInfo memberInfo) {
        return ChatMemberDto.builder()
                .memberId(memberInfo.getMemberId())
                .nickname(memberInfo.getNickname())
                .name(memberInfo.getMemberName())
                .profileImage(memberInfo.getProfileImage())
                .build();
    }
}
