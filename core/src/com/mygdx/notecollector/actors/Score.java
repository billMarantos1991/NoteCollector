package com.mygdx.notecollector.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.notecollector.Utils.Assets;

/**
 * Created by bill on 6/28/16.
 */
public class Score extends Actor {

    private float score;

    private Rectangle bounds;
    private BitmapFont font;
    public Score(Rectangle bounds, Assets AssetsManager) {
      setHeight(bounds.height);
      setWidth(bounds.width);
      this.bounds = bounds;
      score = 0;
        font = AssetsManager.createBimapFont(28);
        font.setColor(Color.BLACK);

    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (score == 0)
            return;
        font.draw(batch, String.format("%d", getScore()), bounds.x,bounds.y);

    }
    public int getScore() {
        return (int) Math.floor(score);
    }

    public void setScore(int score) {
            this.score = score ;
    }




}
