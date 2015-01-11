//Goal of this game is to pull a string through all the pins in the alloted time

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;

import java.util.LinkedList;
import java.util.Random;

public class PinGame extends JPanel implements ActionListener
{

	//Static constants of the rectangles placed on the panels to give it the visual effect of a Nintendo DS screen
	public static final Rectangle2D.Double rect1 = new Rectangle2D.Double(WWTProgram.topX, WWTProgram.topY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2);
	public static final Rectangle.Double rect2 = new Rectangle2D.Double(WWTProgram.bottomX, WWTProgram.bottomY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2 - 27);

	//Declare global variables
	private ImageIcon[] imgString, imgPin;
	private ImageIcon imgFireTip, imgString1, imgString2, imgBomb, imgTimeNumber, imgText, imgStart, imgLine;
	private int COUNTER, cutCount, fireTip, onFire, xExplode , yExplode;
	private int xText, yText, textCounter, textPos, amount, xStart, yStart, x, y;
	private int COUNTER_AMOUNT, drawCounter, thread;
	private int[] xValues, yValues, xPin, yPin, pinCounter, disappear;
	private Timer tmrGame, tmrSeconds, tmrText, tmrDraw;
	private boolean pressed, clicked;
	private boolean[] threaded;
	private Random rnd;
	private LinkedList<Rectangle2D.Double> rectangles;
	private Line2D.Double startLine;
	private AffineTransform trans;

	public static boolean win;
	
	public PinGame()
	{
		//Declare timers, x/y values, pictures, and text to their settings
		rnd = new Random();
		
		win = false;

		trans = new AffineTransform();

		tmrText = new Timer(15, this);
		tmrGame = new Timer(75, this);
		tmrSeconds = new Timer(600 + WWTProgram.SECONDS, this);
		tmrDraw = new Timer(1, this);

		rectangles = new LinkedList<Rectangle2D.Double>();

		xValues = new int[]{-5, 3, 2};
		yValues = new int[]{2, -3, 1};

		amount = rnd.nextInt(5) + 1;

		imgPin = new ImageIcon[amount];
		xPin = new int[imgPin.length];
		yPin = new int[imgPin.length];
		pinCounter = new int[imgPin.length];
		threaded = new boolean[imgPin.length];
		disappear = new int[imgPin.length];

		//Sets the location of the pins (randomized x locations, randomized if upside down or rightside up)
		for (int i = 0; i < imgPin.length; i++)
		{
			imgPin[i] = new ImageIcon("images\\game1\\pin" + pinCounter[i] + ".png");
			xPin[i] = 250 - (i * 50);

			if ( i == 1 || i == 3)
			{
				yPin[i] = rnd.nextInt(50) + 250;
			}
			else
			{
				yPin[i] = rnd.nextInt(150) + 450;
			}
		}

		xStart = 350;
		yStart = 400;

		imgText = new ImageIcon("images\\game1\\pinText.png");
		imgStart = new ImageIcon("images\\game1\\drawStart" + drawCounter + ".png");
		imgLine = new ImageIcon("images\\game1\\start.png");

		xText = (WWTProgram.FRAME_WIDTH - imgText.getIconWidth())/2;
		yText = 500;

		imgFireTip = new ImageIcon();
		imgString1 = new ImageIcon();
		imgString2 = new ImageIcon();
		imgBomb = new ImageIcon();
		imgTimeNumber = new ImageIcon();
		imgString = new ImageIcon[6];

		for (int i = 0; i < imgString.length; i++)
		{
			imgString[i] = new ImageIcon();
		}

		setLayout(null);
		addMouseListener(new MouseListener());
		addMouseMotionListener(new MouseMotionListener());
		setVisible(true);

		tmrGame.start();
		tmrSeconds.start();
		tmrText.start();
		tmrDraw.start();
	}

