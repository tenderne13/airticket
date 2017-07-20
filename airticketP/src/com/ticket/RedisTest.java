package com.ticket;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisTest {
	private ApplicationContext applicationContext;
	@Before
	public void setUp()throws Exception{
		applicationContext=new ClassPathXmlApplicationContext("applicationContext-redis.xml");
	}
	
	@Test
	public void test(){
		RedisTemplate<String, Object> redisTemplate = (RedisTemplate<String, Object>) applicationContext.getBean("redisTemplate");
		ListOperations<String, Object> ops= redisTemplate.opsForList();
		ops.leftPush("ticket", 12314);
		for(int i=0 ; i<100;i++){
			ops.leftPush("ticket", i*10);
		}
		
		for(int i=0 ; i<100;i++){
			System.out.println("队列中的值为:"+ops.rightPop("ticket"));
		}
	}
}
