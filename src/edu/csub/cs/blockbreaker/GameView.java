package edu.csub.cs.Blockbreaker;

import android.util.Log;
import java.util.Vector;
import java.util.Random;
import android.graphics.Bitmap;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Rect;

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

	// dimensions of screen
	public static int width;
	public static int height;

	// dimensions of block grid
	public int rows;
	public int cols;
	public int totalRows;

	// constants used for calculating random grid
	public static final int ROWS_TOTAL_MIN = 10;
	public static final int ROWS_TOTAL_MAX = 14;
	public static final int COLS_TOTAL_MIN = 4;
	public static final int COLS_TOTAL_MAX = 10;
	private final float ROWS_USED_PCT_MAX = .8f;
	private final float ROWS_USED_PCT_MIN = .5f;
	private static final Random rand = new Random();

	// used for scaling the objects on the screen correctly
	public static int BALL_DIAMETER;
	public static int BLOCK_WIDTH;
	public static int BLOCK_HEIGHT;
	public static int PADDLE_WIDTH;
	public static int PADDLE_HEIGHT;
	public static final float BLOCK_ACTUAL_WIDTH = .9f;
	public static final float BLOCK_ACTUAL_HEIGHT = .9f;
	public static int statusBarHeight;

	// used for grace period between levels
	private static final long LEVEL_GRACE_PERIOD = 1500;
	private static long levelEndedTime = 0;

	// level definitions
	private static Level[] levels;
	public static int currentLevel;
	
	// frames per second
	public static final int FPS = 30;

	// magic booleans - DO NOT TOUCH
	protected boolean instantiated;
	protected boolean started;
	public boolean run;

	// load the bitmaps
	private Bitmap paddleBmp = BitmapFactory.decodeResource(
			getResources(), R.drawable.paddle);

	private Bitmap bgBmp = BitmapFactory.decodeResource(
			getResources(), R.drawable.background);

	private Bitmap bmpBlocks[] = {
			BitmapFactory.decodeResource(getResources(), R.drawable.block), 
			BitmapFactory.decodeResource(getResources(), R.drawable.block2), 
			BitmapFactory.decodeResource(getResources(), R.drawable.block3), 
			BitmapFactory.decodeResource(getResources(), R.drawable.block4), 
			BitmapFactory.decodeResource(getResources(), R.drawable.block5)
	};

	// Rect objects stuff that is going to be drawn and scaled
	private Rect paddleRect = null;
	private Rect ballRect = null;
	private Rect blockRect = null;
	private Rect bgRect = null;
	private Rect statusBarRect = null;

	// keep track of important objects
	private Canvas canvas = null;
	public Game game = null;

	// the collidable objects
	public static Vector<Ball> balls = null;
	public static Paddle paddle = null;
	public static Block[][] blocks = null;

	// this is paint
	private Paint statusTextPaint;
	private Paint statusBarPaint;

	// magic variables
	private SurfaceHolder surfaceHolder = null;
	private Thread viewThread = null;

	public GameView(Context context) {
		super(context);
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		game = (Game)context;

		instantiated = false;
		run = false;
		started = false;
		
		currentLevel = 0;
		levels = Level.createLevels();

		if (Game.mode == Game.ENDURANCE_MODE) {       
        	totalRows = rand.nextInt(ROWS_TOTAL_MAX - ROWS_TOTAL_MIN) + ROWS_TOTAL_MIN;
        	float rows_pct_used = rand.nextFloat() * (ROWS_USED_PCT_MAX - ROWS_USED_PCT_MIN) + ROWS_USED_PCT_MIN;
        	rows = (int) (rows_pct_used * (float)totalRows);
        	cols = rand.nextInt(COLS_TOTAL_MAX - COLS_TOTAL_MIN) + COLS_TOTAL_MIN;	
		}
		else if (Game.mode == Game.ARCADE_MODE) {
			loadLevel(levels[currentLevel]);
		}
		Block.count = 0;
	}
	
	@Override
	public synchronized void run() {
		while (run) {
			canvas = surfaceHolder.lockCanvas();
			if (canvas != null) {
				checkGameStatus();
				doDraw(canvas);
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	private void checkGameStatus() {
		if (started && instantiated) {
			if (Block.count == 0) {
				resetLevel();
			}
			if (Ball.count == 0) {
				endGame();
			}
		}
	}
	
	void endGame() {
		Log.d("fuck", "shit");
		killBalls();
		run = false;
		game.startActivity(new Intent(game, GameOver.class));
		game.finish();
	}
	
	// does the actual drawing
		public void doDraw(Canvas canvas) {
			this.canvas = canvas;
			if (instantiated == true) {
				buildObjects();
			}

			// activates special abilities
			switch (Specials.mode) { 
			case Specials.MORE_BALLS :
				Specials.addBalls(balls);
				break;
			}

			// draw background
			canvas.drawBitmap(bgBmp, null, bgRect, null);
			
			// draw blocks
			for (Block blockRow[] : blocks) {
				for (Block block : blockRow) {
					if (block == null)
						continue;

					if (block.lives > 0) {
						block.doDraw(canvas);
					}
				}
			}
			
			// draw paddle
			paddle.doDraw(canvas);
			
			// draw balls
			for (Ball ball : balls) {
				if (ball.alive) { 
					ball.doDraw(canvas);
				}
			}

			// draw status
			canvas.drawRect(statusBarRect, statusBarPaint);
			canvas.drawText("Score: " + Game.score, 10, statusBarHeight, statusTextPaint);
			
			
		}


	// loads variables from a Level into the GameView object
	private void loadLevel(Level level) {
		cols = level.cols;
		rows = level.rows;
		totalRows = level.rows;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		width = w;
		height = h;
		scaleDimensions();
		instantiated = true;

		if (!run) {
			run = true;
			viewThread = new Thread(this);
			viewThread.start();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		run = false;
		killBalls();
	}

	// moves the paddle when the player moves their finger
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (System.currentTimeMillis() < (levelEndedTime + LEVEL_GRACE_PERIOD)) {
			return true;
		}
		
		if (!started) {
			started = true;
			new Thread(balls.get(0)).start();
		}
		paddle.setCenterX((int)event.getX());

		return true;
	}

	// scales dimensions according to screen dimensions
	private void scaleDimensions() {
		BLOCK_WIDTH = width / cols;
		BLOCK_HEIGHT = height / totalRows;
		PADDLE_WIDTH = Scale.calc(width, Scale.PADDLE_WIDTH);
		PADDLE_HEIGHT = Scale.calc(PADDLE_WIDTH, Scale.PADDLE_HEIGHT);
		BALL_DIAMETER = Scale.calc(height, Scale.BALL_DIAMETER);
		Ball.speed = Scale.calc(height+width, Scale.BALL_SPEED);
		statusBarHeight = Scale.calc(BLOCK_HEIGHT, Scale.STATUSBAR_HEIGHT);
	}

	// Calls createBlocks and passes the matrix from the level
	public void createBlocksFromLevel(Level level) {
		createBlocks(level.blocks);
	}


	// Creates a random integer matrix and calls createBlocks
	public void createRandomBlocks() {    	
		int lives_weighted[] = {1, 4, 9};
		int grid[][] = new int[rows][cols];
		
		int weighted_max = 10;
		for (int row[] : grid) {
			for (int i=0; i<row.length; i++) {
				int weighted_rand = rand.nextInt(weighted_max);
				for (int j=0; j<lives_weighted.length; j++) {
					if (weighted_rand < lives_weighted[j]) {
						row[i] = rand.nextInt(bmpBlocks.length) + (j - 1) * bmpBlocks.length;
						break;
					}
				}
			}
			
		}

		createBlocks(grid);
	}

	/* Creates the blocks based off of an integer matrix
	 * It calculates the correct x and y coordinates of the Rect */
	protected void createBlocks(int[][] grid) {

		blocks = new Block [rows][cols];

		int xmargin = (int)((BLOCK_WIDTH * (1 - BLOCK_ACTUAL_WIDTH)) / 2),
			ymargin = (int)((BLOCK_HEIGHT * (1 - BLOCK_ACTUAL_HEIGHT)) / 2);

		int x1, x2;
		int	y1 = statusBarHeight + ymargin,
				y2 = BLOCK_HEIGHT + statusBarHeight - ymargin;


		for (int i=0; i<rows; i++) {
			x1 = xmargin;
			x2 = BLOCK_WIDTH - xmargin;
			for (int j=0; j<cols; j++) {
				blockRect = new Rect(x1, y1, x2, y2);
				if (grid[i][j] >= 0) {
					blocks[i][j] = new Block(
							bmpBlocks[ grid[i][j] % bmpBlocks.length ], 
							blockRect, 
							null, 
							this, 
							grid[i][j] / bmpBlocks.length + 1);
				}
				else {
					blocks[i][j] = null;
				}
				x1 += BLOCK_WIDTH;
				x2 += BLOCK_WIDTH;
			}
			y1 += BLOCK_HEIGHT;
			y2 += BLOCK_HEIGHT;
		}	
	}
	
	protected void killBalls() {
		for (Ball ball : balls) {
			ball.kill();
		}
	}
	
	protected void createBalls() {
		Ball.count = 0;
		ballRect = new Rect(
				(width/2-BALL_DIAMETER/2), (paddleRect.top-BALL_DIAMETER-1),
				(width/2+BALL_DIAMETER/2), (paddleRect.top-1) );
		balls = new Vector<Ball>();
		balls.add(new Ball(ballRect, this));
		balls.get(0).ySign = -1;
	}

	/* builds a new random level or goes to the next built-in level
	 * sets the paddle and ball back to their original positions
	 * generates a random x and y speed
	 * records the time for the grace period to work */
	public void resetLevel() {

		paddle.setCenterX(width / 2);

		killBalls();
		
		started = false;
		
		if (Game.mode == Game.ENDURANCE_MODE) {
			createRandomBlocks();
		}
		else if (Game.mode == Game.ARCADE_MODE) {
			try {
				currentLevel++;
				loadLevel(levels[currentLevel]);
				scaleDimensions();
				createBlocksFromLevel(levels[currentLevel]);
			}
			catch (ArrayIndexOutOfBoundsException e) {
				endGame();
			}
		}
		
		createBalls();

		levelEndedTime = System.currentTimeMillis();
	}
	
	public void buildObjects() {
		paddleRect = new Rect(
				(int)(width/2)-PADDLE_WIDTH/2, (int)(height - 2*PADDLE_HEIGHT), 
				(int)(width/2)+PADDLE_WIDTH/2, (int)(height - 1*PADDLE_HEIGHT));
		paddle = new Paddle(paddleBmp, paddleRect, null, this);
		
		bgRect = new Rect(0, 0, (int)width, (int)height);
		
		statusBarRect = new Rect(0, 0, (int)width, statusBarHeight);
		statusBarPaint = new Paint();
		statusBarPaint.setColor(Color.LTGRAY);
		
		statusTextPaint = new Paint();
		statusTextPaint.setColor(Color.RED);
		statusTextPaint.setTextSize(statusBarHeight);
		
		createBalls();

		if (Game.mode == Game.ENDURANCE_MODE) {
			createRandomBlocks();
		}
		else if (Game.mode == Game.ARCADE_MODE){
			createBlocksFromLevel(levels[currentLevel]);
		}
		
		started = false;
		instantiated = false;
	}
}

// Class that holds constants for scaling dimensions and has method for scaling them
class Scale {
	public static final double PADDLE_WIDTH = .16d;
	public static final double PADDLE_HEIGHT = .25d;
	public static final double BALL_DIAMETER = .03d;
	public static final double BALL_SPEED = .009d;
	public static final double STATUSBAR_HEIGHT = .5d;

	public static int calc(int m, double n) {
		return (int)((double)m * n);
	}
}
