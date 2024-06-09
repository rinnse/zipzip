package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Manager;
import com.example.demo.repository.ManagerRepository;

@Service
public class CustomManagerDetailsService implements UserDetailsService {

	@Autowired
	private ManagerRepository managerRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Manager manager = managerRepository.findById(username);
		System.out.println("manager : " + username);
		System.out.println("manager : " + manager);
		
		if (manager == null) {
			System.out.println("아이디 없음");
			throw new UsernameNotFoundException("존재하지 않는 계정입니다");
		}
		
		return new CustomManagerDetails(manager);
	}
	
}
