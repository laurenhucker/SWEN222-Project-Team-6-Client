package client.level;

import java.util.Random;

public class RandomLevel extends Level {
	
	private static final Random random = new Random();

	public RandomLevel(int w, int h) {
		super(w, h);
	}
	
	/**
	 * Fills tiles in super class with random tile IDs from 0-3
	 */
	public void generateLevel(){
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				tilesInt[x + y * width] = random.nextInt(4);
			}
		}
	}

}
