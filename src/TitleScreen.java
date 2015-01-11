import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.ArrayList;

public class TitleScreen extends JPanel implements ActionListener
{

	/* Static constants of the rectangles placed on the panels to give it the visual effect of a Nintendo DS screen (top screen and
	 * bottom screen). The final static constant is a rectangle covering the whole JPanel for a 'fading' effect when the user clicks
	 * another button to bring them to another JPanel. THESE ARE IN ALL JPANELS
	 */
	public static final Rectangle2D.Double rect1 = new Rectangle2D.Double(WWTProgram.topX, WWTProgram.topY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2);

	public static final Rectangle.Double rect2 = new Rectangle2D.Double(WWTProgram.bottomX, WWTProgram.bottomY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2 - 27);

	public static final Rectangle.Double fadeRect = new Rectangle2D.Double(WWTProgram.topX, WWTProgram.topY, 
			WWTProgram.FRAME_WIDTH, WWTProgram.FRAME_HEIGHT);

	
	//Declare global variables and static variables that aren't constants
	public static int mouseX, mouseY;

	private ImageIcon imgTitle, imgStartButton;
	private Timer tmrTitle;
	private int yLogo, yStartButton, xStartButton, sbPacer, sb, waitCounter, fadeTrans;
	private boolean held, start;
	private ArrayList<TitleObject> objects;

	private final int maxHeight;

	private TitleObject object;

	public TitleScreen()
	{

		//Declare ArrayList of object(onion, ball, etc) objects on the screen
		objects = new ArrayList<TitleObject>();

		for (int i = 0; i < 4; i++)
		{
			objects.add(new TitleObject(i)); //Set the objects to certain things (ball, star, birdnest, etc)
		}

		//Declare 'nose' object, which operates completely differently from the previous ArrayList of objects
		object = new TitleObject(new ImageIcon("images\\title\\bigNose0.png"));

		//Declare and start the timer
		tmrTitle = new Timer(25, this);
		tmrTitle.start();

		//Declare images, x positions, y positions for certain images and components
		imgTitle = new ImageIcon("images\\title\\WWT_Logo.png");
		imgStartButton = new ImageIcon("images\\title\\startButton0.png");

		yLogo = WWTProgram.FRAME_HEIGHT;
		yStartButton = -imgStartButton.getIconHeight();
		xStartButton = (WWTProgram.FRAME_WIDTH - imgStartButton.getIconWidth()) / 2;
		maxHeight = (WWTProgram.FRAME_HEIGHT/2 - imgTitle.getIconHeight() - 25) / 2;

		//Set details of JPanel
		setLayout(null);
		addMouseListener(new MouseListener());
		addMouseMotionListener(new MouseMotionListener());
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		// Timer for all things involved in the Title Screen. This timer is responsible for changing the x/y components of objects/images
		if (e.getSource() == tmrTitle)
		{
			//Set positional integers in place
			yLogo -= 10;
			yStartButton += 15;
			waitCounter++;
			
			// Allow objects to enter the screen sequentially after a given amount of time.
			if (waitCounter >= 100)
			{
				object.drag();
			}

			if (waitCounter >= 150)
			{
				objects.get(0).move();
			}
			if (waitCounter >= 150 * 2)
			{
				objects.get(1).move();
			}
			if (waitCounter >= 150 * 3)
			{
				objects.get(2).move();
			}
			if (waitCounter >= 150 * 4)
			{
				objects.get(3).move();
			}

			//Raises the logo from the bottom
			if (yLogo <= maxHeight)
			{
				yLogo = maxHeight;
			}

			//Drops the Start Button from the top
			if (yStartButton >= 600)
			{
				yStartButton = 600;
			}

			sbPacer++;

			/* Animation for the start button, which has two images. Timer interval is too quick for the animation to flow smoothly, so a
			 * space bar (sb) counter is set to a certain value when the image will continuously change.
			 */
			if (!held)
			{
				if (sbPacer >= 5)
				{
					if (sb == 0)
					{
						sb = 1;
					}
					else
					{
						sb = 0;
					}

					sbPacer = 0;
				}

				imgStartButton = new ImageIcon("images\\title\\startButton" + sb + ".png");
			}

			/* When the user starts, the 'start' boolean is true. When it is true, continuously add to a variable called 'fadeTrans'. This 
			 * variable draws a black rectangle over the frame, giving it a 'fade out' effect. When its maximized at 255, the JPanel
			 * on the frame will change to the Main Screen. The JFrame will also validate to process the information and the timer will
			 * stop to prevent any further complications.
			 */
			
			if (start)
			{
				fadeTrans += 7;

				if (fadeTrans >= 255)
				{
					fadeTrans = 255;
					WWTProgram.frame.setContentPane(new MainScreen());
					WWTProgram.frame.validate();
					tmrTitle.stop();
				}
			}

			repaint();
		}
	}

