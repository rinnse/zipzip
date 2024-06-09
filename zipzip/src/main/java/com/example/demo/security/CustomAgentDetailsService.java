package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Agent;
import com.example.demo.repository.AgentRepository;

@Service
public class CustomAgentDetailsService implements UserDetailsService {

	@Autowired
	private AgentRepository agentRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Agent agent = agentRepository.findById(username);
		System.out.println("agent : " + username);
		System.out.println("agent : " + agent);
		
		if (agent == null) {
			System.out.println("아이디 없음");
			throw new UsernameNotFoundException("존재하지 않는 계정입니다 회원가입 후 로그인해주세요");
		}
		
		return new CustomAgentDetails(agent);
	}
	
}
