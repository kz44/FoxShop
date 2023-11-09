package com.greenfoxacademy.foxshopnullpointerninjasotocyon.config;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //private final JwtTokenService jwtTokenService;
 @Autowired
 private UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        String token = jwtTokenService.resolveToken(request); //TODO needed Zoltan code with JwtTokenService
//        if (StringUtils.hasText(token) && jwtTokenService.validateTokenSignature(token) ) {
//            String username = jwtTokenService.parseJwt(token);
//            UserDetails userDetails = userService.loadUserByUsername(username);
//            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, List.of(new SimpleGrantedAuthority("user"));
//            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);//
//        }
        filterChain.doFilter(request, response);

    }


}
