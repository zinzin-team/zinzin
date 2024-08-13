package com.fanclub.zinzin.domain.searcher.repository;

import com.fanclub.zinzin.domain.person.entity.Person;
import com.fanclub.zinzin.domain.searcher.dto.SearchedResultDto;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface SearcherRepository extends Neo4jRepository<Person, String> {
    @Query("MATCH (me:Person{member_id:$memberId}) " +
            "OPTIONAL MATCH (person:Person{search_id:$searchId}) WHERE me <> person " +
            "OPTIONAL MATCH (me)-[fr]->(person) " +
            "OPTIONAL MATCH (person)-[rf:REQUEST_FOLLOW]->(me) " +
            "WITH person, rf, " +
            "[rel IN collect(type(fr)) WHERE rel <> 'KAKAO' AND rel IN ['REQUEST_FOLLOW', 'FOLLOW']] AS rels " +
            "RETURN person.member_id AS id, person.name AS name, person.profile_image AS profileImagePath, rels " +
            "+ CASE WHEN rf IS NOT NULL THEN ['RECEIVE_REQUEST'] "+
            "WHEN person.member_id IS NOT NULL AND size(rels) = 0 THEN ['MEMBER'] " +
            "ELSE [] END AS relationships")
    SearchedResultDto findPersonBySearchId(Long memberId, String searchId);
}