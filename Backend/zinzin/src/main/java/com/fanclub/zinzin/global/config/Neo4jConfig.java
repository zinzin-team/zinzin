//package com.fanclub.zinzin.global.config;
//
//import org.neo4j.driver.Driver;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@Configuration
//@EnableTransactionManagement
//public class Neo4jConfig {
//
//    private final Driver driver;
//
//    public Neo4jConfig(Driver driver) {
//        this.driver = driver;
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager(Driver driver) {
//        return new Neo4jTransactionManager(driver);
//    }
//}