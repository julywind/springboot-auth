package com.github.julywind.auth.aop;

import com.github.julywind.auth.anno.Authorized;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public abstract class AuthorizedCache implements ApplicationContextAware {
    private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());
    /**
     * UnmodifiableMap 所有mapping的方法缓存，不可更改，启动后不提供刷新（没必要）
     */
    private Map<Method, AuthMethod> methodCache;

    /**
     * UnmodifiableSet 需要拦截的Mapping注解，不可更改
     */
    private Set<Object> methodMapping;

    void init(ApplicationContext applicationContext) {
        this.methodMapping = this.initMethodSet();
        this.methodCache = this.refreshAuthMethod(applicationContext);
    }

    AuthMethod getAuthMethod(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        return getAuthMethod(signature.getMethod(), pjp.getTarget().getClass());
    }

    /**
     * 根据方法获取AuthMethod
     */
    private AuthMethod getAuthMethod(Method method, Class clz) {
        if (methodCache == null) {
            log.warn("methodCache IS NULL!");
            return new AuthMethod(method, getTypeAnno(clz));
        }
        AuthMethod authMethod = methodCache.get(method);
        if (authMethod == null) {
            log.info("Cannot find {} in cache", method.toString());
            return new AuthMethod(method, getTypeAnno(clz));
        }
        return authMethod;
    }

    /**
     * 获取AuthMethod集合，非线程安全
     */
    private Map<Method, AuthMethod> refreshAuthMethod(ApplicationContext applicationContext) {
        log.info("Refresh Controller Authorized Method Cache");
        HashMap<Method, AuthMethod> cache = new HashMap<>();
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        for (HandlerMethod handlerMethod : map.values()) {
            if (checkController(handlerMethod.getBeanType()) &&
                    checkMethod(handlerMethod.getMethod())) {
                cacheMethod(handlerMethod.getMethod(), handlerMethod.getBeanType(), cache);
            }
        }

        log.info("{} AuthMethods Cached", cache.size());
        return Collections.unmodifiableMap(cache);
    }

    private void cacheMethod(Method method, Class type, HashMap<Method, AuthMethod> cache) {
        if (!cache.containsKey(method)) {
            cache.put(method, new AuthMethod(method, this.getTypeAnno(type)));
        }
    }


    private Set<Object> initMethodSet() {
        HashSet<Object> set = new HashSet<>();
        set.add(RequestMapping.class);
        set.add(GetMapping.class);
        set.add(PostMapping.class);
        set.add(DeleteMapping.class);
        set.add(PutMapping.class);
        return Collections.unmodifiableSet(set);
    }

    private boolean checkController(Class clz) {
        return clz.getDeclaredAnnotation(Controller.class) != null ||
                clz.getDeclaredAnnotation(RestController.class) != null;
    }

    private Authorized getTypeAnno(Class clz) {
        return (Authorized) clz.getAnnotation(Authorized.class);
    }

    private boolean checkMethod(Method method) {
        if (method != null) {
            for (Annotation annotation : method.getAnnotations()) {
                if (methodMapping.contains(annotation.annotationType())) {
                    return true;
                }
            }
        }
        return false;
    }
}
