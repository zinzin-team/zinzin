package com.fanclub.zinzin.domain.chatting.dto;

import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberChatDto {

    private Long memberId;
    private String name;
    private String nickname;
    private String profileImage;

    public static MemberChatDto fromEntity(MemberInfo memberInfo) {
        return MemberChatDto.builder()
                .memberId(memberInfo.getMemberId())
                .nickname(memberInfo.getNickname())
                .name(memberInfo.getMemberName())
                .profileImage(memberInfo.getProfileImage())
                .build();
    }
}
