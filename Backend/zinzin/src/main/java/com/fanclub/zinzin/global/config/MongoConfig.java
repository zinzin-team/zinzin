package com.fanclub.zinzin.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import jakarta.annotation.PostConstruct;

@Configuration
public class MongoConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void initIndexes() {
        mongoTemplate.indexOps("chatting_content").ensureIndex(
                new org.springframework.data.mongodb.core.index.Index()
                        .on("roomId", org.springframework.data.domain.Sort.Direction.ASC)
                        .on("timestamp", org.springframework.data.domain.Sort.Direction.DESC)
        );
    }
}