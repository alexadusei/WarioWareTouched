//Goal of this game is to push all the lead out of the pencil in the alloted time

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;

import java.util.Random;

public class TapGame extends JPanel implements ActionListener
{

	//Static constants of the rectangles placed on the panels to give it the visual effect of a Nintendo DS screen
	public static final Rectangle2D.Double rect1 = new Rectangle2D.Double(WWTProgram.topX, WWTProgram.topY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2);
	public static final Rectangle.Double rect2 = new Rectangle2D.Double(WWTProgram.bottomX, WWTProgram.bottomY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2 - 27);

	//Declare global variables
	private ImageIcon[] imgString;
	private ImageIcon imgFireTip, imgString1, imgString2, imgBomb, imgTimeNumber, imgPen, imgText;
	private int COUNTER, cutCount, fireTip, onFire, xExplode , yExplode, penCounter;
	private int xStart, yStart, xAmount, yAmount, xText, yText, textCounter, textPos;
	private int COUNTER_AMOUNT;
	private int[] xValues, yValues;
	private Timer tmrGame, tmrSeconds, tmrText;
	private static boolean win;

	//Declare inner class object
	private Confetti[] confetti;

	public TapGame()
	{
		win = false; // Set static variable to false to be reset to the current game
		
		//Declare timers, x/y values, pictures, and text to their settings
		tmrText = new Timer(15, this);
		tmrGame = new Timer(75, this);
		tmrSeconds = new Timer(400 + WWTProgram.SECONDS, this);
		
		xValues = new int[]{-5, 3, 2};
		yValues = new int[]{2, -3, 1};

		confetti = new Confetti[50];

		for (int i = 0; i < confetti.length; i++)
		{
			confetti[i] = new Confetti();
		}

		imgPen = new ImageIcon("images\\game\\pen" + penCounter + ".png");
		imgText = new ImageIcon("images\\game\\tapText.png");

		xText = (WWTProgram.FRAME_WIDTH - imgText.getIconWidth())/2;
		yText = 500;

		xStart = 245;
		yStart = 468;

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
		//Timer for the animnation for the text and its y position
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
			//Animation for the pen
			imgPen = new ImageIcon("images\\game\\pen" + penCounter + ".png");

			penCounter++;
			textCounter++;
			
			xText += xValues[textPos];
			yText += yValues[textPos];
			
			textPos ++;
			
			if (textPos >= 3)
			{
				textPos = 0;
			}

			if (penCounter >= 2)
			{
				penCounter = 0;
			}

			//The lead of the pen is a line object drawn in the paint class. When it's start position is outside the pen, the game is won
			if (yStart + yAmount >= 486)
			{
				yAmount += 7;
				win = true;
				win = true;
			}

			//If this is not the case, set the win boolean to false (is not read properly when passing through other classes)
			if (!win)
			{
				win = false;
			}

			//If the win boolean is true, call the 'fall' method of the inner class confetti so the confetti will fall
			if (win)
			{
				for (int i = 0; i < confetti.length; i++)
				{
					confetti[i].fall();
				}
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
			/* When the cutCount counter reaches 10, the user will be brought back to the top-panel of the layered pane, and will be judged
			 * from there
			 */
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

	class MouseListener extends MouseAdapter
	{
		//Checks if the user clicks the top-part of the pen
		public void mousePressed(MouseEvent e)
		{
			if (e.getX() >= 290 && e.getX() <= 310 && e.getY() >= 409 && e.getY() <= 424 && !win)
			{
				penCounter = 2;
				xAmount += 2 ;
				yAmount += 2 ;
			}
		}
	}

	//Draws all the necessary things on the JPanel
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.drawImage(new ImageIcon("images\\game\\top1.png").getImage(), WWTProgram.topX, WWTProgram.topY, null);
		g2.drawImage(new ImageIcon("images\\game\\tapBackground.png").getImage(), WWTProgram.bottomX , WWTProgram.bottomY + 1, null);

		g2.drawImage(imgFireTip.getImage(), 392 - 15 - fireTip, 623, null); // fireTip = 12

		for (int i = 0; i < imgString.length; i++)
		{
			g2.drawImage(imgString[i].getImage(), 339 - (54 * i) - 15, 635, null);
		}

		g2.drawImage(imgString1.getImage(), 58 - 15, 616, null);
		g2.drawImage(imgString2.getImage(), 37 - 15, 601 - onFire, null); // onFire = 14
		g2.drawImage(imgBomb.getImage(), 17 - 15 - xExplode, 612 - yExplode, null);
		g2.drawImage(imgTimeNumber.getImage(), 28, 577, null);

		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(2));
		g2.draw(new Line2D.Double(xStart - xAmount, yStart + yAmount, 224 - xAmount, 488 + yAmount));
		g2.drawImage(imgPen.getImage(), (WWTProgram.FRAME_WIDTH - imgPen.getIconWidth())/2 + 50, 400, null);
		g2.drawImage(new ImageIcon("images\\game\\tapArrow.png").getImage(), 320, 400, null);
		g2.drawImage(imgText.getImage(), xText, yText, null);

		if (win)
		{
			for (int i = 0; i < confetti.length; i++)
			{
				confetti[i].drawConfetti(g2);
			}
		}

		g2.setStroke(new BasicStroke(7));
		g2.setColor(new Color (25, 25, 25));
		g2.draw(rect1);
		g2.draw(rect2);
	}

	/* Inner class of TapGame class. The Confetti class is used for the confetti that falls off the screen when the user wins. Amount of 
	 * confetti and confetti location/colours are randomized in the no-args constructor. There are no accessor or mutator methods due to it
	 * being a very simple class.
	 */
	class Confetti
	{
		int x, y, colourType, imgCounter;
		ImageIcon imgConfetti;
		String[] colour;
		Random rnd;

		public Confetti()
		{
			rnd = new Random();
			x = rnd.nextInt(300) + 50;
			y = rnd.nextInt(200) + 350;
			colourType = rnd.nextInt(4);
			colour = new String[]{"Blue", "Pink", "Purple", "White"};

			imgConfetti = new ImageIcon("images\\game\\confetti" + colour[colourType] + "0.png");
		}

		public void fall()
		{
			if (imgCounter < 1)
			{
				imgConfetti = new ImageIcon("images\\game\\confetti" + colour[colourType] + imgCounter + ".png");
			}
			else
			{
				imgConfetti = new ImageIcon("images\\game\\confetti" +  imgCounter + ".png");
			}

			y += 5;

			imgCounter++;

			if (imgCounter >= 4)
			{
				imgCounter = 0;
			}
		}

		public void drawConfetti(Graphics2D g2)
		{
			g2.drawImage(imgConfetti.getImage(), x, y, imgConfetti.getIconWidth(), imgConfetti.getIconHeight(), null);
		}
	}
	
	public static boolean getWin(){
		return win;
	}
}