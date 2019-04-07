package com.github.julywind.auth.aop;

import com.github.julywind.auth.anno.Authorized;
import com.github.julywind.util.AnnotationUtil;
import com.github.julywind.auth.anno.CurrentUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
@Setter
@NoArgsConstructor
public class AuthMethod {
    private Authorized authorized;
    private CurrentUser currentUser;
    private Class currentUserType;
    private int paramIndex;

    public AuthMethod(Method method, Authorized typeAnnotation) {
        Authorized authorized = method.getAnnotation(Authorized.class);
        if (authorized == null) {
            authorized = typeAnnotation;
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
}
