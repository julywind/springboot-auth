package com.github.julywind.auth.anno;

import com.github.julywind.auth.aop.AuthInterceptor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(value = {
        AuthInterceptor.class
    })
public @interface EnableJSecurity {

}
