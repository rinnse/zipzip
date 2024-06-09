package com.example.demo.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.KakaoPayApprove;
import com.example.demo.dto.KakaoPayReady;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoPayService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String HOST = "https://kapi.kakao.com";
    
    private KakaoPayReady kakaoPayReady;
    private KakaoPayApprove kakaoPayApprove;

    
    public String kakaoPayReady() {
    	
    	String userId = SecurityContextHolder.getContext().getAuthentication().getName();
 
        RestTemplate restTemplate = new RestTemplate();
 
        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "9f23f6427ef69a351cafcbdccb64c480");
        headers.add("Accept", "application/json");
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        
        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", userId);
        params.add("item_name", "ZIPZIP중개인결제");
        params.add("quantity", "1");
        params.add("total_amount", "36800");
        params.add("tax_free_amount", "100");
        params.add("approval_url", "http://localhost:8088/payment/success");
        params.add("cancel_url", "http://localhost:8088/payment/cancel");
        params.add("fail_url", "http://localhost:8088/payment/fail");
 
         HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);
 
        try {
            kakaoPayReady = restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), body, KakaoPayReady.class);
            
            log.info("" + kakaoPayReady);
            
            return kakaoPayReady.getNext_redirect_pc_url();
 
        } 
        
        catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
        catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return "/pay";
        
    }
    
    public KakaoPayApprove kakaoPayInfo(String pg_token) {
    	
        log.info("KakaoPayInfo............................................");
        log.info("-----------------------------");
        
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        
        RestTemplate restTemplate = new RestTemplate();
        
        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "9f23f6427ef69a351cafcbdccb64c480");
        headers.add("Accept", "application/json");
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
 
        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", kakaoPayReady.getTid());
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", userId);
        params.add("pg_token", pg_token);
        params.add("total_amount", "36800");
        
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        
        try {
        	kakaoPayApprove = restTemplate.postForObject(new URI(HOST + "/v1/payment/approve"), body, KakaoPayApprove.class);
            log.info("" + kakaoPayApprove);
          
            return kakaoPayApprove;
        
        } 
        
        catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
        catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
}
