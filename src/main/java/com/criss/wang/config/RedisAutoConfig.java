package com.criss.wang.config;

import java.time.Duration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * author: wangqiubao
 *
 * date: 2019-02-15 15:49:46
 *
 * describe: 配置多个redis数据源
 */
@Configuration
public class RedisAutoConfig {

	// 注意：有多个ConnectionFactory存在时，在创建默认的RedisTemplate就不知道该选择哪一个了，
	// 所以需要借助@Primary来指定默认的连接工厂，然后在使用工程的时候，通过@Qualifier注解来显示指定，
	// 我需要的工厂是哪个（主要是localRedisTemplate这个bean的定义，如果不加，则会根据defaultLettuceConnectionFactory这个实例来创建Redis连接了）

	@Primary
	@Bean
	public LettuceConnectionFactory defaultLettuceConnectionFactory(RedisStandaloneConfiguration defaultRedisConfig,
			GenericObjectPoolConfig defaultPoolConfig) {
		LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
				.commandTimeout(Duration.ofMillis(100)).poolConfig(defaultPoolConfig).build();
		return new LettuceConnectionFactory(defaultRedisConfig, clientConfig);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public RedisTemplate<String, String> defaultRedisTemplate( @Qualifier("defaultLettuceConnectionFactory") LettuceConnectionFactory defaultLettuceConnectionFactory) {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(defaultLettuceConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer()); // key的序列化类型
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer); // value序列化为json
		return redisTemplate;
	}

	@Bean
	@ConditionalOnBean(name = "localRedisConfig")
	public LettuceConnectionFactory localLettuceConnectionFactory(RedisStandaloneConfiguration localRedisConfig,
			GenericObjectPoolConfig localPoolConfig) {
		LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
				.commandTimeout(Duration.ofMillis(100)).poolConfig(localPoolConfig).build();
		return new LettuceConnectionFactory(localRedisConfig, clientConfig);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	@ConditionalOnBean(name = "localLettuceConnectionFactory")
	public RedisTemplate<String, String> localRedisTemplate(@Qualifier("localLettuceConnectionFactory") LettuceConnectionFactory localLettuceConnectionFactory) {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(localLettuceConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer()); // key的序列化类型
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer); // value序列化为json
		return redisTemplate;
	}

	@Configuration
	@ConditionalOnProperty(name = "host", prefix = "spring.local-redis")
	public static class LocalRedisConfig {
		@Value("${spring.local-redis.host}")
		private String host;
		@Value("${spring.local-redis.port}")
		private Integer port;
		@Value("${spring.local-redis.password}")
		private String password;
		@Value("${spring.local-redis.database}")
		private Integer database;

		@Value("${spring.local-redis.lettuce.pool.max-active}")
		private Integer maxActive;
		@Value("${spring.local-redis.lettuce.pool.max-idle}")
		private Integer maxIdle;
		@Value("${spring.local-redis.lettuce.pool.max-wait}")
		private Long maxWait;
		@Value("${spring.local-redis.lettuce.pool.min-idle}")
		private Integer minIdle;

		@Bean
		public GenericObjectPoolConfig localPoolConfig() {
			GenericObjectPoolConfig config = new GenericObjectPoolConfig();
			config.setMaxTotal(maxActive);
			config.setMaxIdle(maxIdle);
			config.setMinIdle(minIdle);
			config.setMaxWaitMillis(maxWait);
			return config;
		}

		@Bean
		public RedisStandaloneConfiguration localRedisConfig() {
			RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
			config.setHostName(host);
			config.setPassword(RedisPassword.of(password));
			config.setPort(port);
			config.setDatabase(database);
			return config;
		}
	}

	@Configuration
	public static class DefaultRedisConfig {
		@Value("${spring.redis.host}")
		private String host;
		@Value("${spring.redis.port}")
		private Integer port;
		@Value("${spring.redis.password}")
		private String password;
		@Value("${spring.redis.database}")
		private Integer database;

		@Value("${spring.redis.lettuce.pool.max-active}")
		private Integer maxActive;
		@Value("${spring.redis.lettuce.pool.max-idle}")
		private Integer maxIdle;
		@Value("${spring.redis.lettuce.pool.max-wait}")
		private Long maxWait;
		@Value("${spring.redis.lettuce.pool.min-idle}")
		private Integer minIdle;

		@Bean
		public GenericObjectPoolConfig defaultPoolConfig() {
			GenericObjectPoolConfig config = new GenericObjectPoolConfig();
			config.setMaxTotal(maxActive);
			config.setMaxIdle(maxIdle);
			config.setMinIdle(minIdle);
			config.setMaxWaitMillis(maxWait);
			return config;
		}

		@Bean
		public RedisStandaloneConfiguration defaultRedisConfig() {
			RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
			config.setHostName(host);
			config.setPassword(RedisPassword.of(password));
			config.setPort(port);
			config.setDatabase(database);
			return config;
		}
	}
}