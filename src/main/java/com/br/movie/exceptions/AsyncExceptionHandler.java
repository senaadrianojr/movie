package com.br.movie.exceptions;

import java.lang.reflect.Method;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
	
	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {
		System.out.println("[ERROR-ASYNC] An error ocurred while running an async proccess ");
		System.out.println("Exception message - " + ex.getMessage());
        System.out.println("Method name - " + method.getName());
        for (Object param : params) {
            System.out.println("Parameter value - " + param);
        }
	}

}
