package edu.csub.cs.Blockbreaker;

import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.Bitmap;

public class Paddle extends Collidable {
	public Paddle(Bitmap bmp, Rect rect, Paint paint, GameView gameView) {
		super(bmp, rect, paint, gameView);
	}
}
