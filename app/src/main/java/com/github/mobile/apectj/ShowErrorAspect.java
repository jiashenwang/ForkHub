package com.github.mobile.apectj;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.github.mobile.util.ToastUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import java.io.IOException;
import java.lang.reflect.Method;

@Aspect
public class ShowErrorAspect {
    private static final String POINTCUT_METHOD =
            "execution(@com.github.mobile.apectj.ShowError * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@com.github.mobile.apectj.ShowError *.new(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotatedWithShowError() {}

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructorAnnotatedShowError() {}

    @Around("methodAnnotatedWithShowError() || constructorAnnotatedShowError()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();

        Method method = methodSignature.getMethod();
        ShowError myAnnotation = method.getAnnotation(ShowError.class);
        String logMsg = myAnnotation.logMsg();

        Object[] args = joinPoint.getArgs();
        Exception e = (Exception) args[0];

        Object result = joinPoint.proceed();

        Log.d(className, logMsg, e);
        ToastUtils.show((Activity) joinPoint.getThis(), e.getMessage());

        return result;
    }

}