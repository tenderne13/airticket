package com.ticket;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.ticket.util.thread.CrawlThread;

public class RedisTest {
	private ApplicationContext applicationContext;
	@Before
	public void setUp()throws Exception{
		applicationContext=new ClassPathXmlApplicationContext("applicationContext-redis.xml");
	}
	
	@Test
	public void test(){
		RedisTemplate<String, Object> redisTemplate = (RedisTemplate<String, Object>) applicationContext.getBean("redisTemplate");
		HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
		for(int i=0;i<30;i++){
			new CrawlThread("MU5128", i, opsForHash).start();
		}
	}
}
