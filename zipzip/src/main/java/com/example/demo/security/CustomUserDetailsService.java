package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.User;
import com.example.demo.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findById(username);
		System.out.println("user : " + username);
		System.out.println("user : " + user);
		
		if (user == null) {
			System.out.println("아이디 없음");
			throw new UsernameNotFoundException("존재하지 않는 계정입니다 회원가입 후 로그인해주세요");
		}
		
		return new CustomUserDetails(user);
	}

}
