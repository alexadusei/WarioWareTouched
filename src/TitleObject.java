//Sub class for the TitleScreen. This class takes care of all the Objects involved in the main screen

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;

public class TitleObject
{
	//Declare Global variables
	private Random rnd;
	private ImageIcon imgObject;
	private int xPos, yPos, width, height, side, xDir, yDir;
	private int imageNum, count, counter, moveCounter, red, green, blue;
	private int  transparency, posCount;
	private String noseName;
	private boolean entered, inside, touched;
	private LinkedList<Ellipse2D.Double> circles;
	private int[] xValues, yValues;

	private final int FROM_LEFT = 0;
	private final int FROM_RIGHT = 1;

	/* Constructor for the objects. Two constructors for the group of objects and the 'nose' object, which have completely different tasks
	 * While the following constructor is for the ArrayList of objects, the constructor after this is for the nose object alone. This 
	 * constructor holds a parameter of an integer called imageNum. imageNum deals with numbers from 0 through 3. The number determines which
	 * image will appear on the screen ( 0 = onion, 1 = ball, etc). Colour variables are set for the trail colours of the ball and star, a
	 * LinkedList of circles is declared for drawing the trail, a variable 'side' is randomized to determine which side the objects will
	 * come from.
	 */
	public TitleObject(int imageNum)
	{
		rnd = new Random();

		this.imageNum = imageNum;

		red = 255;
		green = 255;
		blue = 0;

		xDir = 5;
		yDir = 4;
		posCount = 0;
		transparency = 255;

		circles = new LinkedList<Ellipse2D.Double>();

		this.imgObject = new ImageIcon("images\\title\\object" + imageNum + "_" + count + ".png");
		this.width = imgObject.getIconWidth();
		this.height = imgObject.getIconHeight();
		this.yPos = rnd.nextInt(550) + 50;

		side = rnd.nextInt(2);

		if (side == FROM_LEFT)
		{
			this.xPos = -width;
		}
		else if (side == FROM_RIGHT)
		{
			this.xPos = WWTProgram.FRAME_WIDTH;
		}
	}

	/* Object constructor for the big nose. Takes an image as a parameter. Sides are randomized from which side it comes from at the start of
	 * program (left or right). An array of x and y locations are set for the 'shaking' of the nose when it's clicked/held.
	 */
	public TitleObject(ImageIcon imgObject)
	{
		rnd = new Random();
		xDir = 1;
		yDir = 1;

		xValues = new int[]{-5, 3, 2};
		yValues = new int[]{2, -5, 3};

		this.noseName = "";
		this.imgObject = imgObject;
		this.width = imgObject.getIconWidth();
		this.height = imgObject.getIconHeight();
		this.yPos = rnd.nextInt(520) + 50;

		side = rnd.nextInt(2);

		if (side == FROM_LEFT)
		{
			this.xPos = -width;
		}
		else if (side == FROM_RIGHT)
		{
			this.xPos = WWTProgram.FRAME_WIDTH;
		}
	}

