package com.example.bankapp.security;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthorizationService {

    public boolean isAdmin() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));
    }

    public String getCurrentUsername() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authentication required"
            );
        }

        return authentication.getName();
    }

    public void requireAdmin() {
        if (!isAdmin()) {
            throw new AccessDeniedException("Admin access required");
        }
    }

    public void requireUserAccess(User user) {
        if (isAdmin()) {
            return;
        }

        if (!getCurrentUsername().equals(user.getUsername())) {
            throw new AccessDeniedException("Access denied");
        }
    }

    public void requireAccountAccess(Account account) {
        if (isAdmin()) {
            return;
        }

        if (account.getUser() == null
                || !getCurrentUsername()
                .equals(account.getUser().getUsername())) {

            throw new AccessDeniedException("Access denied");
        }
    }
}