	class MouseListener extends MouseAdapter
	{
		/* When the user simply presses down on any of the components (start button or the objects), certain things will happen. The start 
		 * button will stop changing its image and will move down slightly, the 'held' boolean will also be set to true, allowing the program
		 * to know it has been pressed upon. Also, the objects will set is method boolean to true, which will set its location to the mouse's
		 * x and y positions, and will stop its 'move' algorithm.
		 */
		public void mousePressed(MouseEvent e)
		{
			//If-statement for the start button.
			if (e.getY() >= yStartButton && e.getY() <= yStartButton + imgStartButton.getIconHeight() &&
					e.getX() >= xStartButton && e.getX() <= xStartButton + imgStartButton.getIconWidth())
			{
				held = true;
				imgStartButton = new ImageIcon("images\\title\\startButton1.png");
			}

			/* If-statement for the nose object. When the nose is clicked, it will shake violently in place, something different 
			 * from the other objects
			 */
			if (e.getY() >= object.getY() && e.getY() <= object.getY() + object.getHeight() &&
					e.getX() >= object.getX() && e.getX() <= object.getX() + object.getWidth())
			{
				object.setTouched(true);
			}

			//If-statement for the other objects.
			for (int i = 0; i < objects.size(); i++)
			{
				if (e.getY() >= objects.get(i).getY() && e.getY() <= objects.get(i).getY() + objects.get(i).getHeight() &&
						e.getX() >= objects.get(i).getX() && e.getX() <= objects.get(i).getX() + objects.get(i).getWidth())
				{
					objects.get(i).setTouched(true);
				}
			}
		}

		/* When the mouse is released off of the boundaries of the start button, the 'held' boolean will be set back to false. Also, if
		 * the mouse was within the boundaries of the start button, the 'start' boolean will be set to true. Which will allow the variable
		 * 'fadeTrans' to continuously increase to allow the JPanel to fade out, bringing the user to the next screen. All other instance of
		 * objects will continue to do their job until the screen is completely black, where the timer stops.
		 */
		public void mouseReleased(MouseEvent e)
		{
			//If-statement for the start button
			if (e.getY() >= yStartButton && e.getY() <= yStartButton + imgStartButton.getIconHeight() &&
					e.getX() >= xStartButton && e.getX() <= xStartButton + imgStartButton.getIconWidth())
			{
				held = false;
				start = true;
			}

			//If statement for the objects. If the objects are released from the mouse, they will continue their moving algorithms once again
			if (e.getY() >= object.getY() && e.getY() <= object.getY() + object.getHeight() &&
					e.getX() >= object.getX() && e.getX() <= object.getX() + object.getWidth())
			{
				object.setTouched(false);
			}

			for (int i = 0; i < objects.size(); i++)
			{
				if (e.getY() >= objects.get(i).getY() && e.getY() <= objects.get(i).getY() + objects.get(i).getHeight() &&
						e.getX() >= objects.get(i).getX() && e.getX() <= objects.get(i).getX() + objects.get(i).getWidth())
				{
					objects.get(i).setTouched(false);
				}
			}
		}
	}

	class MouseMotionListener extends MouseMotionAdapter
	{
		/* When the user drags the mouse while clicking, certain things will happen, once again. If the user clicks the start button, the 
		 * picture will not move and will be 'pressed' down, as it would seem. Once the user moves the mouse off of the start button,
		 * its boolean will be reset and it will go back to its old algorithm of moving slightly and will seem 'untouched' as before. 
		 * The objects, on the other hand, will continue to follow the location of the mouse, wherever it goes. The ball and star can be used
		 * to draw on the title screen with this feature. The mouses x and y will be set to static variables for the program to use in other 
		 * classes
		 */
		public void mouseDragged(MouseEvent e)
		{
			if (object.getTouched() == true)
			{
				if (e.getY() >= object.getY() && e.getY() <= object.getY() + object.getHeight() &&
						e.getX() >= object.getX() && e.getX() <= object.getX() + object.getWidth())
				{}
				else
				{
					object.setTouched(false);
				}
			}

			mouseX = e.getX();
			mouseY = e.getY();
		}
		//Mouses components will be saved to static variables for simply moving the mouse, whether its being dragged or not
		public void mouseMoved(MouseEvent e)
		{
			mouseX = e.getX();
			mouseY = e.getY();
		} 
	}

	/* Paint method draws certain images to the screen. Objects are drawn on this screen from the ArrayList in a particular order so the
	 * drawing of the ball and star are last, so they do not overlap the other objects and titles being drawn.
	 */
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setStroke(new BasicStroke(7));

		g2.drawImage(new ImageIcon("images\\title\\background.png").getImage(), WWTProgram.topX, WWTProgram.topY, null);
		g2.drawImage(new ImageIcon("images\\title\\background.png").getImage(), WWTProgram.bottomX, WWTProgram.bottomY, null);

		g2.drawImage(imgTitle.getImage(), (WWTProgram.FRAME_WIDTH - imgTitle.getIconWidth()) / 2, yLogo, null);
		g2.drawImage(imgStartButton.getImage(), xStartButton, yStartButton, null);

		objects.get(1).drawObject(g2);
		objects.get(2).drawObject(g2);
		objects.get(3).drawObject(g2);
		objects.get(0).drawObject(g2);

		object.drawObject(g2);

		g2.setColor(new Color (25, 25, 25));
		g2.draw(rect1);
		g2.draw(rect2);

		g2.setColor(new Color(0, 0, 0, fadeTrans));
		g2.fill(fadeRect);
	}
}