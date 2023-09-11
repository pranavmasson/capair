package com.capair.api.aspect;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	@Around("execution(* com.capair.api.controller.*.*(..)) || "
			+ "execution(* com.capair.api.service.*.*(..)) ")
	public Object log(ProceedingJoinPoint pjp) throws Throwable {
		String classname = pjp.getTarget().getClass().getName();
		final Logger log = LogManager.getLogger(classname);
		if(log.isTraceEnabled()) {
			final String methodName = classname + "." + pjp.getSignature().getName() + "(..)";
			log.trace("Entering " + methodName);
			Object response = null;
			try {
				response = pjp.proceed();
				log.trace("Exiting " + methodName);
				return response;
			} catch (Throwable e) {
				log.error("Exception found in " + methodName + " - " + e.getMessage());
				log.trace(e);
				throw e;
			}
		}
		else {
			return pjp.proceed();
		}
	}
}

