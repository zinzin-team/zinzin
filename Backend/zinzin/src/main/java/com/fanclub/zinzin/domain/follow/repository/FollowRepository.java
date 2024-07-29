package com.fanclub.zinzin.domain.follow.repository;

import com.fanclub.zinzin.domain.person.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface FollowRepository extends Neo4jRepository<Person, Long> {
    @Query("MATCH (a:Person {member_id: $userMemberId})" +
            "MATCH (b:Person {member_id: $targetMemberId}) " +
            "OPTIONAL MATCH (a)-[r:FOLLOW|REQUEST_FOLLOW|REJECT_FOLLOW|UNFOLLOW]-(b) " +
            "WITH a, b, r WHERE r IS NULL " +
            "MERGE (a)-[:REQUEST_FOLLOW]->(b)" +
            "RETURN count(*) AS created")
    Integer createFollowRelation(Long userMemberId, Long targetMemberId);
}