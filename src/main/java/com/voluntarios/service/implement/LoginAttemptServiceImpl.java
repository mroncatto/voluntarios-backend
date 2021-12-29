package com.voluntarios.service.implement;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.voluntarios.service.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MINUTES;

@Slf4j
@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {
    @Value("${app.login.attempts}")
    private int attempts;
    private final LoadingCache<String, Integer> loginAttemptCache;

    public LoginAttemptServiceImpl() {
        this.loginAttemptCache = CacheBuilder.newBuilder().expireAfterWrite(15, MINUTES).maximumSize(100)
                .build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    // Limpia el cache al realizar login
    @Override
    public void evictUserFromLoginAttemptCache(String username) {
        loginAttemptCache.invalidate(username);
    }

    // Incrementa el cache por una tentativa incorrecta de login
    @Override
    public void addUserToLoginAttemptCache(String username) {
        int attempts = 0;
        try {
            attempts = 1 + loginAttemptCache.get(username);
        } catch (ExecutionException e) {
            log.error("Error logging in: {}", e.getMessage());
        }
        loginAttemptCache.put(username, attempts);
    }

    // Verifica si el usuario ultrapaso las tentativas permitidas de login
    @Override
    public boolean hasExceededMaxAttempts(String username) {
        try {
            return loginAttemptCache.get(username) >= attempts;
        } catch (ExecutionException e) {
            log.error("Error logging in: {}", e.getMessage());
        }
        return false;
    }
}
