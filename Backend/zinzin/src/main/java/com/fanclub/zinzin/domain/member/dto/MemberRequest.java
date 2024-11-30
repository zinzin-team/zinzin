package com.fanclub.zinzin.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberRequest {
    private String searchId;
    private String a; // 필드가 2개 이상이어야 JSON parsing 오류가 발생하지 않으므로 가짜 필드를 넣었다.
}
