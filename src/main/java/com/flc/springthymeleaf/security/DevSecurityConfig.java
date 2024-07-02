package com.flc.springthymeleaf.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class DevSecurityConfig {

    @Bean
    public UserDetailsManager userDetaisManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorizeRequests -> 
                authorizeRequests
                    .requestMatchers("/").hasRole("EMPLOYEE")
                    .requestMatchers("/grupos/cadastrar").hasRole("EMPLOYEE")
                    .requestMatchers("/subgrupos/cadastrar").hasRole("EMPLOYEE")
                    .requestMatchers("/produtos/cadastrar").hasRole("EMPLOYEE")
                    .requestMatchers("/propriedades/cadastrar").hasRole("EMPLOYEE")
                    .requestMatchers(HttpMethod.POST, "/notas-fiscais/save").permitAll() 
                    .anyRequest().authenticated()
            )
            //.httpBasic(customizer -> customizer)
            
            .formLogin(form -> 
                form
                    .loginPage("/showMyLoginPage")
                    .loginProcessingUrl("/authenticateTheUser")
                    .permitAll()
            )
            .logout(logout -> logout.permitAll())
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/access_denied")
            )
            .httpBasic(httpBasic -> { 
            });

        return http.build();
    }
}
