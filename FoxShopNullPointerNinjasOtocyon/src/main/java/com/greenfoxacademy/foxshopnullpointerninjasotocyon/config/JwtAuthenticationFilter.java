package com.greenfoxacademy.foxshopnullpointerninjasotocyon.config;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.FoxUserDetails;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //private final JwtTokenService jwtTokenService;
// @Autowired
// private UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        String token = jwtTokenService.resolveToken(request); //TODO needed Zoltan code with JwtTokenService
//        if (StringUtils.hasText(token) && jwtTokenService.validateTokenSignature(token) ) { //TODO add check if not expired

//        String username = jwtTokenService.parseJwt(token);
//        Optional<User> userOpt = userService.findByUsername(username);
//        try {
//            User user = userOpt.orElseThrow(AuthenticationException::new);
//            UserDetails userDetails = FoxUserDetails.fromUser(user);
//            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, List.of(new SimpleGrantedAuthority("user"));
//            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);//
//        } catch (AuthenticationException e) {
//            throw new RuntimeException(e);
//        }
        filterChain.doFilter(request, response);


    }


}
