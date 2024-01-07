package com.intern.futsalBookingSystem.security;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.lang.Assert;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {



 //   private final JwtService jwtService;
 //   private final UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserModelUserDetailsService userDetailsService;
  //  private final TokenRepository tokenRepository;

    @Autowired
    private FutsalOwnerDetailsSerive futsalOwnerDetailsSerive;
    @Autowired
    private AdminUserDetailsService adminUserDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

         String authHeader = request.getHeader("Authorization");
         String jwt;
         String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            String url=request.getServletPath();
            UserDetails userDetails=null;
            if(url.startsWith("/users/")) {
                userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            }else if(url.startsWith("/admin/")) {
                userDetails = this.adminUserDetailsService.loadUserByUsername(userEmail);
            }else if(url.startsWith("/futsal-owner/")){
                userDetails=this.futsalOwnerDetailsSerive.loadUserByUsername(userEmail);
            }
                        if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}



