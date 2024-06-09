package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.dto.User;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();
		
		CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
		
		String dbPassword = customUserDetails.getPassword();
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		if (!passwordEncoder.matches(password, dbPassword)) {
			System.out.println("비밀번호 불일치");
			throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다");
		}
		
		User user = customUserDetails.getUser();
		
		if ("N".equals(user.getStatus())) {
			System.out.println("계정 정지");
			throw new BadCredentialsException("정지된 계정입니다 관리자에게 문의해주세요");
		}
		
		if ("Q".equals(user.getStatus())) {
			System.out.println("계정 탈퇴");
			throw new BadCredentialsException("탈퇴한 계정입니다");
		}
		
		return new UsernamePasswordAuthenticationToken(username, password, customUserDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
}
