package com.bighuobi.api.gate.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huobi.commons.utils.StringUtil;

/**
 * 发送Http请求工具类
 */
public class HttpUtil {
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	/**
	 * 默认编码
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";
	private static HttpParams httpParams;
	private static PoolingClientConnectionManager connectionManager;
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
		httpParams = new BasicHttpParams();
		// 设置连接超时时间
		HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
		// 设置读取超时时间
		HttpConnectionParams.setSoTimeout(httpParams, READ_TIMEOUT);

		connectionManager = new PoolingClientConnectionManager();
		// 设置最大连接数
		connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		// 设置每个路由最大连接数
		connectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
		client = new DefaultHttpClient(connectionManager, httpParams);
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			// set up a TrustManager that trusts everything

			sslContext.init(null, new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs,
						String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs,
						String authType) {
				}
			} }, new SecureRandom());
			SSLSocketFactory ssf = new SSLSocketFactory(sslContext,
					SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = client.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, ssf));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String doGet(final String url) throws IOException {
	    
	  if (HttpProxySupport.shouldProxy(url)) {
	      return HttpProxySupport.get(url, new ResponseHandler<String>() {
	        public String handleResponse(HttpResponse response)
	            throws ClientProtocolException, IOException {
	          if (response.getStatusLine().getStatusCode() == 200) {
	            return EntityUtils.toString(response.getEntity(),
	                DEFAULT_ENCODING);// 取出应答字符串
	          }else{
	            throw new IOException("http communication error: " + response.getStatusLine().getStatusCode());
	          }
	        }
	      });
	    }
	    
		return HttpUtil.get(url, new ResponseHandler<String>() {
			public String handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				if (response.getStatusLine().getStatusCode() == 200) {
					return EntityUtils.toString(response.getEntity(),
							DEFAULT_ENCODING);// 取出应答字符串
				}
				logger.debug("url {}, response code {}", url, response
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
	    if (HttpProxySupport.shouldProxy(url)) {
	      return HttpProxySupport.post(url, httpEntity);
	    }
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
			System.out.println(doGet("https://btc-e.com/api/2/btc_usd/ticker"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
