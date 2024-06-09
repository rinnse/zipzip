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

@Component
public class ManagerAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private CustomManagerDetailsService customManagerDetailsService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();
		
		CustomManagerDetails customManagerDetails = (CustomManagerDetails) customManagerDetailsService.loadUserByUsername(username);
		
		String dbPassword = customManagerDetails.getPassword();
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		if (!passwordEncoder.matches(password, dbPassword)) {
			System.out.println("비밀번호 불일치");
			throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다");
		}
		
		return new UsernamePasswordAuthenticationToken(username, password, customManagerDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
}
