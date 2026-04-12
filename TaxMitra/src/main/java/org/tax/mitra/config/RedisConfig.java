package org.tax.mitra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }
    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        StringRedisSerializer serializer = new StringRedisSerializer();
        template.setKeySerializer(serializer);
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(serializer);
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }
    @Bean
    public DefaultRedisScript<String> otpVerifyScript() {
        DefaultRedisScript<String> script = new DefaultRedisScript<>();
        script.setScriptText(
                """
                local key = KEYS[1]
                local inputOtp = ARGV[1]
        
                if redis.call("EXISTS", key) == 0 then
                    return "EXPIRED"
                end
        
                local code = redis.call("HGET", key, "code")
                local attempts = tonumber(redis.call("HGET", key, "attempts"))
        
                if attempts >= 3 then
                    redis.call("DEL", key)
                    return "MAX_ATTEMPTS"
                end
        
                if code ~= inputOtp then
                    redis.call("HINCRBY", key, "attempts", 1)
                    return "INVALID"
                end
        
                redis.call("DEL", key)
                return "VERIFIED"
                """
        );
        script.setResultType(String.class);
        return script;
    }
}


