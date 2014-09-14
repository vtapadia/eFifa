package com.vtapadia.fifa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * MAIN CLASS - Starts the application
 *
 * Following properties are required to be set
 *  eFifa.environment : (default:local) local/prod/test
 *  eFifa.db.url : URL of the database
 *  eFifa.db.username : self explanatory
 *  eFifa.db.password : self explanatory
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.vtapadia.fifa"})
public class StartApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(StartApplication.class, args);
    }
}