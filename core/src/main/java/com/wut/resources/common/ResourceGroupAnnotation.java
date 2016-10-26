package com.wut.resources.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceGroupAnnotation {
	String name();
	String group() default "none";
	String desc() default "n/a";
	int revision() default 1;
}
