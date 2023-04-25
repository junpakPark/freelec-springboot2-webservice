package com.jojoldu.book.springboot.config.auth;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.jojoldu.book.springboot.domain.user.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomOAuth2UserService customOAuth2UserService;

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.csrf().disable()
			.headers().frameOptions().disable()
			.and()
				.authorizeRequests()
				.antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
				.antMatchers("/api/v1/**").hasRole(Role.USER.name())
				.anyRequest().authenticated()
			.and()
				.logout()
				.logoutSuccessUrl("/")
			.and()
				.oauth2Login()
				.userInfoEndpoint()
					.userService(customOAuth2UserService);

	}
}
