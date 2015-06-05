package opPlanner.OPmatcher.Service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Thomas on 05.06.2015.
 */
@Aspect
@Component
public class DataInitAspect {

    @Pointcut("within(@org.springframework.stereotype.Service *) && !within(opPlanner.OPmatcher.Service.DataInitializerService))")
    public void serviceBean() {};

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {}

    @Autowired
    private DataInitializerService dataInitializerService;

    @Before("serviceBean() && publicMethod()")
    public void checkDataInit(JoinPoint joinPoint) {
        if (!dataInitializerService.isInitialized()) {
                dataInitializerService.loadData();
            if (!dataInitializerService.isInitialized()) {
                throw new OPMatcherServicesNotAvailableException();
            }
        }
    }
}
