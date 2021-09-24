package com.sroka.grouptripsorganizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GroupTripsOrganizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GroupTripsOrganizerApplication.class, args);
    }
}
