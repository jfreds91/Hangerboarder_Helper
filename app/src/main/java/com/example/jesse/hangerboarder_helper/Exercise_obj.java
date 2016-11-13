package com.example.jesse.hangerboarder_helper;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jesse on 10/13/2016.
 */

public class Exercise_obj implements Serializable { //extends ArrayList {
    private String name;
    private String spinnerType;
    private int spinnerPosition;
    private ArrayList<Double> exweights = new ArrayList<>();

    public Exercise_obj(String name){
        super();
        this.setName(name);
        exweights.add(new Double(0)); //when an exercise is initialized, it will start with a weight of 0
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void add(Double weight) {
        this.exweights.add(weight);
    }
    public Double getLast() { return this.exweights.get(this.exweights.size() - 1); }

    public int getSpinnerPosition() { return this.spinnerPosition; }
    public void setSpinnerPosition(int i) {this.spinnerPosition = i;}

    public String getSpinnerType() { return this.spinnerType; }
    public void setSpinnerType(String s) {this.spinnerType = s; }
}
