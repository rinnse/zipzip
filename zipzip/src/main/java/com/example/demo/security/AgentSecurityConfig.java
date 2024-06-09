package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AgentSecurityConfig {
	
	@Autowired
	AgentAuthenticationProvider agentAuthenticationProvider;
	
	@Bean
	public BCryptPasswordEncoder agentPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain agentFilterChain(HttpSecurity http) throws Exception {
		
		
		http
			.csrf().disable();
		
		http
			.authenticationProvider(agentAuthenticationProvider);
		
		http
			.antMatcher("/agent/**")					// agent로 시작하는 페이지
			.authorizeRequests()						// 요청권한지정
			.antMatchers("/agent/home",
					"/agent/join",
					"/agent/insert").permitAll()		// 통과
			.anyRequest().hasAuthority("ROLE_AGENT");	// 모두 ROLE_AGENT
		
		http
			.formLogin()
				.loginPage("/agent/login")
				.loginProcessingUrl("/agent/login")
				.usernameParameter("agentId")
				.passwordParameter("agentPwd")
				.defaultSuccessUrl("/")
				.permitAll();
		
		http
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/agent/logout"))
				.logoutSuccessUrl("/")
				.deleteCookies("JSESSIONID")
				.invalidateHttpSession(true);
		
		return http.build();
	}
	
}
