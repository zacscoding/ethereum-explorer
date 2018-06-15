package org.explorer.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.explorer.util.TimeUtil;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-06-15
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
@Aspect
public class ElapsedCheckerAdvice {

    @Around("@annotation(org.explorer.aop.annotation.Elapsed)")
    public Object checkElapsed(ProceedingJoinPoint pjp) throws Throwable {
        String id = pjp.getTarget().getClass().getSimpleName() + " :: " + pjp.getSignature().getName();
        long elapsed = 0L;
        boolean isError = false;

        try {
            long start = System.currentTimeMillis();
            Object result = pjp.proceed();
            long end = System.currentTimeMillis();
            elapsed = end - start;
            return result;
        } catch (Throwable t) {
            isError = true;
            throw t;
        } finally {
            if (isError) {
                log.info("## {} is error occur", id);
            } else {
                log.info("## {} elapsed : {}", id, TimeUtil.displayElapsedString(elapsed));
            }
        }
    }


}
