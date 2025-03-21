package com.deng.pp.db.config;

import com.deng.pp.Constants;
import com.deng.pp.entity.ProxyEntity;
import com.deng.pp.utils.PropsUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Protocol;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Created by hcdeng on 17-7-3.
 */
@Slf4j
public class RedisConfiguration {

    private static final Properties REDIS_PROPS = PropsUtil.loadProps(Constants.REDIS_PROPS);

    private static final RedisConfiguration config = new RedisConfiguration();

    public static RedisTemplate<String, ProxyEntity> getRedisTemplate(){
        return config.redisTemplate();
    }

    private JedisConnectionFactory jedisConnFactory() {
        try {

            String redistogoUrl = REDIS_PROPS.getProperty(Constants.REDIS_URL);
            URI redistogoUri = new URI(redistogoUrl);

            JedisConnectionFactory jedisConnFactory = new JedisConnectionFactory();
            String hostName = redistogoUri.getHost();
            int port = redistogoUri.getPort();
            jedisConnFactory.setUsePool(true);
            jedisConnFactory.setHostName(hostName);
            jedisConnFactory.setPort(port);
            jedisConnFactory.setTimeout(Protocol.DEFAULT_TIMEOUT);
            jedisConnFactory.setShardInfo(new JedisShardInfo(hostName, port));

            return jedisConnFactory;

        } catch (URISyntaxException e) {
            log.warn("error when in jedisConnFactory: "+e.getMessage());
            return null;
        }
    }

    private StringRedisSerializer stringRedisSerializer() {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        return stringRedisSerializer;
    }

    private JacksonJsonRedisSerializer<ProxyEntity> jacksonJsonRedisJsonSerializer() {
        return new JacksonJsonRedisSerializer<>(ProxyEntity.class);
    }

    private RedisTemplate<String, ProxyEntity> redisTemplate() {
        RedisTemplate<String, ProxyEntity> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnFactory());
        redisTemplate.setKeySerializer(stringRedisSerializer());
        redisTemplate.setValueSerializer(jacksonJsonRedisJsonSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
