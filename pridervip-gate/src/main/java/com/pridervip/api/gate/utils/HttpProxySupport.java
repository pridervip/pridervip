package com.bighuobi.api.gate.utils;

import java.io.IOException;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huobi.commons.utils.StringUtil;

/**
 * 发送Http请求工具类
 */
public class HttpProxySupport {
	private static Logger logger = LoggerFactory.getLogger(HttpProxySupport.class);
	/**
	 * 默认编码
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";
	private static HttpClient client = null;
	/**
	 * 最大连接数
	 */
	public final static int MAX_TOTAL_CONNECTIONS = 800;
	/**
	 * 获取连接的最大等待时间
	 */
	public final static int WAIT_TIMEOUT = 30000;
	/**
	 * 每个路由最大连接数
	 */
	public final static int MAX_ROUTE_CONNECTIONS = 400;
	/**
	 * 连接超时时间
	 */
	public final static int CONNECT_TIMEOUT = 10000;
	/**
	 * 读取超时时间
	 */
	public final static int READ_TIMEOUT = 5000;

	static {
		try {
		  SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, new TrustManager[] { new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
      } }, null);
      SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext,
          new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

      Registry<ConnectionSocketFactory> register = RegistryBuilder
          .<ConnectionSocketFactory> create()
          .register("http", PlainConnectionSocketFactory.getSocketFactory())
          .register("https", sslSocketFactory).build();

      HttpHost proxy = new HttpHost(Constants.PROXY_HOST, Constants.PROXY_PORT, Constants.PROXY_PROTOCOL);
      
      RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(READ_TIMEOUT)
          .setConnectTimeout(CONNECT_TIMEOUT).setProxy(proxy).build();
      // 设置连接超时 的时间
      PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager(
          register);
      httpClientConnectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
      httpClientConnectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
      client = HttpClients.custom().setConnectionManager(httpClientConnectionManager)
          .setDefaultRequestConfig(requestConfig).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static List<String> proxyHostsList;
    static {
        String proxyHosts = Constants.PROXY_HOSTS_INCLUDE;
        proxyHostsList = Arrays.asList(proxyHosts.split(","));
    }

