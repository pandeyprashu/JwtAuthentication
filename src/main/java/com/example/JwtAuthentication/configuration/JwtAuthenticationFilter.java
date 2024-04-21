package com.example.JwtAuthentication.configuration;

import com.example.JwtAuthentication.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.*;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {

        // Fetch token from request
        var jwtTokenOptional = getTokenFromRequest(request);

        //Validate Token -> JWT Utils
        jwtTokenOptional.ifPresent(jwtToken -> {
            if (jwtUtils.validateToken(jwtToken)) {

                //Get username from token
                var usernameOptional = jwtUtils.extractUsername(jwtToken);

                usernameOptional.ifPresent(username -> {
                    //Fetch User details
                    var userDetails = userDetailsService.loadUserByUsername(username);

                    //Create Authentication
                    var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //set details

                    // Set authentication to security context
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                });


            }
        });


        // Pass request and response to next filter
        filterChain.doFilter(request, response);
    }


    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        //Extract Authentication Header
        var authHeader = request.getHeader(AUTHORIZATION);
        logger.info("Auth Header : " + authHeader);
        // Bearer <JWT Token>
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.substring(7));
        }

        return Optional.empty();
    }


}