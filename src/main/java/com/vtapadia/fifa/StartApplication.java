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
 *  fifa.environment : dev/prod
 *  fifa.db.url : URL of the database
 *  fifa.db.username : self explanatory
 *  fifa.db.password : self explanatory
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