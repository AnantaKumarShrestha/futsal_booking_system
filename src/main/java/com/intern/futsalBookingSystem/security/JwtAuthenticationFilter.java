package com.intern.futsalBookingSystem.security;


import com.intern.futsalBookingSystem.token.AdminTokenRepo;
import com.intern.futsalBookingSystem.token.FutsalOwnerTokenRepo;
import com.intern.futsalBookingSystem.token.UserTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserModelUserDetailsService userDetailsService;
    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private AdminTokenRepo adminTokenRepo;

    @Autowired
    private FutsalOwnerTokenRepo futsalOwnerTokenRepo;

    @Autowired
    private FutsalOwnerDetailsSerive futsalOwnerDetailsSerive;
    @Autowired
    private AdminUserDetailsService adminUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

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
            boolean isTokenValid=false;
            if(url.startsWith("/users/")) {
                userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                 isTokenValid = userTokenRepository.findByToken(jwt).map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
            }else if(url.startsWith("/admin/")) {
                userDetails = this.adminUserDetailsService.loadUserByUsername(userEmail);
                isTokenValid = adminTokenRepo.findByToken(jwt).map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
            }else if(url.startsWith("/futsal-owner/")){
                userDetails=this.futsalOwnerDetailsSerive.loadUserByUsername(userEmail);
                isTokenValid = futsalOwnerTokenRepo.findByToken(jwt).map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
            }
                        if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
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



