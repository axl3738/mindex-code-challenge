package com.mindex.challenge.service.helper;

import java.text.*;

public class DateValidatorWithDateFormat{
	
	private String dateFormat;

	public DateValidatorWithDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public boolean isValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
