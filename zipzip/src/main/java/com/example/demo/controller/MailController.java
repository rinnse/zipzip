package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.Mail;
import com.example.demo.dto.User;
import com.example.demo.service.MailService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MailController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final MailService mailService;
	
	@ResponseBody
	@PostMapping("/mail/mailAuth")
	public String MailSend(String userEmail) {
		log.info("MailSend");
		
		int number = mailService.sendMail(userEmail);
		
		String num = "" + number;
		
		return num;
	}
	
	@Transactional
	@PostMapping("/mail/tempPwd")
	public String tempPwd(User user) {
		log.info("tempPwd");
		
		System.out.println("search : " + user);
		
		Mail mail = mailService.tempPwdMail(user);
		mailService.tempPwdMailSend(mail);
		
		return "user/login";
	}
}