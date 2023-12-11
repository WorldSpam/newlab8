package com.mygdx.game;

public class MyData {
    private String wat;
    private boolean ded;
    MyData(String wat, boolean ded){
        this.wat = wat;
        this.ded = ded;
    }
    MyData(String wat){
        this.wat = wat;
        this.ded = false;
    }

    public void setDed(boolean ded) {
        this.ded = ded;
    }

    public String getWat() {
        return wat;
    }

    public boolean isDed() {
        return ded;
    }
}
