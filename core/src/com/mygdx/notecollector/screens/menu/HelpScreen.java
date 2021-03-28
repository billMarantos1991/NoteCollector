package com.mygdx.notecollector.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.notecollector.NoteCollector;
import com.mygdx.notecollector.Utils.Assets;
import com.mygdx.notecollector.Utils.Constants;


/**
 * Created by bill on 7/22/16.
 */
public class HelpScreen implements Screen {

    private NoteCollector noteCollector;
    private Assets AssetsManger;
    private Stage stage;
    private Viewport viewport;
    private static final int VIEWPORT_WIDTH = Constants.APP_WIDTH;
    private static final int VIEWPORT_HEIGHT = Constants.APP_HEIGHT;

    private BitmapFont font;
    private TextureRegionDrawable selectionColor;
    private TextureRegionDrawable selectionColorPressed;

    public HelpScreen(NoteCollector noteCollector) {

        this.noteCollector = noteCollector;
        AssetsManger = noteCollector.getAssetsManager();
        font = AssetsManger.createBimapFont(25);
        noteCollector.adsHandler.showAds(1);


    }

    @Override
    public void show() {
        setupCamera();
        createBackground();
        createLogo();
        Texture img = AssetsManger.assetManager.get(Constants.text);
        Image background = new Image(img);
        background.setPosition(((stage.getCamera().viewportWidth-background.getWidth())/2)-30f,(stage.getCamera().viewportHeight-background.getHeight())-100f);

        stage.addActor(background);
        createButton("Menu",5f);
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {

        if (Gdx.input.justTouched()){
            noteCollector.adsHandler.showAds(0);
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

    }
    private void createButton(String text,float Xaxis){
        selectionColor =new TextureRegionDrawable(new TextureRegion(AssetsManger.assetManager.get(Constants.ButtonImage,Texture.class))) ;
        selectionColor.setRightWidth(5f);
        selectionColor.setBottomHeight(2f);
        selectionColorPressed = new TextureRegionDrawable(new TextureRegion(AssetsManger.assetManager.get(Constants.ButtonPressed,Texture.class)));
        selectionColorPressed.setRightWidth(5f);
        selectionColorPressed.setBottomHeight(2f);

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
                        noteCollector.getClick().play();
                    }
                    noteCollector.adsHandler.showAds(1);

                    Timer.schedule(new Timer.Task() {

                        @Override
                        public void run() {
                            noteCollector.setScreen(new MainMenuScreen(noteCollector));

                        }

                    }, 0.4f);

                }
                return true;
            }

        });

    }
    private ImageTextButton.ImageTextButtonStyle createButtonStyle(TextureRegionDrawable ButtonImage){

        BitmapFont  font = AssetsManger.createBimapFont(45);

        ImageTextButton.ImageTextButtonStyle textButtonStyle = new ImageTextButton.ImageTextButtonStyle();
        textButtonStyle.up = ButtonImage;
        textButtonStyle.down = selectionColorPressed;
        textButtonStyle.over = ButtonImage;
        textButtonStyle.font = font;
        return textButtonStyle;
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
    private Label createLabel(String text){

        Label.LabelStyle labelstyle = new Label.LabelStyle(font, Color.WHITE);
        Label fileLabel = new Label(text, labelstyle);
        return  fileLabel;

    }
    private void setupCamera(){
        viewport = new ScalingViewport(Scaling.stretch, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT));
        stage = new Stage(viewport);
        stage.getCamera().update();
    }

}
