package com.fanclub.zinzin.domain.chatting.dto;

import com.fanclub.zinzin.domain.chatting.entity.ChatRoomMember;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoomType;
import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberChatDto {

    private Long memberId;
    private String nickname;
    private String profileImage;

    public static MemberChatDto fromEntity (MemberInfo memberInfo) {
        return MemberChatDto.builder()
                .memberId(memberInfo.getMemberId())
                .nickname(memberInfo.getNickname())
                .profileImage(memberInfo.getProfileImage())
                .build();
    }
}
