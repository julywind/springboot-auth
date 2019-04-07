package com.github.julywind.auth;

import javax.servlet.http.HttpServletRequest;

public interface AuthUser<T> {
    /**
     * to get T object from request, using for injection into the target method
     */
    T getUser(HttpServletRequest request);

    /**
     * to check user permission
     *
     * @return true: yes, false:no
     */
    boolean isAuthorized(String requiredRole, T user);
}
