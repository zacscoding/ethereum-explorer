package org.explorer.aop;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.explorer.util.GsonUtil;
import org.explorer.util.ServletUtil;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-04-23
 * @GitHub : https://github.com/zacscoding
 */

@Slf4j
@Component
@Aspect
public class LoggerAdvice {

    @Around("execution(* org..*Controller.*(..))")
    public Object logProcess(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long end = System.currentTimeMillis();
        HttpServletRequest req = ServletUtil.getHttpServletRequest();

        log.info("## =================================================================== ##");
        log.info("Request {} :: {}", pjp.getTarget().getClass().getName(), pjp.getSignature().getName());
        log.info("Param : " + GsonUtil.toStringPretty(pjp.getArgs()));
        log.info("Elapsed : {} ms", (end - start));
        log.info("URI : " + req.getRequestURI());
        log.info("IP : " + ServletUtil.getIpAddr(req));
        log.info("## =================================================================== ##");

        return result;
    }
}
