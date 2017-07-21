package com.ticket.util.thread;

import org.apache.commons.lang3.StringUtils;

import com.virjar.dungproxy.client.httpclient.HttpInvoker;

public class DuangThread extends Thread{

	
	
	@Override
	public void run() {
		while(true){
			String s= HttpInvoker.get("http://www.12306.cn/mormhweb/");
			if(StringUtils.isEmpty(s)){
				System.out.println("访问失败");
			}else{
				System.out.println("访问成功");
			}
		}
		
	}

	public static void main(String[] args) {
		for(int i=0;i<30;i++){
			new DuangThread().start();
		}
	}
}
