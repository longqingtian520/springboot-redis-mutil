package com.criss.wang.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

	@Autowired
	private RedisTemplate<String, String> localRedisTemplate;

	@Autowired
	private RedisTemplate<String, String> defaultRedisTemplate;

	@RequestMapping(value = "/get/value", method = RequestMethod.GET)
	public String testGet() {
		System.out.println(this.localRedisTemplate.opsForValue().get("study"));
		return "success";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String testSet() throws InterruptedException {
		String vo = "kdjfkd";
		this.localRedisTemplate.opsForValue().set("study", vo);
		this.defaultRedisTemplate.opsForValue().set("aa", vo);


		localRedisTemplate.opsForValue().set("key", "value", 7000, TimeUnit.MILLISECONDS);
		String ans = localRedisTemplate.opsForValue().get("key");
		System.out.println("value".equals(ans));
		TimeUnit.MILLISECONDS.sleep(6990);
		ans = localRedisTemplate.opsForValue().get("key");
		System.out.println("value".equals(ans) + " >> false ans should be null! ans=[" + ans + "]");

		defaultRedisTemplate.opsForValue().set("key", "value", 10000, TimeUnit.MILLISECONDS);
		ans = defaultRedisTemplate.opsForValue().get("key");
		System.out.println(ans);
		return "success";
	}
}
