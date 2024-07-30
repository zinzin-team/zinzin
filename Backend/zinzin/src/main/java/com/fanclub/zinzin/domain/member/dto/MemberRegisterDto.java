package com.fanclub.zinzin.domain.member.dto;

import com.fanclub.zinzin.domain.member.entity.Gender;
import com.fanclub.zinzin.domain.member.entity.MatchingVisibility;
import com.fanclub.zinzin.domain.member.entity.Role;
import com.fanclub.zinzin.domain.member.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegisterDto {
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
}
