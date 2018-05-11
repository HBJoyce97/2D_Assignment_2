package com.allsopg.game.utility;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static com.allsopg.game.utility.Constants.UNITSCALE;
import static com.allsopg.game.utility.Constants.VIRTUAL_WIDTH;

/**
 * Created by gerard on 14/03/2017.
 * updated 12/03/18
 */

public class CameraManager {
    private Vector2 position;
    private Sprite target;
    private OrthographicCamera camera;
    private TiledMap tiledMap;
    float levelWidth;

    public CameraManager(OrthographicCamera camera, TiledMap tiledMap) {
        this.camera = camera;
        this.tiledMap = tiledMap;
        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer)
                this.tiledMap.getLayers().get(0);
        levelWidth = tiledMapTileLayer.getWidth();
        position = new Vector2();

    }
    public void update () {
        if (!hasTarget()) return;
        if(cameraTrackX()) {
            position.x = target.getX() + target.getOriginX();
            camera.position.set(position.x, camera.position.y, 0);
            camera.update();
        }
    }

    public void setTarget (Sprite target) { this.target = target; }
    public boolean hasTarget () { return target != null; }

    private boolean cameraTrackX() {
        if ((target.getX() > (VIRTUAL_WIDTH * UNITSCALE) / 2f) &&
                (target.getX() < (levelWidth - (VIRTUAL_WIDTH * UNITSCALE )/2))) {
            return true;
        }
        return false;
    }
}
