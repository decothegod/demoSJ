package com.example.demoSmartJob.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

public class UtilsServices {
    private UtilsServices() {
    }

    public static Boolean validateValueWithRegex(String value, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(value)
                .matches();
    }

    public static String covertDateStr(long timeMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(new Date(timeMillis));
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
