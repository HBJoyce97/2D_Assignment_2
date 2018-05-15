package com.allsopg.game.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

import static com.allsopg.game.utility.Constants.FRAME_DURATION;
import static com.allsopg.game.utility.Constants.PLAYER_HEIGHT;
import static com.allsopg.game.utility.Constants.PLAYER_WIDTH;

/**
 * Created by gerard on 09/11/2016.
 * updated 02/03/18
 */

public abstract class AnimatedSprite extends Sprite {
    protected Animation animation;
    protected Animation.PlayMode playmode;
    private TextureAtlas atlas;

    public AnimatedSprite(String atlasString, Texture t, Vector2 pos){
        super(t,PLAYER_WIDTH,PLAYER_HEIGHT);
        this.setX(pos.x);
        this.setY(pos.y);
        playmode = Animation.PlayMode.NORMAL;
        initAtlas(atlasString);
    }

    public void update(float animationTime){
        this.setRegion((TextureRegion) animation.getKeyFrame(animationTime));
    }

    public void initAtlas(String atlasString){
        atlas = new TextureAtlas(Gdx.files.internal(atlasString));
        //load animations
        Array<TextureAtlas.AtlasRegion> regions = new
                Array<TextureAtlas.AtlasRegion>(atlas.getRegions());
        regions.sort(new RegionComparator());
        animation = new Animation(FRAME_DURATION,regions, Animation.PlayMode.NORMAL);
    }

    private static class RegionComparator implements Comparator<TextureAtlas.AtlasRegion> {
        @Override
        public int compare(TextureAtlas.AtlasRegion region1, TextureAtlas.AtlasRegion region2) {
            return region1.name.compareTo(region2.name);
        }
    }

}