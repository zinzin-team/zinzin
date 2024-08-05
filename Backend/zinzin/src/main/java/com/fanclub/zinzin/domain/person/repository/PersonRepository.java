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

    @Query("MATCH (me:Person{member_id: $memberId})-[:FOLLOW]->(mate:Person)-[:FOLLOW]->(matching:Person) " +
            "WHERE me.gender <> matching.gender " +
            "AND NOT (matching)-[:FOLLOW]->(me) " +
            "AND matching.matching_mode = true " +
            "AND matching.card_id IS NOT NULL " +
            "AND NOT (me)-[:INTEREST|BLOCKED|FOLLOW]->(matching) " +
            "RETURN DISTINCT matching")
    List<MatchingPartner> getMatchingPartners(Long memberId);

    @Query("MATCH (me:Person{member_id: $memberId})-[:FOLLOW]->(mate:Person)-[:FOLLOW]->(matching:Person{member_id: $matchingPartnerId}) " +
            "RETURN DISTINCT mate")
    List<Mate> getMatesByMatchingPartnerId(Long memberId, Long matchingPartnerId);

    MatchingPartner getPersonByCardId(Long cardId);
}
