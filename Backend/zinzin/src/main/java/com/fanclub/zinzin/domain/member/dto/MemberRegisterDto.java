package com.fanclub.zinzin.domain.member.dto;

import com.fanclub.zinzin.domain.member.entity.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public final class MemberRegisterDto {
    private String email;
    private String name;
    private String sub;
    private LocalDate birth;
    private Gender gender;
    private Status status;
    private Role role;
    private String profileImage;
    private String nickname;
    private String searchId;
    private MatchingVisibility matchingVisibility;
    private boolean matchingMode;

    public Member toMemberEntity() {
        return Member.builder()
                .email(email)
                .name(name)
                .sub(sub)
                .birth(birth)
                .gender(gender)
                .status(status)
                .role(role).build();
    }

    public MemberInfo toMemberInfoEntity(Member member) {
        return MemberInfo.builder()
                .profileImage(profileImage)
                .nickname(nickname)
                .searchId(searchId)
                .matchingVisibility(matchingVisibility)
                .matchingMode(matchingMode)
                .member(member)
                .build();
    }
}