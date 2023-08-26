package com.hirshi001.game;

import com.badlogic.gdx.graphics.Color;

public enum Team {

    RED(Color.RED), BLUE(Color.BLUE);

    private Color color;

    Team(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }
}
