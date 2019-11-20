package com.incomm.vms.fileprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan("com.incomm.vms.fileprocess")
@EnableTransactionManagement
@EnableJdbcRepositories(basePackages = "com.incomm.vms.fileprocess.repository")
@EnableScheduling
public class FileProcessConsumerApplication implements CommandLineRunner {
    private static Logger LOGGER = LoggerFactory.getLogger(FileProcessConsumerApplication.class);

    public static void main(String[] args) {
        LOGGER.info("ReturnFileApplication is being started");
        SpringApplication.run(FileProcessConsumerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("FileProcessConsumerApplication is started and listneing on topic");
    }
}
