package com.springcloud.common.redis;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Autowired
    private RedisParameter redisParameter;

    @Bean
    public RedisConnectionFactory lettuceConnectionFactory() {

        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(redisParameter.getMaxIdle());
        genericObjectPoolConfig.setMinIdle(redisParameter.getMinIdle());
        genericObjectPoolConfig.setMaxTotal(redisParameter.getMaxActive());
        genericObjectPoolConfig.setMaxWaitMillis(redisParameter.getMaxWait());
        LettucePoolingClientConfiguration lettuceClientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig)
                .build();
        if (!StrUtil.isEmpty(redisParameter.getHostServer())) {
            RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
            standaloneConfiguration.setDatabase(redisParameter.getDataBase());
            standaloneConfiguration.setHostName(redisParameter.getHostServer().split(":")[0]);
            standaloneConfiguration.setPassword(redisParameter.getPassword());
            standaloneConfiguration.setPort(Integer.parseInt(redisParameter.getHostServer().split(":")[1]));
            return new LettuceConnectionFactory(standaloneConfiguration, lettuceClientConfiguration);
        } else {
            RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration(redisParameter.getMaster(), redisParameter.getNodes());
            redisSentinelConfiguration.setPassword(RedisPassword.of(redisParameter.getPassword().toCharArray()));
            redisSentinelConfiguration.setDatabase(redisParameter.getDataBase());
            return new LettuceConnectionFactory(redisSentinelConfiguration, lettuceClientConfiguration);
        }
    }

    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

}