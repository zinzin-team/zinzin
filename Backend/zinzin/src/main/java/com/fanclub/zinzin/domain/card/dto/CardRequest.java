package com.fanclub.zinzin.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class CardRequest {
    private String info;
    private List<MultipartFile> images;
    private List<String> tags;
}
