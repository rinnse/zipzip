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

import com.example.demo.dto.Agent;

@Component
public class AgentAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private CustomAgentDetailsService customAgentDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		CustomAgentDetails customAgentDetails = (CustomAgentDetails)customAgentDetailsService.loadUserByUsername(username);
		
		String dbPassword = customAgentDetails.getPassword();
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		if (!passwordEncoder.matches(password, dbPassword)) {
			System.out.println("비밀번호 불일치");
			throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다");
		}
		
		Agent agent = customAgentDetails.getAgent();
		
		if ("N".equals(agent.getStatus())) {
			System.out.println("계정 정지");
			throw new BadCredentialsException("정지된 계정 입니다");
		}
		
		if ("Q".equals(agent.getStatus())) {
			System.out.println("계정 탈퇴");
			throw new BadCredentialsException("탈퇴한 계정 입니다");
		}
		
		if ("N".equals(agent.getAgentRight())) {
			System.out.println("가입 대기");
			throw new BadCredentialsException("현재 가입 대기중인 계정입니다 관리자에게 문의해주세요");
		}
		
		if ("F".equals(agent.getAgentRight())) {
			System.out.println("가입 거부");
			throw new BadCredentialsException("가입이 거부된 계정입니다 관리자에게 문의해주세요");
		}
		
		return new UsernamePasswordAuthenticationToken(username, password, customAgentDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
