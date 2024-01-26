package com.example.demoSmartJob.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

import static com.example.demoSmartJob.util.UtilsServices.*;

public class UtilsServicesTest {
    private static final String EMAIL_GOOD = "email@domain.org";
    private static final String EMAIL_BAD = "emaildomain.org";
    private static final String REGEX_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private static final Long TIME = 1700150751269L;

    @Test
    void validateValueWithRegex_Successful_Test() {
        Assert.assertTrue(validateValueWithRegex(EMAIL_GOOD, REGEX_PATTERN));
    }

    @Test
    void validateValueWithRegex_Fail_Test() {
        Assert.assertFalse(validateValueWithRegex(EMAIL_BAD, REGEX_PATTERN));
    }

    @Test
    public void covertDateStr_Successful_Test() {
        String date = "16-11-2023 13:05:51";
        Assert.assertEquals(date, covertDateStr(TIME));
    }

    @Test
    public void generateUUID_Test() {
        Assert.assertEquals(36, generateUUID().length());
    }

}
