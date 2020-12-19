package com.example.newprogram4;

import android.widget.Toast;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        String password = "123245a";
        final String passwordPattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$";
        assertTrue(!password.matches(passwordPattern));
        if (!password.matches(passwordPattern)){
            System.out.println(123);
        }
    }
}