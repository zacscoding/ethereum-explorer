package org.explorer.aop;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.explorer.entity.annotation.ValidateMe;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
@Aspect
public class PreValidateAdvice {

    private static Object LOCK = new Object();
    private Map<Class<?>, Boolean> validateMeCache;


    @PostConstruct
    private void setUp() {
        validateMeCache = new ConcurrentHashMap<>();
    }

    @Before("@annotation(org.explorer.aop.annotation.PreValidate)")
    public void preValidate(JoinPoint jp) throws Throwable {
        for (Object object : jp.getArgs()) {
            Boolean existValidate = validateMeCache.get(object.getClass());
            if (existValidate == null) {
                synchronized (LOCK) {
                    if ((existValidate = validateMeCache.get(object.getClass())) == null) {
                        existValidate = Boolean.FALSE;
                        for (Method method : object.getClass().getDeclaredMethods()) {
                            if (method.isAnnotationPresent(ValidateMe.class)) {
                                existValidate = Boolean.TRUE;
                                break;
                            }
                        }
                        validateMeCache.put(object.getClass(), existValidate);
                    }
                }
            }

            if (existValidate == Boolean.TRUE) {
                try {
                    for (Method method : object.getClass().getDeclaredMethods()) {
                        if (method.isAnnotationPresent(ValidateMe.class)) {
                            method.invoke(object);
                        }
                    }
                } catch (Throwable t) {
                    log.error("failed to validate ", t);
                    throw t;
                }
            }
        }
    }
}