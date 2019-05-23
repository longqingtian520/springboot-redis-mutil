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
 * @author wangqiubao
 *
 * @date 2019-05-23 15:47
 *
 * @description redis多数据源配置
 */
@Configuration
public class RedisAutoConfig {
	//---------------------redis-default-------------------
		@Primary
		@Bean
		public LettuceConnectionFactory defaultLettuceConnectionFactory(RedisStandaloneConfiguration defaultRedisConfig,
				GenericObjectPoolConfig defaultPoolConfig) {
			LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
					.commandTimeout(Duration.ofMillis(100)).poolConfig(defaultPoolConfig).build();
			return new LettuceConnectionFactory(defaultRedisConfig, clientConfig);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Bean("redisTemplateDB1")
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

		//--------------------redis-local------------------

		@Bean
		@ConditionalOnBean(name = "twoRedisConfig")
		public LettuceConnectionFactory twoLettuceConnectionFactory(RedisStandaloneConfiguration twoRedisConfig,
				GenericObjectPoolConfig twoPoolConfig) {
			LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
					.commandTimeout(Duration.ofMillis(100)).poolConfig(twoPoolConfig).build();
			return new LettuceConnectionFactory(twoRedisConfig, clientConfig);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Bean("redisTemplateDB2")
		@ConditionalOnBean(name = "twoLettuceConnectionFactory")
		public RedisTemplate<String, String> twoRedisTemplate(@Qualifier("twoLettuceConnectionFactory") LettuceConnectionFactory twoLettuceConnectionFactory) {
			RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
			redisTemplate.setConnectionFactory(twoLettuceConnectionFactory);
			redisTemplate.setKeySerializer(new StringRedisSerializer()); // key的序列化类型
			Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
			ObjectMapper om = new ObjectMapper();
			om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
			om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
			redisTemplate.setValueSerializer(jackson2JsonRedisSerializer); // value序列化为json
			return redisTemplate;
		}

		//---------------redis-three--------------

		@Bean
		@ConditionalOnBean(name = "threeRedisConfig")
		public LettuceConnectionFactory threeLettuceConnectionFactory(RedisStandaloneConfiguration threeRedisConfig,
				GenericObjectPoolConfig threePoolConfig) {
			LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
					.commandTimeout(Duration.ofMillis(100)).poolConfig(threePoolConfig).build();
			return new LettuceConnectionFactory(threeRedisConfig, clientConfig);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Bean("redisTemplateDB3")
		@ConditionalOnBean(name = "threeLettuceConnectionFactory")
		public RedisTemplate<String, String> threeRedisTemplate(@Qualifier("threeLettuceConnectionFactory") LettuceConnectionFactory threeLettuceConnectionFactory) {
			RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
			redisTemplate.setConnectionFactory(threeLettuceConnectionFactory);
			redisTemplate.setKeySerializer(new StringRedisSerializer()); // key的序列化类型
			Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
			ObjectMapper om = new ObjectMapper();
			om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
			om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
			redisTemplate.setValueSerializer(jackson2JsonRedisSerializer); // value序列化为json
			return redisTemplate;
		}

		@Configuration
		@ConditionalOnProperty(name = "host", prefix = "spring.redis-two")
		public static class TwoRedisConfig {
			@Value("${spring.redis-two.host}")
			private String host;
			@Value("${spring.redis-two.port}")
			private Integer port;
			@Value("${spring.redis-two.password}")
			private String password;
			@Value("${spring.redis-two.database}")
			private Integer database;

			@Value("${spring.redis-two.lettuce.pool.max-active}")
			private Integer maxActive;
			@Value("${spring.redis-two.lettuce.pool.max-idle}")
			private Integer maxIdle;
			@Value("${spring.redis-two.lettuce.pool.max-wait}")
			private Long maxWait;
			@Value("${spring.redis-two.lettuce.pool.min-idle}")
			private Integer minIdle;

			@Bean
			public GenericObjectPoolConfig twoPoolConfig() {
				GenericObjectPoolConfig config = new GenericObjectPoolConfig();
				config.setMaxTotal(maxActive);
				config.setMaxIdle(maxIdle);
				config.setMinIdle(minIdle);
				config.setMaxWaitMillis(maxWait);
				return config;
			}

			@Bean
			public RedisStandaloneConfiguration twoRedisConfig() {
				RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
				config.setHostName(host);
				config.setPassword(RedisPassword.of(password));
				config.setPort(port);
				config.setDatabase(database);
				return config;
			}
		}


		@Configuration
		@ConditionalOnProperty(name = "host", prefix = "spring.redis-three")
		public static class RedisThreeConfig {
			@Value("${spring.redis-three.host}")
			private String host;
			@Value("${spring.redis-three.port}")
			private Integer port;
			@Value("${spring.redis-three.password}")
			private String password;
			@Value("${spring.redis-three.database}")
			private Integer database;

			@Value("${spring.redis-three.lettuce.pool.max-active}")
			private Integer maxActive;
			@Value("${spring.redis-three.lettuce.pool.max-idle}")
			private Integer maxIdle;
			@Value("${spring.redis-three.lettuce.pool.max-wait}")
			private Long maxWait;
			@Value("${spring.redis-three.lettuce.pool.min-idle}")
			private Integer minIdle;

			@Bean
			public GenericObjectPoolConfig threePoolConfig() {
				GenericObjectPoolConfig config = new GenericObjectPoolConfig();
				config.setMaxTotal(maxActive);
				config.setMaxIdle(maxIdle);
				config.setMinIdle(minIdle);
				config.setMaxWaitMillis(maxWait);
				return config;
			}

			@Bean
			public RedisStandaloneConfiguration threeRedisConfig() {
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
