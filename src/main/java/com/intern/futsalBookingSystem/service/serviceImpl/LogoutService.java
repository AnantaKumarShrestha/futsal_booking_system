package com.intern.futsalBookingSystem.service.serviceImpl;


import com.intern.futsalBookingSystem.db.AdminRepo;
import com.intern.futsalBookingSystem.token.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

  @Autowired
  private UserTokenRepository userTokenRepository;

  @Autowired
  private AdminTokenRepo adminTokenRepo;

  @Autowired
  private FutsalOwnerTokenRepo futsalOwnerTokenRepo;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    jwt = authHeader.substring(7);
    String url=request.getServletPath();
    if(url.startsWith("/users/")) {
      UserToken storedUserToken = userTokenRepository.findByToken(jwt).orElse(null);
      if (storedUserToken != null) {
        userTokenRepository.delete(storedUserToken);
        SecurityContextHolder.clearContext();
      }
    }else if(url.startsWith("/admin/")){
      AdminToken storedUserToken = adminTokenRepo.findByToken(jwt).orElse(null);
      if (storedUserToken != null) {
        adminTokenRepo.delete(storedUserToken);
        SecurityContextHolder.clearContext();
      }
    }else{
      FutsalOwnerToken storedUserToken = futsalOwnerTokenRepo.findByToken(jwt).orElse(null);
      if (storedUserToken != null) {
        futsalOwnerTokenRepo.delete(storedUserToken);
        SecurityContextHolder.clearContext();
      }
    }
  }
}
