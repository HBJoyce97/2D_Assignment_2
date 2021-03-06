package com.allsopg.game.screens;

import com.allsopg.game.TBWGame;
import com.allsopg.game.bodies.AmmoAnimationMulti;
import com.allsopg.game.bodies.BottleAnimationMulti;
import com.allsopg.game.bodies.JacketAnimationMulti;
import com.allsopg.game.bodies.PlayerCharacter;
import com.allsopg.game.physics.WorldManager;
import com.allsopg.game.sound.SoundLink;
import com.allsopg.game.utility.CameraManager;
import com.allsopg.game.utility.Constants;
import com.allsopg.game.utility.HUD;
import com.allsopg.game.utility.UniversalResource;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Set;

import static com.allsopg.game.utility.Constants.AMMO_START_POS;
import static com.allsopg.game.utility.Constants.ARMOUR_START_POS;
import static com.allsopg.game.utility.Constants.HEALTH_START_POS;
import static com.allsopg.game.utility.Constants.MEDIUM;
import static com.allsopg.game.utility.Constants.PLAYER_ATLAS_PATH;
import static com.allsopg.game.utility.Constants.PLAYER_SECOND_ATLAS_PATH;
import static com.allsopg.game.utility.Constants.SMALL;
import static com.allsopg.game.utility.Constants.START_POSITION;
import static com.allsopg.game.utility.Constants.TINY;
import static com.allsopg.game.utility.Constants.UNITSCALE;
import static com.allsopg.game.utility.Constants.VIRTUAL_HEIGHT;
import static com.allsopg.game.utility.Constants.VIRTUAL_WIDTH;
import static com.badlogic.gdx.Input.Keys.R;

/**
 * Created by gerard on 12/02/2017.
 */

public class GameScreen extends ScreenAdapter {
    private TBWGame game;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private PlayerCharacter smif;
    private HUD gameHUD;
    private CameraManager cameraManager;
    private float frameDelta = 0;
    private AmmoAnimationMulti ammoanim; // Variable used to set up the animation change for ammo
    private JacketAnimationMulti jacketanim; // Variable used to set up the animation change for jacket
    private BottleAnimationMulti bottleanim; // Variable used to set up the animtion change for bottle
    private float animationTime; // Variable used to set up the frame timing
    Music GameSnd = Gdx.audio.newMusic(Gdx.files.internal("sfx/Game.ogg")); // Intialize the following music

    public GameScreen(TBWGame tbwGame){

        this.game = tbwGame;
        playMusic(); // Calls the following method to play music
    }

    @Override
    public void resize(int width, int height) {
        game.camera.setToOrtho(false, VIRTUAL_WIDTH * UNITSCALE / 2, VIRTUAL_HEIGHT * UNITSCALE / 2);
        game.batch.setProjectionMatrix(game.camera.combined);
    }

    @Override
    public void show() {
        super.show();
        tiledMap = game.getAssetManager().get(Constants.BACKGROUND); // Retrieves the Blade Runner tilemap
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(this.tiledMap,UNITSCALE);
        orthogonalTiledMapRenderer.setView(game.camera);
        if(!WorldManager.isInitialised()){WorldManager.initialise(game,tiledMap);}
        ammoanim = new AmmoAnimationMulti("gfx/ammo_collision/idle/idle_assets.atlas", "gfx/ammo_collision/animation/anim_assets.atlas", MEDIUM, AMMO_START_POS, Animation.PlayMode.LOOP); // Initializes the animation change. Passes in both animation atlases, the texture to size it, sets the vector position and the animation playmode
        jacketanim = new JacketAnimationMulti("gfx/armour_collision/idle/jacket_assets.atlas", "gfx/armour_collision/animation/collision_assets.atlas", MEDIUM, ARMOUR_START_POS, Animation.PlayMode.LOOP); // Initializes the animation change. Passes in both animation atlases, the texture to size it, sets the vector position and the animation playmode
        bottleanim = new BottleAnimationMulti("gfx/bottle_collision/idle/bottle_assets.atlas", "gfx/bottle_collision/animation/smash_assets.atlas", TINY, HEALTH_START_POS, Animation.PlayMode.LOOP); // Initializes the animation change. Passes in both animation atlases, the texture to size it, sets the vector position and the animation playmode
        smif = new PlayerCharacter(PLAYER_ATLAS_PATH,SMALL,START_POSITION, ammoanim, jacketanim, bottleanim); // Sets up player (includes parameters for sprite collisions)
        cameraManager = new CameraManager(game.camera,tiledMap);
        cameraManager.setTarget(smif);
        gameHUD = new HUD(game.batch,smif,game);
    }

    @Override
    public void render(float delta) {
        frameDelta += delta;
        animationTime +=Gdx.graphics.getDeltaTime();
        UniversalResource.getInstance().tweenManager.update(animationTime);
        smif.update(frameDelta);
        gameHUD.update(delta);
        game.batch.setProjectionMatrix(game.camera.combined);
        clearScreen();
        ammoanim.update(animationTime); // Updates the animation every frame
        jacketanim.update(animationTime); // Updates the animation every frame
        bottleanim.update(animationTime); // Updates the animation every frame
        draw();
        WorldManager.getInstance().doPhysicsStep(delta);
    }

    private void draw() {
       orthogonalTiledMapRenderer.setView(game.camera);
       orthogonalTiledMapRenderer.render();
        cameraManager.update();
        game.batch.begin();
        smif.draw(game.batch);
        ammoanim.draw(game.batch); // Draws the animation to the screen
        jacketanim.draw(game.batch); // Draws the animation to the screen
        bottleanim.draw(game.batch); // Draws the animation to the screen
        game.batch.end();
        gameHUD.stage.draw();
//      WorldManager.getInstance().debugRender();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g,
                Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    // Used to play looped music at a set volume
    private void playMusic(){
        GameSnd.play();
        GameSnd.setVolume(0.5f);
        GameSnd.setLooping(true);
    }
}