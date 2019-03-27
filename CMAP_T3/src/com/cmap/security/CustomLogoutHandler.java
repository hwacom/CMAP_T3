package com.cmap.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.utils.impl.CloseableHttpClientUtils;

public class CustomLogoutHandler implements LogoutHandler {
	@Log
	private static Logger log;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		/*
		 ** 呼叫PRTG logout URI進行登出
		 */
		CloseableHttpClient httpclient = CloseableHttpClientUtils.prepare();

		HttpPost httpPost = new HttpPost(Env.PRTG_LOGOUT_URI);

		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(Env.HTTP_CONNECTION_TIME_OUT)			//設置連接逾時時間，單位毫秒。
				.setConnectionRequestTimeout(Env.HTTP_CONNECTION_TIME_OUT)	//設置從connect Manager獲取Connection 超時時間，單位毫秒。這個屬性是新加的屬性，因為目前版本是可以共用連接池的。
				.setSocketTimeout(Env.HTTP_SOCKET_TIME_OUT)					//請求獲取資料的超時時間，單位毫秒。 如果訪問一個介面，多少時間內無法返回資料，就直接放棄此次調用。
				.build();
		httpPost.setConfig(requestConfig);

		HttpClientContext context = HttpClientContext.create();

		try {
			CloseableHttpResponse closeableResponse = httpclient.execute(httpPost, context);

			int statusCode = closeableResponse.getStatusLine().getStatusCode();
			log.info(">>>>>>>>>>>>>>>>>> PRTG logout statusCode: " + statusCode);

			closeableResponse.close();

		} catch (IOException e) {
			e.printStackTrace();

		}
	}
}
