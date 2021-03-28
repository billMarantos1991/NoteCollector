package com.mygdx.notecollector.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.notecollector.NoteCollector;
import com.mygdx.notecollector.Utils.Assets;
import com.mygdx.notecollector.Utils.Constants;
import com.mygdx.notecollector.midilib.MidiManipulator;
import com.mygdx.notecollector.midilib.MidiNote;
import com.mygdx.notecollector.screens.GameScreen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bill on 7/19/16.
 */
public class LoadingScreen implements Screen {


    private NoteCollector noteCollector;
    private  Assets AssetsManger;
    private Stage stage;
    private Viewport viewport;
    private static final int VIEWPORT_WIDTH = Constants.APP_WIDTH;
    private static final int VIEWPORT_HEIGHT = Constants.APP_HEIGHT;
    private int speed;
    private long delay;

    private MidiManipulator midiManipulator;
    private File f;

    private ArrayList<MidiNote> notes;

    private float TickPerMsec;

    private BitmapFont font;
    private Thread t = null;
    private Texture spinnerImageTexture;
    private Image spinnerImage;
    private String filepath;


    public LoadingScreen(NoteCollector noteCollector,String filepath,int speed,long delay) {
        this.noteCollector = noteCollector;
        this.filepath = filepath;
        AssetsManger = noteCollector.getAssetsManager();
        f = new File(filepath);
        AssetsManger.LoadLoadingAssets(filepath);
        LoadAssets();
        this.delay = delay;
        this.speed = speed;
            //noteCollector.closeAds();

    }

    @Override
    public void show() {
        setupCamera();
        createBackground();
        createLogo();
        createLabel("Please Wait ....");
        createSpinner();
        setupMidiManipulator();

    }


    private void createSpinner(){
        //create spinner image from spinner texture
        spinnerImage = new Image(spinnerImageTexture);
        //find the origin and start rotating the spinner
        spinnerImage.setPosition(stage.getWidth()/2-spinnerImage.getWidth()/2,stage.getHeight()/2-spinnerImage.getHeight()/2);
        spinnerImage.setOrigin(spinnerImage.getWidth()/2, spinnerImage.getHeight()/2);
        createActions();
        stage.addActor(spinnerImage);

    }
    private void createActions(){
        RotateByAction rotateAction = new RotateByAction();
        rotateAction.setAmount(90f);
        rotateAction.setDuration(1);
        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setAction(rotateAction);
        repeatAction.setCount(RepeatAction.FOREVER);
        spinnerImage.addAction(repeatAction);
    }

    private void setupMidiManipulator(){
        t = new Thread() {
            @Override
            public void run() {
                try {
                    setupMidi();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        try {
            showLoadProgress();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
     stage.getViewport().update(width,height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font.dispose();
        AssetsManger.assetManager.unload(Constants.spinner);
    }

    private void showLoadProgress() throws IOException, InterruptedException {

        if ( !t.isAlive() && AssetsManger.assetManager.update() && AssetsManger.assetManagerFiles.update())
            noteCollector.setScreen(new GameScreen(noteCollector,TickPerMsec,notes,filepath,speed,delay));

    }
    private void LoadAssets(){
        font = AssetsManger.createBimapFont(45);
        spinnerImageTexture = AssetsManger.assetManager.get(Constants.spinner);

    }
    private void createLogo(){
        Texture img = AssetsManger.assetManager.get(Constants.logo);
        Image background = new Image(img);
        background.setPosition((stage.getCamera().viewportWidth-background.getWidth())/2,(stage.getCamera().viewportHeight-background.getHeight()));
        stage.addActor(background);
    }
    private void createBackground(){
        Texture img = AssetsManger.assetManager.get(Constants.BackgroundMenu, Texture.class);
        Image background = new Image(img);
        background.setScaling(Scaling.fit);
        stage.addActor(background);

    }
    private void setupMidi() throws IOException {
        midiManipulator = new MidiManipulator(f);
        TickPerMsec = midiManipulator.getTickPerMsec();
        notes = midiManipulator.GetAllNotes();



    }
    private void createLabel(String text){
        Label.LabelStyle labelstyle = new Label.LabelStyle(font, Color.WHITE);
        Label label = new Label(text, labelstyle);
        label.setPosition((stage.getCamera().viewportWidth-label.getWidth())/2,100f);
        stage.addActor(label);
    }
    private void setupCamera(){
        viewport = new ScalingViewport(Scaling.stretch, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT));
        stage = new Stage(viewport);
        stage.getCamera().update();
    }

}
