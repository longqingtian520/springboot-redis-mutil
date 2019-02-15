package com.criss.wang.controller;

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
	public String testSet() {
		String vo = "kdjfkd";
		this.localRedisTemplate.opsForValue().set("study", vo);
		this.defaultRedisTemplate.opsForValue().set("aa", vo);
		return "success";
	}
}
