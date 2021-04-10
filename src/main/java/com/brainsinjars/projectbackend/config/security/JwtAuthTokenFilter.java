package com.brainsinjars.projectbackend.config.security;

import com.brainsinjars.projectbackend.service.impl.UserDetailsServiceImpl;
import com.brainsinjars.projectbackend.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Nilesh Pandit
 * @since 10-03-2021
 */

@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthTokenFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            String jwtToken = parseJwt(request);
            String username = null;

            try {
                if (jwtToken != null)
                    username = jwtUtil.extractUsername(jwtToken, false);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
                filterChain.doFilter(request, response);
            } catch (SignatureException | MalformedJwtException var8) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid access token!");
            } catch (ExpiredJwtException var9) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token expired!");
            }
    }

    private String parseJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isEmpty() && authHeader.startsWith(BEARER)) {
            return authHeader.substring(BEARER.length());
        }
        return null;
    }
}
