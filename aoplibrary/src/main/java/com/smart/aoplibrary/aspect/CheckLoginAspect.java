package com.smart.aoplibrary.aspect;

import android.content.Context;
import android.view.View;

import com.smart.aoplibrary.utils.CommonUtils;
import com.smart.aoplibrary.utils.LogUtils;
import com.smart.aoplibrary.utils.SPUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @date : 2018/6/9 16:07
 * @author: lichen
 * @email : 1960003945@qq.com
 * @description :判断是否登录的切面处理
 */

public class CheckLoginAspect {


    @Pointcut("execution(@com.example.pc.aopdemo.aop.annotation.CheckLogin * *(..))")
    public void methodAnnotated() {
    }


    /**
     * 判断是否登录
     * @param joinPoint
     * @throws Throwable
     */
    @Around("methodAnnotated()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Context context= (Context) joinPoint.getThis();
        if (null == SPUtils.get(context.getApplicationContext(),"user",null)) {
            return;
        }
        joinPoint.proceed();//执行原方法
    }
}
