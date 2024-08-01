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
public class Person {

    @Id
    @Property(name = "member_id")
    private Long memberId;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Property(name = "card_id")
    private Long cardId;

    @Property(name = "matching_mode")
    private boolean matchingMode;

    private String nickname;

    @Property(name = "profile_image")
    private String profileImage;

    @Builder
    private Person(Long memberId, String name, Gender gender, Long cardId, boolean matchingMode, String nickname, String profileImage) {
        this.memberId = memberId;
        this.name = name;
        this.gender = gender;
        this.cardId = cardId;
        this.matchingMode = matchingMode;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

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

