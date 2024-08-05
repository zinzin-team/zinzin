package com.fanclub.zinzin.global.auth.dto;

import com.fanclub.zinzin.domain.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class MemberAuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private boolean isUser;
    private String email;
    private String sub;
    private Role role;

    @Builder(builderClassName = "tokenBuilder", builderMethodName = "tokenBuilder")
    public MemberAuthResponseDto(String accessToken, String refreshToken, boolean isUser) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isUser = isUser;
    }

    @Builder(builderClassName = "registerBuilder", builderMethodName = "registerBuilder")
    public MemberAuthResponseDto(String sub, Role role, boolean isUser, String email) {
        this.sub = sub;
        this.role = role;
        this.isUser = isUser;
        this.email = email;
    }

    public static MemberAuthResponseDto createTokenResponse(String accessToken, String refreshToken) {
        return tokenBuilder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isUser(true)
                .build();
    }

    public static MemberAuthResponseDto createRegisterResponse(String sub, String email) {
        return registerBuilder()
                .sub(sub)
                .role(Role.USER)
                .isUser(false)
                .email(email)
                .build();
    }
}
