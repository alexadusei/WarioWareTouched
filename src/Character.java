//Sub class for the MainScreen. This class takes care for the objects walking around at the bottom of the Main Screen

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class Character
{
	//Declare global variables
	private Random rnd;
	private ImageIcon imgCharacter;
	private int xPos, yPos, width, height, xDir, yDir, charCounter, action, direction;
	private int imageNum, count, counter;
	private boolean touched, selected;
	private String[] name;
	
	int num = 5, num2 = 10;

	/* Declare constructors with the parameter imageNum. imageNum will determine which character is in play in the Main Screen. Their direction
	 * and action is set to -1, a variable that means nothing will happen to the characters while they're in use (to be explained).
	 */
	public Character(int imageNum)
	{
		rnd = new Random();
		direction = -1;
		action = -1;

		this.imageNum = imageNum;
		this.name = new String[]{"wm", "ash"}; //array of strings for the type of character being used in the class.
		this.imgCharacter = new ImageIcon("images\\main\\" + name[imageNum] + "IdleFront" + count + ".png");
		this.width = imgCharacter.getIconWidth();
		this.height = imgCharacter.getIconHeight();
		//the x and y position are set this way to place the character in the centre of the bottom screen
		this.xPos = (WWTProgram.rectWidth2 - this.width)/2;
		this.yPos = (WWTProgram.rectHeight1)  + (WWTProgram.rectHeight2 - this.height)/2 - 20;
		
	}

	// Mutator methods of the class
	public void setX (int x)
	{
		this.xPos = x;
	}

	public void setY (int y)
	{
		this.yPos = y;
	}

	public void setLocation(int x, int y)
	{
		this.xPos = x;
		this.yPos = y;
	}

	public void setWidth (int w)
	{
		this.width = w;
	}

	public void setHeight (int h)
	{
		this.height = h;
	}

	public void setSize (int w, int h)
	{
		this.width = w;
		this.height = h;
	}

	public void setImage(ImageIcon img)
	{
		this.imgCharacter = img;
	}

	public void setImageNum(int imageNum)
	{
		this.imageNum = imageNum;
	}

	public void setTouched(boolean touched)
	{
		this.touched = touched;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	// Accessor methods of the class
	public int getX()
	{
		return this.xPos;
	}

	public int getY()
	{
		return this.yPos;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public ImageIcon getImage()
	{
		return this.imgCharacter;
	}

	public int getImageNum()
	{
		return this.imageNum;
	}

	public boolean getTouched()
	{
		return this.touched;
	}

	public boolean getSelected()
	{
		return this.selected;
	}

	/* Moves the character in a certain algorithm. If a character is not selected, their movement will be randomized; up, down left, or right. 
	 * Based on their variable, they will move in a certain direction, in another randomized amount in the x axis and a randomized amount in the
	 * y axis. After a certain amount of time, they will stop, and the process will repeat itself
	 */
	public void move()
	{
		charCounter++;

		if (charCounter >= 5)
		{
			if (!selected)
			{
				counter++;
			}
			else
			{
				counter = 0;
				direction = -1;
			}

			if (counter <= 20)
			{
				/* If the direction is -1, the character image will be facing the front, which has further animation. Otherwise, the characyer
				 * will be facing whichever direction the 'direction' variable is set to, which has no further animation.
				 */
				if (direction <= 0)
				{
					this.imgCharacter = new ImageIcon("images\\main\\" + name[imageNum] + "IdleFront" + count + ".png");
				}
				else
				{
					this.imgCharacter = new ImageIcon("images\\main\\" + name[imageNum] + "Idle" + direction + ".png");
				}

				count++;

				if (count > 3)
				{
					count = 0;
				}

				//Changes the width/height to the width/height of the current image in-play
				this.width = imgCharacter.getIconWidth();
				this.height = imgCharacter.getIconHeight();
			}


			// When the counter is standing still has reached 20, the character will make a decision of which direction its going to move in
			if (counter == 20)
			{
				action = rnd.nextInt(4);
			}

			/* When the counter is greater than 20 and less than 20, the character will move in the direction the 'action' variable was set to
			 * (left, right, up, down, etc). And based on that direction, the amount of pixels moved in that direction will be randomized, for 
			 * further flexibility.
			 */
			if (counter >= 20 && counter <= 50)
			{
				if (action == 0)
				{
					direction = action;
					xDir = rnd.nextInt(3) - 1;
					yDir = rnd.nextInt(3) + 1;
					action = -1;
				}
				else if (action == 1)
				{
					direction = action;
					xDir = rnd.nextInt(3) - 1;
					yDir = -(rnd.nextInt(3) + 1);
					action = -1;
				}
				else if (action == 2)
				{
					direction = action;
					xDir = -(rnd.nextInt(4) + 2);
					yDir = rnd.nextInt(5) - 2;
					action = -1;
				}
				else if (action == 3)
				{
					direction = action;
					xDir = rnd.nextInt(4) + 2;
					yDir = rnd.nextInt(5) - 2;
					action = -1;
				}

				this.imgCharacter = new ImageIcon("images\\main\\" + name[imageNum] + direction + "_" + count + ".png");

				count++;

				if (count > 3)
				{
					count = 0;
				}

				this.width = imgCharacter.getIconWidth();
				this.height = imgCharacter.getIconHeight();

				xPos += xDir;
				yPos += yDir;

				if (counter >= 50)
				{
					counter = 0;
				}

				detectBearings(); //Private method to detect the walls of the Frame in the lower screen (see below for details).

			}
			
			charCounter = 0;
		}
	}

	//Draws the character on where needed.
	public void drawCharacter (Graphics2D g2)
	{
		g2.drawImage(imgCharacter.getImage(), xPos, yPos, width, height, null);
	}
	
	/* Inner method of the class for finding out where the walls of the lower screen are. When the character hits one of the walls, it will set the
	 * counter to 0. As seen if the if-statements below, nothing will happen unless the counter is greater than 20 or less than 50. So when
	 * the character hits a wall, it will stop its movements and stand still, until the counter reaches 20 again and a new action will be 
	 * randomized. Character will continue standing still until its action moves it away from the wall. This way, the character will not pass
	 * the bounds.
	 */
	private void detectBearings()
	{

		if (yPos <= WWTProgram.bottomY + 10)
		{
			yPos = WWTProgram.bottomY + 10;
			counter = 0;
		}
		else if (yPos + imgCharacter.getIconHeight() >= WWTProgram.FRAME_HEIGHT - 60)
		{
			yPos = WWTProgram.FRAME_HEIGHT - imgCharacter.getIconHeight() - 60;
			counter = 0;
		}

		if (xPos <= 7)
		{
			xPos = 7;
			counter = 0;
		}
		else if (xPos + imgCharacter.getIconWidth() >= WWTProgram.FRAME_WIDTH - 15)
		{
			xPos = WWTProgram.FRAME_WIDTH - imgCharacter.getIconWidth() - 15;
			counter = 0;
		}
	}
}