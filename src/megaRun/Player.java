package megaRun;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Player {
	
	public enum PlayerState			// Possible states our protagonist can have
	{
		RUNNING, SLIDING, JUMPING, STANDING, FALLING, BUMPED
		
	}
	
	private SpriteSheet sheet;		// Sprites for every 'view' of the player

	private Animation runAni;		// Animation for running
	private Animation jumpAni;		// Animation for jumping
	private Animation slideAni;		// Animation for sliding
	private Animation standAni;		// Animation for standing (intro)
	
	private Animation fallAni;		// Animation for falling
	private Animation bumpAni;		// Animation for hitting a block; bumping
	
	private PlayerState     state;	// Current state
	
	int timeElapsed;				// Used to limit jumping/sliding time, incremented by delta in update(). Milliseconds
	
	/*
	 * Constructor
	 */
	Player() throws SlickException
	{
		
		timeElapsed = 0;
		
		sheet = new SpriteSheet("res/SpriteSheet.png", 32, 36);
		
		runAni = new Animation();
		jumpAni = new Animation();
		slideAni = new Animation();
		standAni = new Animation();
		
		fallAni = new Animation();
		bumpAni = new Animation();
		
		
		
		state = PlayerState.STANDING;
		
		for (int i = 3; i < 6; i++)							// Initializes the run animation
		{
			runAni.addFrame(sheet.getSprite(i, 0), 100);
		}
		
		standAni.addFrame(sheet.getSprite(0, 1), 200);		// Initializes the standing animation.
															
		for (int i = 1; i < 10; i++)						// Most complex since it is split into 2 rows in the sheet...
		{
			standAni.addFrame(sheet.getSprite(i, 1), 100);
		}
		
		standAni.addFrame(sheet.getSprite(0, 0), 1000);		// Make the last frame long, so the animation won't loop
		
		
		slideAni.addFrame(sheet.getSprite(6, 0), 1);		// Initialize the other animations
		jumpAni.addFrame(sheet.getSprite(7,0), 1);			// Should something else be done since
		bumpAni.addFrame(sheet.getSprite(8,0), 1);			// they are only one frame each?
		fallAni.addFrame(sheet.getSprite(9,0), 1);
		
		slideAni.setAutoUpdate(false);						// Set autoupdate to false for still animations.
		jumpAni.setAutoUpdate(false);
		fallAni.setAutoUpdate(false);
		bumpAni.setAutoUpdate(false);
	}
	
	/*
	 * Accessor for the current player animation
	 * Depends on the player's state
	 */
	public Animation getAni()
	{
		
		if (state == PlayerState.RUNNING)
		{
			return runAni;
		}
		
		if (state == PlayerState.SLIDING)
		{
		
			return slideAni;
		}
		
		if (state == PlayerState.JUMPING)
		{
			return jumpAni;
		}
		
		if (state == PlayerState.STANDING)
		{
			return standAni;
		}
		
		if (state == PlayerState.FALLING)
		{
			return fallAni;
		}
		
		if (state == PlayerState.BUMPED)
		{
			return bumpAni;
		}
		
		else return null;				// else { Computer.implode(); }
		
	}
	
	/*
	 * Mutator for player's state
	 */
	public void setState(PlayerState s)
	
	{
		if(state != s)
		{
			timeElapsed = 0;			// Resets the timer. Needed to function correctly
			state = s;					// when going from jumping to sliding and vice-versa.
										// Keeps player from jumping or sliding infinitely.
		}
		

	}

	/*
	 * Updates the player
	 * Keeps player from jumping or running infinitely.
	 */
	public void update(int delta) 
	{
		
		// Only update if jumping or sliding
		if(state == PlayerState.JUMPING || state == PlayerState.SLIDING)
		{
			timeElapsed += delta;		// Increment timeElapsed in milliseconds
			
			if(timeElapsed >= 320)		// Update two tiles from state change,
			{							// since tiles are shifted every 160 ms.
				timeElapsed = 0;
				state = PlayerState.RUNNING;
			}
		}
		
	}
	
	
	/*
	 * Accessor for player's state
	 */
	public PlayerState getState()
	{
		return state;
	}

	/*
	 * Resets to initial values upon losing
	 */
	public void reset() 
	{	
		timeElapsed = 0;
		state = PlayerState.STANDING;	
	}
}
