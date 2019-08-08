package com.github.julywind.auth.aop;

import com.github.julywind.auth.anno.Authorized;
import com.github.julywind.auth.anno.SkipAuthorize;
import com.github.julywind.util.AnnotationUtil;
import com.github.julywind.auth.anno.CurrentUser;

import java.lang.reflect.Method;

public class AuthMethod {
    private Authorized authorized;
    private CurrentUser currentUser;
    private Class currentUserType;
    private int paramIndex;

    public AuthMethod() {
    }

    public AuthMethod(Method method, Authorized typeAnnotation) {
        SkipAuthorize skipAuthorize = method.getAnnotation(SkipAuthorize.class);
        Authorized authorized = null;
        if(skipAuthorize==null){
            authorized = method.getAnnotation(Authorized.class);
            if (authorized == null) {
                authorized = typeAnnotation;
            }
        }

        this.authorized = authorized;
        AnnotationUtil.getAnnotationParam(method, CurrentUser.class,
                (i, annotation) -> {
                    this.paramIndex = i;
                    this.currentUser = annotation;
                });
        if (this.paramIndex >= 0) {
            this.currentUserType = method.getParameterTypes()[this.paramIndex];
        }
    }

    public boolean hasAuthorizedAnno() {
        return this.getAuthorized() != null;
    }

    public boolean hasCurrentUserAnno() {
        return this.getParamIndex() > -1;
    }

    public boolean isLegalType(Class clz) {
        if (clz != null) {
            return clz.equals(this.currentUserType);
        }
        return false;
    }

    public Authorized getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Authorized authorized) {
        this.authorized = authorized;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    public Class getCurrentUserType() {
        return currentUserType;
    }

    public void setCurrentUserType(Class currentUserType) {
        this.currentUserType = currentUserType;
    }

    public int getParamIndex() {
        return paramIndex;
    }

    public void setParamIndex(int paramIndex) {
        this.paramIndex = paramIndex;
    }
}
