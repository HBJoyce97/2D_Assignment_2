package com.allsopg.game.utility;

import com.allsopg.game.TBWGame;
import com.allsopg.game.bodies.PlayerCharacter;
import com.allsopg.game.screens.EndScreen;
import com.allsopg.game.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HUD implements Disposable {

    public Stage stage;
    private Viewport viewport;
    //structural elements for UI
    Table tableData;
    Table tableControls;
    // Navigation widgets
    private Button leftBtn,rightBtn,upBtn,downBtn;
    private PlayerCharacter playerCharacter;
    private TBWGame game;

    //score && time tracking variables
    private Integer worldTimer;
    private float timeCount;
    private static Integer score;
    private boolean timeUp;
    private static int health, armour, ammo;

    //Scene2D Widgets
    private Label countdownLabel, timeLabel, healthLabel, armourLabel, ammoLabel, scoreLabel;
    private static Label healthnumLabel, armournumLabel, ammonumLabel, scorenumlabel;

    public HUD(SpriteBatch sb, PlayerCharacter playerCharacter, TBWGame tbwGame) {
        this.playerCharacter = playerCharacter;
        this.game = tbwGame;
        //define tracking variables
        worldTimer = Constants.LEVEL_TIME;
        timeCount = 0;
        score = 0;
        health = 100; // Variable for health value
        armour = 0; // Variable for armour value
        ammo = 0; // Variable for ammo value
        //new camera used to setup the HUD viewport seperate from the main Game Camera
        //define stage using that viewport and games spritebatch
        viewport = new FitViewport(Constants.VIRTUAL_WIDTH,
                Constants.VIRTUAL_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        tableData = new Table();
        tableData.bottom();
        tableData.setFillParent(true);
        tableControls = new Table();
        tableControls.top();
        tableControls.setFillParent(true);

        createScoreAndTimer();
        createNavButtons();
        stage.addActor(tableData);
        stage.addActor(tableControls);
        Gdx.input.setInputProcessor(stage);

    }

    private void createScoreAndTimer(){
        countdownLabel = new Label(String.format("%03d", worldTimer),
                new Label.LabelStyle(new BitmapFont(), Color.RED));
        healthnumLabel = new Label(String.format("%03d", health),
                new Label.LabelStyle(new BitmapFont(), Color.GREEN));
        armournumLabel =  new Label(String.format("%03d", armour),
                new Label.LabelStyle(new BitmapFont(), Color.GOLD));
        ammonumLabel =  new Label(String.format("%03d", ammo),
                new Label.LabelStyle(new BitmapFont(), Color.ORANGE));
        scorenumlabel = new Label(String.format("%03d", score),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME REMAINING:",
                new Label.LabelStyle(new BitmapFont(), Color.RED));
        healthLabel = new Label("HEALTH:",
                new Label.LabelStyle(new BitmapFont(), Color.GREEN));
        armourLabel = new Label("ARMOUR:",
                new Label.LabelStyle(new BitmapFont(), Color.GOLD));
        ammoLabel = new Label("AMMO:",
                new Label.LabelStyle(new BitmapFont(), Color.ORANGE));
        scoreLabel = new Label("SCORE:",
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //labels added to table using padding and expandX
        tableData.add(healthLabel).padBottom(5).padLeft(10);
        tableData.add(healthnumLabel).expandX().padBottom(5).padLeft(-280);
        tableData.add(armourLabel).padBottom(5).padLeft(-300);
        tableData.add(armournumLabel).expandX().padBottom(5).padLeft(-440);
        tableData.add(ammoLabel).padBottom(5).padLeft(-500);
        tableData.add(ammonumLabel).expandX().padBottom(5).padLeft(-680);
        tableData.add(scoreLabel).padBottom(5).padLeft(-750);
        tableData.add(scorenumlabel).expandX().padBottom(5).padLeft(-900);
        tableData.add(timeLabel).padBottom(5).padRight(-240);
        tableData.add(countdownLabel).expandX().padBottom(5).padRight(-180);
        healthLabel.setFontScale(2);
        healthnumLabel.setFontScale(2);
        armourLabel.setFontScale(2);
        armournumLabel.setFontScale(2);
        ammoLabel.setFontScale(2);
        ammonumLabel.setFontScale(2);
        countdownLabel.setFontScale(2);
        timeLabel.setFontScale(2);
        scoreLabel.setFontScale(2);
        scorenumlabel.setFontScale(2);

    }

    private void createNavButtons(){
        Texture actorUpBtn =
                new Texture(Gdx.files.internal("buttons/up.png"));
//        Texture actorDownBtn =
//                new Texture(Gdx.files.internal("buttons/down.png"));
        Texture actorLeftBtn =
                new Texture(Gdx.files.internal("buttons/left.png"));
        Texture actorRightBtn =
                new Texture(Gdx.files.internal("buttons/right.png"));

        Button.ButtonStyle buttonStyleUp = new Button.ButtonStyle();
        buttonStyleUp.up =
                new TextureRegionDrawable(new TextureRegion(actorUpBtn));
        upBtn = new Button( buttonStyleUp );

//        Button.ButtonStyle buttonStyleDown = new Button.ButtonStyle();
//        buttonStyleDown.up =
//                new TextureRegionDrawable(new TextureRegion(actorDownBtn));
//        downBtn = new Button( buttonStyleDown );

        Button.ButtonStyle buttonStyleLeft = new Button.ButtonStyle();
        buttonStyleLeft.up =
                new TextureRegionDrawable(new TextureRegion(actorLeftBtn));
        leftBtn = new Button( buttonStyleLeft );

        Button.ButtonStyle buttonStyleRight = new Button.ButtonStyle();
        buttonStyleRight.up =
                new TextureRegionDrawable(new TextureRegion(actorRightBtn));
        rightBtn = new Button( buttonStyleRight );

        //add buttons
        tableControls.add(upBtn).padLeft(50).padTop(80);
        tableControls.add(downBtn).expandX();
        tableControls.add(leftBtn).expandX().padLeft(150);
        tableControls.add(rightBtn).expandX().padRight(25).padTop(10);
        //add listeners to the buttons
        addButtonListeners();
    }

    private void addButtonListeners(){
        //up
        upBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playerCharacter.move(CurrentDirection.UP);
            }
        });
//        //down
//        downBtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//
//            }
//        });
        //left
        leftBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playerCharacter.move(CurrentDirection.LEFT);
            }
        });
        //right
        rightBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playerCharacter.move(CurrentDirection.RIGHT);
            }
        });
    }

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
                GameData.getInstance().setScore(score);
                GameData.getInstance().setTime(worldTimer);
                game.setScreen(new EndScreen());
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    // Used to add health to the player. Called in PlayerCharacter
    public static void addHealth(int value) {
        health -= value;
        healthnumLabel.setText(String.format("%03d", health));
    }

    // Used to add armour to the player. Called in PlayerCharacter
    public static void addArmour(int value) {
        armour += value;
        armournumLabel.setText(String.format("%03d", armour));
    }

    // Used to add ammo to the player. Called in PlayerCharacter
    public static void addAmmo(int value) {
        ammo += value;
        ammonumLabel.setText(String.format("%03d", ammo));
    }

    // Used to increase the score. Called in PlayerCharacter
    public static void addScore(int value) {
        score = score + value;
        scorenumlabel.setText(String.format("%03d", score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isTimeUp() {
        return timeUp;
    }


    public static Label getScoreLabel() {
        return healthnumLabel;
    }

    public static Integer getScore() {
        return score;
    }

}