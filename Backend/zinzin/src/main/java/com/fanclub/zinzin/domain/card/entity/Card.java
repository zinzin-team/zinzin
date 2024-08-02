package com.fanclub.zinzin.domain.card.entity;

import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String info;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    public void setInfo(String info) {
        this.info = info;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public static Card createCard(String info, Member member) {
        Card card = new Card();
        card.setMember(member);
        card.setInfo(info);
        return card;
    }
}
