package com.fanclub.zinzin.domain.friend.repository;

import com.fanclub.zinzin.domain.friend.dto.Friend;
import com.fanclub.zinzin.domain.person.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface FriendRepository extends Neo4jRepository<Person, String> {
@Query("MATCH (me:Person{member_id:$memberId})-[kakao:KAKAO]->(person:Person) " +
        "OPTIONAL MATCH (me)-[fr]->(person) " +
        "OPTIONAL MATCH (person)-[rf:REQUEST_FOLLOW]->(me) " +
        "WITH person, kakao.kakao_name AS kakaoName, rf, " +
        "[rel IN collect(type(fr)) WHERE rel <> 'KAKAO' AND rel IN ['REQUEST_FOLLOW','FOLLOW']] as rels " +
        "RETURN person, kakaoName, rels " +
        "+ CASE WHEN rf IS NOT NULL THEN [type(rf)] "+
        "WHEN person.member_id IS NOT NULL AND size(rels) = 0 THEN ['MEMBER'] "+
        "ELSE [] END AS relationships")
    List<Friend> getKakaoRelationships(Long memberId);
}