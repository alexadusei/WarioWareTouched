//Goal of this game is to burst all bubbles on bubble-wrap in alloted time.

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;

import java.util.Random;

public class BubbleGame extends JPanel implements ActionListener
{

	//Static constants of the rectangles placed on the panels to give it the visual effect of a Nintendo DS screen
	public static final Rectangle2D.Double rect1 = new Rectangle2D.Double(WWTProgram.topX, WWTProgram.topY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2);
	public static final Rectangle.Double rect2 = new Rectangle2D.Double(WWTProgram.bottomX, WWTProgram.bottomY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2 - 27);

	//Declare global variables
	private ImageIcon[] imgString;
	private ImageIcon[][] imgBubbles;
	private int[][] xBubble, yBubble;
	private ImageIcon imgFireTip, imgString1, imgString2, imgBomb, imgTimeNumber, imgText;
	private int COUNTER, cutCount, fireTip, onFire, xExplode , yExplode;
	private int xText, yText, textCounter, textPos, generate, popped, amount;
	private int COUNTER_AMOUNT;
	private int[] xValues, yValues;
	private Timer tmrGame, tmrSeconds, tmrText;
	private Random rnd;
	private boolean[][] fullBubble;
	private static boolean win;

	public BubbleGame()
	{
		//Declare timers, x/y values, pictures, and text to their settings
		win = false;
		
		tmrText = new Timer(15, this);
		tmrGame = new Timer(75, this);
		tmrSeconds = new Timer(500 + WWTProgram.SECONDS, this);

		rnd = new Random();
		
		amount = rnd.nextInt(7) + 1;

		xValues = new int[]{-5, 3, 2};
		yValues = new int[]{2, -3, 1};

		imgText = new ImageIcon("images\\game\\bubbleText.png");

		xText = (WWTProgram.FRAME_WIDTH - imgText.getIconWidth())/2;
		yText = 500;

		//The two-dimensional array places all the bubbles on the bottom screen, along with the 2D-array of x/y positions and booleans
		imgBubbles = new ImageIcon[5][9];
		xBubble = new int[5][9];
		yBubble = new int[5][9];
		fullBubble = new boolean[5][9];

		xBubble[0][0] = 15;
		yBubble[0][0] = 380;

		/* Place all popped bubbles on bottom screen. A randomizer will randomize from 1 to 100, if the randomizer is under 20, a boolean
		 * will be set to true and an unpopped bubble will be set at that location. Randomizer set to have a 20% chance of unpopped bubbles
		 * placed on the bottom-screen.
		 */
		for (int i = 0; i < imgBubbles.length; i++)
		{
			for (int k = 0; k < imgBubbles[i].length; k++)
			{
				generate = rnd.nextInt(100) + 1;
				
				if (generate <= 20 && popped <= amount)
				{
					imgBubbles[i][k] = new ImageIcon("images\\game\\fullBubble.png");
					fullBubble[i][k] = true;
					popped++;		
				}
				else
				{
					imgBubbles[i][k] = new ImageIcon("images\\game\\squishedBubble" + rnd.nextInt(6) + ".png");
				}
				xBubble[i][k] = xBubble[0][0] + (k * 45);
				yBubble[i][k] = yBubble[0][0] + (i * 45) ;
			}
		}

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
		setVisible(true);

		tmrGame.start();
		tmrSeconds.start();
		tmrText.start();
	}

	public void actionPerformed(ActionEvent e)
	{
		//Animate the text
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
		else if (e.getSource() == tmrGame)
		{
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

	
	/* Check if the mouse is within the boundaries of a bubble. Then checks if the bubble the mouse is in is set to an 'unpopped' bubble.
	 * If this is true, the bubble will be popped and a bubble counter, which is initially set to how many unpopped bubble are in the screen,
	 * will continually decrease. When this amount reaches 0, the game is won.
	 */
	class MouseListener extends MouseAdapter
	{
		public void mouseReleased(MouseEvent e)
		{
			for (int i = 0; i < imgBubbles.length; i++)
			{
				for (int k = 0; k < imgBubbles[i].length; k++)
				{
					if (e.getX() >= xBubble[i][k] && e.getX() <= xBubble[i][k] + imgBubbles[i][k].getIconWidth() &&
							e.getY() >= yBubble[i][k] && e.getY() <= yBubble[i][k] + imgBubbles[i][k].getIconHeight() &&
									fullBubble[i][k])
					{
						imgBubbles[i][k] = new ImageIcon("images\\game\\squishedBubble" + rnd.nextInt(6) + ".png");
						fullBubble[i][k] = false;
						popped--;
					}
				}
			}
			
			if (popped <= 0)
			{
				win = true;
			}
		}
	}

	//Draws certain things on the screen
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.drawImage(new ImageIcon("images\\game\\top1.png").getImage(), WWTProgram.topX, WWTProgram.topY, null);
		g2.drawImage(new ImageIcon("images\\game\\bubbleBackground.png").getImage(), WWTProgram.bottomX + 1, WWTProgram.bottomY + 1, null);
		g2.drawImage(new ImageIcon("images\\game\\bubbleBackground1.png").getImage(), WWTProgram.bottomX + 1, WWTProgram.bottomY + 1, null);

		for (int i = 0; i < imgBubbles.length; i++)
		{
			for (int k = 0; k < imgBubbles[i].length; k++)
			{
				g2.drawImage(imgBubbles[i][k].getImage(), xBubble[i][k], yBubble[i][k], null);
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