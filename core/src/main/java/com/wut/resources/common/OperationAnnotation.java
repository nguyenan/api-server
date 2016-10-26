package com.wut.resources.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface OperationAnnotation {
	String name();
	WutOperation.TYPE type();
}
