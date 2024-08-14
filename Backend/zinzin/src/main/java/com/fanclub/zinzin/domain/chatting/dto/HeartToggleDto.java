package com.fanclub.zinzin.domain.chatting.dto;


import com.fanclub.zinzin.domain.person.dto.Mate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class HeartToggleDto {
    private boolean isHeart;
    private List<Mate> mates;

    public boolean getIsHeart() {
        return isHeart;
    }
}
