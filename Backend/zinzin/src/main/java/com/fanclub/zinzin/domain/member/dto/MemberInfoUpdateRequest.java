package com.fanclub.zinzin.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class MemberInfoUpdateRequest {
    private final MultipartFile profileImage;
    private final String searchId;
}
