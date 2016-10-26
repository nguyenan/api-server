package com.wut.resources.common;

import com.wut.model.Data;

public @interface ParamAnnotation {
	String name();
	Class<? extends Data> clazz();
	String desc() default "";
}
