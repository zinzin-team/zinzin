package com.fanclub.zinzin.domain.chatting.dto;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class HeartToggleDto {
    private boolean isHeart;

    public boolean getIsHeart() {
        return isHeart;
    }
}
