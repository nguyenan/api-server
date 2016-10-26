package com.wut.support.annotations;

public @interface ClassPreamble {
	   String author();
	   String date() default "n/a";
	   int currentRevision() default 1;
	   String lastModified() default "N/A";
	   String lastModifiedBy() default "N/A";
	   String[] reviewers() default {};
}
