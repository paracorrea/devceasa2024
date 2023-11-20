package com.flc.springthymeleaf.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DevSecurityConfig {

	
	// add suport for JDBC... no more hard code
	
		@Bean
		public UserDetailsManager userDetaisManager(DataSource dataSource) {
			
			
			return new JdbcUserDetailsManager(dataSource);
		}
		
		
		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			
			http.authorizeHttpRequests(configurer -> 
			
					configurer
							.requestMatchers("/").hasRole("EMPLOYEE")
							.requestMatchers("/grupos/cadastrar").hasRole("MANAGER")
							.requestMatchers("/subgrupos/cadastrar").hasRole("MANAGER")
							.anyRequest().authenticated()
							)
					.formLogin(form -> 
								form
									.loginPage("/showMyLoginPage") // need controller
									.loginProcessingUrl("/authenticateTheUser") // no need a controller
									.permitAll()
							  )
							  .logout(logout -> logout.permitAll()
							 )
							  .exceptionHandling(configurer ->
									configurer.accessDeniedPage("/access_denied")
							);		
			
			
			return http.build();
			
			
			
			
		}
		
		/*
		 * @Bean public InMemoryUserDetailsManager userDetailsManager() {
		 * 
		 * UserDetails john = User.builder() .username("john")
		 * .password("{noop}teste123") .roles("EMPLOYEE") .build();
		 * 
		 * UserDetails mary = User.builder() .username("mary")
		 * .password("{noop}teste123") .roles("EMPLOYEE","MANAGER") .build();
		 * 
		 * UserDetails susan = User.builder() .username("susan")
		 * .password("{noop}teste123") .roles("EMPLOYEE","MANAGER","ADMIN") .build();
		 * 
		 * 
		 * 
		 * return new InMemoryUserDetailsManager(john,mary,susan);
		 * 
		 * }
		 */
	
}
