package edu.csub.cs.audio;

import java.util.Random;


public class Assets {
	public static Audio audio;
	
	public static Sound click;
	public static Sound blockHit;
	public static Sound wallHit;
	public static Sound paddleHit;
	
	public static Music music;
	
	final static String names [] = {
			"artofgardens-intsr.mp3", "mars-and-stars.mp3", 
			"vestige-en-antigua.mp3", "magicghost.mp3"};
	
	public static void load(Audio activityAudio) {
		audio = activityAudio;
		
		music = audio.newMusic("long/the-experiment.mp3");
		music.setLooping(true);
		music.setVolume(0.5f);
		//if(Settings.soundEnabled)
		music.play();
		
		click = audio.newSound("short/smack-1.mp3");
		blockHit = audio.newSound("short/button-11.mp3");
		wallHit = audio.newSound("short/drum.mp3");
		paddleHit = audio.newSound("short/mechanical-clonk-1.mp3");
	}
	
	public static void reloadTitle(Audio activityAudio) {
		audio = activityAudio;
		music = audio.newMusic("long/the-experiment.mp3");
		music.setLooping(true);
		music.setVolume(0.5f);
	}
	
	public static String songName() {
		Random rnd = new Random();
		 
		 return("long/" + names[ rnd.nextInt(names.length)] );
	}
}