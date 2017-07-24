package com.ticket.util.httpclient;

import java.io.FileReader;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;




import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.virjar.dungproxy.client.httpclient.DunProxyHttpRequestRetryHandler;
import com.virjar.dungproxy.client.httpclient.conn.ProxyBindRoutPlanner;
import com.virjar.dungproxy.client.ippool.IpPool;
import com.virjar.dungproxy.client.util.PoolUtil;

public class PostUtil {
	 /** 
     * 重写验证方法，取消检测ssl 
     */  
    private static TrustManager truseAllManager = new X509TrustManager(){  
  
        public void checkClientTrusted(  
                java.security.cert.X509Certificate[] arg0, String arg1)  
                throws CertificateException {  
            // TODO Auto-generated method stub  
              
        }  
  
        public void checkServerTrusted(  
                java.security.cert.X509Certificate[] arg0, String arg1)  
                throws CertificateException {  
            // TODO Auto-generated method stub  
              
        }  
  
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
            // TODO Auto-generated method stub  
            return null;  
        }  
          
    }; 
	/** 
     * 访问https的网站 
     * @param httpclient 
     */  
    private static void enableSSL(DefaultHttpClient httpclient){  
        //调用ssl  
         try {  
                SSLContext sslcontext = SSLContext.getInstance("TLS");  
                sslcontext.init(null, new TrustManager[] { truseAllManager }, null);  
                SSLSocketFactory sf = new SSLSocketFactory(sslcontext);  
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
                Scheme https = new Scheme("https", sf, 443);  
                httpclient.getConnectionManager().getSchemeRegistry().register(https);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
    }  
    
    
	
	
	public static JSONObject doPostStr(String url,String outStr){
		DefaultHttpClient httpClient=new DefaultHttpClient();
		enableSSL(httpClient);
		HttpPost httpPost=new HttpPost(url);
		JSONObject jsonObject=null;
		try {
			httpPost.setEntity(new StringEntity(outStr,"UTF-8"));
			HttpResponse response=httpClient.execute(httpPost);
			String result=EntityUtils.toString(response.getEntity(),"UTF-8");
			jsonObject=JSON.parseObject(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	
	
	
	
	/**
	 * 执行头条js获得请求接口的参数
	 * @return
	 */
	public static JSONObject getUrlParam(){
		JSONObject jsonObject=null;
		FileReader reader=null;
		try {
			ScriptEngineManager manager=new ScriptEngineManager();
			ScriptEngine engine=manager.getEngineByName("javascript");
			String jsFileName="E:/toutiao.js";
			reader=new FileReader(jsFileName);
			engine.eval(reader);
			if (engine instanceof Invocable) {
				Invocable invoke = (Invocable) engine;
				Object obj = invoke.invokeFunction("getParam");
				jsonObject = JSONObject.parseObject(obj != null ? obj.toString() : null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(reader!=null){
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return jsonObject;
	}
	
	public static String doGetStr(String url){
		DefaultHttpClient httpClient=new DefaultHttpClient();
		enableSSL(httpClient);
		HttpGet httpGet=new HttpGet(url);
		httpGet.setHeader("Host", "www.toutiao.com");
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
		httpGet.setHeader("Accept","text/javascript, text/html, application/xml, text/xml, */*");
		httpGet.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		httpGet.setHeader("X-Requested-With","XMLHttpRequest");
		httpGet.setHeader("Content-Type","text/*, application/xml");
		httpGet.setHeader("Connection","keep-alive");
		httpGet.setHeader("Cookie", "csrftoken=30dcf3990fa6aae5e2bc32ce86e5ec48; tt_webid=6436619940845880834; WEATHER_CITY=%E5%8C%97%E4%BA"+
							"%AC; UM_distinctid=15cee0bbd12d8-0b0e1e2319539d-4d584131-13c680-15cee0bbd13139; CNZZDATA1259612802=1009653813-1498637798-null"+
							"%7C1498702598; uuid='w:519f60b4044649639aedec9bd897dd23'; _ga=GA1.2.1944013063.1498642370; _gid=GA1.2"+
							".1874914792.1498642370; _ba=BA0.2-20170306-51d9e-7ViJkABCocglT3ZWa1Rt; utm_source=toutiao; __tasessionId"+
							"=w4uunxnea1498706518539; sso_login_status=1; login_flag=31e611b5884dc27e1524824fd3f0a04f; sessionid=6aaf97545a4502872be1747b53a80b3c"+
							"; uid_tt=f8d062cc0c5b920d3729a23fdf443978; sid_tt=6aaf97545a4502872be1747b53a80b3c; sid_guard='6aaf97545a4502872be1747b53a80b3c"+
							"|1498706541|2591999|Sat\054 29-Jul-2017 03:22:20 GMT'");
		try {
			HttpResponse response=httpClient.execute(httpGet);
			HttpEntity entity=response.getEntity();
			if(entity!=null){
				String result =EntityUtils.toString(entity,"UTF-8");
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			httpClient.close();
		}
		return "";
		
	}
	
	
	public static JSONObject doGetJson(String url){
		DefaultHttpClient httpClient=new DefaultHttpClient();
		//HttpClient httpClient=HttpClientManager.getHttpClient("111.155.116.201", 8123);
		//enableSSL(httpClient);
		HttpGet httpGet=new HttpGet(url);
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
		try {
			HttpResponse response=httpClient.execute(httpGet);
			HttpEntity entity=response.getEntity();
			if(entity!=null){
				String result =EntityUtils.toString(entity,"UTF-8");
				return JSON.parseObject(result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(">>>>>>>>>出现异常开始重试<<<<<<"+e);
			doGetJson(url);
		}
		return null;
		
	}
	
	/**
	 * doPost请求方法
	 * @param url
	 * @param outStr
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String doPostStr(String url,Map<String, String> params){
		DefaultHttpClient httpClient=new DefaultHttpClient();
		HttpPost httpPost=new HttpPost(url);
		httpPost.setHeader("Host", "www.toutiao.com");
		httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
		httpPost.setHeader("Accept","text/javascript, text/html, application/xml, text/xml, */*");
		httpPost.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		httpPost.setHeader("X-Requested-With","XMLHttpRequest");
		httpPost.setHeader("Content-Type","text/*, application/xml");
		httpPost.setHeader("Connection","keep-alive");
		httpPost.setHeader("Cookie", "csrftoken=30dcf3990fa6aae5e2bc32ce86e5ec48; tt_webid=6436619940845880834; WEATHER_CITY=%E5%8C%97%E4%BA"+
							"%AC; UM_distinctid=15cee0bbd12d8-0b0e1e2319539d-4d584131-13c680-15cee0bbd13139; CNZZDATA1259612802=1009653813-1498637798-null"+
							"%7C1498702598; uuid='w:519f60b4044649639aedec9bd897dd23'; _ga=GA1.2.1944013063.1498642370; _gid=GA1.2"+
							".1874914792.1498642370; _ba=BA0.2-20170306-51d9e-7ViJkABCocglT3ZWa1Rt; utm_source=toutiao; __tasessionId"+
							"=w4uunxnea1498706518539; sso_login_status=1; login_flag=31e611b5884dc27e1524824fd3f0a04f; sessionid=6aaf97545a4502872be1747b53a80b3c"+
							"; uid_tt=f8d062cc0c5b920d3729a23fdf443978; sid_tt=6aaf97545a4502872be1747b53a80b3c; sid_guard='6aaf97545a4502872be1747b53a80b3c"+
							"|1498706541|2591999|Sat\054 29-Jul-2017 03:22:20 GMT'");
		System.out.println(httpPost.getHeaders("Cookie"));
		JSONObject jsonObject=null;
		String result=null;
		try {
			if(params!=null){
				List<NameValuePair> list=new ArrayList<NameValuePair>();
				Set<String> keySet=params.keySet();
				for(String key:keySet){
					list.add(new BasicNameValuePair(key, params.get(key)));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
			}
			HttpResponse response=httpClient.execute(httpPost);
			result=EntityUtils.toString(response.getEntity(),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httpClient.close();
		}
		return result;
	}
	
	
	public void initIpPool(){
		IpPool.getInstance().bind("www.baidu.com", "www.hao123.com");
		CloseableHttpClient closeableHttpClient =HttpClientBuilder.create().setRetryHandler(new DunProxyHttpRequestRetryHandler(null)).setRoutePlanner(new ProxyBindRoutPlanner()).build();
		//PoolUtil.getBindProxy(closeableHttpClient);
	}
	
	public static void main(String[] args) {
		
	}
}
