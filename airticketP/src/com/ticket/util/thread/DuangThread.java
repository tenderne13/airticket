package com.ticket.util.thread;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ticket.util.date.DateTimeUtil;
import com.ticket.util.httpclient.PostUtil;
import com.virjar.dungproxy.client.httpclient.HttpInvoker;

public class DuangThread extends Thread{
	private String startTime;
	private Map<String, Object> map;
	private HashOperations<String, Object, Object> operations;
	private JSONObject flight;
	private String flightNo;
	
	private Integer day;
	
	public DuangThread(String flightNo,HashOperations<String, Object, Object> operations){
		this.flightNo=flightNo;
		this.map=new HashMap<String,Object>();
		this.operations=operations;
		this.day=0;
	}
	
	
	@Override
	public void run() {
		System.out.println("--------------爬取线程开始------------------");
		startTime=DateTimeUtil.getToday("yyyy-MM-dd");
		String timeTemp;
		for(int i=28;i<30;i++){
			System.out.println("------开始爬取第"+(i+1)+"天的数据-------");
			timeTemp=DateTimeUtil.getXDay("yyyy-MM-dd", i);
			putMap(timeTemp);
			
			System.out.println("------第"+(i+1)+"天插入完成，休息30秒------");
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		System.out.println("最终map中的数据为:"+map);
		
		
		
		System.out.println("--------------爬取线程结束-----------------");
		
	}

	
	public void putMap(String time){
		String url="http://jipiao.jd.com/search/queryFlight.action?depCity=北京&arrCity=上海&depDate="+time+"&arrDate="+time+"&queryModule=1&lineType=OW&queryType=listquery&queryuuid=&uniqueKey=&sourceId=&arrTime=";
		//String result=HttpInvoker.get(url);
		//JSONObject json=JSON.parseObject(result);
		JSONObject json;
		json=PostUtil.doGetJson(url);
		JSONObject data = (JSONObject) json.get("data");
		JSONArray flights = data.getJSONArray("flights");
		if(flights!=null){
			flight=null;
			for(int i =0;i<flights.size();i++){
				flight=flights.getJSONObject(i);
				String No=flight.getString("flightNo");
				if(flightNo.equals(No)){
					break;
				}
			}
			
			if(flight==null){
				System.out.println("------本次未查询到该班次--歇30秒开始重新尝试------");
				try {
					Thread.sleep(30000);
					putMap(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				//查询到特定航班后开始插入到map中。
				JSONObject info = (JSONObject) flight.get("bingoLeastClassInfo");
				Integer price=info.getInteger("price");
				map.put(time, price);
				System.out.println("---------插入后map数据为:"+map+"--------");
				//开始像redis中插入数据
				operations.putAll(flightNo+":"+startTime, map);
			}
			
			
		}else{
			System.out.println("-------------航班为空，歇30秒开始重新尝试-----------");
			try {
				Thread.sleep(30000);
				putMap(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
}
