package edu.csub.cs.Blockbreaker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.content.Intent;
import edu.csub.cs.audio.AndroidAudio;
import edu.csub.cs.audio.Assets;
import edu.csub.cs.audio.Audio;

public class Blockbreaker extends Activity implements OnClickListener {
	
	public static final boolean DEBUG = false;
    private static boolean gameMode = false;
	
	// manages music for this activity
	//private MediaPlayer mediaPlayer;
	
	// manages sound effects for all activies
	public Audio audio;
	//public static SFXManager sfx;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        audio = new AndroidAudio(this);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
    
        /** For fade in animations */
        TextView arcade = (TextView) findViewById(R.id.arcade_button);
        TextView endurance = (TextView) findViewById(R.id.endurance_button);
        TextView score = (TextView) findViewById(R.id.scores_button);
        TextView help = (TextView) findViewById(R.id.help_button);
        TextView about = (TextView) findViewById(R.id.about_button);
        TextView exit = (TextView) findViewById(R.id.exit_button);
        Animation fade1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        arcade.startAnimation(fade1);
        endurance.startAnimation(fade1);
        score.startAnimation(fade1);
        help.startAnimation(fade1);
        about.startAnimation(fade1);
        exit.startAnimation(fade1);    
        
        // create buttons and associate with resources
        View arcadebutton = findViewById(R.id.arcade_button);
        View endurancebutton = findViewById(R.id.endurance_button);
        View scoreButton = findViewById(R.id.scores_button);
        View helpButton = findViewById(R.id.help_button);
        View aboutButton = findViewById(R.id.about_button);
        View exitButton = findViewById(R.id.exit_button);
        
        // set listeners for buttons
        arcadebutton.setOnClickListener(this);
        endurancebutton.setOnClickListener(this);
        scoreButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
        
        // set up mediaplayer and sound manager
        Assets.load(audio);
    
    }
    
    public void onClick(View v) {
    	
    	Intent i = null;	
    	switch(v.getId()) {
    	case R.id.arcade_button:
    		i = startGame(Game.ARCADE_MODE);
    		break;
    		
    	case R.id.endurance_button:
    		i = startGame(Game.ENDURANCE_MODE);
    		break;
    		
    	case R.id.scores_button:
    		i = new Intent(this, Scores.class);
    		break;
    		
    	case R.id.help_button:
    		i = new Intent(this, Help.class);
    		break;
    		
    	case R.id.about_button:
    		i = new Intent(this, About.class);
    		break;
    		
    	case R.id.exit_button:
    		finish();
    		break;
    	} 
    	
    	if (i != null) {
    		Assets.click.play(1);
    		startActivity(i);
    	}
    }
    
    private Intent startGame(int mode) {
    	Game.score = 0;
		Game.mode = mode;
		gameMode = true;
		return new Intent(this, Game.class);
    }
    
    protected void onResume() {
		super.onResume();
		if(gameMode) {
		Assets.reloadTitle(audio);
		Assets.music.play();
		gameMode = false;
		}
	}
	
	protected void onPause() {
		super.onPause();
		if(gameMode)
		  Assets.music.pause();
		if (isFinishing()) {
			Assets.music.stop();
			Assets.music.dispose();
		}
	}
}

