package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.Agent;
import com.example.demo.service.AgentService;
import com.example.demo.service.KakaoPayService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class KakaoPayController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final AgentService agentService;
	
    @Autowired
    private KakaoPayService kakaoPayService;
    
    @GetMapping("/payment/ready")
    public ResponseEntity<String> readyPayment() {
    	log.info("kakaoPay post............................................");
        String paymentUrl = kakaoPayService.kakaoPayReady();
        
        return ResponseEntity.ok(paymentUrl);
    }
    
    @GetMapping("/payment/success")
    public String kakaoPaySuccess(@RequestParam("pg_token") String pg_token, Agent agent, Model model) {
        log.info("kakaoPaySuccess get............................................");
        log.info("kakaoPaySuccess pg_token : " + pg_token);
        
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        agent = agentService.findById(userId);
        agent.setAgentRight("P");
        agentService.updateRight(agent);
        System.out.println(agent);
        
        model.addAttribute("info", kakaoPayService.kakaoPayInfo(pg_token));
        
        return "payment/success";
    }
    
    @GetMapping("/payment/cancel")
    public String kakaoPayCancel() {
    	log.info("kakaoPay cancel................................................");
    	
    	return "payment/cancel";
    }
}
