package com.innext.xjx;

import com.innext.xjx.util.TimeUtil;

import org.junit.Test;

import java.util.Calendar;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        //assertEquals(4, 2 + 2);
        System.out.print(TimeUtil.getCurrentDateByOffset("yyyy-MM-dd 00:00:00", Calendar.DAY_OF_MONTH,25));
    }
}