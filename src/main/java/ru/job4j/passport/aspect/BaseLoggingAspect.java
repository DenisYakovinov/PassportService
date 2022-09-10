package ru.job4j.passport.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

@Aspect
@Component
public class BaseLoggingAspect {

    private static final Logger log
            = LoggerFactory.getLogger(BaseLoggingAspect.class);

    @Pointcut("@annotation(ru.job4j.passport.aspect.Loggable)")
    private void loggableMethodAttached() {

    }

    @Pointcut("@within(ru.job4j.passport.aspect.Loggable)")
    private void loggableClassAttached() {

    }

    @Pointcut("execution(public * *(..))")
    private void allPublicMethods() {

    }

    @Before("allPublicMethods() && loggableClassAttached() || loggableMethodAttached()")
    public void logBeforeCall(JoinPoint joinPoint) {
        if (log.isDebugEnabled()) {
            String arguments = buildArgsLine(joinPoint);
            String methodName = joinPoint.getSignature().getName();
            String classname = joinPoint.getTarget().getClass().getName();
            if (!arguments.equals("")) {
                log.debug("begin {}::{} with arguments: {}", classname, methodName, arguments);
            } else {
                log.debug("begin {}::{} as", classname, methodName);
            }
        }
    }

    @AfterReturning(pointcut = "allPublicMethods() && loggableClassAttached()", returning = "result")
    public void logAfterMethodExecute(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String classname = joinPoint.getTarget().getClass().getName();
        if (result != null) {
            log.debug("{}::{} complited with {}", classname, methodName, result);
        } else {
            log.debug("{}::{} complited", classname, methodName);
        }
    }

    private String buildArgsLine(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getMethod().getName();
        Class<?>[] parameterTypes = methodSignature.getMethod().getParameterTypes();
        Annotation[][] parameterAnnotations;
        StringBuilder builder = new StringBuilder();
        try {
            parameterAnnotations = joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes)
                    .getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                Annotation[] annotations = parameterAnnotations[i];
                if (isNonLoggingAnnotationAttached(annotations)) {
                    builder.append("[private parameter], ");
                } else {
                    builder.append("[").append(args[i]).append("] ");
                }
            }
        } catch (NoSuchMethodException | SecurityException e) {
            log.warn(e.getMessage());
            builder.append("can't get arguments");
        }
        return builder.toString();
    }

    private boolean isNonLoggingAnnotationAttached(Annotation[] annotations) {
        return Stream.of(annotations).anyMatch(a -> a.annotationType() == NonLoggableParameter.class);
    }
}