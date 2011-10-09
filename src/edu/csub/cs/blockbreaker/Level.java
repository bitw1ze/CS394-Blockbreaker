package edu.csub.cs.Blockbreaker;

public class Level {
	public final int rows;
	public final int cols;
	public final int[][] blocks;
	public final int background;
	public final String music;
	
	public Level(int grid[][], int background, String music) {
		rows = grid.length;
		cols = grid[0].length;
		blocks = grid;
		
		this.background = background;
		this.music = music;
	}
	
	public static Level [] createLevels() {
    	Level l[] = {
    			new Level(Level.l1, 0, "long/something"),
    	};
    	return l;
    }
	
	public static int l1[][] = {
			{-1,8,-1,5,8,5,-1,8,-1},
			{8,5,-1,8,-1,5,8,5,-1},
			{-1,5,8,5,-1,8,-1,5,8},
			{-1,8,-1,5,8,5,-1,8,-1},
			{8,5,-1,8,-1,5,8,5,-1},
			{-1,5,8,5,-1,8,-1,5,8},
			{-1,8,-1,5,8,5,-1,8,-1},
			{8,5,-1,8,-1,5,8,5,-1},
			{-1,5,8,5,-1,8,-1,5,8},
			{-1,8,-1,5,8,5,-1,8,-1},
			{8,5,-1,8,-1,5,8,5,-1},
			{-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,-1,-1,-1}
		};
}
