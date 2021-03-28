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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.notecollector.NoteCollector;
import com.mygdx.notecollector.Utils.Assets;
import com.mygdx.notecollector.Utils.Constants;
import com.mygdx.notecollector.screens.menu.DialogSocre;
import com.mygdx.notecollector.screens.menu.DifficultyScreen;

/**
 * Created by bill on 7/22/16.
 */
public class EndGameScreen implements Screen {


    private Assets AssetsManager;
    private NoteCollector notecollector;
    private Stage stage;
    private Viewport viewport;

    private static final int VIEWPORT_WIDTH = Constants.APP_WIDTH;
    private static final int VIEWPORT_HEIGHT = Constants.APP_HEIGHT;
    private Table table;


    private TextureRegionDrawable selectionColor;
    private TextureRegionDrawable selectionColorPressed;
    private BitmapFont font;
    private String Score;

    public EndGameScreen(NoteCollector notecollector,String Score) {
        this.notecollector = notecollector;
        this.Score = Score;
        AssetsManager = notecollector.getAssetsManager();
        LoadAssets();
    }

    @Override
    public void show() {
        setupCamera();
        createTable();
        createBackground();
        createLogo();
        createButtons();
        createLabels();
        Gdx.input.setInputProcessor(stage);

    }

    private void createButtons(){
        float Xaxis =(stage.getCamera().viewportWidth/2)-160f;
        createButton("Continue",Xaxis,(stage.getCamera().viewportHeight-52)/2);
        Xaxis =Xaxis+195f;
        createButton("Submit",Xaxis,(stage.getCamera().viewportHeight-52)/2);
    }
    private void createLabels(){
        createLabel("Game finished!",stage.getCamera().viewportHeight/2+100);
        createLabel("Your Score:"+Score,stage.getCamera().viewportHeight/2+30);

    }
    @Override
    public void render(float delta) {

        /*if (Gdx.input.justTouched()){
            notecollector.closeAds();
        }*/
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();


    }

    @Override
    public void resize(int width, int height) {

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



    private void createLabel(String text,float Yaxis){
        Label.LabelStyle labelstyle = new Label.LabelStyle(font, Color.WHITE);
        Label label = new Label(text, labelstyle);
        label.setPosition((stage.getCamera().viewportWidth-label.getWidth())/2,Yaxis);
        stage.addActor(label);

    }
    private void createTable(){
        table = new Table();
        table.center();
        table.setFillParent(true);
        table.pad(10f,10f,30f,10f);
        table.setTouchable(Touchable.enabled);
    }
    private void LoadAssets(){
        font = AssetsManager.createBimapFont(45);
        selectionColor =new TextureRegionDrawable(new TextureRegion(AssetsManager.assetManager.get(Constants.ButtonImage,Texture.class))) ;
        selectionColor.setRightWidth(7f);
        selectionColor.setBottomHeight(2f);
        selectionColorPressed = new TextureRegionDrawable(new TextureRegion(AssetsManager.assetManager.get(Constants.ButtonPressed,Texture.class)));
        selectionColorPressed.setRightWidth(7f);
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

    private void createButton(String text,float Xaxis,float Yaxis){

        ImageTextButton.ImageTextButtonStyle textButtonStyle = createButtonStyle(selectionColor);
        ImageTextButton MenuButton = new ImageTextButton(text, textButtonStyle);
        AddButtonListener(MenuButton,text);
        System.out.println(MenuButton.getWidth());
        MenuButton.setPosition(Xaxis,Yaxis);
        stage.addActor(MenuButton);
    }

    private void AddButtonListener(final ImageTextButton MenuButton, final String text){
        MenuButton.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Preferences prefs = Gdx.app.getPreferences("NoteCollectorPreferences");

                if (MenuButton.isPressed()) {
                    if (prefs.getBoolean("sound")) {
                        notecollector.getClick().play();
                    }
                    Timer.schedule(new Timer.Task() {


                        @Override
                        public void run() {
                            if (text.equals("Submit"))
                                notecollector.setScreen(new DialogSocre(Score, notecollector));
                            else
                                notecollector.setScreen(new DifficultyScreen(notecollector));


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
