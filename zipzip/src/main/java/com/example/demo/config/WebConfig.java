package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	private String resourcePath = "/static/resources/upload/**";		// view에서 사용할 경로
	
	private String savePath = "file:///C:/project/final/zipzip/src/main/resources/static/resources/upload/";
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(resourcePath).addResourceLocations(savePath);
	}
	
	@Bean
	MappingJackson2JsonView jsonView() {
		return new MappingJackson2JsonView();
	}
	
}
