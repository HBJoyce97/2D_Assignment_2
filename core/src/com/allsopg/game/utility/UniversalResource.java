package com.allsopg.game.utility;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by Harry on 11/05/2018.
 */

public class UniversalResource {
    public TweenManager tweenManager;

    private static UniversalResource instance;

    public static UniversalResource getInstance(){
        if(instance==null){
            instance = new UniversalResource();
        }
        return instance;
    }

    private UniversalResource(){configure();}

    public void configure(){
        Tween.setCombinedAttributesLimit(4);
        tweenManager = new TweenManager();
        Tween.registerAccessor(TweenData.class,
                new TweenDataAccessor());
    }
}
