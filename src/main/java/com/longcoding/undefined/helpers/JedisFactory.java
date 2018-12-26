package com.longcoding.undefined.helpers;

import com.longcoding.undefined.configs.JedisConfig;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by longcoding on 16. 4. 7..
 * Updated by longcoding on 18. 12. 26..
 */
@Component
@AllArgsConstructor
@EnableConfigurationProperties(JedisConfig.class)
public class JedisFactory {

    private static final Logger logger = LogManager.getLogger(JedisFactory.class);

    private JedisConfig jedisConfig;

    private static JedisPool jedisPool;

    @PostConstruct
    private void initializeJedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(jedisConfig.getMaxTotal());
        jedisPoolConfig.setMaxWaitMillis(jedisConfig.getMaxWaitMillis());
        jedisPoolConfig.setMaxIdle(jedisConfig.getMaxidle());
        jedisPoolConfig.setMinIdle(jedisConfig.getMinidle());
        jedisPoolConfig.setNumTestsPerEvictionRun(jedisConfig.getNumTestsPerEvictionRun());
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(jedisConfig.getTimeBetweenEvictionRunsMillis());
        jedisPoolConfig.setBlockWhenExhausted(jedisConfig.isBlockWhenExhausted());
        jedisPoolConfig.setTestOnBorrow(jedisConfig.isTestOnBorrow());
        jedisPoolConfig.setTestOnReturn(jedisConfig.isTestOnReturn());
        jedisPoolConfig.setTestWhileIdle(jedisConfig.isTestWhileIdle());

        String jedisHost = jedisConfig.getHost();
        int jedisPort = jedisConfig.getPort();
        int jedisTimeout = jedisConfig.getTimeout();

        jedisPool = new JedisPool(jedisPoolConfig, jedisHost, jedisPort, jedisTimeout);
    }

    public Jedis getInstance() {
        return jedisPool.getResource();
    }

    @PreDestroy
    private void releaseResource() {
        jedisPool.close();
    }

}
