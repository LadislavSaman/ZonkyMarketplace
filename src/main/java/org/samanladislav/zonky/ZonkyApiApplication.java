package org.samanladislav.zonky;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application main class.
 */
@SpringBootApplication
@EnableScheduling
public class ZonkyApiApplication {

    private static final Logger LOG = LoggerFactory.getLogger(ZonkyApiApplication.class);

    public static void main(String[] args) {
        LOG.info("Application is starting");
        SpringApplication.run(ZonkyApiApplication.class);
    }
}
