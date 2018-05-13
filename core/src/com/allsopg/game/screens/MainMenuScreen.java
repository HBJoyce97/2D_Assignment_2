package com.allsopg.game.screens;

import com.allsopg.game.TBWGame;
import com.allsopg.game.utility.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.graphics.Texture;

import java.awt.Menu;

import static com.allsopg.game.utility.Constants.BACKGROUND;
import static com.allsopg.game.utility.Constants.VIRTUAL_HEIGHT;
import static com.allsopg.game.utility.Constants.VIRTUAL_WIDTH;


/**
 * Created by gerard on 16/02/2018.
 */

public class MainMenuScreen extends ScreenAdapter {
        private TBWGame game;
        private Texture bladerunnertitle; // Variable used to display texture on screen
        private Texture bladerunnerstart; // Variable used to display texture on screen
        Music MenuSnd = Gdx.audio.newMusic(Gdx.files.internal("sfx/Main_Menu.ogg")); // Initialize the following music

        public MainMenuScreen(TBWGame aGame) {

            this.game = aGame;
            bladerunnertitle = new Texture(Gdx.files.internal("gfx/BladeRunner_Title.png")); // Displays title on main menu screen
            bladerunnerstart = new Texture(Gdx.files.internal("gfx/BladeRunner_StartGame.png")); // Displays start button on main menu screen
            playMusic(); // Calls the following method to play music
        }

    @Override
    public void show() {
        game.getAssetManager().load(BACKGROUND, TiledMap.class);
        game.getAssetManager().finishLoading();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.camera.update();
        game.batch.begin();
        game.batch.draw(bladerunnertitle, 0, 50, VIRTUAL_WIDTH, VIRTUAL_HEIGHT); // Draws the texture to the screen
        game.batch.draw(bladerunnerstart, 0, -450, VIRTUAL_WIDTH, VIRTUAL_HEIGHT); // Draws the texture to the screen
        game.batch.end();


        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            stopMusic(); // Calls the following method to stop music before switching screens
        }
    }

    // Used to play looped music
    private void playMusic(){
        MenuSnd.play();
        MenuSnd.setLooping(true);
    }

    // Used to stop music from playing
    private void stopMusic(){
        MenuSnd.stop();
    }


    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}
