package edu.csub.cs.blockbreaker;

import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.Canvas;

abstract public class Collidable {  
    protected Bitmap bitmap;
    public Rect rect;
	public Paint paint;
	protected GameView gameView;
    
    public Collidable(Bitmap bitmap, Rect rect, Paint p, GameView gameView) {
    	this.rect = new Rect(rect);
        this.bitmap = Bitmap.createBitmap(bitmap);
        this.paint = (p == null) ? new Paint() : p;
        this.gameView = gameView;
    }
    
    public Collidable(Rect rect, GameView gameView) {
    	this.rect = new Rect(rect);
        this.gameView = gameView;
    }
    
    public void doDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, rect, paint);
    }
	
	public int diffX(Collidable c1, Collidable c2) {
		return c1.rect.centerX() - c2.rect.centerX();
	}
	
	public int diffY(Collidable c1, Collidable c2) {
		return c1.rect.centerY() - c2.rect.centerY();
	}
	
	public void setCenterX(int mX) {
		rect.offsetTo(mX - rect.width()/2, rect.top);
	}
}
