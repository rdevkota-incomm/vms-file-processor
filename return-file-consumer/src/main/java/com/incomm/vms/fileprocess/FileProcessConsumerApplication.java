package com.incomm.vms.fileprocess;

import com.incomm.vms.fileprocess.model.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan("com.incomm.vms.fileprocess")
@EnableTransactionManagement
@EnableJdbcRepositories(basePackages = "com.incomm.vms.fileprocess.repository")
@EnableCaching
@EnableScheduling
public class FileProcessConsumerApplication implements CommandLineRunner {
    private static Logger LOGGER = LoggerFactory.getLogger(FileProcessConsumerApplication.class);

    public static void main(String[] args) {
        LOGGER.info("ReturnFileApplication is being started");
        SpringApplication.run(FileProcessConsumerApplication.class, args);
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory(){
        return new JedisConnectionFactory();
    }

    @Bean
    RedisTemplate<String, RedisCache> redisTemplate(){
        RedisTemplate<String,RedisCache> redisTemplate = new RedisTemplate<String, RedisCache>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("FileProcessConsumerApplication is started and listneing on topic");
    }
}
