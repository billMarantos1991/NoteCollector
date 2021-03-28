package com.mygdx.notecollector.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.notecollector.GameStage.GameStage;
import com.mygdx.notecollector.NoteCollector;
import com.mygdx.notecollector.midilib.MidiNote;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bill on 6/18/16.
 */
public class GameScreen implements  Screen {

    public GameStage stage;
    private NoteCollector game;
    private String filepath;
    private int speed;
    private long delay;
    private boolean paused;
    private long pausetime;

    public GameScreen(NoteCollector game, float TickPerMsec, ArrayList<MidiNote> notes, String filepath,int speed,long delay) throws IOException, InterruptedException {

        stage = new GameStage(game,TickPerMsec,notes,speed,delay);
        this.game = game;
        this.filepath = filepath;
        this.delay = delay;
        this.speed = speed;
        paused =false;

        stage.setGameState("running");
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Clear the screen

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Update the stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if (stage.getGameState() == "finished")
            game.setScreen(new EndGameScreen(game,stage.getScore()));

        if (stage.getGameState() == "paused")
            pause();

        if (stage.getGameState() == "resume")
            stage.resumeGame();

        if (isGameOver()){
            game.setScreen(new GameOverScreen(game,stage.getScore(),filepath,speed,delay));
            dispose();
        }
    }
    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width,height,true);
    }

    @Override
    public void resume() {
        stage.setGameState("paused");
        stage.pausetime = pausetime;
    }

    @Override
    public void hide() {


    }

    @Override
    public void dispose() {
        stage.dispose();

    }

    @Override
    public void pause() {
        pausetime  =  System.currentTimeMillis();
        stage.pauseGame(pausetime);

    }

    private boolean isGameOver(){
        if (stage.getRedcounts() == 5){

            return true;
        }
        return  false;
    }



}