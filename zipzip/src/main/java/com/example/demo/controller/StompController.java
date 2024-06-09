package com.example.demo.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StompController {
	
	private final SimpMessagingTemplate simpMessagingTemplate;
	
	@GetMapping("/chatting")
	public String chatting(Model model, Principal principal) {
		
		System.out.println(principal.getName());
		
		model.addAttribute("userId" , principal.getName());
		
		return "chatting";
	}
	
	@MessageMapping("/chat/send")
	public void sendMsg(@Payload Map<String, Object> data) {
		simpMessagingTemplate.convertAndSend("/topic/1", data);
	}
}
