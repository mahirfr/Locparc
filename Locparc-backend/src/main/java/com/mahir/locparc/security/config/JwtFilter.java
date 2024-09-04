package com.mahir.locparc.security.config;

import com.mahir.locparc.security.MyUserDetails;
import com.mahir.locparc.security.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor // If there are any private final fields lombok will add them to the constructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,   // We can intercept our request to extract data from it
            HttpServletResponse response, // We can modify our response, like set a header
            FilterChain filterChain       // Chain of responsibility design pattern
    ) throws ServletException, IOException {

        final String jwt = request.getHeader("Authorization"); // Role header contains Bearer token

        if (jwt == null || !jwt.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // We pass it on to another filter
            return;
        }

        String token = jwt.substring(7);
        String userEmail = jwtService.extractEmail(token);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(token, (MyUserDetails) userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }


}
