package edu.csub.cs.blockbreaker;

import java.util.Random;
import edu.csub.cs.audio.Assets;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Color;
import android.graphics.Canvas;
//import android.util.Log;

public class Ball extends Collidable implements Runnable {
	
	public static int speed;
	public static int count = 0;
	protected int damage = 1;
	
	protected static final double ANGLE_MAX = .7d;
	protected static final double ANGLE_MIN = .07d;
	
	// x and y velocity
	public double xSpeed;
	public double ySpeed;
	public int xSign = 1;
	public int ySign = -1;
	protected double xDiff;
	
	public static int[] COLORS = {Color.RED, Color.BLUE, Color.WHITE, Color.CYAN, Color.GREEN, Color.YELLOW};
	
	Random rand = new Random();
	
	private boolean impendingDeath;
	protected boolean running;
	public boolean alive;
	
	public Ball (Rect rect, GameView gameView) {
		super(rect, gameView);
        set();
	}
	
	public Ball (Ball ball) {
		super(ball.rect, ball.gameView);
		set();
	}
	
	protected void set() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(COLORS[rand.nextInt(COLORS.length)]);
		
		running = false;
		alive = true;
		impendingDeath = false;
		
		generateSpeed();
		count++;
	}
	
	@Override
	public void doDraw(Canvas canvas) {
		canvas.drawCircle(rect.exactCenterX(), rect.exactCenterY(), (float)GameView.BALL_DIAMETER / 2, paint);
	}
	
	public void run() {
		running = true;
		while (running) {
			animate();
			try {
				Thread.sleep(1000 / GameView.FPS);
			}
			catch (InterruptedException e) {}
		}
		alive = false;
	}
	
	protected void checkCollision() {
		
		// collision with left or right walls?
		if (rect.left <= 0) {
			xSign = 1;
			Assets.wallHit.play(1);
		} else if (rect.right >= GameView.width) {
			xSign = -1;
			Assets.wallHit.play(1);
		}
		// collision with top or bottom walls?
		else if (rect.top <= GameView.statusBarHeight) {
			ySign = 1;
			Assets.wallHit.play(1);
		} else if (rect.bottom >= GameView.height) {
			kill();
		}
		
		// collision with GameView.paddle?
		if (!impendingDeath && Rect.intersects(rect, GameView.paddle.rect)) {
			Assets.paddleHit.play(1);
			
			xDiff = (double)diffX(this, GameView.paddle) / ((double)GameView.paddle.rect.width() / 2);
			xSign = (xDiff > 0 ? 1 : -1);
			
			if (checkNextCollision(GameView.paddle)) {
				if (Math.abs(xDiff) >  ANGLE_MAX) {
					xDiff = ANGLE_MAX * xSign;
				}
				if (Math.abs(xDiff) < ANGLE_MIN) {
					xDiff = ANGLE_MIN * xSign;
				}
				
				xSpeed = Math.abs(xDiff) * speed;
				
				ySign = -1;
				ySpeed = (1 - Math.abs(xDiff)) * speed;
			}
			else {
				impendingDeath = true;
			}
		}
		
		// collision with GameView.blocks?
		for (Block blockRow[] : GameView.blocks) {
			for (Block block : blockRow) {
				if (block == null)
					continue;
					
				if (block.lives > 0 && Rect.intersects(rect, block.rect)) {
					if (block.lives > 1) {
						if (checkNextCollision(block))
						{
							ySign = -ySign;
						}
						else {
							xSign = -xSign;
						}
					}
					block.takeDamage(this, damage);
					break;
				}
			}
		}
	}
	
	// moves the ball
	public void animate() {
		rect.offset((int)xSpeed * xSign, (int)ySpeed * ySign);
        checkCollision();
    }
	
	// used for smart collision detection
	protected boolean checkNextCollision(Collidable c) {
		Rect nextRectUp = new Rect(rect.left, rect.top-(int)ySpeed, rect.right, rect.bottom-(int)ySpeed);
		Rect nextRectDown = new Rect(rect.left, rect.top+(int)ySpeed, rect.right, rect.bottom+(int)ySpeed);
		return (Rect.intersects(nextRectUp, c.rect) != Rect.intersects(nextRectDown, c.rect));
	}
	
	public void kill() {
		running = false;
		count--;
	}
	
	// generates a random speed
	public void generateSpeed() {
		xDiff = rand.nextDouble();
		if (xDiff > ANGLE_MAX) {
			xDiff = ANGLE_MAX;
		}
		if (xDiff < ANGLE_MIN) {
			xDiff = ANGLE_MIN;
		}
		xSpeed = speed * xDiff;
		ySpeed = speed * (1-xDiff);
		while ((xSign = rand.nextInt(3) - 1) == 0);
		while ((ySign = rand.nextInt(3) - 1) == 0);
	}
}