    protected static boolean shouldProxy(String url) {
        try {
            URI currentUri = new URI(url);
            return proxyHostsList.contains(currentUri.getHost());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
	
	public static String doGet(final String url) throws IOException {
		return HttpProxySupport.get(url, new ResponseHandler<String>() {
			public String handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				if (response.getStatusLine().getStatusCode() == 200) {
					return EntityUtils.toString(response.getEntity(),
							DEFAULT_ENCODING);// 取出应答字符串
				}
				logger.info("url {}, response code {}", url, response
						.getStatusLine().getStatusCode());
				return null;
			}
		});
	}

	/**
	 * 以POST方式发起一个请求，响应结果在用户的 ResponseHandler 处理
	 * 
	 * @param url
	 * @param httpEntity
	 * @param rh
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static <T> T post(String url, HttpEntity httpEntity,
			ResponseHandler<T> rh) throws ClientProtocolException, IOException {
		HttpPost method = new HttpPost(url);
		method.setEntity(httpEntity);
		// 发送请求
		try {
			return client.execute(method, rh);
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * post data with header info
	 */
	public static <T> T postWithHeaders(String url,
			Map<String, String> headersMap, HttpEntity httpEntity,
			ResponseHandler<T> rh) throws ClientProtocolException, IOException {
		HttpPost method = new HttpPost(url);
		if (headersMap != null && !headersMap.isEmpty()) {
			String key = null;
			for (Iterator<String> it = headersMap.keySet().iterator(); it
					.hasNext();) {
				key = it.next();
				method.setHeader(key, headersMap.get(key));
			}
		}
		method.setEntity(httpEntity);
		// 发送请求
		try {
			return client.execute(method, rh);
		} finally {
			method.releaseConnection();
		}
	}

	public static String post(String url, String content)
			throws ClientProtocolException, IOException {
		HttpEntity httpEntity = new StringEntity(content, DEFAULT_ENCODING);
		return post(url, httpEntity, DEFAULT_ENCODING);
	}

	public static String post(String url, HttpEntity httpEntity)
			throws ClientProtocolException, IOException {
		return post(url, httpEntity, DEFAULT_ENCODING);
	}

	public static String post(String url, HttpEntity httpEntity, String encoding)
			throws ClientProtocolException, IOException {
		HttpPost postMethod = new HttpPost(url);
		postMethod.setEntity(httpEntity);
		// 发送请求
		try {
			HttpResponse httpResponse = client.execute(postMethod);
			return EntityUtils.toString(httpResponse.getEntity(), encoding);// 取出应答字符串
		} finally {
			postMethod.releaseConnection();
		}
	}

	/**
	 * 以PUT方式发起一个请求，响应结果在用户的 ResponseHandler 处理
	 * 
	 * @param url
	 * @param httpEntity
	 * @param rh
	 * @throws IOException
	 */
	public static <T> T put(String url, HttpEntity httpEntity,
			ResponseHandler<T> rh) throws IOException {
		HttpPut method = new HttpPut(url);
		method.setEntity(httpEntity);
		// 发送请求
		try {
			return client.execute(method, rh);
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 以GET方式发起一个请求，响应结果在用户的 ResponseHandler 处理
	 * 
	 * @param url
	 * @param rh
	 * @throws IOException
	 */
	public static <T> T get(String url, ResponseHandler<T> rh)
			throws IOException {
		HttpGet method = new HttpGet(url);
		// 发送请求
		try {
			return client.execute(method, rh);
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 以DELETE方式发起一个请求，响应结果在用户的 ResponseHandler 处理
	 * 
	 * @param url
	 * @param rh
	 * @throws IOException
	 */
	public static <T> T delete(String url, ResponseHandler<T> rh)
			throws IOException {
		HttpDelete method = new HttpDelete(url);
		// 发送请求
		try {
			return client.execute(method, rh);
		} finally {
			method.releaseConnection();
		}
	}

	public static String httpsGet(final String url) throws IOException {
		return HttpProxySupport.get(url, new ResponseHandler<String>() {
			public String handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				if (response.getStatusLine().getStatusCode() == 200) {
					return EntityUtils.toString(response.getEntity(),
							DEFAULT_ENCODING);// 取出应答字符串
				}
				logger.info("url {}, response code {}", url, response
						.getStatusLine().getStatusCode());
				return null;
			}
		});
	}

	public static String post(String url, Map<String, Object> data,
			String encoding) throws ClientProtocolException, IOException {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (Map.Entry<String, Object> me : data.entrySet()) {
			if (me.getValue() != null) {
				nvps.add(new BasicNameValuePair(me.getKey(), StringUtil
						.toString(me.getValue())));
			}
		}
		return post(url, new UrlEncodedFormEntity(nvps, DEFAULT_ENCODING),
				encoding);
	}

	public static void main(String[] args) {
		try {
			//System.out.println(doGet("http://www.hb.com/withdraw/auto_withdraw.php?a=get_blust_info&user_id=327"));
		    Map<String, Object> data = new HashMap<String,Object>();
//		    long time = System.currentTimeMillis();
		    String key = DigestUtils.md5Hex("ljj900522");
		    System.out.println(key);
		    data.put("password", "ww123456");
		    System.out.println("1234"+post("http://api.huobi.com/mobile/user/login", data));
//		    data.put("auth_level", "C1");
//		    data.put("check", 1);
//		   
//		    data.put("name","wangxiao");
//		    data.put("birthday",System.currentTimeMillis());
//		    data.put("countries_id",1);
//		    data.put("card_type",2);
//		    data.put("card_id","420683197701163332");		    
//		    
//		    data.put("user_id", 19);
//		    data.put("source", "app");
//		    data.put("time",time);
//		    data.put("key",key);
//		    data.put("photo", new File("G:\\WIN_20150227_123742.jpg"));
//		    File f =new File("G:\\WIN_20150227_123742.jpg");	
//
//		    System.out.println(f.getName()+" ");
//		    
	//	    System.out.println("post"+post("http://www.lxd.com/account/auth.php?a=upload_photo_api",data));
		    
		    
//		    String json="";
//		    HttpClient httpclient = new DefaultHttpClient();
//	        // 请求处理页面
//	        HttpPost httppost = new HttpPost("http://www.lxd.com/account/auth.php?a=upload_photo_api&user_id=19&source=app&time="+time+"&key="+key);
//	        // 创建待处理的文件
//	        FileBody file = new FileBody(new File("G:\\WIN_20150227_123742.jpg"));
//	        // 创建待处理的表单域内容文本
//	  
//
//	        // 对请求的表单域进行填充
//	        MultipartEntity reqEntity = new MultipartEntity();
//	        reqEntity.addPart("photo", file);
//	        
//	        	        
//	        
//	       
//	        // reqEntity.addPart("descript", descript);
//	        // 设置请求
//	        httppost.setEntity(reqEntity);
//	        // 执行
//	        HttpResponse response = httpclient.execute(httppost);
//
//	        if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
//	            HttpEntity entity = response.getEntity();
//	            // 显示内容
//	            if (entity != null) {
//	                json = EntityUtils.toString(entity);
//	            }
//	            if (entity != null) {
//	                entity.consumeContent();
//	            }
//	        }
		    
		    
		    
		    
		    
		    
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String post(String url, Map<String, Object> data ) throws ClientProtocolException,IOException 
    {
        logger.debug("post to {} \r\n data is {}",url,data);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> me : data.entrySet()) 
        {
            if(me.getValue() != null)
            {
                nvps.add(new BasicNameValuePair(me.getKey(), me.getValue().toString()));
            }
        }
        String ret = post(url, new UrlEncodedFormEntity(nvps, DEFAULT_ENCODING));
        logger.debug("response body is {}",ret);
        return ret;
    }
	
//	public static String imgPost(String url,File photo,Integer userId,long time,String key)throws ClientProtocolException,IOException {
//	    
//	    String json="";
//        HttpClient httpclient = new DefaultHttpClient();
//        // 请求处理页面
//        HttpPost httppost = new HttpPost(Constants.URL_AUTH_USER_PHOTO+"&user_id="+userId+"&source=app&time="+time+"&key="+key);
//        // 创建待处理的文件
//        FileBody file = new FileBody(photo); 
//  
//
//        // 对请求的表单域进行填充
//        MultipartEntity reqEntity = new MultipartEntity();
//        reqEntity.addPart("photo", file);
//        
//                    
//        
//       
//        // reqEntity.addPart("descript", descript);
//        // 设置请求
//        httppost.setEntity(reqEntity);
//        // 执行
//        HttpResponse response = httpclient.execute(httppost);
//
//        if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
//            HttpEntity entity = response.getEntity();
//            // 显示内容
//            if (entity != null) {
//                json = EntityUtils.toString(entity);
//                return json;
//            }
//            if (entity != null) {
//                entity.consumeContent();
//            }
//        }
//        return null;
//	}
	

}
