package com.mygdx.notecollector.GameStage;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.notecollector.NoteCollector;
import com.mygdx.notecollector.Utils.Assets;
import com.mygdx.notecollector.Utils.Constants;
import com.mygdx.notecollector.Utils.WorldUtils;
import com.mygdx.notecollector.actors.Collector;
import com.mygdx.notecollector.actors.Piano;
import com.mygdx.notecollector.actors.Score;
import com.mygdx.notecollector.actors.SquareNotes;
import com.mygdx.notecollector.actors.Text;
import com.mygdx.notecollector.midilib.MidiNote;
import com.mygdx.notecollector.screens.menu.MainMenuScreen;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bill on 6/18/16.
 */
public class GameStage extends Stage implements ContactListener {

    private World world;
    private Vector3 touchPoint,PuttonPoint;

    private Rectangle collectorPosition;
    private BitmapFont font;

    //Variables for time loop
    private long starttime;
    private float prevTick;
    private ArrayList<MidiNote> notes;
    private Timer timer;
    private Timer.Task task;
    private Music music;
    private float TickPerMsec;
    private float curretnTick;
    private long msec;
    //Texture for background
    private Texture Background;
    //actors objects
    private SquareNotes squareNotes;
    private Collector collector;
    private Score score;
    private Piano piano;
    private Text text;

    private int redcounts;
    private Assets AssetsManager;
    private OrthographicCamera camera;
    private boolean fisttime;
    private String message;
    private String MessageScore;
    private String GameState;
    private Preferences prefs;
    private static final int VIEWPORT_WIDTH = Constants.APP_WIDTH;
    private static final int VIEWPORT_HEIGHT = Constants.APP_HEIGHT;

    private float accumulator = 0;
    private final float TIME_STEP = 1 / 300f;

    private NoteCollector noteCollector;
    private TextureRegionDrawable selectionColor;
    private BitmapFont fontbutton;
    private float squarewidth,squareheight;

    private Label resume,quit;
    private Image BackgroundPause;
    public long pausetime;
    public GameStage(NoteCollector noteCollector, float TickPerMsec, ArrayList<MidiNote> notes, int speed, long delay) throws IOException, InterruptedException {
        super(new ScalingViewport(Scaling.stretch, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)));

        squarewidth=32f;
        squareheight=32f;
        this.AssetsManager =noteCollector.getAssetsManager();
        this.noteCollector=noteCollector;
        this.notes =notes;
        this.TickPerMsec = TickPerMsec;
        prefs= Gdx.app.getPreferences("NoteCollectorPreferences");

