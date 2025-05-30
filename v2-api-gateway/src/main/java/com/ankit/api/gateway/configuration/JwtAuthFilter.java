package com.ankit.api.gateway.configuration;

import com.ankit.api.gateway.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            try {
                String authHeader = request.getHeader("Authorization");
                String token = null;
                String username = null;
                if(authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                    username = JwtHelper.extractEmail(token);
                }

                // If the accesstoken is null. It will pass the request to next filter in the chain.
                // Any login and signup requests will not have jwt token in their header,
                // therefore they will be passed to next filter chain
                if (token == null) {
                    filterChain.doFilter(request, response);
                    return;
                }

                //If any accesstoken is present, then it will validate the token and
                //then authenticate the request in security context
                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if(JwtHelper.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, null);
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }

                filterChain.doFilter(request, response);
            }
            catch (AccessDeniedException ae) {
                log.error("Got Exception in JwtAuthFilter : {}", ae.getMessage());
                ae.printStackTrace();
            }

    }
}
