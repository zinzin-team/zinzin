package com.fanclub.zinzin.global.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class MemberDto {
    private String sub;
    private String email;
    private String role;
}
