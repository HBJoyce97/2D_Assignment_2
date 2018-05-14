package com.allsopg.game.bodies;

import com.allsopg.game.physics.WorldManager;
import com.allsopg.game.screens.GameScreen;
import com.allsopg.game.utility.Constants;
import com.allsopg.game.utility.CurrentDirection;
import com.allsopg.game.utility.GameData;
import com.allsopg.game.utility.HUD;
import com.allsopg.game.utility.IWorldObject;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Timer;


import static com.allsopg.game.utility.Constants.AMMO_FRAME_DURATION;
import static com.allsopg.game.utility.Constants.AMMO_START_POS;
import static com.allsopg.game.utility.Constants.ARMOUR_START_POS;
import static com.allsopg.game.utility.Constants.DENSITY;
import static com.allsopg.game.utility.Constants.FORCE_X;
import static com.allsopg.game.utility.Constants.FORCE_Y;
import static com.allsopg.game.utility.Constants.FRICTION;
import static com.allsopg.game.utility.Constants.HEALTH_START_POS;
import static com.allsopg.game.utility.Constants.MAX_HEIGHT;
import static com.allsopg.game.utility.Constants.MAX_VELOCITY;
import static com.allsopg.game.utility.Constants.MEDIUM;
import static com.allsopg.game.utility.Constants.PLAYER_OFFSET_X;
import static com.allsopg.game.utility.Constants.PLAYER_OFFSET_Y;
import static com.allsopg.game.utility.Constants.RESTITUTION;
import static com.allsopg.game.utility.Constants.START_POSITION;
import static com.sun.jmx.mbeanserver.Util.cast;

/**
 * Created by gja10 on 13/02/2017.
 * Updated 02/03/18
 */

public class PlayerCharacter extends AnimatedSprite implements IWorldObject {
    private static float SPAWNER_SCAN_TICK = 0.5f;
    public Body playerBody;
    private boolean facingRight =true;
    private AmmoAnimationMulti ammoref; // Reference to AmmoAnimationMulti for collision detection
    private JacketAnimationMulti jacketref; // Reference to JacketAnimationMulti for collision detection
    private BottleAnimationMulti bottleref; // Reference to BottleAnimationMulti for collision detection
    private HUD hud;                        // Reference to on-screen scores
    private boolean ammoDestroyed = false;  // Flag to check if ammo sprite has been collided with
    private boolean jacketDestroyed = false;  // Flag to check if jacket sprite has been collided with
    private boolean bottleDestroyed = false;  // Flag to check if bottle sprite has been collided with

    // Constructor includes parameters from GameScreen
    public PlayerCharacter(String atlas, Texture t, Vector2 pos, AmmoAnimationMulti ammoanim, JacketAnimationMulti jacketanim, BottleAnimationMulti bottleanim) {
        super(atlas, t, pos);
        buildBody();
        ammoref = ammoanim; // Initialize reference
        ammotickMethod(); // Call collision checking method for ammo
        jacketref = jacketanim; // Initialize reference
        jackettickMethod(); // Call collision checking method for jacket
        bottleref = bottleanim; // Initialize reference
        bottletickMethod(); // Call collision checking method for bottle
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

    // Used to continuously check if player has collided with ammo sprite
    public void ammotickMethod() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                ammoCollide();
                cancel();
            }
        }, 1, SPAWNER_SCAN_TICK, 1);
    }

    // Ammo collision
    public void ammoCollide() {
        if (((1.0f * AMMO_START_POS.x > playerBody.getPosition().x-PLAYER_OFFSET_X -2.25) && (1.0f * AMMO_START_POS.x < playerBody.getPosition().x-PLAYER_OFFSET_X +2.25)) &&
                (1.0f * AMMO_START_POS.y) < (playerBody.getPosition().y-PLAYER_OFFSET_Y) && ((1.0f * AMMO_START_POS.y > playerBody.getPosition().y-PLAYER_OFFSET_Y -1.25))) {
            ammoref.destroyRoutine();
            hud.addAmmo(30);
            hud.addScore(15);
            ammoDestroyed = true;
        }
       else if(ammoDestroyed == false) {
        ammotickMethod();
    }
    }

    // Used to continuously check if player has collided with jacket sprite
    public void jackettickMethod() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                jacketCollide();
                cancel();
            }
        }, 1, SPAWNER_SCAN_TICK, 1);
    }

    // Jacket collision
    public void jacketCollide() {
       if (((1.0f * ARMOUR_START_POS.x > playerBody.getPosition().x-PLAYER_OFFSET_X -1.20) && (1.0f * ARMOUR_START_POS.x < playerBody.getPosition().x-PLAYER_OFFSET_X +1.20)) &&
               (1.0f * ARMOUR_START_POS.y) < (playerBody.getPosition().y-PLAYER_OFFSET_Y +1.95) && ((1.0f * ARMOUR_START_POS.y > playerBody.getPosition().y-PLAYER_OFFSET_Y -1.5))){
            jacketref.destroyRoutine();
            hud.addArmour(100);
            hud.addScore(10);
            jacketDestroyed = true;
          }
       else if(jacketDestroyed == false) {
           jackettickMethod();
       }
    }

    // Used to continuously check if player has collided with bottle sprite
    public void bottletickMethod() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                bottleCollide();
                cancel();
            }
        }, 1, SPAWNER_SCAN_TICK, 1);
    }

    // Bottle collision
    public void bottleCollide() {
        if (((1.0f * HEALTH_START_POS.x > playerBody.getPosition().x-PLAYER_OFFSET_X -1.20) && (1.0f * HEALTH_START_POS.x < playerBody.getPosition().x-PLAYER_OFFSET_X +1.20)) &&
                    (1.0f * HEALTH_START_POS.y) < (playerBody.getPosition().y-PLAYER_OFFSET_Y) && ((1.0f * HEALTH_START_POS.y > playerBody.getPosition().y-PLAYER_OFFSET_Y -1.0))){
                bottleref.destroyRoutine();
                hud.addHealth(50);
                hud.addScore(5);
                bottleDestroyed = true;
            }
        else if(bottleDestroyed == false) {
            bottletickMethod();
        }
    }

    @Override
    public void reaction() {
    }
}
