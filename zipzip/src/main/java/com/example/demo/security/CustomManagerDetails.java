package com.example.demo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.dto.Manager;

public class CustomManagerDetails implements UserDetails {

	private Manager manager;
	
	public CustomManagerDetails(Manager manager) {
		this.manager = manager;
	}
	
	public Manager getManager() {
		return manager;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		authorities.add(new SimpleGrantedAuthority(manager.getRole()));
		
		return authorities;
	}

	@Override
	public String getPassword() {
		return manager.getManagerPwd();
	}

	@Override
	public String getUsername() {
		return manager.getManagerId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
