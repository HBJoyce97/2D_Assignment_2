package com.allsopg.game.utility;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Harry on 11/05/2018.
 */

public class TweenData {
    private Vector2 xy;
    private Color colour;
    private float scale;
    private float rotation;

    public TweenData(){
        xy = new Vector2();
        colour = new Color();
    }

    //accessors
    public float getScale(){return scale;}
    public Vector2 getXY(){return xy;}
    public Color getColour(){return colour;}
    public float getRotation(){return rotation;}

    public void setXY(float x,float y){xy.x = x;xy.y=y;}
    public void setColour(Color c){colour=c;}
    public void setScale(float s){scale=s;}
    public void setRotation(float r){rotation=r;}
}