	// Mutator methods for all the objects.
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
		this.imgObject = img;
	}

	public void setImageNum(int imageNum)
	{
		this.imageNum = imageNum;
	}

	public void setNoseName(String noseName)
	{
		this.noseName = noseName;
	}

	public void setTouched(boolean touched)
	{
		this.touched = touched;
	}

	public void setTransparency(int transparency)
	{
		this.transparency = transparency;
	}
	
	//Accecssor methods for all the objects.
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
		return this.imgObject;
	}

	public int getImageNum()
	{
		return this.imageNum;
	}

	public String getNoseName()
	{
		return this.noseName;
	}

	public boolean getTouched()
	{
		return this.touched;
	}

	public int getTransparency()
	{
		return this.transparency;
	}

	/* Moves the objects throughout the screen with its moving algorithm. Also changes the picture of the objects with its respective
	 * imageNum variable. Objects have different number of pictures, however. So based on its imageNum, the value of loops will 
	 * be determined by the image Number.
	 */
	public void move()
	{		
		if (imageNum > 0 && imageNum < 3) //Green ball, yellow star have 3 images
		{
			counter++;

			if (counter >= 3)
			{

				imgObject = new ImageIcon("images\\title\\object" + imageNum + "_" + count + ".png");

				count++;

				if (count > 2)
				{
					count = 0;
				}

				counter = 0;
			}
		}
		else if (imageNum == 3) //Bird nest has 2 images
		{
			counter++;

			if (counter >= 10)
			{
				count++;
				counter = 0;

				if (count > 1)
				{
					count = 0;
				}

				imgObject = new ImageIcon("images\\title\\object" + imageNum + "_" + count + ".png");
			}
		}
		else //Onion has a different sort of animation (blinking, not continuous image changing). Images change through an uneven timer
		{
			counter++;

			if (counter == 15)
			{
				count++;

				imgObject = new ImageIcon("images\\title\\object" + imageNum + "_" + count + ".png");
			}
			else if (counter >= 15 + 2)
			{
				counter = 0;
				count = 0;

				imgObject = new ImageIcon("images\\title\\object" + imageNum + "_" + count + ".png");
			}
		}

		/* Program continuously changes the width and height of the images due to the images changing constantly, which also have different
		 * widths/heights from its previous image.
		 */
		this.width = imgObject.getIconWidth();
		this.height = imgObject.getIconHeight();

		if (!touched)
		{
			detectBearings(); //Private method for detecting the walls of the frame (see below for details)

			moveCounter ++;

			/* Moving algorithm for the objects. Meant to add variation other than just bouncing on the screen at a constant speed. This only
			 * takes place if the objects are not clicked upon.
			 */
			if (moveCounter >= 10)
			{
				if (xDir > 0)
				{
					xDir--;
				}
				else if (xDir > 0)
				{
					xDir++;
				}
				if (yDir > 0)
				{
					yDir--;
				}
				else if (yDir > 0)
				{
					yDir++;
				}
				moveCounter = 0;
			}

			if (xDir == 0)
			{
				xDir = 10;
			}
			if (yDir == 0)
			{
				yDir = 8;
			}

			this.xPos += xDir;
			this.yPos += yDir;
		}
		// If the objects are being clicked, set their x and y to the x/y position of the statc variables for the mouse.
		else
		{	
			this.xPos = TitleScreen.mouseX - width/2;
			this.yPos = TitleScreen.mouseY - height/2;

			detectBearings(); // Regardless of whether the objects are clicked or not, still check for the walls of the frame
		}
		/* If the imageNumber is 1 or 2 (the green ball or yellow star), continuously add circle objects to the LinkedList. If there are
		 * over 100 circle objects already made, delete the first one in the LinkedList so the program will not slowdown and will continue
		 * smoothly.
		 */
		if (imageNum == 1 || imageNum == 2)
		{		
			if (circles.size() >= 100)
			{
				circles.removeFirst();
			}

			if (imageNum == 1)
			{
				circles.add(new Ellipse2D.Double(xPos + 3, yPos + 3, width / 1.20, height / 1.20));
			}
			else
			{
				circles.add(new Ellipse2D.Double(xPos + 8, yPos + 8, width / 1.70, height / 1.70));
			}
		}

	}

	//Drag method is used for the nose, which travels at a much different algorithm than the other objects. Animation is also different
	public void drag()
	{
		if (!touched)
		{
			counter++;

			if (counter == 15)
			{
				count++;

				imgObject = new ImageIcon("images\\title\\bigNose" + noseName + count + ".png");
			}
			else if (counter >= 15 + 2)
			{
				counter = 0;
				count = 0;

				imgObject = new ImageIcon("images\\title\\bigNose" + noseName + count + ".png");
			}

			//Set the width/height to the new picture
			this.width = imgObject.getIconWidth();
			this.height = imgObject.getIconHeight();

			detectBearings(); //Still use the private method to check for walls

			this.xPos += xDir;
			this.yPos += yDir;
		}
		/* If the nose is clicked, add the xValues and yValues to the current x and y position of the nose to give it a 'violent shaking' effect
		 * The nose will cease to move while this is happening.
		 */
		else
		{
			xPos += xValues[posCount];
			yPos += yValues[posCount];

			posCount++;

			if (posCount >= 3)
			{
				posCount = 0;
			}
		}
	}

	//Draw the objects to whichever screen they must be drawn on.
	public void drawObject (Graphics2D g2)
	{
		/* If the objects in play are either the ball or the star, set the colour to whichever is being used (green for ball, yellow for star)
		 * and begin to draw the circles that were added to the LinkedList earlier in the class. Add a small bit of transparency to give it a
		 * 'painting' effect, as if they're smearing the JPanel with paint. LinkedList of circle objects will be drawn before the actual object
		 * to ensure it is behind the object.
		 */
		if (imageNum == 1 || imageNum == 2)
		{
			if (imageNum == 1)
			{
				g2.setColor(new Color(0, 255, 0, 200));
			}
			else
			{
				g2.setColor(new Color(red, green, blue, 200));
			}

			for (int i = 0; i < circles.size(); i++)
			{
				g2.fill(circles.get(i));
			}
		}

		g2.drawImage(imgObject.getImage(), xPos, yPos, width, height, null);
	}

	/* Inner method for this class to check for walls. If the object hits the left, right, top or bottom sides of the JFrame, the pixels 
	 * being added to the x and y positions will be reversed. This will be applied even if they are being dragged by the mouse. So if 
	 * an object is being slid to the edge of the screen by a mouse, it will be bounced off and no longer under the control of the mouse
	 */
	private void detectBearings()
	{
		if (!entered)
		{
			if (side == FROM_RIGHT)
			{
				xDir = -xDir;
			}

			if (yPos >= WWTProgram.FRAME_HEIGHT/2)
			{
				yDir = -yDir;
			}
			entered = true;
		}

		if (xPos >= 20 && xPos + width <= WWTProgram.FRAME_WIDTH - 20)
		{
			inside = true;
		}

		if (inside)
		{
			if (yPos <= 15)
			{
				yPos = 15;
				yDir = -yDir;
				touched = false;
			}
			else if (yPos + imgObject.getIconHeight() >= WWTProgram.FRAME_HEIGHT - 30)
			{
				yPos = WWTProgram.FRAME_HEIGHT - 30 - imgObject.getIconHeight();
				yDir = -yDir;
				touched = false;
			}

			if (xPos <= 7)
			{
				xPos = 7;
				xDir = -xDir;
				touched = false;
			}
			else if (xPos + imgObject.getIconWidth() >= WWTProgram.FRAME_WIDTH - 15)
			{
				xPos = WWTProgram.FRAME_WIDTH - 15 - imgObject.getIconWidth();
				xDir = -xDir;
				touched = false;
			}
		}
	}
}