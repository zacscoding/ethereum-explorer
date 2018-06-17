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
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
@Aspect
public class ControllerLoggerAdvice {

    @Around("execution(* org.explorer.web.*Controller.*(..))"
            + "&& !@annotation(org.explorer.aop.annotation.NoLogging)")
    public Object logProcess(ProceedingJoinPoint pjp) throws Throwable {

        String id = pjp.getTarget().getClass().getSimpleName() + " :: " + pjp.getSignature().getName();
        HttpServletRequest req = ServletUtil.getHttpServletRequest();
        StringBuilder sb = new StringBuilder();
        sb.append("## ======================Before Catch =================================== ##\n");
        sb.append("ID : " + id).append('\n');
        sb.append("Param : " + GsonUtil.toStringPretty(pjp.getArgs())).append('\n');
        sb.append("URI : " + req.getRequestURI()).append('\n');
        sb.append("IP : " + ServletUtil.getIpAddr(req)).append('\n');
        sb.append("## ===================================================================== ##").append('\n');
        log.info("\n" + sb.toString());

        long elapsed = 0L;
        String exception = null;
        try {
            long start = System.currentTimeMillis();
            Object result = pjp.proceed();
            long end = System.currentTimeMillis();
            elapsed = (end - start);
            return result;
        } catch (Exception e) {
            exception = e.getMessage();
            throw e;
        } finally {
            /*log.info("## ======================After Catch =================================== ##");
            log.info("ID : " + id);
            if (exception == null) {
                log.info("Elapsed : {} [ms]", elapsed);
            } else {
                log.info("ERROR : " + exception);
            }
            log.info("## ===================================================================== ##");*/
        }
    }

}
