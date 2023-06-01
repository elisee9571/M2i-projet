package com.example.demo.config;

import com.example.demo.config.token.JwtRequestFilter;
import com.example.demo.service.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserPrincipalService userPrincipalService;

    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public SecurityConfig(UserPrincipalService userPrincipalService, JwtRequestFilter jwtRequestFilter) {
        this.userPrincipalService = userPrincipalService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
                .and().httpBasic()
                .and().csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/login", "/auth/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/offers/**").permitAll()
                .requestMatchers("/users/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/offers/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/products/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/favorites/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/categories/**").hasRole("ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().exceptionHandling()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userPrincipalService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
