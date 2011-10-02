package edu.csub.cs.blockbreaker;

import android.app.Activity;

import android.os.Bundle;
//import java.io.IOException;
//import android.content.res.AssetFileDescriptor;
//import android.content.res.AssetManager;
//import android.media.AudioManager;
//import android.media.MediaPlayer;

import edu.csub.cs.audio.AndroidAudio;
import edu.csub.cs.audio.Assets;
import edu.csub.cs.audio.Audio;

public class Game extends Activity{
	
	private static GameView gameView;
	
	public Audio audio;

	public static int score = 0;
	public static final int BLOCK_SCORE = 10;
	
	public static final int ARCADE_MODE = 0;
	public static final int ENDURANCE_MODE = 1;
	public static int mode;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        audio = new AndroidAudio(this);
        //Assets.music = audio.newMusic("long/magicghost.mp3");
        Assets.music = audio.newMusic(Assets.songName());
		Assets.music.setLooping(true);
		Assets.music.setVolume(0.5f);
		Assets.music.play();
		
		gameView = new GameView(this);
		setContentView(gameView);
		gameView.requestFocus();
		
	}
	
	protected void onResume() {
		super.onResume();
		if(!Assets.music.isPlaying())
		    Assets.music.play();
	}
	
	protected void onPause() {
		super.onPause();
		if(Assets.music.isPlaying())
			Assets.music.pause();
		if (isFinishing()) {
			Assets.music.stop();
			Assets.music.dispose();
		}
	}
}
