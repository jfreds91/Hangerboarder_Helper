package com.example.jesse.hangerboarder_helper;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jesse on 10/13/2016.
 */

public class Exercise_obj implements Serializable { //extends ArrayList {
    private String name;
    private ArrayList<Integer> exweights = new ArrayList<>();

    public Exercise_obj(String name){
        super();
        this.setName(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void add(Integer weight) {
        this.exweights.add(weight);
    }

}
