package com.allsopg.game.utility;

import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by gerard on 24/02/2018.
 */

public interface IWorldObject {
    public void buildBody();
    public FixtureDef getFixtureDef(float density, float friction, float restitution);
    public void reaction();
}
