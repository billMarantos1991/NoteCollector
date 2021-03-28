package com.mygdx.notecollector.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.notecollector.Utils.Assets;
import com.mygdx.notecollector.Utils.Constants;
import com.mygdx.notecollector.midilib.MidiNote;

import java.util.ArrayList;

/**
 * Created by bill on 5/31/16.
 */
public class Piano extends  GameActor {


    private Sprite spritewhite, spriteblack;
    private SquareNotes squareNotes;
    private Texture textureWhite, textureBlack, textureWhitepressed, textureBlackpressed;
    private boolean pressed;
    private int octave, notescale,octave2, notescale2;
    private float[] MultipleNotes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private ArrayList<MidiNote> notes;
    private Batch batch;
    private int i = 0, j = 0;
    private Sprite spriteblackPressed,spritewhitePressed;

    private int lastIndex,notesize;
    private int startTime,endTime,nextNote;
    private int numberSameNotes;
    private int start,end;
    private int left,right;
    private boolean paused;

    public Piano(Body body, Assets AssetsManager) {
        super(body,AssetsManager);
        textureWhite = AssetsManager.assetManager.get(Constants.WhiteKey);
        textureBlack = AssetsManager.assetManager.get(Constants.BlackKey);
        textureWhitepressed = AssetsManager.assetManager.get(Constants.WhitePressedKey);
        textureBlackpressed = AssetsManager.assetManager.get(Constants.BlackPressedKey);
        pressed = false;
        spriteblack = new Sprite(textureBlack);
        spriteblackPressed = new Sprite(textureBlackpressed);
        spritewhitePressed = new Sprite(textureWhitepressed);
        spritewhite = new Sprite(textureWhite);
        paused =false;

    }



    @Override
    public void draw(Batch batch, float parentAlpha) {



        i=21;
        j=0;
        this.batch = batch;
        while (i< 109) {
            octave = (i/ 12) -1;
            notescale = i% 12;

            if(!checkBlackPosition(i))
                createwhitekeys();
            i++;
        }
        i=21;

        while (i< 109) {
            octave = (i/ 12) -1;
            notescale = i% 12;

            if(checkBlackPosition(i))
                createblackkeys();
            i++;
        }

    }

    public void setSquareNotes(SquareNotes squareNotes) {
        this.squareNotes = squareNotes;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    private void createblackkeys(){

        if (noteposition[octave][notescale] == MultipleNotes[j] && pressed == true) {

            spriteblackPressed.setPosition(MultipleNotes[j], 20f);
            spriteblackPressed.draw(batch);
                squareNotes.StartNote(noteposition[octave][notescale]);

            j++;
        } else {

            spriteblack.setPosition(noteposition[octave][notescale], 20f);
            spriteblack.draw(batch);


        }

    }
    private void createwhitekeys(){

        if (noteposition[octave][notescale] == MultipleNotes[j] && pressed == true) {

            spritewhitePressed.setPosition( MultipleNotes[j], 5f);
            spritewhitePressed.draw(batch);
            squareNotes.StartNote(noteposition[octave][notescale]);

            j++;
        } else {
            spritewhite.setPosition(noteposition[octave][notescale], 5f);
            spritewhite.draw(batch);
        }

    }

    private boolean checkBlackPosition(int i){

        if(notescale == 1 || notescale == 3 || notescale == 6 || notescale == 8 || notescale == 10  )
            return true;
        return false;

    }
    public void setNotes(ArrayList<MidiNote> notes) {
        this.notes = notes;
    }

    @Override
    public void act(float delta) {

        super.act(delta);

    }

    public void DrawNotes(int currentTcik, int prevTick){
        notesize = notes.size();
        lastIndex = findClosestStartTime(prevTick);
        for (int i =lastIndex;i<notesize;i++){
             startTime = (int) notes.get(i).getStarttime();
             endTime = (int) notes.get(i).getEndTime();
             nextNote = NextStartTime(i);
            sameTicks(startTime,lastIndex);

            if (startTime > prevTick && startTime > currentTcik) {
                break;
            }

            if ((startTime <=currentTcik) && (startTime <nextNote)
                    && (currentTcik <endTime)
                    && (startTime <= prevTick) &&(prevTick < nextNote)
                    && (prevTick < endTime) ) {
                break;
            }
            if((startTime <=currentTcik) && (startTime < endTime)){
                pressed =true;

            }else if((startTime <= prevTick) && (prevTick <endTime)){
                pressed = false;

            }

        }


    }

    private void sameTicks(int tick,int index){
        numberSameNotes=0;

        for (int i=index;i<notesize;i++){
            if(notes.get(i).getStarttime()==tick){
                octave2 = (notes.get(i).getNotenumber() / 12) -1;
                notescale2 = notes.get(i).getNotenumber() % 12;
                MultipleNotes[numberSameNotes] = noteposition[octave2][notescale2];
                numberSameNotes++;
            }

        }

    }


    private int NextStartTime(int i) {
        start = (int) notes.get(i).getStarttime();
         end = (int) notes.get(i).getEndTime();

        while (i < notesize) {
            if (notes.get(i).getStarttime() > start) {
                return (int) notes.get(i).getStarttime();
            }
            end = (int) Math.max(end, notes.get(i).getEndTime());
            i++;
        }
        return end;
    }
    private int findClosestStartTime(long tick){
         left =0;
         right = notesize -1;

        while(right -left> 1) {
            int i = (right + left) / 2;
            if (notes.get(i).getStarttime() == tick)
                break;
            else if (notes.get(i).getStarttime() <= tick)
                left = i;
            else
                right = i;
        }
        while(left >=1 && notes.get(left-1).getStarttime() == notes.get(left).getStarttime()) {
            left--;
        }
        return left;
    }


}
