package com.qixiu.lejia;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        //assertEquals(4, 2 + 2);
        String s = "420114199103103719";
        System.out.println(s.length());

    }

    @Test
    public void md5() throws Exception{
//        582043c00bd1cd061d7ce73017ff7746
        String s = "58:20:43:C0:0B:D1:CD:06:1D:7C:E7:30:17:FF:77:46";
        String replace = s.replace(":", "");
        System.out.println(replace.toLowerCase());



    }


}