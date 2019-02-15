package com.criss.wang;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootRedisMutilApplication {

//	public SpringbootRedisMutilApplication(RedisTemplate<String, String> localRedisTemplate,
//			RedisTemplate<String, String> defaultRedisTemplate) throws InterruptedException {
//		// 10s的有效时间
//		localRedisTemplate.delete("key");
//		localRedisTemplate.opsForValue().set("key", "value", 100, TimeUnit.MILLISECONDS);
//		String ans = localRedisTemplate.opsForValue().get("key");
//		System.out.println("value".equals(ans));
//		TimeUnit.MILLISECONDS.sleep(200);
//		ans = localRedisTemplate.opsForValue().get("key");
//		System.out.println("value".equals(ans) + " >> false ans should be null! ans=[" + ans + "]");
//
//		defaultRedisTemplate.opsForValue().set("key", "value", 100, TimeUnit.MILLISECONDS);
//		ans = defaultRedisTemplate.opsForValue().get("key");
//		System.out.println(ans);
//	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootRedisMutilApplication.class, args);
	}

}