	public void actionPerformed(ActionEvent e)
	{
		//Animation for the text
		if (e.getSource() == tmrText)
		{
			if (textCounter >= 5)
			{
				yText -= 4;

				if (yText <= 270)
				{
					yText = 270;
				}
			}
		}
		//Sets the position of the rectangle objects when the user draws a line
		else if (e.getSource() == tmrDraw)
		{
			if (pressed)
			{
				rectangles.add(new Rectangle2D.Double(x, y, 3, 3));
			}
			else
			{
				rectangles.clear();
			}
		}
		
		/* Checks if the line of the mouse has passed the head of the pin. If it did, the pin will go under a short animation and disappear
		 * the pin counter will go up. If the pin counter equals the amount of pins in the game, the user wins
		 */
		else if (e.getSource() == tmrGame)
		{
			for (int i = 0; i < imgPin.length; i++)
			{
				if (threaded[i])
				{
					imgPin[i] = new ImageIcon("images\\game1\\pin" + pinCounter[i] + ".png");
					
					pinCounter[i]++;
					
					if (pinCounter[i] >= 4)
					{
						pinCounter[i] = 1;
						disappear[i]++;
						
						if (disappear[i] >= 3)
						{
							xPin[i] = 600;
							thread++;
							
							if (thread >= imgPin.length)
							{
								win = true;
								//System.out.println(thread);
							//	ReadyScreen.win = true;
							}
						}
					}
				}
			}
			
			imgStart = new ImageIcon("images\\game1\\drawStart" + drawCounter + ".png");

			drawCounter++;

			if (drawCounter >= 4)
			{
				drawCounter = 0;
			}

			textCounter++;

			xText += xValues[textPos];
			yText += yValues[textPos];

			textPos ++;

			if (textPos >= 3)
			{
				textPos = 0;
			}

			repaint();
		}
		
		/* This is the counter for the timer in the game. This timer is in every minigame. After a certain amount of time, this timer will 
		 * appear as a bomb connected to a flaming string. The string will continuously get smaller, until it reaches the bomb and explodes. If
		 * this happens and the conditions to the minigame is not met, the user will lose.
		 */
		else
		{
			COUNTER++;

			if (COUNTER >= COUNTER_AMOUNT)
			{
				cutCount++;
			}

			if (COUNTER >= COUNTER_AMOUNT && cutCount == 1)
			{
				imgFireTip = new ImageIcon("images\\game\\fire.png");

				for (int i = 0; i < imgString.length; i++)
				{
					imgString[i] = new ImageIcon("images\\game\\timerString.png");
				}

				imgString1 = new ImageIcon("images\\game\\timerString1.png");
				imgString2 = new ImageIcon("images\\game\\timerString2.png");
				imgBomb = new ImageIcon("images\\game\\timerBomb2.png");

				imgTimeNumber = new ImageIcon();

			}
			else if (cutCount > 0 && cutCount <= 6)
			{
				imgString[cutCount - 2] = new ImageIcon();
				fireTip += 54;

				if (cutCount == 6)
				{
					imgTimeNumber = new ImageIcon("images\\game\\time3.png");
				}

			}
			else if (cutCount == 7)
			{
				imgString[cutCount - 2] = new ImageIcon();
				imgFireTip = new ImageIcon();
				imgString1 = new ImageIcon("images\\game\\timerFire2.png");
				imgTimeNumber = new ImageIcon("images\\game\\time2.png");
			}
			else if (cutCount == 8)
			{
				imgString1 = new ImageIcon();
				imgString2 = new ImageIcon("images\\game\\timerFire3.png");
				onFire = 14;
				imgTimeNumber = new ImageIcon("images\\game\\time1.png");
			}
			else if (cutCount == 9)
			{
				imgString2 = new ImageIcon();
				imgTimeNumber = new ImageIcon();
				imgBomb = new ImageIcon("images\\game\\timerExplode.png");
				xExplode = -3;
				yExplode = 28;
			}
			else if (cutCount == 10)
			{
				if (!ReadyScreen.gameover)
				{
					PlayScreen.topPanel.setBounds(0, 0, 436, 647);
					tmrGame.stop();
					ReadyScreen.tmrReturn.start();
				}
			}
		}
	}

