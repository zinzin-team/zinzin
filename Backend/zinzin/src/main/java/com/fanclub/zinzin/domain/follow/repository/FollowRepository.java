package com.fanclub.zinzin.domain.follow.repository;

import com.fanclub.zinzin.domain.follow.dto.FollowingRequestResponse;
import com.fanclub.zinzin.domain.follow.dto.FollowingResponse;
import com.fanclub.zinzin.domain.person.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface FollowRepository extends Neo4jRepository<Person, String> {

    @Query("MATCH (a:Person {member_id: $memberId})-[:FOLLOW]->(b:Person), (b)-[:FOLLOW]->(a) " +
            "RETURN b.member_id AS id, b.name AS name")
    List<FollowingResponse> findPeopleByFollowRelation(Long memberId);

    @Query("MATCH (a:Person {member_id: $userMemberId}) " +
            "MATCH (b:Person {member_id: $targetMemberId}) " +
            "OPTIONAL MATCH (a)-[r:REJECT_FOLLOW|UNFOLLOW]-(b) " +
            "DELETE r " +
            "WITH a, b " +
            "OPTIONAL MATCH (a)-[r2:FOLLOW|REQUEST_FOLLOW]-(b) " +
            "WITH a, b, r2 WHERE r2 IS NULL " +
            "MERGE (a)-[:REQUEST_FOLLOW]->(b) " +
            "RETURN count(*) AS created")
    Integer createFollowRelation(Long userMemberId, Long targetMemberId);

    @Query("MATCH (a:Person {member_id: $fromMemberId})-[r:FOLLOW]-(b:Person {member_id: $toMemberId}) " +
            "RETURN count(r)")
    Integer countFollowRelation(Long fromMemberId, Long toMemberId);

    @Query("MATCH (a:Person {member_id: $fromMemberId})-[r:REQUEST_FOLLOW]->(b:Person {member_id: $toMemberId}) " +
            "RETURN count(r)")
    Integer existsRequestFollowRelation(Long fromMemberId, Long toMemberId);

    @Query("MATCH (a:Person {member_id: $fromMemberId})-[r:FOLLOW]->(b:Person {member_id: $toMemberId}) " +
            "DELETE r " +
            "WITH a, b " +
            "MATCH (b)-[r:FOLLOW]->(a) " +
            "DELETE r " +
            "CREATE (a)-[:UNFOLLOW]->(b) " +
            "RETURN count(*)")
    Integer unfollow(Long fromMemberId, Long toMemberId);

    @Query("MATCH (a:Person {member_id: $fromMemberId})-[r:REQUEST_FOLLOW]->(b:Person {member_id: $toMemberId}) " +
            "DELETE r " +
            "CREATE (a)-[:FOLLOW]->(b) " +
            "WITH a, b " +
            "MERGE (b)-[:FOLLOW]->(a) " +
            "RETURN count(*)")
    Integer acceptFollowRequest(Long fromMemberId, Long toMemberId);

    @Query("MATCH (a:Person {member_id: $fromMemberId})-[r:REQUEST_FOLLOW]->(b:Person {member_id: $toMemberId}) " +
            "DELETE r " +
            "MERGE (b)-[:REJECT_FOLLOW]->(a) " +
            "RETURN count(*)")
    Integer rejectFollowRequest(Long fromMemberId, Long toMemberId);

    @Query("MATCH (a:Person)-[:REQUEST_FOLLOW]->(b:Person{member_id: $memberId})" +
            "RETURN a.member_id AS id, a.name AS name")
    List<FollowingRequestResponse> findPeopleByRequestFollowRelation(Long memberId);
}