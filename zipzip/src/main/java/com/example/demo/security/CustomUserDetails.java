package com.example.demo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.dto.User;

public class CustomUserDetails implements UserDetails {
	
	private User user;
	
	public CustomUserDetails(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
	// 권한 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {	
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		authorities.add(new SimpleGrantedAuthority(user.getRole()));
		
		return authorities;
	}

	// 패스워드 리턴
	@Override
	public String getPassword() {
		return user.getUserPwd();
	}

	// 아이디 리턴
	@Override
	public String getUsername() {
		return user.getUserId();
	}

	// 계정 만료
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	// 계정 잠금
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 비밀번호 변경 기간 지남
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 계정 활성화
	@Override
	public boolean isEnabled() {
		return true;
	}

}
