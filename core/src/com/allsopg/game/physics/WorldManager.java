package com.allsopg.game.physics;

import com.allsopg.game.TBWGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import static com.allsopg.game.utility.Constants.PHYSICS_MATERIALS_PATH;
import static com.allsopg.game.utility.Constants.POSITION_ITERATIONS;
import static com.allsopg.game.utility.Constants.TILE_SIZE;
import static com.allsopg.game.utility.Constants.TIME_STEP;
import static com.allsopg.game.utility.Constants.VELOCITY_ITERATIONS;

/**
 * Created by gerard on 01/03/2017.
 * Updates 24/02/18
 */

public class WorldManager {
    private final Vector2 gravity;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private static TBWGame game;
    private MapBodyManager mapBodyManager;
    private static Map map;
    private static boolean initialised=false;

    private static WorldManager WORLDMANAGER;

    public static WorldManager getInstance(){
        if(WORLDMANAGER != null)return WORLDMANAGER;
        else if(WORLDMANAGER == null && initialised){
            WORLDMANAGER = new WorldManager();
            return WORLDMANAGER;
        }
        return null;
    }

    private WorldManager(){
        gravity = new Vector2(0,-9.8f);
        Box2D.init();
        world = new World(gravity, true);
        mapBodyManager = new MapBodyManager(world, TILE_SIZE, Gdx.files.internal(PHYSICS_MATERIALS_PATH));
        mapBodyManager.createPhysics(map,"Collisions"); // Creates the physics around the obstacles in the tilemap
        debugRenderer = new Box2DDebugRenderer();
    }

    public static void initialise(TBWGame aGame, Map aMap){
        game = aGame;
        map =  aMap;
        initialised = true;
    }

    public static boolean isInitialised(){
        return initialised;
    }

    public World getWorld(){return world;}

    public void doPhysicsStep(float deltaTime) {
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }

    public void debugRender(){
        game.batch.begin();
            debugRenderer.render(world, game.camera.combined);
        game.batch.end();
    }

}
