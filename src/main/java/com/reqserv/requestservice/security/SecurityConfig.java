//package com.reqserv.requestservice.security;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http
//        .authorizeRequests(authorizeRequests ->
//            authorizeRequests
//                .requestMatchers("/auth").permitAll()
//                .requestMatchers("/users/**").hasRole("ADMIN")
//                .anyRequest().authenticated()
//        )
//        .logout(logout ->
//            logout
//                .logoutUrl("/logout")
//        );
//
//    http.addFilterBefore(new JwtAuthenticationFilter(new JwtUtil()), UsernamePasswordAuthenticationFilter.class);
//
//
//    return http.build();
//  }
//
//}
