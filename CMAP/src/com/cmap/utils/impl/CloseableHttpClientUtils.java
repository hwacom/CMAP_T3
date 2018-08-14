package com.cmap.utils.impl;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

public class CloseableHttpClientUtils {

	public static org.apache.http.impl.client.CloseableHttpClient prepare() {
		SSLContext sslContext;
		try {
			sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
	        HttpClientBuilder builder = HttpClientBuilder.create();
	        
	        //不處理SSL驗證問題
	        SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
	        builder.setSSLSocketFactory(sslConnectionFactory);
	        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
	                .register("https", sslConnectionFactory)
	                .register("http", new PlainConnectionSocketFactory())
	                .build();
	        HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(registry);
	        builder.setConnectionManager(ccm);
	        return builder.build();
	        
		} catch (Exception e) {
	    	e.printStackTrace();
	    }
		
		return null;
	}
}
