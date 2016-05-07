package net.jarl.kata.currencyzoo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import mockit.Mocked;
import mockit.NonStrictExpectations;

/**
 * Base class for all test classes that assume some user has been successfully
 * authenticated.
 *
 * @author José A. Romero L.
 */
public abstract class AuthenticatedTestContext {

    @Mocked
    protected SecurityContext securityCtx;

    @Mocked
    protected Authentication authentication;

    @Mocked
    protected UserDetails userDetails;

    protected void assumeAuthenticatedUser() {
        new NonStrictExpectations( SecurityContextHolder.class ) {{
            SecurityContextHolder.getContext(); result = securityCtx;
        }};
        new NonStrictExpectations() {{
            securityCtx.getAuthentication(); result = authentication;
            authentication.getPrincipal(); result = userDetails;
        }};
    }
}
