package com.kurs;// создает развертываемый JAR-файл.
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;


public class InfoTest {
    private Info info;
    @Before
    public void setUp() throws Exception{
        info = new Info();
    }

    @Test
    public void testInvertMetka(){
        boolean temp = info.getMetka();
        info.invertMetka();
        assertEquals(temp, !info.getMetka());
    }

    @Test
    public void testEquals() {
        Info info2 = new Info(1,"abcd", "abcd",0.3,new Date(),1,30);
        assertTrue(!info.equals(info2));
        info2 = new Info();
        assertTrue(info.equals(info2));
    }
}
