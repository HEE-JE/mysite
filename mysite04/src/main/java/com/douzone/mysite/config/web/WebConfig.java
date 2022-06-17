package com.douzone.mysite.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.douzone.config.web.MVCConfig;
import com.douzone.config.web.SecurityConfig;
import com.douzone.mysite.interceptor.SiteInterceptor;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan({ "com.douzone.mysite.controller", "com.douzone.mysite.exception" })
@Import({ MVCConfig.class, SecurityConfig.class })
public class WebConfig implements WebMvcConfigurer {

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
}