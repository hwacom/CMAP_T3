package com.cmap.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.cmap.configuration.filter.RequestBodyReaderAuthenticationFilter;
import com.cmap.security.AuthSuccessHandler;
import com.cmap.security.AuthUnsuccessHandler;
import com.cmap.security.CustomLogoutHandler;
import com.cmap.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public AuthSuccessHandler authSuccessHandler() {
		return new AuthSuccessHandler();
	};

	@Bean
	public AuthUnsuccessHandler authUnsuccessHandler() {
		return new AuthUnsuccessHandler();
	};

	@Override
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	};

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	};

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		UserDetailsService userDetailsService = userDetailsService();
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
    public RequestBodyReaderAuthenticationFilter authenticationFilter() throws Exception {
        RequestBodyReaderAuthenticationFilter authenticationFilter
            = new RequestBodyReaderAuthenticationFilter();
        authenticationFilter.setAuthenticationSuccessHandler(authSuccessHandler());
        authenticationFilter.setAuthenticationFailureHandler(authUnsuccessHandler());
//        authenticationFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login"));
        authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationFilter;
    }

	@Bean
	public CustomLogoutHandler customLogoutHandler() {
	    return new CustomLogoutHandler();
	}

	/*
	@Bean
	public LogoutFilter logoutFilter() {
	    // NOTE: See org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
	    // for details on setting up a LogoutFilter

		/*
		 ** 呼叫PRTG logout URI進行登出
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
			httpclient.execute(httpPost, context);

		} catch (IOException e) {
			e.printStackTrace();
		}

	    SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
	    securityContextLogoutHandler.setInvalidateHttpSession(true);

	    LogoutFilter logoutFilter = new LogoutFilter("/", securityContextLogoutHandler);
	    logoutFilter.setLogoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	    return logoutFilter;
	}
	*/

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/resources/**").permitAll()
			.antMatchers("/login").permitAll()
			.antMatchers("/loginOIDC").permitAll()
			.antMatchers("/login/code/**").permitAll()
			.antMatchers("/login/authByOIDC/**").permitAll()
			.antMatchers("/admin/env/refreshAll").permitAll()
			.antMatchers("/plugin/module/vmswitch/**").permitAll()	//提供PRTG呼叫切換VM備援 (Y190117, Case No.C31001704016 >> APT HeNBGW & ePDG-LI Expansion)
			.anyRequest().hasAnyRole("ADMIN", "USER")
			.and()
			.addFilterBefore(authenticationFilter(),
	                UsernamePasswordAuthenticationFilter.class)
			.formLogin().loginPage("/check").permitAll()
//			.successHandler(authSuccessHandler())
//			.failureHandler(authUnsuccessHandler())
			.and()
		.logout()
		.addLogoutHandler(customLogoutHandler())
	    .permitAll()
	    	.and()
	    	.headers()
			//.contentSecurityPolicy("default-src 'self'")	//http://www.ruanyifeng.com/blog/2016/09/csp.html
			//.and()
			.frameOptions()
			.disable()
			.addHeaderWriter(new StaticHeadersWriter("X-FRAME-OPTIONS", "ALLOW-FROM https://163.19.163.170"))
			.addHeaderWriter(new StaticHeadersWriter("X-FRAME-OPTIONS", "ALLOW-FROM https://163.19.163.170:1443"))
			.and()
		.csrf().disable();
	}
}
