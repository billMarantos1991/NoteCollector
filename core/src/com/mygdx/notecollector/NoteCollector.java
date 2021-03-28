package com.mygdx.notecollector;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.notecollector.Utils.Assets;
import com.mygdx.notecollector.screens.IntroScreen;

public class NoteCollector extends Game {

	private Assets AssetsManager;
	private Sound click;
	public AdsHandler adsHandler;
	boolean toggle;
	public NoteCollector(AdsHandler adsHandler) {

		this.adsHandler = adsHandler;
	}


	/*public NoteCollector() {

	}*/

	public Assets getAssetsManager() {
		return AssetsManager;
	}

	public void create() {
		AssetsManager = new Assets();
		click = Gdx.audio.newSound(Gdx.files.internal("data/ui/Sounds/click.mp3"));
		setScreen(new IntroScreen(this));
	}

	public Sound getClick() {
		return click;
	}
}
