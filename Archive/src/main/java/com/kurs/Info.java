package com.kurs;

import java.io.Serializable;
import java.util.Date;

public class Info implements Serializable{
    private String name;
    private int ID;
    private String material;
    private Double volume;
    private int quantity;
    private Date date;
    private int price;
    private boolean metka = false;

    Info(){
        this(0,"abcd", "abcd",0.3,new Date(),1,30);
    }//конструктор

    Info(int ID, String name, String material, Double volume, Date date, int quantity, int price){//конструктор
        this.name = name;
        this.material = material;
        this.volume = volume;
        this.date = date;
        this.quantity = quantity;
        this.price = price;
        this.ID = ID;
    }

    public void invertMetka(){
        metka = !metka;
    }


    public String getName(){
        return name;
    }

    public String getMaterial(){
        return material;
    }

    public Double getVolume(){
        return volume;
    }

    public Date getDate(){
        return date;
    };

    public int getQuantity(){
        return quantity;
    }

    public int getPrice(){
        return price;
    }

    public boolean getMetka(){
        return metka;
    }

    public int getID(){
        return ID;
    }

    public void setMetka(boolean metka){
        this.metka = metka;
    }

    @Override
    public boolean equals(Object o){
        Info i = (Info) o;
        return i.getID()==ID;
    }

}
