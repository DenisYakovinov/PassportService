package ru.job4j.passport.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceExceptionAspect {

    @Pointcut("execution(public * *(..))")
    private void allPublicMethods() {
    }

    @Pointcut("within(ru.job4j.passport.service.*)")
    private void inService() {
    }

    @AfterThrowing(pointcut = "allPublicMethods() && inService()", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, DataAccessException exception) {
        throw new ServiceException(exception.getMessage(), exception);
    }
}