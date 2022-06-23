package com.douzone.mysite.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.douzone.mysite.interceptor.SiteInterceptor;

@SpringBootConfiguration
@PropertySource("classpath:config/webConfig.properties")
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private Environment env;

	// Site Interceptor
	@Bean
	public HandlerInterceptor siteInterceptor() {
		return new SiteInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(siteInterceptor()).addPathPatterns("/**").excludePathPatterns("/assets/**")
				.excludePathPatterns("/user/auth").excludePathPatterns("/user/logout");
	}

	// MVC Resource Mapping
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(env.getProperty("fileupload.resourceMapping"))
				.addResourceLocations("file:" + env.getProperty("fileupload.uploadLocation"));
	}
}