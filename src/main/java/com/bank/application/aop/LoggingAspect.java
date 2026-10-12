package com.bank.application.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

	@Around("""
			execution(* com.bank.application.controller..*(..)) ||
			execution(* com.bank.application.service..*(..)) ||
			execution(* com.bank.application.mapper..*(..))
			""")
	public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {

		long startTime = System.currentTimeMillis();

		String className = joinPoint.getSignature().getDeclaringTypeName();

		String methodName = joinPoint.getSignature().getName();

		Object[] inputParameters = joinPoint.getArgs();

		log.info("START - Class: {}, Method: {}, Input: {}", className, methodName, Arrays.toString(inputParameters));

		try {

			Object response = joinPoint.proceed();

			long executionTime = System.currentTimeMillis() - startTime;

			log.info("SUCCESS - Class: {}, Method: {}, Response: {}, Time: {} ms", className, methodName, response,
					executionTime);

			return response;

		} catch (Exception exception) {

			long executionTime = System.currentTimeMillis() - startTime;

			log.error("FAILED - Class: {}, Method: {}, Error: {}, Time: {} ms", className, methodName,
					exception.getMessage(), executionTime, exception);

			throw exception;
		}
	}

	@AfterThrowing(pointcut = """
			    execution(* com.bank.application.service..*(..)) ||
			    execution(* com.bank.application.mapper..*(..))
			""", throwing = "exception")
	public void afterThrowing(JoinPoint joinPoint, Exception exception) {

		log.error("EXCEPTION AFTER THROWING - Class: {}, Method: {}, Error: {}",
				joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
				exception.getMessage(), exception);
	}

	/**
	 * @Before(""" execution(* com.bank.application.controller..*(..)) ||
	 * execution(* com.bank.application.service..*(..)) || execution(*
	 * com.bank.application.mapper..*(..)) """) public void beforeMethod(JoinPoint
	 * joinPoint) { Object[] inputParameters = joinPoint.getArgs();
	 * 
	 * log.info("Before Aspect Class is : {} , method is : {} & parameters are {} ",
	 * joinPoint.getSignature().getDeclaringTypeName(),
	 * joinPoint.getSignature().getName(), Arrays.toString(inputParameters)); }
	 */

	/**
	 * @After(""" execution(* com.bank.application.controller..*(..)) || execution(*
	 * com.bank.application.service..*(..)) || execution(*
	 * com.bank.application.mapper..*(..)) """) public void afterMethod(JoinPoint
	 * joinPoint) { log.info("After Aspect Class is : {} , method is : {}",
	 * joinPoint.getSignature().getDeclaringTypeName(),
	 * joinPoint.getSignature().getName()); }
	 */

	/**
	 * @AfterReturning(pointcut = """ execution(*
	 *                          com.bank.application.controller..*(..))) """,
	 *                          returning = "response") public void
	 *                          afterReturningMethod(JoinPoint joinPoint, Object
	 *                          response) {
	 * 
	 *                          String className =
	 *                          joinPoint.getSignature().getDeclaringTypeName();
	 * 
	 *                          String methodName =
	 *                          joinPoint.getSignature().getName();
	 * 
	 *                          log.info("AfterReturning Aspect - Class: {}, Method:
	 *                          {}, Response: {}", className, methodName, response);
	 *                          }
	 */

}
