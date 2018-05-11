package com.allsopg.game.bodies;

import com.allsopg.game.physics.WorldManager;
import com.allsopg.game.utility.CurrentDirection;
import com.allsopg.game.utility.IWorldObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import static com.allsopg.game.utility.Constants.DENSITY;
import static com.allsopg.game.utility.Constants.FORCE_X;
import static com.allsopg.game.utility.Constants.FORCE_Y;
import static com.allsopg.game.utility.Constants.FRICTION;
import static com.allsopg.game.utility.Constants.MAX_HEIGHT;
import static com.allsopg.game.utility.Constants.MAX_VELOCITY;
import static com.allsopg.game.utility.Constants.PLAYER_OFFSET_X;
import static com.allsopg.game.utility.Constants.PLAYER_OFFSET_Y;
import static com.allsopg.game.utility.Constants.RESTITUTION;

/**
 * Created by gja10 on 13/02/2017.
 * Updated 02/03/18
 */

public class PlayerCharacter extends AnimatedSprite implements IWorldObject {
    private Body playerBody;
    private boolean facingRight =true;

    public PlayerCharacter(String atlas, Texture t, Vector2 pos) {
        super(atlas, t, pos);
        buildBody();
    }

    @Override
    public void buildBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX(),getY());

        playerBody = WorldManager.getInstance().getWorld().createBody(bodyDef);
        playerBody.setUserData(this);
        playerBody.setFixedRotation(true);
        playerBody.createFixture(getFixtureDef(DENSITY,FRICTION,RESTITUTION));
    }

    @Override
    public void update(float stateTime) {
        super.update(stateTime);
        this.setPosition(playerBody.getPosition().x-PLAYER_OFFSET_X,playerBody.getPosition().y-PLAYER_OFFSET_Y);
        if(!facingRight){flip(true,false);}
    }

    public void move(CurrentDirection direction){
        Vector2 vel = playerBody.getLinearVelocity();
        Vector2 pos = playerBody.getPosition();
        switch(direction){
            case LEFT:
                facingRight=false;
                playmode = Animation.PlayMode.LOOP;
                if (vel.x > -MAX_VELOCITY) {
                playerBody.applyLinearImpulse(-FORCE_X, 0, pos.x, pos.y, true);
                }
                break;
            case RIGHT:
                facingRight=true;
                playmode = Animation.PlayMode.LOOP;
                if (vel.x < MAX_VELOCITY) {
                    playerBody.applyLinearImpulse(FORCE_X, 0, pos.x, pos.y, true);
                }
                break;
            case UP:
                playmode = Animation.PlayMode.NORMAL;
                if (pos.y< MAX_HEIGHT && vel.y < MAX_VELOCITY) {
                    playerBody.applyLinearImpulse(0, FORCE_Y, pos.x, pos.y, true);
                }
                break;
            case STOP:
                if(vel.x > -8 & vel.x < 8)
                    playmode = Animation.PlayMode.NORMAL;
        }
        animation.setPlayMode(playmode);
    }

    @Override
    public FixtureDef getFixtureDef(float density, float friction, float restitution) {
        //prepare for Fixture definition
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth()/2)-.75f,getHeight()/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution=restitution;
        return fixtureDef;
    }

    @Override
    public void reaction() {

    }
}