        if(prefs.getBoolean("normal")) {
            squarewidth =32f;
            squareheight=32f;
        }else if (prefs.getBoolean("big")){
            squarewidth =40f;
            squareheight=40f;
        }else if (prefs.getBoolean("vbig")){
            squarewidth =48f;
            squareheight=48f;

        }
        message ="";
        GameState ="running";
        redcounts =0;
        fisttime =true;
        createObjects();
        createBackground();
        setupWorld();
        setupCamera();
        setupActros(speed,delay);
        StartTimer();
        createPauseLabels();
        createBackgroundPause();
        addActorSPause();

    }



    private void createObjects(){
        touchPoint = new Vector3();
        PuttonPoint= new Vector3();
        timer = new Timer();
        collectorPosition = new Rectangle(touchPoint.x, touchPoint.y, 36f, 36f);
    }
    private void createPauseLabels(){
        fontbutton = AssetsManager.createBimapFont(45);

        resume = createLabel("Resume");
        resume.setPosition((getCamera().viewportWidth-resume.getWidth())/2,(getCamera().viewportHeight)/2 +50f);
        resume.setVisible(false);

        quit = createLabel("Quit");
        quit.setPosition((getCamera().viewportWidth-quit.getWidth())/2,(getCamera().viewportHeight-quit.getHeight())/3 +50f);
        quit.setVisible(false);
    }
    private void addActorSPause(){
        addActor(BackgroundPause);
        addActor(resume);
        addActor(quit);
    }
    private void createBackgroundPause(){
        selectionColor =new TextureRegionDrawable(new TextureRegion(AssetsManager.assetManager.get(Constants.ButtonImage,Texture.class))) ;
        selectionColor.setRightWidth(5f);
        selectionColor.setBottomHeight(2f);
        BackgroundPause = new Image(selectionColor);
        BackgroundPause.setSize(resume.getWidth()+quit.getWidth(),resume.getHeight()+quit.getHeight()+50f);
        BackgroundPause.setPosition((getCamera().viewportWidth- BackgroundPause.getWidth())/2,(getCamera().viewportHeight  -BackgroundPause.getHeight())/2 +20f);
        BackgroundPause.setVisible(false);
    }
    private void setupActros(int speed,long delay){
        setupCollector();
        setupSquareNotes(speed,delay);
        setupPiano();
        setUpScore();
        setUpText();
    }
    private void createBackground(){
        Background = AssetsManager.assetManager.get(Constants.BackgroundGame);
        Image mazePreview = new Image(Background);
        mazePreview.setScaling(Scaling.fit );
        addActor(mazePreview);
    }
    private void setUpText() {

        Rectangle textBounds = new Rectangle(getCamera().viewportWidth  / 64,
                getCamera().viewportHeight * 62 / 64, getCamera().viewportWidth / 4,
                getCamera().viewportHeight / 6);
        text = new Text(textBounds,AssetsManager);


    }
    private void CreateText(String message,Color color) {
        text.setColor(color);
        text.setMessage(message);
        text.addAction(Actions.sequence(
                Actions.show(),
                Actions.fadeIn(0f),
                Actions.delay(1.5f),
                Actions.fadeOut(1f),
                Actions.hide())
        );
        addActor(text);
    }
    private void setupWorld(){
        world = WorldUtils.CreateWorld();
        world.setContactListener(this);
    }
    private void setupSquareNotes(int speed,long delay){
        squareNotes = new SquareNotes(WorldUtils.CreateSquareNotes(world), AssetsManager,speed,delay);
        addActor(squareNotes);
    }
    private void setupCollector(){
        collector = new Collector(WorldUtils.CreateCollector(world), AssetsManager);
        addActor(collector);
    }
    private void setupPiano(){
        piano = new Piano(WorldUtils.CreatePianoKeyboard(world), AssetsManager);
        piano.setNotes(notes);
        piano.setSquareNotes(squareNotes);
        addActor(piano);

    }

    private void setUpScore() {
        Rectangle scoreBounds = new Rectangle(getCamera().viewportWidth * 57 / 64,
                getCamera().viewportHeight * 62 / 64, getCamera().viewportWidth / 4,
                getCamera().viewportHeight / 6);
        score = new Score(scoreBounds, AssetsManager);
        addActor(score);

    }

     private void setupCamera() {
         camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
         camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
         camera.update();
     }

    private void StartTimer(){
        createTimer();
        SetupMusic();

        if (prefs.getBoolean("music")) music.setVolume(100f);
        else music.setVolume(0f);

        music.play();
        timer.scheduleTask(task, 0f,0.1f);
        starttime = System.currentTimeMillis();
        curretnTick = 0;

    }
    private void createTimer(){

        task = new Timer.Task() {
            @Override
            public void run() {
                if(GameState.equals("running")) {
                    msec = System.currentTimeMillis()- starttime;
                    prevTick =  curretnTick;
                    curretnTick =  (msec / TickPerMsec);
                    piano.DrawNotes((int) curretnTick,(int) prevTick);

                }


            }
        };
    }
    private void SetupMusic(){
        music =AssetsManager.assetManagerFiles.get(AssetsManager.MusicName,Music.class);
        music.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                GameState ="finished";
            }
        });
    }


    public void setGameState(String gameState) {
        GameState = gameState;
    }

    private Label createLabel(String text){
        Label.LabelStyle labelstyle = new Label.LabelStyle(fontbutton, Color.WHITE);
        Label fileLabel = new Label(text, labelstyle);
        return  fileLabel;

    }

    private void pauseTimer(long paused){
        if (music == null)
            return;

        if (music.isPlaying()) {
            this.pausetime = paused;
            long   msec = System.currentTimeMillis() - starttime;
            prevTick = curretnTick;
            curretnTick = (msec / TickPerMsec);
            piano.DrawNotes((int) curretnTick, (int) prevTick);
        }
        music.pause();
        timer.stop();
    }


    private void pauseObjects(boolean paused){
        squareNotes.setPaused(paused);
        collector.setPaused(paused);
        piano.setPaused(true);

    }

    private void fadeInPause(boolean visible){
        BackgroundPause.setVisible(visible);
        resume.setVisible(visible);
        quit.setVisible(visible);
    }
     public void pauseGame(long paused){
         pauseTimer(paused);
         pauseObjects(true);
         fadeInPause(true);

     }
    @Override
     public void act(float delta) {
         super.act(delta);

         squareNotes.setCollector(collectorPosition);
         redcounts = squareNotes.getRedcounts();
         score.setScore(squareNotes.getScore());

         MessageScore = String.valueOf(squareNotes.getScore());

         checkMessages();

         accumulator += delta;
         while (accumulator >= delta){
             world.step(TIME_STEP, 6, 2);
             accumulator -= TIME_STEP;
         }


     }

    private void checkMessages(){
        if (squareNotes.getSquareColor() == 3 && fisttime == true && squareNotes.getTypeOfFailure() == 1) {
            createMessage(Color.RED,"Collector disabled for ");
        }else  if (squareNotes.getSquareColor() == 3 && fisttime == true && squareNotes.getTypeOfFailure() == 2) {
            createMessage(Color.RED,"Speed up for ");
        }
        else if(squareNotes.getSquareColor() ==2 && fisttime == true ) {
            createMessage(Color.GREEN,"Bonus X "+squareNotes.getMultiplier()+" for ");
        }else if(squareNotes.getSquareColor() == 1 && fisttime == false){
            message="";
            fisttime=true;
        }
    }
    private void createMessage(Color color,String message){
        long time = squareNotes.getTime();
        message += String.valueOf(time/1000)+" seconds";
        CreateText(message,color);
        fisttime =false;

    }



    public String getScore() {
        return MessageScore;
    }

    public String getGameState() {
        return GameState;
    }

    public int getRedcounts() {
         return redcounts;
     }

    public void resumeTimer(){
        GameState ="running";
        pausetime =  System.currentTimeMillis() - pausetime;
        starttime = pausetime + starttime;
        music.play();
        timer.start();
    }

    public void resumeGame(){
        pauseObjects(false);
        fadeInPause(false);
        resumeTimer();
    }

     @Override
     public void draw() {

        super.draw();
         this.getBatch().setProjectionMatrix(camera.combined);
     }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

        translateScreenToWorldCoordinates(x,y);

        getCamera().unproject(PuttonPoint.set(x,y,0));
        if ( GameState=="paused" && checkPositionButtons(PuttonPoint.y) ) {
             GameState ="resume";
            return false;
        }else if( GameState=="paused" && checkPositionButtons(PuttonPoint.y) == false){
            dispose();
            noteCollector.setScreen(new MainMenuScreen(noteCollector));
            return  false;
        }
        if (PuttonPoint.y <56f && PuttonPoint.x<800f && GameState !="paused") {
            GameState = "paused";
        }

        return true;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        translateScreenToWorldCoordinates(screenX,screenY);
        return true;
    }

   @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
       translateScreenToWorldCoordinates(screenX,screenY);



       if (touchPoint.x+ 32f  >getCamera().viewportWidth || touchPoint.y +32f >getCamera().viewportHeight || touchPoint.y+32f < 56f ) {
           return false;
       }
       else {
          collectorPosition.set(touchPoint.x,touchPoint.y,squarewidth,squareheight);
           collector.changeposition(touchPoint.x, touchPoint.y);

       }
       return true;
    }

    private boolean checkPositionButtons(float y){

        if(  y >getCamera().viewportHeight/2 )
            return  true;

        return false;

    }

    private void translateScreenToWorldCoordinates(int x, int y){
        getCamera().unproject(touchPoint.set(x, y, 0));
    }
    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public void dispose() {
        timer.clear();
        task.cancel();
        music.stop();
        AssetsManager.assetManagerFiles.dispose();
    }


}
