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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bill on 7/7/16.
 */
public class Browse implements Screen {
    private Viewport viewport;
    private Stage stage;
    private BitmapFont font,fontList;

    private static final int VIEWPORT_WIDTH = Constants.APP_WIDTH;
    private static final int VIEWPORT_HEIGHT = Constants.APP_HEIGHT;
    private NoteCollector noteCollector;
    private Assets assetsManager;
    private TextureRegionDrawable selectionColor;
    private TextureRegionDrawable selectionColorPressed;
    private TextureRegionDrawable selectionColorList;
    private Table table;
    private ScrollPane scrollPane;
    private List<Object> list;
    private Skin skin;
    private ArrayList<String> item = null;
    private ArrayList<String> path = null;
    private  String filepath;
    private Texture img;
    private String root = Constants.root;
    private int speed;
    private long delay;
    public Browse(NoteCollector noteCollector,int speed,long delay) {
        this.delay = delay;
        this.speed = speed;
        this.noteCollector = noteCollector;
        filepath="";
        assetsManager = noteCollector.getAssetsManager();
        LoadAssets();
        ListStyle();
    }

    @Override
    public void show() {
        setupCamera();
        Gdx.input.setInputProcessor(stage);
        createBackground();
        createTable();
        createList();
        getDir(root);
        createLogo();
        table.add(createLabel("Select a track:")).padTop(70f);
        table.row();

        createScrollPane();
        createButton("Back",5f);
        stage.addActor(table);
    }
    @Override
    public void render(float delta) {

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


    private void createLogo(){
        img = assetsManager.assetManager.get(Constants.logo);
        Image background = new Image(img);
        background.setPosition((stage.getCamera().viewportWidth-background.getWidth())/2,390f);
        stage.addActor(background);

    }
    private void createTable(){
        table = new Table();
        table.center();
        table.setFillParent(true);
        table.pad(10f,10f,30f,10f);
        table.setTouchable(Touchable.enabled);
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


                if (MenuButton.isPressed()){
                    if (prefs.getBoolean("sound")) {
                        noteCollector.getClick().play();
                    }
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            noteCollector.setScreen(new SamplesTrack(noteCollector,speed,delay));
                    } },0.4f);
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


    private void LoadAssets(){
        font = assetsManager.createBimapFont(45);
        selectionColor =new TextureRegionDrawable(new TextureRegion(assetsManager.assetManager.get(Constants.ButtonImage,Texture.class))) ;
        selectionColor.setRightWidth(5f);
        selectionColor.setBottomHeight(2f);
        selectionColorPressed = new TextureRegionDrawable(new TextureRegion(assetsManager.assetManager.get(Constants.ButtonPressed,Texture.class)));
        selectionColorPressed.setRightWidth(5f);
        selectionColorPressed.setBottomHeight(2f);


    }

    private void ListStyle(){
        assetsManager.LoadListAssets();
        fontList = assetsManager.createBimapFont(28);
        selectionColorList = new TextureRegionDrawable(new TextureRegion(assetsManager.assetManager.get(Constants.SelectionColor,Texture.class)));
        skin = assetsManager.assetManager.get(Constants.Skin,Skin.class);
    }

    private void createList()  {
        list = new List<Object>(skin);
        list.getStyle().selection = selectionColorList;
        list.getStyle().font = fontList;
        addListener(list);
    }

    private void createScrollPane(){
        scrollPane = new ScrollPane(list);
        scrollPane.setSmoothScrolling(false);
        scrollPane.setScrollingDisabled(true,false);
        table.add(scrollPane).expand().fill().padBottom(50f);
        table.row();
    }


    private void addListener(Actor actor){
        actor.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

                try {
                    changeDirectory(list.getSelectedIndex());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private Label createLabel(String text){
        Label.LabelStyle labelstyle = new Label.LabelStyle(font, Color.WHITE);
        Label fileLabel = new Label(text, labelstyle);
        return  fileLabel;

    }

    private void createBackground(){
        Texture img = assetsManager.assetManager.get(Constants.BackgroundMenu, Texture.class);
        Image background = new Image(img);
        background.setScaling(Scaling.fit);
        stage.addActor(background);

    }

    private void setupCamera(){
        viewport = new ScalingViewport(Scaling.stretch, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT));
        stage = new Stage(viewport);
        stage.getCamera().update();
    }
    private void changeDirectory(int position) throws IOException, InterruptedException {
        File file = new File(path.get(position));

        if (file.isDirectory()) {

            if (file.canRead()) {
                getDir(path.get(position));
            }
        }else{
            filepath = file.getAbsolutePath();
            noteCollector.setScreen(new LoadingScreen(noteCollector,filepath,speed,delay));

        }
    }


    private String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length()-1)=='/') {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }
    private void getDir(String dirPath) {

        path = new ArrayList<String>();
        item = new ArrayList<String>();

        if (dirPath.equals(method(root)))
            dirPath = root;
        File f = new File(dirPath);
        File [] files = f.listFiles();

        if (!dirPath.equals(root)) {

            item.add("../");
            path.add(f.getParent());
        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if( (file.isDirectory()  &&  checkStartsWith(file.getName())) ||checkMidiTYpe(file.getName()))
                path.add(file.getPath());

            if (file.isDirectory() && checkStartsWith(file.getName()) )
                item.add(file.getName() + "/");
            else  if(checkMidiTYpe(file.getName())  )
            {
                item.add(file.getName());

            }
        }
        list.setItems(item.toArray());
    }

    private boolean checkStartsWith(String name){

        if(!name.toLowerCase().startsWith(".") )
            return true;
        return false;

    }
    private boolean checkMidiTYpe(String name){

        if(name.toLowerCase().endsWith(".mid") ||name.toLowerCase().endsWith(".midi"))
            return true;
        return false;

    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
        fontList.dispose();
        img.dispose();

    }

}
