package megaRun;

import megaRun.Player.PlayerState;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.AppGameContainer;


public class MainGame extends BasicGame {
	
	public enum GameState		// Possible states while playing the game
	{							// Paused?
		INTRO, PLAYING, LOST 
	}

	Player player;				// Main instantiation of the Player class
	Background background;		// Main instantiation of the Background class
	
	int score;					// Do you really need a comment?
	int high;					// Highest score
	
	int textY;
	int[] textX;
	int timeElapsed;			// 

	GameState state;

    public MainGame() {
        super("MegaRun");
    }
    
    @Override
    public void init(GameContainer container) throws SlickException 
    {
    	player = new Player();
    	background = new Background();
    	
    	score = 0;
    	timeElapsed = 0;
    	
    	// Centers text so I won't go crazy..
    	textY = 16 - container.getGraphics().getFont().getHeight("Score")/2;
    	
    	textX = new int[6];
    	textX[0] = (32*19)/8- container.getGraphics().getFont().getWidth("Score: 1000")/2;
    	textX[1] = (32*19)*7/8 - container.getGraphics().getFont().getWidth("High Score: 10000")/2 - 4;
    	textX[2] = (32*19)/2 - container.getGraphics().getFont().getWidth("Ready")/2;
    	textX[3] = (32*19)/2 - container.getGraphics().getFont().getWidth("Set")/2;
    	textX[4] = (32*19)/2 - container.getGraphics().getFont().getWidth("GO!")/2;
    	textX[5] = (32*19)/2 - container.getGraphics().getFont().getWidth("Press Enter to Play")/2;
    		
  	
    	state = GameState.INTRO;

    }

    @Override
    public void update(GameContainer container, int delta)
            throws SlickException 
    {
  
    	Input input = container.getInput();
    	
    	if(state == GameState.INTRO)
    	{
    		timeElapsed += delta;
    		if (timeElapsed >= 2000)			// Start playing after 3 seconds
    		{
    			timeElapsed = 0;
    			
    			background.startScrolling();
    			player.setState(PlayerState.RUNNING);
    			
    			input.clearKeyPressedRecord();
    			
    			state = GameState.PLAYING;
    		}
    		
    	}
    	
    	else if ( state == GameState.PLAYING)
    	{
    		background.update(delta);
	        player.update(delta);
	        
	        score += delta/10;
    		
	    	if(background.mustJump() && player.getState() != Player.PlayerState.JUMPING)
	    	{
	    		background.stopScrolling();
	    		player.setState(PlayerState.FALLING);
	    		
	    		input.clearKeyPressedRecord();
	    		state = GameState.LOST;
	    	}
	    	
	    	else if(background.mustSlide() && player.getState() != Player.PlayerState.SLIDING)
	    	{
	    		background.stopScrolling();
	    		player.setState(PlayerState.BUMPED);
	    		
	    		input.clearKeyPressedRecord();
	    		state = GameState.LOST;
	    	}
	    	
	 	
	    	if(input.isKeyPressed(Input.KEY_DOWN))
	    	{
	    		player.setState(PlayerState.SLIDING);

	    	}
	    	
	    	if(input.isKeyPressed(Input.KEY_UP))
	    	{
	    		player.setState(PlayerState.JUMPING);

	    	}
    	
    	}
    	
    	else if (state == GameState.LOST)
    	{
    		timeElapsed += delta;
    		

    		if(input.isKeyPressed(Input.KEY_ENTER))
    		{
    			if (score > high)
    			{
    				high = score;
    			}

    			// In place of container.reinit(), so SpriteSheets aren't reloaded
    			input.clearKeyPressedRecord();
    			score = 0;
    			timeElapsed = 0;
    			state = GameState.INTRO;
    			player.reset();
    			background.reset();
    		}
    	}
    	
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException
    {
    	
    	if(state == GameState.INTRO)
    	{
    		if (timeElapsed < 1000)
    		{
    			g.drawString("Ready", textX[2], textY);
    		}
    		
    		else
    		{
    			g.drawString("Set", textX[3], textY);
    		}
    		
    		g.drawImage(background.getImage(), 0, 32);
	    	g.drawAnimation(player.getAni(), 32, 62);
	    	
	    	g.drawString("Score: " + score, textX[0], textY);
	    	g.drawString("High Score: " + high, textX[1], textY);
    	}
    	
    	else if (state == GameState.PLAYING)
    	{
	    	g.drawImage(background.getImage(), 0, 32);
	    	g.drawAnimation(player.getAni(), 32, 62);
	    	
	    	g.drawString("Score: " + score, textX[0], textY);
	    	g.drawString("High Score: " + high, textX[1], textY);
	    	g.drawString("GO!", textX[4], textY);
    	}
    	
    	else 
    	{
    		g.drawImage(background.getImage(), 0, 32);
	    	g.drawString("Score: " + score, textX[0], textY);
	    	g.drawString("High Score: " + high, textX[1], textY);
	    	
	    	if(timeElapsed < 3000)
	    	{
	    		g.drawString("Press Enter To Play", textX[5], textY);
		    	
		    	if(player.getState() == Player.PlayerState.BUMPED)
		    	{
		    		g.drawAnimation(player.getAni(), 32, 62);
		    	}
		    	
		    	if(player.getState() == Player.PlayerState.FALLING)
		    	{
		    		g.drawAnimation(player.getAni(), 32, 66 + timeElapsed/5);
		    	}
	    	}
	    	
	    	else
	    	{
	    		g.drawString("Press Enter To Play", textX[5], textY);
	    	}
    	}
    }

    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new MainGame());
            app.setShowFPS(false);
            app.setDisplayMode(32*19, 4*32, false);

            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}