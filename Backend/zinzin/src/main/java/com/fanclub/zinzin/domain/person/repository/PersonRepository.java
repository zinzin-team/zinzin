package com.fanclub.zinzin.domain.person.repository;

import com.fanclub.zinzin.domain.matchingstatus.repository.MatchingStatusRepository;
import com.fanclub.zinzin.domain.person.dto.MatchingPartner;
import com.fanclub.zinzin.domain.person.dto.Mate;
import com.fanclub.zinzin.domain.person.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person, String>, MatchingStatusRepository {

    Optional<Person> findPersonByMemberId(Long memberId);

    @Query("MATCH (me:Person {member_id:$memberId}) " +
            "SET me.nickname = $randomNickname")
    void updateNicknameByMemberId(Long memberId, String randomNickname);

    @Query("MATCH (me:Person {member_id:$memberId}) " +
            "SET me.profile_image = $profileImage")
    void updateProfilImage(Long memberId, String profileImage);

    @Query("MATCH (me:Person {sub: $mySub}) " +
            "MERGE (friend:Person {sub: $friendSub}) " +
            "MERGE (me)-[r:KAKAO]->(friend) " +
            "ON CREATE SET r.kakao_name = $nickname")
    void saveKakaoFriends(String mySub, String friendSub, String nickname);

    @Query("MATCH (p:Person {member_id: $memberId}) SET p.matching_mode = $matchingMode")
    void updateMatchingMode(Long memberId, boolean matchingMode);

    @Query("MATCH (me:Person {member_id: $memberId})-[:FOLLOW]->(mate:Person)-[:FOLLOW]->(matching:Person) " +
            "WHERE me.gender <> matching.gender " +
            "AND matching.matching_mode = true " +
            "AND matching.card_id IS NOT NULL " +
            "AND NOT (me)-[:INTEREST|FOLLOW]->(matching) " +
            "AND NOT (me)-[:REJECT_FOLLOW|BLOCKED|REQUEST_FOLLOW|UNFOLLOW]-(matching)" +
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

    @Query("MATCH (me:Person {member_id:$memberId})-[r:GET_CARD_OF]->(matchingPartner:Person{card_id:$cardId}) " +
            "DELETE r " +
            "CREATE (me)-[:INTEREST]->(matchingPartner) " +
            "RETURN EXISTS((matchingPartner)-[:INTEREST]->(me))")
    boolean interestCard(Long memberId, Long cardId);

    @Query("MATCH (me:Person {member_id:$memberId})-[r:GET_CARD_OF]->(matchingPartner:Person{card_id:$cardId}) " +
            "SET r.rejectCnt = r.rejectCnt + 1 " +
            "WITH me, r, matchingPartner "+
            "WHERE r.rejectCnt >= 2 " +
            "DELETE r " +
            "CREATE (me)-[:BLOCKED]->(matchingPartner)")
    void rejectCard(Long memberId, Long cardId);

    @Query("MATCH (me:Person {sub:$sub}) " +
            "REMOVE me.member_id, me.name, me.birth, me.gender, me.card_id, me.matching_mode, me.matching_visibility, me.nickname, me.profile_image " +
            "WITH me " +
            "MATCH (me)-[r]-() " +
            "WHERE type(r) <> 'KAKAO' " +
            "DELETE r ")
    void withdraw(String sub);

    @Query("MATCH (me:Person {member_id:$memberId}) " +
            "SET me.card_id = $cardId")
    void saveCard(Long memberId, Long cardId);

    @Query("MATCH (me:Person {member_id:$memberId})-[r:INTEREST|GET_CARD_OF]-(target:Person {member_id:$target}) " +
            "DELETE r " +
            "MERGE (me)-[:BLOCKED]->(target)")
    void report(Long memberId, Long target);

    @Query("MATCH (me:Person {member_id:$memberId})-[r:INTEREST]-(target:Person {member_id:$target}) " +
            "delete r " +
            "merge (me)-[:BLOCKED]->(target)")
    void exitChatroom(Long memberId, Long target);
}
