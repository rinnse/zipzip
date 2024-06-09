package com.example.demo.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.News;
import com.example.demo.dto.User;
import com.example.demo.service.NewsService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EtcController {
	
	private final NewsService newsService;
	private final UserService userService;
		
	@GetMapping("/etc/news")
	public String news(Model model) throws Exception {
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.findById(userId);
		
		List<News> newsList = newsService.newsList();
		
		model.addAttribute("user", user);
		model.addAttribute("news", newsList);
		
		return "etc/news";
	}
	
	//24.05.02 소스 추가 시작
	@GetMapping("/etc/faq")
	public String faq(Model model) {
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.findById(userId);
		
		model.addAttribute("user", user);
		
		return "etc/faq";
	}	
	//24.05.02 소스 추가 종료
}
