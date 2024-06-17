package com.dreamgames.backendengineeringcasestudy.configuration;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.CountryLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardUserDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.serializer.CountryLeaderboardDTOSerializer;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.serializer.GroupLeaderboardUserDTOSerializer;
import java.util.List;
import java.util.Stack;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Configuration class for Redis. This class provides the necessary beans for Redis templates that
 * are used for storing different types of data in Redis.
 */
@Configuration
@EnableRedisRepositories
public class RedisConfig {

  /**
   * Provides a Redis template for storing Number values.
   *
   * @param connectionFactory the Redis connection factory
   * @return a Redis template for storing Number values
   */
  @Bean
  public RedisTemplate<String, Number> redisTemplateLong(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Number> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    return template;
  }

  /**
   * Provides a Redis template for storing List of GroupLeaderboardUserDTO objects.
   *
   * @param connectionFactory the Redis connection factory
   * @return a Redis template for storing List of GroupLeaderboardUserDTO objects
   */
  @Bean
  public RedisTemplate<String, List<GroupLeaderboardUserDTO>> redisTemplateString(
      RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, List<GroupLeaderboardUserDTO>> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GroupLeaderboardUserDTOSerializer());
    return template;
  }

  /**
   * Provides a Redis template for storing Stack of Number objects.
   *
   * @param connectionFactory the Redis connection factory
   * @return a Redis template for storing Stack of Number objects
   */
  @Bean
  public RedisTemplate<String, Stack<Number>> redisTemplateCountry(
      RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Stack<Number>> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Stack.class));
    return template;
  }

  /**
   * Provides a Redis template for storing List of CountryLeaderboardDTO objects.
   *
   * @param connectionFactory the Redis connection factory
   * @return a Redis template for storing List of CountryLeaderboardDTO objects
   */
  @Bean
  public RedisTemplate<String, List<CountryLeaderboardDTO>> redisTemplateCountryLeaderboard(
      RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, List<CountryLeaderboardDTO>> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new CountryLeaderboardDTOSerializer());
    return template;
  }

}