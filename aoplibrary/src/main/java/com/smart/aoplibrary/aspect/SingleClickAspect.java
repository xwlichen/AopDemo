package com.smart.aoplibrary.aspect;



import com.smart.aoplibrary.utils.CommonUtils;
import com.smart.aoplibrary.utils.LogUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by xw
 * 防止View被连续点击,间隔时间600ms
 */


@Aspect
public class SingleClickAspect {


    /**
     * 切点作用于使用SingleClick注解的方法
     */
    @Pointcut("execution(@com.example.pc.aopdemo.aop.annotation.SingleClick * *(..))")
    public void methodAnnotated() {
    }


    /**
     * 点击间隔判断
     * @param joinPoint
     * @throws Throwable
     */
    @Around("methodAnnotated()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        LogUtils.d("methodAnnotated");
        if (CommonUtils.isSingleClick()) {
            joinPoint.proceed();  //执行原方法
        }

    }
}
