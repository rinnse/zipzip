package com.example.demo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.dto.Agent;

public class CustomAgentDetails implements UserDetails {

	private Agent agent;
	
	public CustomAgentDetails(Agent agent) {
		this.agent = agent;
	}
	
	public Agent getAgent() {
		return agent;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		authorities.add(new SimpleGrantedAuthority(agent.getRole().toString()));
		
		return authorities;
	}

	@Override
	public String getPassword() {
		return agent.getAgentPwd();
	}

	@Override
	public String getUsername() {
		return agent.getAgentId();
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
