package com.github.julywind.auth.aop;

import com.github.julywind.auth.AuthUser;
import com.github.julywind.auth.exception.AuthenticationFailedException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;


@Slf4j
@Aspect
@Repository
public class AuthInterceptor extends AuthorizedCache {
    @Autowired
    private AuthUser authUser;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void authFilter() {
    }

    @Around("authFilter()")
    public Object aroundApi(ProceedingJoinPoint pjp) throws Throwable {
        AuthMethod authMethod = super.getAuthMethod(pjp);
        return this.checkAuthority(authMethod, pjp);
    }


    /**
     * 1、无注解时，跳过
     * 2、需要权限时，进行验证
     * 3、需要注入用户时，注入；如果authUser返回空，注入User会为null
     */
    private Object checkAuthority(@NotNull AuthMethod authMethod, ProceedingJoinPoint pjp) throws Throwable {
        if (authUser == null) {
            log.debug("Empty AuthFilter Entity");
            return pjp.proceed();
        }
        Object user = authUser.getUser(this.getRequest());
        boolean hasAuthorizedAnno = authMethod.hasAuthorizedAnno();
        boolean hasCurrentUserAnno = authMethod.hasCurrentUserAnno();
        if (!hasAuthorizedAnno && !hasCurrentUserAnno) {
            return pjp.proceed();
        }
        if (hasAuthorizedAnno && !authUser.isAuthorized(authMethod.getAuthorized().role(), user)) {
            throw new AuthenticationFailedException(HttpStatus.UNAUTHORIZED);
        }
        if (hasCurrentUserAnno) {
            if (user != null && authMethod.isLegalType(user.getClass())) {
                pjp.getArgs()[authMethod.getParamIndex()] = user;
            }
        }

        return pjp.proceed(pjp.getArgs());
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.init(applicationContext);
    }
}
