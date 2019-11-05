package com.github.julywind.auth;

import javax.servlet.http.HttpServletRequest;

public interface AuthUser<T> {
    /**
     * to get T object from request, using for injection into the target method
     * @param request httpServletRequest
     */
    T getUser(HttpServletRequest request);

    /**
     * to check user permission
     * @param requiredRole required role name
     * @param user user to check
     *
     * @return true: yes, false:no
     */
    boolean isAuthorized(String requiredRole, T user);
}
