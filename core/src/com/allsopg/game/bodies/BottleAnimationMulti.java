package com.allsopg.game.bodies;


import com.allsopg.game.sound.SoundLink;
import com.allsopg.game.utility.Constants;
import com.allsopg.game.utility.TweenData;
import com.allsopg.game.utility.TweenDataAccessor;
import com.allsopg.game.utility.UniversalResource;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by Harry on 13/05/2018.
 */

public class BottleAnimationMulti extends Sprite {
    public Animation idle, smash, animation; // Variables used to set the current animation
    private TextureAtlas atlas_1, atlas_2; // Variables used to set up the two animation atlases

    private TweenData tweenData; // Used to tween the animation
    private TweenManager tweenManager; // Used to tween the animation

    private SoundLink soundLink; // Variable used to set up the soundLink which will enable sounds from the IntMap in the 'SoundLink' class to be played

    // Sets up the two atlases and their regions used for the animation change. atlas_1 will be set to the 'idle' animation; atlas_2 will be set to the 'anim' animation
    public BottleAnimationMulti(String atlasString_1, String atlasString_2, Texture t, Vector2 pos, Animation.PlayMode loopType){
        super(t, 2, 2);
        atlas_1 = new TextureAtlas(Gdx.files.internal(atlasString_1));
        Array<TextureAtlas.AtlasRegion> regions_1 = new
                Array<TextureAtlas.AtlasRegion>(atlas_1.getRegions());
        regions_1.sort(new BottleAnimationMulti.RegionComparator());
        idle = new Animation(Constants.BOTTLE_FRAME_DURATION, regions_1, loopType);

        atlas_2 = new TextureAtlas(Gdx.files.internal(atlasString_2));
        Array<TextureAtlas.AtlasRegion> regions_2 = new
                Array<TextureAtlas.AtlasRegion>(atlas_2.getRegions());
        regions_2.sort(new BottleAnimationMulti.RegionComparator());
        smash = new Animation(Constants.BOTTLE_FRAME_DURATION, regions_2, loopType);

        animation = idle; // Animation is set to 'idle' by default, so the crate remains closed at the beginning of the animation

        this.setPosition(pos.x, pos.y);
        initTweenData(); // Initializes the tween
        soundLink = new SoundLink(); // Intializes the SoundLink
    }

    public void update(float deltaTime){
        this.setRegion((TextureAtlas.AtlasRegion) animation.getKeyFrame(deltaTime)); // Updates the region every frame

        // Updates the following tween data to be used in the tween
        this.setX(tweenData.getXY().x);
        this.setY(tweenData.getXY().y);
        this.setColor(tweenData.getColour());
        this.setScale(tweenData.getScale());
        this.setRotation(tweenData.getRotation());
    }

    private static class RegionComparator implements Comparator<TextureAtlas.AtlasRegion> {

        @Override
        public int compare(TextureAtlas.AtlasRegion region1, TextureAtlas.AtlasRegion region2) {
            return region1.name.compareTo(region2.name); // Compares the regions of the atlases
        }
    }

    // Initializes the tween data and the tweenManager
    private void initTweenData(){
        tweenData = new TweenData();
        tweenData.setXY(this.getX(),this.getY());
        tweenData.setColour(this.getColor());
        tweenData.setScale(this.getScaleX());
        tweenManager = UniversalResource.getInstance().tweenManager; //tweenManager;
    }

    // The following code is the main animation for the sprite on screen
    public void destroyRoutine(){
        Tween.to(tweenData, TweenDataAccessor.TYPE_POS,0f) // Sets the start position of the sprite (off screen in the center)
                .target(11, 10)
                .setCallback(new TweenCallback() { // Initializes the callback
                    @Override
                    public void onEvent(int type, BaseTween<?> source) { // The following event causes the animation change
                        soundLink.play(4); // Play the third sound in the IntMap (Bottle)
                        animation = smash; // Change the animation from 'idle' to 'smash'
                    }
                }).start(tweenManager)
                .to(tweenData, TweenDataAccessor.TYPE_COLOUR,150f) // Delays the colour from changing until the animation has finished. Once finished, the sprite turns black, disappearing from the screen
                .delay(3600f)
                .target(1f,1f,1f,.0f).start(tweenManager);
    }

    private TweenData getTweenData(){return tweenData;} // Returns tweenData
}
