package com.renx.commom.config;

import com.renx.commom.redis.RedisClient;
import com.renx.commom.redis.RedisClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {

    private Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);

    @Value("${jedis.pool.maxTotal}")
    private int maxTotal;

    @Value("${jedis.pool.maxIdle}")
    private int maxIdle;

    @Value("${jedis.pool.maxWaitMillis}")
    private int maxWaitMillis;

    @Value("${jedis.pool.host}")
    private String host;

    @Value("${jedis.pool.port}")
    private int port;

    @Value("${jedis.pool.password}")
    private String passwd;

    public RedisConfiguration(){

    }

    //redis 连接池
    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        JedisPool pool = new JedisPool(config, host, port, 3000, passwd);
        return pool;
    }

    @Bean(name = "redisClient")
    public RedisClient constructRedisClient(){
        RedisClient client = new RedisClientImpl();
        return  client;
    }

}
