package com.mygdx.notecollector.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.io.File;

/**
 * Created by bill on 7/14/16.
 */
public  class Assets {
    private  AbsoluteFileHandleResolver fileHandleResolver;
    public AssetManager assetManager;
    public AssetManager assetManagerFiles;
    public String MusicName;
    private FreetypeFontLoader.FreeTypeFontLoaderParameter parameter;
    private Preferences prefs;

    public Assets() {
         fileHandleResolver = new AbsoluteFileHandleResolver();
         assetManager = new AssetManager();
         assetManagerFiles = new AssetManager(fileHandleResolver);
        prefs = Gdx.app.getPreferences("NoteCollectorPreferences");

    }


    public void LoadMenuAssets(){

        LoadTexture(Constants.ButtonImage);
        LoadTexture(Constants.BackgroundGame);
        LoadTexture(Constants.BackgroundMenu);
        LoadTexture(Constants.logo);
        LoadTexture(Constants.text);
        LoadTexture(Constants.ButtonPressed);
        assetManager.finishLoading();
    }

    public void LoadListAssets(){
        LoadTexture(Constants.SelectionColor);
        assetManager.load(Constants.skinAtlas, TextureAtlas.class);
        assetManager.load(Constants.Skin, Skin.class);
        assetManager.finishLoading();
    }


    public void LoadLoadingAssets(String filepath){
        LoadTexture(Constants.spinner);
        LoadGameAssets();
        loadMusic(filepath);
    }




    private void LoadGameAssets(){
        LoadTexture(Constants.ButtonImage);
        LoadTexture(Constants.BackgroundGame);
        LoadTexture(Constants.square);
        if(prefs.getBoolean("normal")) {
            LoadTexture(Constants.Collector);
        }else if (prefs.getBoolean("big")){
            LoadTexture(Constants.BigCollector);
        }else if (prefs.getBoolean("vbig")){
            LoadTexture(Constants.VeryBigCollector);

        }
        LoadTexture(Constants.WhiteKey);
        LoadTexture(Constants.BlackKey);
        LoadTexture(Constants.WhitePressedKey);
        LoadTexture(Constants.BlackPressedKey);
        assetManager.finishLoading();

    }

    public BitmapFont createBimapFont(int size ){

        com.mygdx.notecollector.Utils.fonts.SmartFontGenerator fontGen = new com.mygdx.notecollector.Utils.fonts.SmartFontGenerator();
        FileHandle exoFile = Gdx.files.internal("data/ui/fonts/Ubuntu-I.ttf");
        BitmapFont font = fontGen.createFont(exoFile, "exo-medium", size);
        return font;
    }

    private void setSize(int size) {

        parameter.fontParameters.size = size;
    }

    private void LoadTexture(String name){

        if (!assetManager.isLoaded(name))
            assetManager.load(name, Texture.class);
    }


    private  void loadMusic(String filepath){
        fileHandleResolver = new AbsoluteFileHandleResolver();
        assetManagerFiles = new AssetManager(fileHandleResolver);

        File  music = new File(filepath);
        this.MusicName = music.getAbsolutePath();
        assetManagerFiles.load(music.getAbsolutePath(), Music.class);
        assetManagerFiles.finishLoading();

    }




public  void Dispose(){
    assetManager.dispose();
    assetManagerFiles.dispose();
}

}
