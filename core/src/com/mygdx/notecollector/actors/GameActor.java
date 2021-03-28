package com.mygdx.notecollector.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.mygdx.notecollector.Utils.Assets;

/**
 * Created by bill on 6/18/16.
 */
public abstract class GameActor extends Actor {

    protected Body body;
    protected Array<Rectangle> bubbledrops;
    protected Array<Rectangle> pianokeys;
    protected Assets AssetsManager;

    public float noteposition[][]=
            {
                    {0,0,0,0,0,0,0,0,0,36f,47f,50f},
                    {64f,75f,78f,89f,92f,106f,117f,120f,131f,134f,145f,148},
                    {162f,173f,176f,187f,190f,204f,215f,218f,229f,232f,243f,246f},
                    {260f,271f,274f,285f,288f,302f,313f,316f,327f,330,341f,344f},
                    {358f,369f,372f,383f,386f,400f,411f,414,425f,428f,439f,442f},
                    {456f,467f,470f,481f,484f,498f,509f,512f,523f,526f,537f,540f},
                    {554f,565f,568f,579f,582f,596f,607f,610f,621f,624f,635f,638f},
                    {652f,663f,666f,677f,680f,694f,705f,708f,719f,722f,733f,736f},
                    {750f,0,0,0,0,0,0,0,0,0f,0f,0f},
            };

    public GameActor(Body body,Assets AssetsManager) {
        this.body = body;
        bubbledrops =new Array<Rectangle>();
        pianokeys = new Array<Rectangle>();
        this.AssetsManager = AssetsManager;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }


}
