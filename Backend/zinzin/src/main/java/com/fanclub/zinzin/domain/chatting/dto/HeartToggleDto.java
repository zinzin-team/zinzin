package com.fanclub.zinzin.domain.chatting.dto;


import com.fanclub.zinzin.domain.person.dto.Mate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HeartToggleDto {
    private boolean heart;
    private List<Mate> mates;
}
