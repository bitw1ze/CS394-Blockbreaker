package edu.csub.cs.blockbreaker;

import android.app.Activity;
import android.os.Bundle;

public class GameOver extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        new Alerts("Game Over", "You lose", R.layout.gameover, this);
	}
}