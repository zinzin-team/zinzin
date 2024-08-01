package com.fanclub.zinzin.domain.member.dto;

import com.fanclub.zinzin.domain.member.entity.*;
import com.fanclub.zinzin.domain.person.entity.Person;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MemberRegisterDto {
    private final String email;
    private final String name;
    private final String sub;
    private final LocalDate birth;
    private final Gender gender;
    private String nickname;
    private final String searchId;
    private final MatchingVisibility matchingVisibility;
    private final boolean matchingMode;

    public Member toMemberEntity() {
        return Member.builder()
                .email(email)
                .name(name)
                .sub(sub)
                .birth(birth)
                .gender(gender)
                .build();
    }

    public MemberInfo toMemberInfoEntity(Member member, String nickname) {
        return MemberInfo.builder()
                .nickname(nickname)
                .searchId(searchId)
                .matchingVisibility(matchingVisibility)
                .matchingMode(matchingMode)
                .member(member)
                .build();
    }

    public Person toPersonEntity(Member member, MemberInfo memberInfo) {
        return Person.builder()
                .memberId(member.getId())
                .name(name)
                .gender(gender)
                .matchingMode(matchingMode)
                .nickname(nickname)
                .profileImage(memberInfo.getProfileImage())
                .build();
    }
}