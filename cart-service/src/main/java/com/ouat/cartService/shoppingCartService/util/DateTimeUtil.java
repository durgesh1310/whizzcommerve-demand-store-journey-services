package com.ouat.cartService.shoppingCartService.util;

import java.util.Calendar;

public class DateTimeUtil {
    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
