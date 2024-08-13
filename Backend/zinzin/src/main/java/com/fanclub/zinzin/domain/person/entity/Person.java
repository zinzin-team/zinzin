package com.fanclub.zinzin.domain.person.entity;

import com.fanclub.zinzin.domain.matchingstatus.dto.MatchingMate;
import com.fanclub.zinzin.domain.member.entity.Gender;
import com.fanclub.zinzin.domain.member.entity.MatchingVisibility;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.LocalDate;

@Node("Person")
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class Person {

    @Id
    private String sub;

    @Getter
    @Property(name = "member_id")
    private Long memberId;

    @Getter
    private String name;

    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Property(name = "card_id")
    private Long cardId;

    @Property(name = "matching_mode")
    private boolean matchingMode;

    @Property(name = "matching_visibility")
    private MatchingVisibility matchingVisibility;

    @Property(name = "search_id")
    private String searchId;

    @Getter
    private String nickname;

    @Getter
    @Property(name = "profile_image")
    private String profileImage;

    @Builder
    private Person(String sub, Long memberId, String name, LocalDate birth, Gender gender, Long cardId, boolean matchingMode, MatchingVisibility matchingVisibility, String searchId, String nickname, String profileImage) {
        this.sub = sub;
        this.memberId = memberId;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.cardId = cardId;
        this.matchingMode = matchingMode;
        this.matchingVisibility = matchingVisibility;
        this.searchId = searchId;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public MatchingMate toMatchingMate(){
        if(this.matchingVisibility == MatchingVisibility.PRIVATE){
            return MatchingMate.of(this.nickname, null);
        }

        return MatchingMate.of(this.name, this.profileImage);
    }

    @Override
    public String toString() {
        return "Peron{"+
                "sub="+sub+
                ", memberId="+memberId+
                ", profileImage="+profileImage+
                ", birth="+birth+
                ", name="+name+
                ", gender="+gender+
                ", cardId="+cardId+
                ", matchingMode="+matchingMode+
                ", matchingVisibility="+matchingVisibility+
                "}";
    }

}

