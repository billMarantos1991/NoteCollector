package com.mygdx.notecollector;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AndroidLauncher extends AndroidApplication implements AdsHandler
{


	private static String TAG = "AndroidLauncher";
	protected AdView adView;
	 RelativeLayout.LayoutParams adParms;
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	private final int DISABLE_ADS = 2;
	private final int  ENABLE_ADS = 3;
	private String id ="ca-app-pub-4355640544133287/3752285058";
	private  AdRequest.Builder builder;

	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case SHOW_ADS:
					adView.setVisibility(View.VISIBLE);
					break;
				case HIDE_ADS:
					adView.setVisibility(View.GONE);
					break;
				case DISABLE_ADS:
					adView.destroy();
					break;
				case ENABLE_ADS:
					adView.loadAd(builder.build());

			}
		}
	};
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RelativeLayout layout = new RelativeLayout(this);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		View gameView = initializeForView(new NoteCollector(this),config);
		layout.addView(gameView);
		setupAds();

		createAds();
		layout.addView(adView,adParms);
		setContentView(layout);
		//adView.destroy();
		//initialize(new NoteCollector(),config);
	}

	public void destroy(){
		adView.destroy();
	}

	private void setupAds(){
		adView = new AdView(this);
		adView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				Log.i(TAG,"Ad loadeed .....");
			}
		});
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(id);
	}
	private void createAds(){
		builder= new AdRequest.Builder();
		//builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
		//builder.addTestDevice("EA1037383C57D7D5111CF78214CF7C98");
		adParms = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT
		);
		adParms.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adParms.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
		adView.loadAd(builder.build());

	}


	@Override
	public void showAds(int choice) {
		switch(choice){
			case 1:
				handler.sendEmptyMessage(SHOW_ADS);
				break;
			case 0:
				handler.sendEmptyMessage(HIDE_ADS);
				break;
			case 2:
				handler.sendEmptyMessage(DISABLE_ADS);
				break;
			case 3:
				handler.sendEmptyMessage(ENABLE_ADS);
		}
	}

}
