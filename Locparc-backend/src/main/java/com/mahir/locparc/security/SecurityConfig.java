package com.mahir.locparc.security;

import com.mahir.locparc.security.config.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors().configurationSource(httpServletRequest -> {
                    var corsConfiguration = new CorsConfiguration();
                    corsConfiguration.applyPermitDefaultValues();
//                    TODO: make an allowed origins list
                    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
                    corsConfiguration.setAllowedHeaders(
                            Arrays.asList("X-Requested-With", "Origin", "Content-Type",
                                    "Accept", "Authorization","Access-Control-Allow-Origin"));
                    return corsConfiguration;
                })
            .and()
            .csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/api/login").permitAll()          // anybody can have access to the login page
            .requestMatchers("/api/admin/**").hasRole("ADMIN")  // the url that contains admin can only be accessed by admins
            .requestMatchers("/api/lender/**").hasAnyRole("ADMIN", "LENDER")
            .requestMatchers("/api/items/admin/**").hasRole("ADMIN")
            .requestMatchers("/api/items/**").hasAnyRole("ADMIN", "LENDER", "USER")
//            .requestMatchers("/api/items/**").hasRole("ADMIN")
            .requestMatchers("/api/**").hasAnyRole("ADMIN", "LENDER", "USER")
            .anyRequest().authenticated()
            .and().exceptionHandling()
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // UsernamePasswordAuthenticationFilter is the filter responsible for
                                                                                           // user auth when first logging in
                                                                                           // and what I'm saying here is that the jwtFilter needs to come first in the filter chain
                                                                                           // and check the user for a JWT token, if the user has it, the auth process will pass through
                                                                                           // the jwtFilter, if not then through UsernamePasswordAuthenticationFilter
        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
