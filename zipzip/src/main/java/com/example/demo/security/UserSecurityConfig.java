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
public class UserSecurityConfig {
	
	@Autowired
	UserAuthenticationProvider userAuthenticationProvider;
	
	@Bean
	public BCryptPasswordEncoder userPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/css/**", "/fonts/**", "/images/**", "/js/**", "/scss/**", "/upload/**", "/mail/**");
    }
	
	@Bean
	public SecurityFilterChain userFileterChain(HttpSecurity http) throws Exception {
		
		// security 설정 추가
		http
			.headers().frameOptions().sameOrigin();
		
		http
			.csrf().disable();
		
		http
			.authenticationProvider(userAuthenticationProvider);
		
		http
			.authorizeRequests()								// 요청권한지정
			.antMatchers("/user/join",
					"/user/insert",
					"/user/findIdPwd",
					"/user/findId",
					"/user/findPwd").permitAll()				// 통과
			.antMatchers("/user/**").hasAuthority("ROLE_USER")	// user로 시작하는 페이지는 모두 ROLE_USER
			.anyRequest().permitAll();							// 나머지 통과
	
		http
			.formLogin()
				.loginPage("/user/login")
				.loginProcessingUrl("/user/login")
				.usernameParameter("userId")
				.passwordParameter("userPwd")
				.defaultSuccessUrl("/")
				.permitAll();
		
		http
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
				.logoutSuccessUrl("/")
				.deleteCookies("JSESSIONID")
				.invalidateHttpSession(true);
		
		return http.build();
	}
	
}
