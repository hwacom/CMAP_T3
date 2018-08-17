package com.cmap.configuration;

import java.util.Locale;

import javax.servlet.Filter;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.cmap.dao.I18nDAO;
import com.cmap.dao.impl.I18nDAOImpl;
import com.cmap.i18n.DatabaseMessageSourceBase;
import com.cmap.utils.EnvUtils;
import com.cmap.utils.impl.SysEnvUtils;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.cmap")
public class AppConfig implements WebMvcConfigurer {

	/*
	 * Register BeanNameViewResolver for pdf
	 * */
	@Bean
	public ViewResolver beanNameViewResolver() {
		BeanNameViewResolver resolver = new BeanNameViewResolver();
		resolver.setOrder(0);
		return resolver;
	}

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		viewResolver.setOrder(1);
		return viewResolver;
	}

	/*
	 * Create SpringResourceTemplateResolver
	 * */
	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		//       templateResolver.setApplicationContext(applicationContext);
		templateResolver.setPrefix("/WEB-INF/views/");
		templateResolver.setSuffix(".html");
		templateResolver.setOrder(2);
		return templateResolver;
	}

	/*
	 * Create SpringTemplateEngine
	 * */
	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}

	/*
	 * Register ThymeleafViewResolver
	 * */
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		registry.viewResolver(resolver);
	}


	//    @Bean
	//    public MessageSource messageSource() {
	//        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	//        messageSource.setBasename("messages");
	//        return messageSource;
	//    }

	@Bean
	public MessageSource propertiesMessageSource() {
		ResourceBundleMessageSource propertiesMessageSource = new ResourceBundleMessageSource();
		propertiesMessageSource.setBasenames("messages");
		propertiesMessageSource.setDefaultEncoding("utf8");
		return propertiesMessageSource;
	}

	@Bean
	public CookieLocaleResolver localeResolver() {
		CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
		cookieLocaleResolver.setDefaultLocale(new Locale("zh", "TW"));
		//        cookieLocaleResolver.setCookieName(Constants.CMP_LANGUAGE);
		return cookieLocaleResolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("locale");
		return localeChangeInterceptor;
	}

	public EnvUtils initEnv() {
		EnvUtils envUtils = new SysEnvUtils();
		return envUtils;
	}


	@Bean
	public DatabaseMessageSourceBase messageSource() {
		DatabaseMessageSourceBase messageSource = new DatabaseMessageSourceBase();
		messageSource.setParentMessageSource(propertiesMessageSource());
		return messageSource;
	}

	@Bean
	public I18nDAO i18nDAO() {
		I18nDAO i18nDAO = new I18nDAOImpl();
		return i18nDAO;
	}

	/*
	@Bean
	public JavaMailSenderImpl javaMailSenderImpl(){
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.aliyun.com");
		mailSender.setPort(465);
		//Set gmail email id
		mailSender.setUsername("cmp.message@aliyun.com");
		//Set gmail email password
		mailSender.setPassword("y7u8i9g0@CMPmail");
		Properties prop = mailSender.getJavaMailProperties();
		prop.setProperty("mail.transport.protocol", "smtps");
		prop.setProperty("mail.smtps.auth", "true");
		prop.setProperty("mail.smtps.ssl.enable", "true");
		//prop.setProperty("mail.smtp.starttls.enable", "true");
		prop.setProperty("mail.debug", "false");
		return mailSender;
	}
	 */

	@Bean
	public Filter characterEncodingFilter() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return characterEncodingFilter;
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	/*
    @Bean
    public MultipartResolver multipartResolver() {
       CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
       multipartResolver.setMaxUploadSize(1048576000); // 100MB
       multipartResolver.setMaxUploadSizePerFile(1048576000); // 100MB
       return multipartResolver;
    }
	 */
}
