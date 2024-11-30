package com.fanclub.zinzin.domain.card.entity;

import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "card")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Card extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String info;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @OneToMany(mappedBy = "card")
    private List<CardImage> cardImages;

    @OneToMany(mappedBy = "card")
    private List<CardTag> cardTags;

    @Builder
    private Card(Member member, String info) {
        this.member = member;
        this.info = info;
    }

    public static Card toCardEntity(Member member, String info) {
        return Card.builder()
                .member(member)
                .info(info)
                .build();
    }

    public void updateInfo(String info) {
        this.info = info;
    }
}
