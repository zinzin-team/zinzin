package com.fanclub.zinzin.domain.matchingstatus.dto;

import com.fanclub.zinzin.domain.person.entity.Person;
import lombok.*;


@Getter
@AllArgsConstructor
public class MatchingStatus {
    private final Person mate1;
    private final Person mate2;
}
