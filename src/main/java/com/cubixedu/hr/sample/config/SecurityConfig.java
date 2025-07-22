package com.cubixedu.hr.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
//	@Autowired
//	private JwtAuthFilter jwtAuthFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
		
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		return http
				.httpBasic(
					Customizer.withDefaults()
				)
				.csrf(csrf ->
					csrf.disable()
				)
				.sessionManagement(smc -> smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth ->
					auth
					//.requestMatchers(HttpMethod.POST, "/api/login").permitAll()
					//.requestMatchers(HttpMethod.POST, "/api/airports/**").hasAuthority("admin")
					//.requestMatchers(HttpMethod.PUT, "/api/airports/**").hasAnyAuthority("admin", "user")
					.requestMatchers("/api/holidayrequests/**").authenticated()
					.anyRequest().permitAll()
				)
				//.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
		
	}
	
//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//		return authenticationConfiguration.getAuthenticationManager();
//	}
}