	/* Allows the user to draw lines on the screen. The user has to start from the start-point and continue drawing. If the user lefts go before
	 * he/she wins, the line will disappear and they will have to start over.
	 */
	class MouseListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			if (e.getX() >= xStart && e.getX() <= xStart + imgStart.getIconWidth() &&
					e.getY() >= yStart && e.getY() <= yStart + imgStart.getIconHeight())
			{
				startLine = new Line2D.Double(359, 408, e.getX(), e.getY());
				clicked = true;
				pressed = true;

				xStart = 600;
			}
		}

		public void mouseReleased(MouseEvent e)
		{
			xStart = 350;

			pressed = false;
			rectangles.clear();
		}
	}

	//Checks if the drawn line is entering the head of the pins
	class MouseMotionListener extends MouseMotionAdapter
	{
		public void mouseDragged(MouseEvent e)
		{
			if (e.getX() >= xStart && e.getX() <= xStart + imgStart.getIconWidth() &&
					e.getY() >= yStart && e.getY() <= yStart + imgStart.getIconHeight())
			{
				pressed = true;

			}

			for (int i = 0; i < imgPin.length; i++)
			{
				if (i == 1 || i == 3)
				{
					if (e.getX() >= xPin[i] && e.getX() <= xPin[i] + imgPin[i].getIconWidth() &&
							e.getY() >= yPin[i] + imgPin[i].getIconHeight() - 36 && e.getY() <= yPin[i] + imgPin[i].getIconHeight() && pressed)
					{
						threaded[i] = true;
					}
				}
				else
				{
					if (e.getX() >= xPin[i] && e.getX() <= xPin[i] + imgPin[i].getIconWidth() &&
							e.getY() >= yPin[i] && e.getY() <= yPin[i] + 36 && pressed)
					{
						threaded[i] = true;
					}
				}
			}

			if (pressed)
			{
				x = e.getX();
				y = e.getY();
			}
		} 
	}

	//Draws certain images on the screen
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(Color.WHITE);
		g2.fill(new Rectangle2D.Double(WWTProgram.bottomX, WWTProgram.bottomY, 426, 310));

		for (int i = 0; i < imgPin.length; i++)
		{
			if (i == 1 || i == 3)
			{
				trans.setToTranslation(xPin[i],  yPin[i]);
				trans.rotate(Math.toRadians(180), imgPin[i].getIconWidth()/2, imgPin[i].getIconHeight()/2);
				g2.drawImage(imgPin[i].getImage(), trans, null);
			}
			else
			{
				g2.drawImage(imgPin[i].getImage(), xPin[i], yPin[i], null);
			}
		}
		
		g2.drawImage(new ImageIcon("images\\game\\top0.png").getImage(), WWTProgram.topX, WWTProgram.topY, null);
		g2.drawImage(new ImageIcon("images\\game1\\pinBackground.png").getImage(), WWTProgram.bottomX , WWTProgram.bottomY + 1, null);
		g2.drawImage(imgLine.getImage(), 359, 408, null);

		g2.setColor(Color.RED);

		if (clicked)
		{
			g2.setStroke(new BasicStroke(3));
			g2.draw(startLine);
		}

		g2.drawImage(imgStart.getImage(), xStart, yStart, null);

		if (pressed)
		{
			for (int i = 0; i < rectangles.size(); i++)
			{
				g2.fill(rectangles.get(i));
			}
		}

		g2.drawImage(imgFireTip.getImage(), 392 - 15 - fireTip, 623, null); // fireTip = 12

		for (int i = 0; i < imgString.length; i++)
		{
			g2.drawImage(imgString[i].getImage(), 339 - (54 * i) - 15, 635, null);
		}

		g2.drawImage(imgString1.getImage(), 58 - 15, 616, null);
		g2.drawImage(imgString2.getImage(), 37 - 15, 601 - onFire, null); // onFire = 14
		g2.drawImage(imgBomb.getImage(), 17 - 15 - xExplode, 612 - yExplode, null);
		g2.drawImage(imgTimeNumber.getImage(), 28, 577, null);

		g2.drawImage(imgText.getImage(), xText, yText, null);

		g2.setStroke(new BasicStroke(7));
		g2.setColor(new Color (25, 25, 25));
		g2.draw(rect1);
		g2.draw(rect2);
	}
	
	public static boolean getWin(){
		return win;
	}
}