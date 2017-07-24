package com.ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ticket.util.thread.CrawlThread;
import com.ticket.util.thread.DuangThread;
import com.virjar.dungproxy.client.ippool.IpPool;

@Controller
public class AnalysisController {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	
	@RequestMapping("analysis")
	@ResponseBody
	public String analysis(){
		HashOperations<String, Object, Object> ops= redisTemplate.opsForHash();
		
		
		new DuangThread("MU5128", ops).start();
		return "success";
	}
}
