package com.wut.model.scalar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.wut.support.ErrorHandler;

public class DateData extends StringData {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	// TODO use epoche
	public DateData(String epoche) {
		super(epoche);
	}

	public String toYyyyMmDd() {
//		try {
			DateFormat df = formatter;
			df.setTimeZone(TimeZone.getTimeZone("GMT+0"));
			//System.out.println(df.format(new Date(elapsed)));
			//Date date = formatter.parse(this.data);
			long longDate = Long.parseLong(this.data);
			Date date = new Date(longDate);
			String dateFormated = formatter.format(date);
			return dateFormated;
//		} catch (ParseException e) {
//			ErrorHandler.userError("invalid date format", e);
//		}
//		return null;
	}

}
