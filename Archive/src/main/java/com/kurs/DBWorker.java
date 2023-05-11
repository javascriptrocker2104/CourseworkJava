package com.kurs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class DBWorker implements Serializable{
    
    private class Data implements Serializable
    {
        ArrayList<Info> entries = new ArrayList<Info>();
        int lastID = 0;
    }

    File file;
    Data data = new Data();
    

    DBWorker(File file){
        this.file = file;
    }//конструктор

    public void read() throws IOException, ClassNotFoundException{
        if (!file.exists()) throw new IOException("Файл не существует");
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);//ранее
        data = (Data) ois.readObject();
        ois.close();

    }
    

    public void write() throws IOException{
        if (!file.exists()) throw new IOException("Файл не существует");
        FileOutputStream fos = new FileOutputStream(file, false);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(data);
        oos.flush();//очистка выходного буфера системы
        oos.close();
    }

    public void addEntry(String name, String material, Double volume, Date date, int quantity, int price){
        data.entries.add(new Info(data.lastID++,name, material, volume, date, quantity, price));
    }


    public Info getEntryByID(int ID){
        if (ID>data.lastID){ throw new IndexOutOfBoundsException("Ошибка номера ID");}
        for (Info entry: data.entries){
            if (entry.getID()==ID)
                return entry;
        }
        throw new IndexOutOfBoundsException("Ошибка номера ID");
    }

    private ArrayList<Info> getMinByMaterial(String material){
        int min = Integer.MAX_VALUE;
        ArrayList<Info> minInfos = new ArrayList<>();
        for (Info entry: data.entries){
            if ((entry.getMaterial().equals(material))&&(entry.getQuantity()<=min)){//среди одинаковых материалов с минимальным кол-вом
                if ((entry.getQuantity()<min)||(entry.getPrice()<minInfos.get(0).getPrice())){ //если меньше минимума, или равен, но цена меньше чем у минимума
                    min = entry.getQuantity();
                    minInfos.clear();
                }
                minInfos.add(entry);
            }
        }
        return minInfos;
        
    }

    public ArrayList<Info> getMinMaterials(){
        ArrayList<String> materials = new ArrayList<>();
        for (Info entry: data.entries){
            if (!materials.contains(entry.getMaterial())){
                materials.add(entry.getMaterial());
            }
        }
        ArrayList<Info> minInfos = new ArrayList<>();
        for (String material: materials){
            minInfos.addAll(getMinByMaterial(material));
        }
        return minInfos;

    }

    public ArrayList<Info> getMarkedEntries(boolean mark){
        ArrayList<Info> markedList = new ArrayList<Info>();
        for (Info entry: data.entries){
            if (entry.getMetka()==mark)
                markedList.add(entry);
        }
        return markedList;
    }

    public void removeMarkedEntries(boolean mark){
        ArrayList<Info> markedList = new ArrayList<Info>();
        for (Info entry: data.entries){
            if (entry.getMetka()==mark)
                markedList.add(entry);
        }
        for (Info entry: markedList){
            data.entries.remove(entry);
            //data.lastID--;
        }
    }

    public ArrayList<Info> getList(){
        return data.entries;
    }//записи данных
}
