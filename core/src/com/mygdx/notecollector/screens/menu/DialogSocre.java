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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
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
import com.mygdx.notecollector.screens.EndGameScreen;
import com.mygdx.notecollector.screens.ScoresScreen;

/**
 * Created by bill on 7/25/16.
 */
public class DialogSocre implements Screen {

    private Assets AssetsManager;
    private Stage stage;
    private Viewport viewport;

    private static final int VIEWPORT_WIDTH = Constants.APP_WIDTH;
    private static final int VIEWPORT_HEIGHT = Constants.APP_HEIGHT;
    private Table table;
    private  TextField textField;

    private TextureRegionDrawable selectionColor;
    private TextureRegionDrawable selectionColorPressed;
    private BitmapFont font;
    private Score score;
    private String ScoreNumber;
    private NoteCollector noteCollector;
    private TextureRegionDrawable selectionColorList;
    private boolean touched;

    public DialogSocre( String ScoreNumber, NoteCollector noteCollector) {
        this.ScoreNumber = ScoreNumber;
        this.noteCollector = noteCollector;
        score = new Score();
        touched=false;
        AssetsManager = noteCollector.getAssetsManager();
        LoadAssets();
    }
    @Override
    public void show() {
        setupCamera();
        createTable();
        createBackground();
        createLogo();
        createLabel("Submit Score",stage.getCamera().viewportHeight/2+100);
        createTextField();
        createButton("Back",5f);
        createButton("Submit",630f);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    private void addListener(){
        textField.addListener(new ClickListener(){
            public void clicked(InputEvent e, float x, float y) {
                if (touched == false)
                    touched =true;
                Gdx.input.setOnscreenKeyboardVisible(true);


            }
        });
    }
    private void createTextField(){
        textField = new TextField("",createTextFieldStyle());
        textField.setMessageText("Write a name");
        textField.setWidth(360);
        textField.setPosition((stage.getCamera().viewportWidth-360)/2,(stage.getCamera().viewportHeight)/2+50);
        addListener();
        table.addActor(textField);
    }

    private TextField.TextFieldStyle createTextFieldStyle(){
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        selectionColorList = new TextureRegionDrawable(new TextureRegion(AssetsManager.assetManager.get(Constants.SelectionColor,Texture.class)));
        textFieldStyle.background = selectionColorList;
        textFieldStyle.font=font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.messageFont =font;
        textFieldStyle.messageFontColor = Color.WHITE;

        return textFieldStyle;
    }
    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        if(Gdx.input.isTouched() && touched == true) {
            Gdx.input.setOnscreenKeyboardVisible(false);
            stage.unfocus(textField);
            touched =false;
        }

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
        font.dispose();
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
        AssetsManager.LoadListAssets();
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

    private void createButton(String text,float Xaxis){

        ImageTextButton.ImageTextButtonStyle textButtonStyle = createButtonStyle(selectionColor);
        ImageTextButton MenuButton = new ImageTextButton(text, textButtonStyle);
        AddButtonListener(MenuButton,text);
        MenuButton.setPosition(Xaxis,10f);
        table.addActor(MenuButton);
    }


    private void AddButtonListener(final ImageTextButton MenuButton, final String text){
        MenuButton.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Preferences prefs = Gdx.app.getPreferences("NoteCollectorPreferences");

                if (MenuButton.isPressed()) {
                    if (prefs.getBoolean("sound")) {
                        noteCollector.getClick().play();
                    }
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            if (text.equals("Submit")) {
                                String name = textField.getText();
                                score.WriteScore(name, Integer.parseInt(ScoreNumber));
                                noteCollector.setScreen(new ScoresScreen(noteCollector));

                            } else
                                noteCollector.setScreen(new EndGameScreen(noteCollector, ScoreNumber));

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
