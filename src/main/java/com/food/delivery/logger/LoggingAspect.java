package com.food.delivery.logger;

import com.food.delivery.exception.FoodDeliveryException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import java.util.Arrays;
@Aspect
@Configuration
public class LoggingAspect
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Environment env;

    public LoggingAspect(Environment env)
    {
        this.env = env;
    }

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Component *)" +
            " || within(@org.springframework.stereotype.Service *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut("within(com.food.delivery.service.impl..*)" +
            " || within(com.food.delivery.schedule..*)" +
            " || within(com.food.delivery.common..*)" +
            " || within(com.food.delivery.repository..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }


    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanControllerPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut("within(com.food.delivery.restapi..*)")
    public void applicationControllerPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e)
    {
        if (env.acceptsProfiles(Profiles.of("dev","stage","demo"))) {
            log.error("Exception in {}.{}() with cause = '{}' and exception = '{}'", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), e.getCause() != null? e.getCause() : "NULL", e.getMessage(), e);

        } else {
            log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), e.getCause() != null? e.getCause() : "NULL");
        }
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        } catch (Exception e){
            if(e instanceof FoodDeliveryException)
                throw e;
            log.error("Error occurred please contact support",e);
            throw new FoodDeliveryException(e.getMessage());
        }
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("applicationControllerPackagePointcut() && springBeanControllerPointcut()")
    public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {

        log.debug("Enter: Controller Name: {}, Method Name: {}, Args:{}", joinPoint.getSignature().getDeclaringTypeName(),joinPoint.getSignature().getName(),joinPoint.getArgs().toString());

        try {
            Object result = joinPoint.proceed();
            log.debug("Exit: Controller Name: {}, Method Name: {}, Args:{}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), result.toString());
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        } catch (Exception e){

            if(e instanceof FoodDeliveryException)
                throw e;
            log.error("Error occurred please contact support",e);
            throw new FoodDeliveryException(e.getMessage());
        }
    }
}
