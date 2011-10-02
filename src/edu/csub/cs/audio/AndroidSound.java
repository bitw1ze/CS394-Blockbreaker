package edu.csub.cs.audio;

import android.media.SoundPool;

import edu.csub.cs.audio.Sound;

public class AndroidSound implements Sound {
	int soundId;
	SoundPool soundPool;
	
	public AndroidSound(SoundPool soundPool, int soundId) {
		this.soundId = soundId;
		this.soundPool = soundPool;
	}
	
	@Override
	public void play(float volume) {
		soundPool.play(soundId, volume, volume, 0, 0, 1);
	}
	
	@Override
	public void dispose() {
		soundPool.unload(soundId);
	}
}