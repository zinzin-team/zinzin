package com.fanclub.zinzin.global.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    private String sub;
    private String email;
    private String role;

}
