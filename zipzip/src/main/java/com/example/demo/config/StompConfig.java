package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer  {
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/chatting")
        		.setAllowedOriginPatterns("*")
        		.withSockJS();
	}
	
	@Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메세지 송신
		registry.enableSimpleBroker("/topic","/queue");
		
		// 메세지 수신
        registry.setApplicationDestinationPrefixes("/app");
    }
}
