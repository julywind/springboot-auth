package com.github.julywind.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;

public class AnnotationUtil {
    /**
     * 查找方法参数中，第一个有此注解的参数索引位置
     */
    public static <T extends Annotation> void getAnnotationParam(Method method, Class<T> annotation,
                                                                 BiConsumer<Integer, ? super T> action) {
        if (method != null && annotation != null) {
            Annotation[][] annotations = method.getParameterAnnotations();
            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i] != null) {
                    for (Annotation exists : annotations[i]) {
                        if (exists != null && exists.annotationType().equals(annotation)) {
                            action.accept(i, (T) exists);
                            return;
                        }
                    }
                }
            }
        }
        action.accept(-1, null);
    }
}
