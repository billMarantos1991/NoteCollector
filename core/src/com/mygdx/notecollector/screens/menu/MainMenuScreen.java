package com.mygdx.notecollector.screens.menu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
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
import com.mygdx.notecollector.screens.ScoresScreen;


public class MainMenuScreen implements Screen {

    private Stage stage;
    private BitmapFont font;
    private Assets assetsManager;
    private Viewport viewport;
    private static final int VIEWPORT_WIDTH = Constants.APP_WIDTH;
    private static final int VIEWPORT_HEIGHT = Constants.APP_HEIGHT;
    private NoteCollector noteCollector;
    private  VerticalGroup verticalGroup;
    private TextureRegionDrawable selectionColor;
    private TextureRegionDrawable selectionColorPressed;


    public MainMenuScreen(NoteCollector noteCollector) {
        this.noteCollector = noteCollector;
        assetsManager = noteCollector.getAssetsManager();
        LoadAssets();
        noteCollector.adsHandler.showAds(1);

    }


    @Override
    public void show() {

        setupCamera();
        Gdx.input.setInputProcessor(stage);

        createVerticalGroup();
        createLogo();
        createButton("Play");
        createButton("Scores");
        createButton("Help");
        createButton("Options");
        createButton("Exit");
        createBackground();

        stage.addActor(verticalGroup);

    }

    private void setupCamera(){
        viewport = new ScalingViewport(Scaling.stretch, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT));
        stage = new Stage(viewport);
        stage.getCamera().update();
    }
    private void LoadAssets(){
        assetsManager.LoadMenuAssets();

        font = assetsManager.createBimapFont(45);
        selectionColor =new TextureRegionDrawable(new TextureRegion(assetsManager.assetManager.get(Constants.ButtonImage,Texture.class))) ;
        selectionColor.setRightWidth(5f);
        selectionColor.setBottomHeight(2f);
        selectionColorPressed = new TextureRegionDrawable(new TextureRegion(assetsManager.assetManager.get(Constants.ButtonPressed,Texture.class)));
        selectionColorPressed.setRightWidth(5f);
        selectionColorPressed.setBottomHeight(2f);

    }

    private void createLogo(){
        Texture img = assetsManager.assetManager.get(Constants.logo);
        Image background = new Image(img);
        addVerticalGroup(background);
    }
    private void createBackground(){
        Texture img = assetsManager.assetManager.get(Constants.BackgroundMenu, Texture.class);
        Image background = new Image(img);
        background.setScaling(Scaling.fit);
        stage.addActor(background);

    }
    private void createVerticalGroup(){
        verticalGroup  = new VerticalGroup();
        verticalGroup.setFillParent(true);
        verticalGroup.center().padTop(50f).space(10f);

    }

    private  void addVerticalGroup(Actor actor){
        verticalGroup.addActor(actor);

    }

    private void createButton(String text){

        ImageTextButton.ImageTextButtonStyle textButtonStyle = createButtonStyle(selectionColor);
        ImageTextButton MenuButton = new ImageTextButton(text, textButtonStyle);
        AddButtonListener(MenuButton,text);
        addVerticalGroup(MenuButton);

    }

    private void AddButtonListener(final ImageTextButton MenuButton, final String text){
        MenuButton.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Preferences prefs = Gdx.app.getPreferences("NoteCollectorPreferences");

                    if (MenuButton.isPressed()){


                         if (prefs.getBoolean("sound")) {
                          noteCollector.getClick().play();
                         }

                        Timer.schedule(new Timer.Task() {
                         @Override
                         public void run() {
                              switch (text) {
                                  case "Play":
                                      noteCollector.adsHandler.showAds(2);
                                      noteCollector.setScreen(new DifficultyScreen(noteCollector));//new Browse(noteCollector));
                                      break;
                                  case "Exit":
                                     Gdx.app.exit();
                                  break;
                                  case "Scores":
                                     noteCollector.setScreen(new ScoresScreen(noteCollector));
                                  break;
                                  case "Help":
                                      noteCollector.setScreen(new HelpScreen(noteCollector));
                                  break;
                                  case "Options":
                                      noteCollector.setScreen(new OptionsScreen(noteCollector));
                                  break;

                              }
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


    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            noteCollector.adsHandler.showAds(0);
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
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
        stage.dispose();


    }
}
