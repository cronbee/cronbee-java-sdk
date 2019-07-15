package com.aliengen.cronbee.aspect;

import com.aliengen.cronbee.Cronbee;
import com.aliengen.cronbee.CronbeePipeline;
import com.aliengen.cronbee.annotation.CronbeeMonitor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class CronbeeAspect {

    @Pointcut(value="@annotation(monitor) && execution(@com.aliengen.cronbee.annotation.CronbeeMonitor * *.*(..))", argNames="monitor")
    public void methodCall(CronbeeMonitor monitor) {
    }

    @Around("methodCall(monitor)")
    public void around(ProceedingJoinPoint joinPoint, CronbeeMonitor monitor) throws Throwable {
        Cronbee api = new Cronbee();
        CronbeePipeline pipeline = api.start(monitor.uuid());

        try {
            joinPoint.proceed();
            pipeline.stop();
        } catch(Exception e) {
            pipeline.error(e.getMessage(), e.getStackTrace().toString());
            throw e;
        }
    }
}
