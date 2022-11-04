package com.springcloud.common.redis;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Data
//@RefreshScope
public class RedisParameter {
    @Value("${spring.redis.sentinel.nodes}")
    private Set<String> nodes;
    @Value("${spring.redis.sentinel.master}")
    private String master;
    @Value("${spring.redis.timeout}")
    private long timeUut;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.lettuce.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.lettuce.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.lettuce.pool.max-wait}")
    private long maxWait;
    @Value("${spring.redis.lettuce.pool.max-active}")
    private int maxActive;
    @Value("${spring.redis.database}")
    private int dataBase;
    @Value("${spring.redis.hostserver}")
    private String hostServer;
    @Value("${spring.redis.perKey}")
    private String perKey;
}
