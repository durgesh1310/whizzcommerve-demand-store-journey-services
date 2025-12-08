package com.ouat.checkout.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DateTimeUtil {

    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static String getCurrentDateWithoutHypens(String format) {
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateObj.format(formatter).replace("-", "");
    }
    
    public static String getCurrentTimeInMillis() {
        return String.valueOf(System.currentTimeMillis() * 1000);
    }
}
