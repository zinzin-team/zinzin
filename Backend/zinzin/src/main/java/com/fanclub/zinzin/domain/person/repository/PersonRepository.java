package com.fanclub.zinzin.domain.person.repository;

import com.fanclub.zinzin.domain.person.dto.MatchingPartner;
import com.fanclub.zinzin.domain.person.dto.Mate;
import com.fanclub.zinzin.domain.person.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    @Query("MATCH (p:Person {member_id: $memberId}) SET p.matching_mode = $matchingMode")
    void updateMatchingMode(Long memberId, boolean matchingMode);

    @Query("MATCH (me:Person {member_id: $memberId})-[:FOLLOW]->(mate:Person)-[:FOLLOW]->(matching:Person) " +
            "WHERE me.gender <> matching.gender " +
            "AND matching.matching_mode = true " +
            "AND matching.card_id IS NOT NULL " +
            "AND NOT (me)-[:INTEREST|BLOCKED|FOLLOW]->(matching) " +
            "AND NOT (me)-[:REJECT_FOLLOW|REQUEST_FOLLOW|UNFOLLOW]-(matching)" +
            "OPTIONAL MATCH (me)-[r:GET_CARD_OF]->(matching) " +
            "WITH matching, r " +
            "ORDER BY CASE WHEN r IS NULL THEN 0 ELSE 1 END, r.rejectCnt " +
            "RETURN DISTINCT matching")
    List<MatchingPartner> getMatchingPartners(Long memberId);

    @Query("MATCH (me:Person{member_id: $memberId})-[:FOLLOW]->(mate:Person)-[:FOLLOW]->(matching:Person{member_id: $matchingPartnerId}) " +
            "RETURN DISTINCT mate")
    List<Mate> getMatesByMatchingPartnerId(Long memberId, Long matchingPartnerId);

    MatchingPartner getPersonByCardId(Long cardId);

    @Query("MATCH (me:Person {member_id: $memberId}) " +
            "MATCH (matching:Person {member_id: $matchingPartnerId}) " +
            "MERGE (me)-[r:GET_CARD_OF]->(matching) " +
            "ON CREATE SET r.rejectCnt = 0 " +
            "RETURN r")
    void updateRecommendedRelation(Long memberId, Long matchingPartnerId);
}
