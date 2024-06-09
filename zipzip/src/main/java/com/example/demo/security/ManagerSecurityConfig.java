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
public class ManagerSecurityConfig {
	
	@Autowired
	ManagerAuthenticationProvider managerAuthenticationProvider;
	
	@Bean
	public BCryptPasswordEncoder managerPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain managerFilterChain(HttpSecurity http) throws Exception {
		
		http
			.csrf().disable();
		
		http
			.authenticationProvider(managerAuthenticationProvider);

		http
			.antMatcher("/manager/**")
			.authorizeRequests()
			.anyRequest().hasAuthority("ROLE_MANAGER");
		
		http
			.formLogin()
				.loginPage("/manager/login")
				.loginProcessingUrl("/manager/login")
				.usernameParameter("managerId")
				.passwordParameter("managerPwd")
				.defaultSuccessUrl("/")
				.permitAll();
		
		http
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/manager/logout"))
				.logoutSuccessUrl("/")
				.deleteCookies("JSESSIONID")
				.invalidateHttpSession(true);
		
		return http.build();
	}
	
}
