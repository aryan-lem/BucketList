package aryan_lem.BucketList.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class RedisConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        logger.info("Configuring Redis connection to host: '{}', port: '{}'", redisHost, redisPort);
        
        try {
            // Try to resolve the hostname to verify DNS resolution
            try {
                java.net.InetAddress address = java.net.InetAddress.getByName(redisHost);
                logger.info("Successfully resolved Redis host '{}' to IP: {}", 
                           redisHost, address.getHostAddress());
            } catch (Exception e) {
                logger.error("Failed to resolve Redis host '{}': {}", redisHost, e.getMessage());
            }
            
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
            return new LettuceConnectionFactory(config);
        } catch (Exception e) {
            logger.error("Error creating Redis connection factory: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new JdkSerializationRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}