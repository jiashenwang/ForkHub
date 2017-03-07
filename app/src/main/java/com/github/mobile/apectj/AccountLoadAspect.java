package com.github.mobile.apectj;


import android.content.Context;
import android.util.Log;

import com.github.mobile.accounts.LoginActivity;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.IOException;


@Aspect
public class AccountLoadAspect {
    private static final String POINTCUT_METHOD =
            //"execution(@org.android10.gintonic.annotation.DebugTrace * *(..))";
            "execution(@com.github.mobile.apectj.AccountLoad * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            //"execution(@org.android10.gintonic.annotation.DebugTrace *.new(..))";
            "execution(@com.github.mobile.apectj.AccountLoad *.new(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotatedWithAccountLoad() {}

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructorAnnotatedAccountLoad() {}

    @Around("methodAnnotatedWithAccountLoad() || constructorAnnotatedAccountLoad()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

//        Object[] args = joinPoint.getArgs();
//        Log.wtf("", "" + args[0].toString());

        Object result = joinPoint.proceed();
        try {
            new AccountLoader((Context) joinPoint.getThis()).call();
        } catch (IOException e) {
            Log.d(className, "Exception loading organizations", e);
        }

        return result;
    }

}