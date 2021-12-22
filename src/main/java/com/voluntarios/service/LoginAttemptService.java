package com.voluntarios.service;

public interface LoginAttemptService {
    void evictUserFromLoginAttemptCache(String username);
    void addUserToLoginAttemptCache(String username);
    boolean hasExceededMaxAttempts(String username);
}
