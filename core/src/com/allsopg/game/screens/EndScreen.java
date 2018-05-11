package com.allsopg.game.screens;

import com.allsopg.game.utility.Constants;
import com.allsopg.game.utility.GameData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by gerard on 23/04/2017.
 */

public class EndScreen extends ScreenAdapter {
    private Stage stage;
    Table tableData;
    //Scene2D Widgets
    private Label countdownLabel, headerLabel, linkLabel;
    private static Label scoreLabel;

    public EndScreen(){
        stage = new Stage(new FitViewport(Constants.VIRTUAL_WIDTH/3, Constants.VIRTUAL_HEIGHT/3));
        Gdx.input.setInputProcessor(stage);
        tableData = new Table();
        tableData.setFillParent(true);
        createScoreAndTimer();
        stage.addActor(tableData);
    }

    public void show() {

    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render(float delta) {
        clearScreen();
        stage.draw();
    }

    private void createScoreAndTimer(){
        //define labels using the String, and a Label style consisting of a font and color
        headerLabel = new Label("LEVEL ONE SCORE", new Label.LabelStyle(new BitmapFont(), Color.LIME));
        scoreLabel = new Label(String.format("%03d", GameData.getInstance().getScore()), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        linkLabel = new Label("POINTS", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        //add labels to table
        tableData.add(headerLabel).padLeft(150);
        tableData.row();
        tableData.add(linkLabel).padLeft(60);
        tableData.add(scoreLabel).expandX().padRight(160);

    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g,
                Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
