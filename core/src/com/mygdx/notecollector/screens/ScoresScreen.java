package com.mygdx.notecollector.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.notecollector.NoteCollector;
import com.mygdx.notecollector.Utils.Assets;
import com.mygdx.notecollector.Utils.Constants;
import com.mygdx.notecollector.Utils.Score;
import com.mygdx.notecollector.screens.menu.MainMenuScreen;

import java.util.ArrayList;

/**
 * Created by bill on 7/22/16.
 */
public class ScoresScreen implements Screen {


    private Score score;
    private ArrayList<String> scores ;
    private Assets AssetsManager;
    private NoteCollector notecollector;
    private Stage stage;
    private Viewport viewport;
    private int toggle =0;

    private static final int VIEWPORT_WIDTH = Constants.APP_WIDTH;
    private static final int VIEWPORT_HEIGHT = Constants.APP_HEIGHT;


    private TextureRegionDrawable selectionColor;
    private TextureRegionDrawable selectionColorPressed;
    private BitmapFont font;
    private VerticalGroup verticalGroup;

    public ScoresScreen(NoteCollector notecollector) {
        this.notecollector = notecollector;
        score = new Score();
        scores = score.getScore();
        AssetsManager = notecollector.getAssetsManager();
        LoadAssets();
        notecollector.adsHandler.showAds(1);

    }

    @Override
    public void show() {
        setupCamera();
        createVerticalGroup();
        createBackground();
        createLogo();
        Label title = createLabel("High Scores",45);
        addVerticalGroup(title);
        printScoreList();
        createButton("Menu",5f);
        stage.addActor(verticalGroup);
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
       if (Gdx.input.justTouched()){
            notecollector.adsHandler.showAds(0);

        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


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


    }
    private void printScoreList(){

        if (scores.size()==0 || scores ==null){
            Label score = createLabel("No recorded score ",28);
            addVerticalGroup(score);
        }else {

            for (int i = 0; i < scores.size(); i++) {
                Label score = createLabel(scores.get(i), 28);
                addVerticalGroup(score);
            }
        }
    }
    private Label createLabel(String text,int size){
        BitmapFont font = AssetsManager.createBimapFont(size);
        Label.LabelStyle labelstyle = new Label.LabelStyle(font, Color.WHITE);
        Label label = new Label(text, labelstyle);
        return label;


    }
    private void createVerticalGroup(){
        verticalGroup  = new VerticalGroup();
        verticalGroup.setFillParent(true);
        verticalGroup.center().padTop(80f).space(5f);

    }

    private  void addVerticalGroup(Actor actor){
        verticalGroup.addActor(actor);

    }
    private void LoadAssets(){
        font = AssetsManager.createBimapFont(45);
        selectionColor =new TextureRegionDrawable(new TextureRegion(AssetsManager.assetManager.get(Constants.ButtonImage,Texture.class))) ;
        selectionColor.setRightWidth(5f);
        selectionColor.setBottomHeight(2f);
        selectionColorPressed = new TextureRegionDrawable(new TextureRegion(AssetsManager.assetManager.get(Constants.ButtonPressed,Texture.class)));
        selectionColorPressed.setRightWidth(5f);
        selectionColorPressed.setBottomHeight(2f);


    }
    private void createLogo(){
        Texture img = AssetsManager.assetManager.get(Constants.logo);
        Image background = new Image(img);
        background.setPosition((stage.getCamera().viewportWidth-background.getWidth())/2,(stage.getCamera().viewportHeight-background.getHeight()));
        stage.addActor(background);
    }
    private void createBackground(){
        Texture img = AssetsManager.assetManager.get(Constants.BackgroundMenu, Texture.class);
        Image background = new Image(img);
        background.setScaling(Scaling.fit);
        stage.addActor(background);

    }

    private void createButton(String text,float Xaxis){

        ImageTextButton.ImageTextButtonStyle textButtonStyle = createButtonStyle(selectionColor);
        ImageTextButton MenuButton = new ImageTextButton(text, textButtonStyle);
        AddButtonListener(MenuButton);
        MenuButton.setPosition(Xaxis,10f);
        stage.addActor(MenuButton);
    }

    private void AddButtonListener(final ImageTextButton MenuButton){
        MenuButton.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Preferences prefs = Gdx.app.getPreferences("NoteCollectorPreferences");

                if (MenuButton.isPressed()) {
                    if (prefs.getBoolean("sound")) {
                        notecollector.getClick().play();
                    }
                    notecollector.adsHandler.showAds(1);
                    Timer.schedule(new Timer.Task() {

                        @Override
                        public void run() {
                            notecollector.setScreen(new MainMenuScreen(notecollector));

                        }

                    }, 0.4f);
                }
                return true;
            }

        });

    }



    private ImageTextButton.ImageTextButtonStyle createButtonStyle(TextureRegionDrawable ButtonImage){

        ImageTextButton.ImageTextButtonStyle textButtonStyle = new ImageTextButton.ImageTextButtonStyle();
        textButtonStyle.up = ButtonImage;
        textButtonStyle.down = selectionColorPressed;
        textButtonStyle.over = ButtonImage;
        textButtonStyle.font = font;
        return textButtonStyle;
    }

    private void setupCamera(){
        viewport = new ScalingViewport(Scaling.stretch, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT));
        stage = new Stage(viewport);
        stage.getCamera().update();
    }
}
