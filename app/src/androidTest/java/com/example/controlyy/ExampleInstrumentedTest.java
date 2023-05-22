package com.example.controlyy;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.Random;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.controlyy", appContext.getPackageName());
    }
    @Test
    public void useAppContextq() {
        // Context of the app under test.

        Random random = new Random();
        long rd = (long) random.nextInt(3);
        Log.d("进入文章无障碍服务", String.valueOf(rd));


//        assertEquals(10, rd,3);
    }
}