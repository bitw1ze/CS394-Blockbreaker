package edu.csub.cs.blockbreaker;

import java.util.Vector;
import java.util.Random;

public class Specials {
	public static final int MORE_BALLS = 1;
	public static final int NONE = -1;
	public static int mode = NONE;
	public static final int MAX_BALLS = 4;
	public static int BALLS_INC = 1;
	public static final int BALL_PROC_CHANCE = 20;
	
	protected static Random rand = new Random();
	
	public int type;
	public static Ball ball;
	
	public static final int CHANCE_TWO_BALLS = 50;
	
	public static void set(Ball b, int m) {
		ball = b;
		mode = m;
	}
	
	public static void reset() {
		ball = null;
		mode = NONE;
	}
	
	public static void addBalls(Vector<Ball> balls) {
		for (int i=0; i<BALLS_INC; i++) {
			if (Ball.count >= MAX_BALLS)
				break;

			balls.add(new Ball(ball));
			new Thread(balls.get(balls.size()-1)).start();
		}
		
		reset();
	}
	
	public static boolean proc(int chance) {
		return (chance > rand.nextInt(100));
	}
}
