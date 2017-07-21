package com.ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ticket.util.thread.CrawlThread;

@Controller
public class AnalysisController {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	
	@RequestMapping("analysis")
	@ResponseBody
	public String analysis(){
		HashOperations<String, Object, Object> ops= redisTemplate.opsForHash();
		for(int i=0;i<30;i++){
			new CrawlThread("MU5128", i, ops).start();
		}
		return "success";
	}
}
