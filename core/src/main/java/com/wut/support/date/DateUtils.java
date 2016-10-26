package com.wut.support.date;

import java.util.Date;

public class DateUtils {

	public static Long getEpocheInUTC() {
//		Date now = new Date();  	
//		Long longTime = new Long(now.getTime()/1000);
//		return longTime;
		Long epocheInUTC = System.currentTimeMillis();
		return epocheInUTC;
	}
}
