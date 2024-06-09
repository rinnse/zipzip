package com.example.demo.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
	    String uri = getPreviousPageUrl(request);
	    
	    System.out.println("getPreviousPageUrl : " + uri);
	    
        if (uri != null && !uri.isEmpty() && !uri.contains("/login")) {
            response.sendRedirect(uri);
        } 
        
        else {
            // 이전 페이지가 없는 경우 기본적인 처리 방법을 설정
            response.sendRedirect("/");
        }
    }

    private String getPreviousPageUrl(HttpServletRequest request) {
        String uri = request.getHeader("Referer");
        
        System.out.println("getHeader : " + uri);
        
        if (uri != null && uri.endsWith("/login")) {
            return null; // 로그인 페이지인 경우 null 반환하여 제외
        }
        
        return uri;
    }

}
