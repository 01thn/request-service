package com.reqserv.requestservice.aop;

import com.reqserv.requestservice.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Profile("local")
public class ExecutionTimeAspect {

  @Around("@within(logExecutionTime)")
  public Object measureExecutionTime(ProceedingJoinPoint joinPoint, LogExecutionTime logExecutionTime) throws Throwable {
    long startTime = System.nanoTime();
    Object result = joinPoint.proceed();
    long endTime = System.nanoTime();
    double executionTime = (endTime - startTime) / 1000000.0;
    log.info("Method {} execution time: {} ms", joinPoint.getSignature(), executionTime);
    return result;
  }

}
