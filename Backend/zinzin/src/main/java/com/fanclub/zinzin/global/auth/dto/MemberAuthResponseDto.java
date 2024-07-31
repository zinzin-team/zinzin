package com.fanclub.zinzin.global.auth.dto;

import com.fanclub.zinzin.domain.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberAuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private boolean isUser;
    private String sub;
    private Role role;

    @Builder(builderMethodName = "tokenBuilder")
    public MemberAuthResponseDto(String accessToken, String refreshToken, boolean isUser) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isUser = isUser;
    }

    @Builder(builderMethodName = "registerBuilder")
    public MemberAuthResponseDto(String sub, Role role, boolean isUser) {
        this.sub = sub;
        this.role = role;
        this.isUser = isUser;
    }
}
