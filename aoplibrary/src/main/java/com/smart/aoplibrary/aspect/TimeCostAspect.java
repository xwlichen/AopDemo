package com.smart.aoplibrary.aspect;



import com.smart.aoplibrary.utils.LogUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.concurrent.TimeUnit;

/**
 * 根据注解TimeLog自动添加打印方法耗代码，通过aop切片的方式在编译期间织入源代码中
 * 功能：自动打印方法的耗时
 */
@Aspect
public class TimeCostAspect {

    /**
     * 切点作用于使用TimeLog注解方法的地方
     */
    @Pointcut("execution(@com.example.pc.aopdemo.aop.annotation.TimeCost * *(..))")
    public void methodAnnotated() {
    }

    /**
     * 切点作用于使用TimeLog注解构造器的地方
     */
    @Pointcut("execution(@com.example.pc.aopdemo.aop.annotation.TimeCost *.new(..))")
    public void constructorAnnotated() {
    }


    /**
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("methodAnnotated() || constructorAnnotated()")//在连接点进行方法替换
    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        LogUtils.showLog("TimeCost getDeclaringClass", methodSignature.getMethod().getDeclaringClass().getCanonicalName());
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        long startTime = System.nanoTime();
        Object result = joinPoint.proceed();//执行原方法
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(methodName + ":");
        for (Object obj : joinPoint.getArgs()) {
            if (obj instanceof String) keyBuilder.append((String) obj);
            else if (obj instanceof Class) keyBuilder.append(((Class) obj).getSimpleName());
        }
        String key = keyBuilder.toString();
        LogUtils.showLog("TimeCost", (className + "." + key + joinPoint.getArgs().toString() + " --->:" + "[" + (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)) + "ms]"));// 打印时间差
        return result;
    }
}