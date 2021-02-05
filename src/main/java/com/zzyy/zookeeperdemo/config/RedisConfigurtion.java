package com.zzyy.zookeeperdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Auther: zhouyu
 * @Date: 2021/2/4 10:01
 * @Description:
 */
@Configuration
public class RedisConfigurtion {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 功能描述:
     * GenericToStringSerializer：使用Spring转换服务进行序列化；
     * JacksonJsonRedisSerializer：使用Jackson 1，将对象序列化为JSON；
     * Jackson2JsonRedisSerializer：使用Jackson 2，将对象序列化为JSON；
     * JdkSerializationRedisSerializer：使用Java序列化；
     * OxmSerializer：使用Spring O/X映射的编排器和解排器（marshaler和unmarshaler）实现序列化，用于XML序列化；
     * StringRedisSerializer：序列化String类型的key和value。实际上是String和byte数组之间的转换。
     *
     * @auther: zhouyu
     * @date: 2021/2/5 14:46
     */

    @Bean
    public RedisTemplate<String, Object> stringSerializerRedisTemplate() {
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();

        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();

        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        return redisTemplate;
    }
}
