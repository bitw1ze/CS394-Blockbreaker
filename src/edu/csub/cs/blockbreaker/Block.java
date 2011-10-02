package edu.csub.cs.blockbreaker;

import java.util.Random;

import edu.csub.cs.audio.Assets;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

public class Block extends Collidable {
	public static final int MAX_LIVES = 2;
	public static final int BASE_ALPHA = 50;
	public static int count = 0;
	
	public int lives = 0;
	
	protected static Random rand = new Random();
	
	public Block(Bitmap bmp, Rect rect, Paint paint, GameView gameView, int lives) {
		super(bmp, rect, paint, gameView);
		setLives(lives);
		count++;
	}
	
	// sets lives of block and alpha
	public void setLives(int n) {
		lives = n;
		paint.setAlpha((BASE_ALPHA + lives * ((255-BASE_ALPHA) / (MAX_LIVES + 1)) ));
	}
	
	// controls what happens when block takes damage
	public void takeDamage(Ball ball, int n) {
		if (lives <= 0) {
			return;
		}
		
		lives -= n;
		setLives(lives);
		
		if (lives <= 0) {
			Game.score += Game.BLOCK_SCORE;
			count--;
			
			if (Specials.proc(Specials.BALL_PROC_CHANCE)) {
				Specials.set(ball, Specials.MORE_BALLS);
			}
		}
		else {
			Assets.blockHit.play(1);
		}
	}
}