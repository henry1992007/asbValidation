package com.company;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by henry on 15/11/4.
 */
@Aspect
public class CheckBizObj {

    @Pointcut("execution(* com.company.Business.biz(..))")
    public void needCheck() {

    }

    @Before("needCheck()")
    public void checkObj() {
        System.out.println("checked");
    }
}
