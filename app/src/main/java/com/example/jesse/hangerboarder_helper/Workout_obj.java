package com.example.jesse.hangerboarder_helper;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jesse on 10/12/2016.
 */

public class Workout_obj implements Serializable {//extends ArrayList<Exercise_obj> {
    private String name;
    private ArrayList<Exercise_obj> exlist = new ArrayList<>();

    public Workout_obj(String name){
        super();
        this.setName(name);
    }

    public int size(){
        int i = exlist.size();
        return i;
    }

    public Exercise_obj get(int i) {
        return exlist.get(i);
    }

    public void clear() {
        exlist.clear();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void add(Exercise_obj ex) {
        this.exlist.add(ex);
    }

    public String extoString() { //this will print all exercises to a string
        String exlist = "";
        /*int n = this.size();

        for (int i = 0; i < n; i++) {
            exlist += this.get(i).getName() + ", ";
        } */

        for (Exercise_obj ex: this.exlist) {
            exlist += ex.getName() + ", ";
        }
        return exlist;
    }

    public void clearAll() {
        this.clear();
        name = "";
    }

    public boolean isEmpty() {
        boolean b = false;
        if (name.isEmpty() && exlist.isEmpty()){
            b = true;
        }
        return b;
    }
}
