package megaRun;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Background {
	
	private Random rand;		// Used to generate gaps and blocks
	private int timeElapsed;	// Used for moving the background, incremented by delta in update(). Milliseconds
	
	private SpriteSheet sheet;	// Holds all background sprites
	
	private Boolean scrolling;	// True when the game is playing, false when not and the background is stationary
	
	private Image noOffset;		// Image of the screen, with one extra column of tiles rendered on the right
	
	
	private Boolean[] gapArray;	// If true, generate a gap to jump over at that position
	private Boolean[] blockArray; // If true, generate a block to slide under at that position
	
	
	/*
	 * Constructor to load images and initialize background stuff
	 */
	
	Background() throws SlickException
	{
		rand = new Random();
		sheet = new SpriteSheet("res/Tiles.png", 32, 32);
		noOffset = new Image(20*32, 3*32);
		
		timeElapsed = 0;
		scrolling = false;
		
		gapArray = new Boolean[21];
		blockArray = new Boolean[21];
		
		for(int i = 0; i < 21; i++)		// Loop to initialize every value in gapArray and blockArray,
		{								// so no blocks or gaps appear in the first screen-full
			gapArray[i] = false;
			blockArray[i] = false;
		}
	}
	
	/*
	 * Returns an image of the whole current background
	 */
	
	public Image getImage() throws SlickException
	{
		noOffset.getGraphics().clear();				// Strange things happen without this...
		
		for (int i = 0; i < 21; i++)				// Loop to generate the background image, which is 20 tiles long
		{
	
			if(blockArray[i] == false)				// Draw these tiles if there isn't a block at this position
			{
				noOffset.getGraphics().drawImage(sheet.getSprite(0, 0), i*32, 0);
				noOffset.getGraphics().drawImage(sheet.getSprite(0, 1), i*32, 32);
			}
			
			else									// Draw these tiles if there IS a block at this position
			{
				noOffset.getGraphics().drawImage(sheet.getSprite(0, 3), i*32, 0);
				noOffset.getGraphics().drawImage(sheet.getSprite(0, 4), i*32, 32);
			}
			
			if(gapArray[i] == false)				// Draw this tile if there isn't a gap. If there is a gap, no tile is drawn (convenient eh?)
			{
				noOffset.getGraphics().drawImage(sheet.getSprite(0, 2), i*32, 64);
			}

		}
		
		return noOffset.getSubImage(timeElapsed/5, 0, 32*19, 32*3);	// Return the part of the image that is drawn on screen,
																	// which is shifted depending on time elapsed.
																	// In 160 ms, the tile will be shifted 32 pixels.
	}

	public void update(int delta) throws SlickException 
	{
		
		if(scrolling)									// Only update if background is scrolling, i.e. game is playing
		{
			timeElapsed += delta;						// Increment time elapsed by how the number of milliseconds since last update
			
			if (timeElapsed >= 160)						// If 160 ms have passed, reset shifting and move tiles down.
			{											// After 160 ms, the tiles will have been shifted 32 pixels,
				timeElapsed = 0;						// so the tiles and shifting should be reset since the tiles are 32 pixels in width.
				
				for(int i = 0; i < 20; i++)				// Loop to move tiles down.
				{										// More efficient with LinkedList?
					gapArray[i] = gapArray[i + 1];
					blockArray[i] = blockArray[i + 1];
				}
				
				// Logic to determine if new tiles should include a gap or block. Have fun :-)
				gapArray[20] = !blockArray[19] && !gapArray[19] && !gapArray[18] && rand.nextInt(7) < 1;
				blockArray[20] = !gapArray[20] && !gapArray[19] && !blockArray[19] && !blockArray[18] && rand.nextInt(7) < 1;
			}
		}
		
	}
	
	/*
	 * Determines if the player needs to be jumping to avoid a gap.
	 * Returns true if he should be jumping.
	 * This code was brute forced with trial and error...
	 */
	public Boolean mustJump()
	{

		if(gapArray[2] == true && timeElapsed >= 40)
		{
			return true;
		}
		
		if(gapArray[1] == true && timeElapsed <= 40)
		{
			return true;
	
		}
		
		return false;
	}
	
	/*
	 * Determines if the player needs to be jumping to avoid a block.
	 * Returns true if he should be jumping.
	 * This code was brute forced with trial and error...
	 */
	public Boolean mustSlide()
	{
		
		if(blockArray[2] == true && timeElapsed >= 80)
		{
			return true;
		}
		
		if(blockArray[1] == true && timeElapsed <= 20)
		{
			return true;
	
		}
		
		return false;
	}
	
	/*
	 * Mutator for scrolling
	 */
	public void startScrolling()
	{
		scrolling = true;
	}
	
	/*
	 * Mutator for scrolling
	 */
	public void stopScrolling()
	{
		scrolling = false;
	}

	
	/*
	 * Resets to initial values upon losing
	 */
	public void reset() {
		
		timeElapsed = 0;
		scrolling = false;
				
		for(int i = 0; i < 21; i++)		// Loop to initialize every value in gapArray and blockArray,
		{								// so no blocks or gaps appear in the first screen-full
			
			gapArray[i] = false;
			blockArray[i] = false;
		}
		
	}

}
