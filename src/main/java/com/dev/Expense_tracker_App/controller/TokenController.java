package com.dev.Expense_tracker_App.controller;

import com.dev.Expense_tracker_App.Entities.RefreshToken;
import com.dev.Expense_tracker_App.request.AuthReqDTO;
import com.dev.Expense_tracker_App.request.RefreshTokenReqDTO;
import com.dev.Expense_tracker_App.response.JwtResponseDTO;
import com.dev.Expense_tracker_App.service.JwtService;
import com.dev.Expense_tracker_App.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("auth/v1/login")
    public ResponseEntity AuthenticateAndGetToken(@RequestBody AuthReqDTO authReqDTO)
    {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authReqDTO.getUsername() , authReqDTO.getPassword()));
                if(authentication.isAuthenticated())
                {
                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(authReqDTO.getUsername());
                    return new ResponseEntity<>(JwtResponseDTO.builder()
                            .accessToken(jwtService.generateToken(authReqDTO.getUsername()))
                            .token(refreshToken.getToken())
                            .build(), HttpStatus.OK);

                }
                else
                {
                    return new ResponseEntity<>("Exception in User Service" , HttpStatus.INTERNAL_SERVER_ERROR);
                }
    }

    @PostMapping("auth/v1/refrshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenReqDTO refreshTokenReqDTO){
        return refreshTokenService.findByToken(refreshTokenReqDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getUsername());
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenReqDTO.getToken()).build();
                }).orElseThrow(()-> new RuntimeException("Refresh Token is not in DB..."));
    }
}
