package com.kurs;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DBWorkerTest {
    private DBWorker dbw;//бд
    private File file;//файл
    @Rule
    public TemporaryFolder folder= new TemporaryFolder();//временная папка
    

    @Before 
    public void setUp() throws Exception{
        file = folder.newFile("test.bin");
        dbw = new DBWorker(file);
        dbw.addEntry("abcd", "abcd",0.3,new Date(),1,30);
    }
    

    @Test 
    public void testAddEntry(){
        Info entry = dbw.getEntryByID(0);
        assertEquals(entry.getID(),0);
        assertEquals(entry.getName(),"abcd");
        assertEquals(entry.getMetka(),false);
        assertEquals(entry.getMaterial(),"abcd");
        assertEquals(entry.getVolume(),0.3,0);
        assertEquals(entry.getQuantity(),1);
        assertEquals(entry.getPrice(),30);
        dbw.addEntry("abcd", "abcd",0.3,new Date(),1,30);
        assertEquals(dbw.getEntryByID(1).getID(),1);
    }
    @Test
    public void testIO() throws IOException, ClassNotFoundException{
        ArrayList<Info> list = dbw.getList();
        dbw.write();
        dbw = new DBWorker(file);
        dbw.read();
        assertArrayEquals(list.toArray(),dbw.getList().toArray());//два массива равны
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDelete(){
        dbw.addEntry("test", "test",0.2,new Date(),10,330);
        dbw.getList().get(0).setMetka(true);
        dbw.removeMarkedEntries(true);
        dbw.getEntryByID(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryByID(){
        dbw.getEntryByID(-1);
    }

    @Test
    public void testGetMinMaterial(){
        dbw.addEntry("test", "test",0.2,new Date(),10,330);
        dbw.addEntry("test", "test",0.2,new Date(),8,320);
        dbw.addEntry("test", "test",0.2,new Date(),8,310);
        dbw.addEntry("test1", "test",0.2,new Date(),8,310);
        ArrayList<Info> expected = new ArrayList<>();
        expected.add(dbw.getEntryByID(0));
        expected.add(dbw.getEntryByID(3));
        expected.add(dbw.getEntryByID(4));
        ArrayList<Info> test = dbw.getMinMaterials();
        assertArrayEquals(expected.toArray(),test.toArray());
    }
    
}
