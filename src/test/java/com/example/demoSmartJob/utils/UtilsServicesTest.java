package com.example.demoSmartJob.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.example.demoSmartJob.util.UtilsServices.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UtilsServicesTest {
    private static final String EMAIL_GOOD = "email@domain.org";
    private static final String EMAIL_BAD = "emaildomain.org";
    private static final String REGEX_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private static final Long TIME = 1700150751269L;

    @Test
    public void validateValueWithRegex_Successful_Test() {
        Assert.assertTrue(validateValueWithRegex(EMAIL_GOOD, REGEX_PATTERN));
    }

    @Test
    public void validateValueWithRegex_Fail_Test() {
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
