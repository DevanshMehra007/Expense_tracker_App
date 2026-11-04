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
}
