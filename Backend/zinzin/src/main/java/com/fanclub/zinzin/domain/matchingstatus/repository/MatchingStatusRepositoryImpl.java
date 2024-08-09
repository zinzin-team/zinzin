package com.fanclub.zinzin.domain.matchingstatus.repository;

import com.fanclub.zinzin.domain.matchingstatus.dto.MatchingStatus;
import com.fanclub.zinzin.domain.member.entity.Gender;
import com.fanclub.zinzin.domain.member.entity.MatchingVisibility;
import com.fanclub.zinzin.domain.person.entity.Person;
import com.fanclub.zinzin.global.error.code.MatchingErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MatchingStatusRepositoryImpl implements MatchingStatusRepository {

    @Autowired
    private Driver driver;

    @Override
    public List<MatchingStatus> getMatchingStatusList(Long memberId) {
        String query = "MATCH (me:Person {member_id: $memberId})-[:FOLLOW]->(mate1:Person) " +
                "MATCH (me)-[:FOLLOW]->(mate2:Person) " +
                "WHERE (mate1)-[:INTEREST]->(mate2) AND (mate2)-[:INTEREST]->(mate1) AND mate1.member_id < mate2.member_id " +
                "RETURN mate1, mate2";

        List<MatchingStatus> result = new ArrayList<>();

        try (Session session = driver.session()) {
            List<Record> records = session.run(query, org.neo4j.driver.Values.parameters("memberId", memberId)).list();
            for (Record record : records) {
                Node mate1Node = record.get("mate1").asNode();
                Node mate2Node = record.get("mate2").asNode();

                Person mate1 = mapNodeToPerson(mate1Node);
                Person mate2 = mapNodeToPerson(mate2Node);

                result.add(new MatchingStatus(mate1, mate2));
            }
        } catch (Exception e){
            throw new BaseException(MatchingErrorCode.ERROR_MATCHING_STATUS_LIST);
        }

        return result;
    }

    private Person mapNodeToPerson(Node node) {
        return Person.builder()
                .sub(node.get("sub").asString())
                .memberId(node.get("member_id").asLong())
                .name(node.get("name").asString())
                .birth(node.get("birth").asLocalDate())
                .gender(Gender.valueOf(node.get("gender").asString()))
                .cardId(node.get("card_id").asLong())
                .matchingMode(node.get("matching_mode").asBoolean())
                .matchingVisibility(MatchingVisibility.valueOf(node.get("matching_visibility").asString()))
                .nickname(node.get("nickname").asString())
                .profileImage(node.get("profile_image").asString())
                .build();
    }
}