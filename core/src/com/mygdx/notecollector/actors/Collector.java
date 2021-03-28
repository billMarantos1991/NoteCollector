package com.mygdx.notecollector.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.notecollector.Utils.Assets;
import com.mygdx.notecollector.Utils.Constants;

/**
 * Created by bill on 6/21/16.
 */
public class Collector extends GameActor {


    private Texture img;
    private Sprite sprite;
    private float Xaxis;
    private float Yaxis;
    private boolean paused;
    private Preferences prefs;


    public Collector(Body body, Assets AssetsManager) {
        super(body,AssetsManager);
        prefs = Gdx.app.getPreferences("NoteCollectorPreferences");


        if(prefs.getBoolean("normal")) {
            img = AssetsManager.assetManager.get(Constants.Collector);
        }else if (prefs.getBoolean("big")){
            img = AssetsManager.assetManager.get(Constants.BigCollector);
        }else if (prefs.getBoolean("vbig")){
            img = AssetsManager.assetManager.get(Constants.VeryBigCollector);
        }
        sprite = new Sprite(img);
        this.Xaxis = Constants.CollectorStartX;
        this.Yaxis = Constants.CollectorStartY;
        sprite.setColor(Color.GRAY);
       paused=false;

    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(Xaxis,Yaxis);
        sprite.draw(batch);
    }

    public void changeposition(float Xaxis, float Yaxis){
        if(paused==false) {
            this.Xaxis = Xaxis;
            this.Yaxis = Yaxis;
        }

    }

}
