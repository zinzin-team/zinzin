package com.fanclub.zinzin.domain.person.entity;

import com.fanclub.zinzin.domain.member.entity.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("Person")
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor
public class Person {

    @Id
    private Long id;

    @Property(name = "member_id")
    private Long memberId;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Property(name = "card_id")
    private Long cardId;

    @Property(name = "matching_mode")
    private boolean matchingMode;

//    @Builder
//    public Person(Long memberId, Gender gender, Long cardId, boolean matchingMode){
//        this.memberId = memberId;
//        this.gender = gender;
//        this.cardId = cardId;
//        this.matchingMode = matchingMode;
//    }

    @Override
    public String toString() {
        return "Peron{"+
                "memberId="+memberId+
                ", name="+name+
                ", gender="+gender+
                ", cardId="+cardId+
                ", matchingMode="+matchingMode+
                "}";
    }
}

