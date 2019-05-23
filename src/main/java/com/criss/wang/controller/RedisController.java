package com.criss.wang.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * author: wangqiubao
 *
 * date: 2019-02-15 15:49:26
 *
 * describe:
 */
@RestController
public class RedisController {

	@Autowired
	private RedisTemplate<String, String> redisTemplateDB2;

	@Autowired
	private RedisTemplate<String, String> redisTemplateDB1;

	@Autowired
	private RedisTemplate<String, String> redisTemplateDB3;

	@RequestMapping(value = "/get/value", method = RequestMethod.GET)
	public String testGet() {
		System.out.println(this.redisTemplateDB2.opsForValue().get("study"));
		return "success";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String testSet() throws InterruptedException {
		String vo = "kdjfkd";
		this.redisTemplateDB2.opsForValue().set("db", vo);
		this.redisTemplateDB1.opsForValue().set("db", vo);
		this.redisTemplateDB3.opsForValue().set("db", vo);


		redisTemplateDB2.opsForValue().set("key", "value", 7000, TimeUnit.MILLISECONDS);
		String ans = redisTemplateDB2.opsForValue().get("key");
		System.out.println("value".equals(ans));
		TimeUnit.MILLISECONDS.sleep(6990);
		ans = redisTemplateDB2.opsForValue().get("key");
		System.out.println("value".equals(ans) + " >> false ans should be null! ans=[" + ans + "]");

		redisTemplateDB1.opsForValue().set("key", "value", 10000, TimeUnit.MILLISECONDS);
		ans = redisTemplateDB1.opsForValue().get("key");
		System.out.println(ans);
		return "success";
	}
}
