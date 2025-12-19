package nl.hkstwk.calculationmodule.aop;

import lombok.extern.slf4j.Slf4j;
import nl.hkstwk.calculationmodule.model.CurrentUser;
import nl.hkstwk.calculationmodule.utils.UserUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* nl.hkstwk.calculationmodule.controllers.*Controller.*(..))")
    public void controllerAllMethods() {
    }

    @Pointcut("execution(* nl.hkstwk.calculationmodule.services.*Service.*(..))")
    public void serviceAllMethods() {
    }

    @Pointcut("execution(* nl.hkstwk.calculationmodule.utils.calculators.*Calculator.*(..))")
    public void calculatorAllMethods() {
    }

    @Around("controllerAllMethods() || serviceAllMethods() || calculatorAllMethods()")
    public Object logControllerMethod(ProceedingJoinPoint controllerMethod) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CurrentUser currentUser = UserUtil.fetchCurrentUserFromContext();
        log.debug("Current User: {}", currentUser);

        String className = controllerMethod.getTarget().getClass().getSimpleName();
        String methodName = controllerMethod.getSignature().getName();
        Object[] arguments = controllerMethod.getArgs();
        log.info("Executing {}.{} with parameters: {}", className, methodName, arguments);

        Object result = null;
        try {
            result = controllerMethod.proceed();
            return result;
        } finally {
            log.info("Completed {}.{} successfully with return value: {}", className, methodName, result);
        }
    }
}
