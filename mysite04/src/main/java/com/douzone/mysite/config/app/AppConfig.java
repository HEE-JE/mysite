package com.douzone.mysite.config.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.douzone.config.app.DBConfig;

@Configuration
@Import({ DBConfig.class, MyBatisConfig.class })
public class AppConfig {

}