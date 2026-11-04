package com.dev.Expense_tracker_App.service;

import com.dev.Expense_tracker_App.Entities.RefreshToken;
import com.dev.Expense_tracker_App.Entities.UserInfo;
import com.dev.Expense_tracker_App.repo.RefreshTokenRepo;
import com.dev.Expense_tracker_App.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService
{

    @Autowired
    RefreshTokenRepo refreshTokenRepo;

    @Autowired
    UserRepo userRepo;

    public RefreshToken  createRefreshToken(String username)
    {
        UserInfo userInfoExtracted = userRepo.findByUsername(username);
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userInfoExtracted)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();
        return refreshTokenRepo.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0)
        {
            refreshTokenRepo.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired . Please make a new log in...");
        }
        return token;
    }
}
