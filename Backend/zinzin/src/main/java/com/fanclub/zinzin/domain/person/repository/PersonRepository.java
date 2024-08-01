package com.fanclub.zinzin.domain.person.repository;

import com.fanclub.zinzin.domain.person.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    @Query("MATCH (p:Person {member_id: $memberId}) SET p.matching_mode = $matchingMode")
    void updateMatchingMode(Long memberId, boolean matchingMode);
}
