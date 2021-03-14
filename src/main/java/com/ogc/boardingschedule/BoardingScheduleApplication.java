package com.ogc.boardingschedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.ogc.boardingschedule" })
public class BoardingScheduleApplication {
    public static void main(String[] args) {
        SpringApplication.run(BoardingScheduleApplication.class, args);
    }
}

