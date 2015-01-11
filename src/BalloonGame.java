//Goal of this game is to pop all balloons on screen in alloted time

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.Random;

public class BalloonGame extends JPanel implements ActionListener
{

	//Static constants of the rectangles placed on the panels to give it the visual effect of a Nintendo DS screen
	public static final Rectangle2D.Double rect1 = new Rectangle2D.Double(WWTProgram.topX, WWTProgram.topY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2);
	public static final Rectangle.Double rect2 = new Rectangle2D.Double(WWTProgram.bottomX, WWTProgram.bottomY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2 - 27);

	//Draw global variables
	private ImageIcon[] imgString, imgBalloons;
	private ImageIcon imgFireTip, imgString1, imgString2, imgBomb, imgTimeNumber, imgText, imgBehind;
	private int COUNTER, cutCount, fireTip, onFire, xExplode , yExplode;
	private int xText, yText, textCounter, textPos;
	private int COUNTER_AMOUNT, behind, counter, popped, animator;
	private int[] xValues, yValues, xBalloon, yBalloon;
	private Timer tmrGame, tmrSeconds, tmrText;
	private static boolean win;
	private Random rnd;

	public BalloonGame()
	{
		win = false;
		
		//Declare timers, x/y values, pictures, and text to their settings
		rnd = new Random();

		tmrText = new Timer(15, this);
		tmrGame = new Timer(75, this);
		tmrSeconds = new Timer(400 + WWTProgram.SECONDS, this);

		xValues = new int[]{-5, 3, 2};
		yValues = new int[]{2, -3, 1};

		behind = rnd.nextInt(4);

		imgText = new ImageIcon("images\\game1\\balloonText.png");
		imgBehind = new ImageIcon("images\\game1\\object" + behind + "_" + counter + ".png");

		//Randomizes how many balloons will be used in the game (10 to 20)
		imgBalloons = new ImageIcon[rnd.nextInt(11) + 10];
		xBalloon = new int[imgBalloons.length];
		yBalloon = new int[imgBalloons.length];

		//Sets the array of x and y locations to a certain range on the bottom screen
		for (int i = 0; i < imgBalloons.length; i++)
		{
			imgBalloons[i] = new ImageIcon("images\\game1\\balloon" + rnd.nextInt(4) + ".png");
			xBalloon[i] = rnd.nextInt(200) + 80;
			yBalloon[i] = rnd.nextInt(130) + WWTProgram.rectHeight1 + (WWTProgram.rectHeight2 - imgBalloons[i].getIconHeight())/2 - 60;
		}

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
			//If the game is won, animate the background image after you pop all balloons (indicating you won)
			if (win)
			{
				animator++;
				if (animator >= 2)
				{
					imgBehind = new ImageIcon("images\\game1\\object" + behind + "_" + counter + ".png");

					counter++;

					if (counter >= 2)
					{
						counter = 0;
					}
					animator = 0;
				}
			}

			textCounter++;

			xText += xValues[textPos];
			yText += yValues[textPos];

			textPos ++;

			if (textPos >= 3)
			{
				textPos = 0;
			}

			if (win)
			{

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

	/* If the mouse is within the boundaries of a balloon, the balloon will be 'popped' and disappear (this can happen to multiple balloons
	 * at the same time). The variable 'popped' will increase until it reaches the amount of balloons that was generated at the beginning
	 * of the game. When this is so, the game is won.
	 */
	class MouseListener extends MouseAdapter
	{
		public void mouseReleased(MouseEvent e)
		{
			for (int i = 0; i < imgBalloons.length; i++)
			{
				if (e.getX() >= xBalloon[i] && e.getX() <= xBalloon[i] + imgBalloons[i].getIconWidth() &&
						e.getY() >= yBalloon[i] && e.getY() <= yBalloon[i] + imgBalloons[i].getIconHeight())
				{
					imgBalloons[i] = new ImageIcon("");
					xBalloon[i] = 600;
					popped++;
				}
			}

			if (popped >= imgBalloons.length)
			{
				win = true;
			}
		}
	}

	//Draw all the components on the JPanel
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.drawImage(new ImageIcon("images\\game\\top0.png").getImage(), WWTProgram.topX, WWTProgram.topY, null);
		g2.drawImage(new ImageIcon("images\\game1\\balloonBackground.png").getImage(), WWTProgram.bottomX , WWTProgram.bottomY + 1, null);
		g2.drawImage(imgBehind.getImage(), WWTProgram.FRAME_WIDTH/2 - imgBehind.getIconWidth()/2, 
				(WWTProgram.rectHeight1)  + (WWTProgram.rectHeight2 - imgBehind.getIconHeight())/2, null);


		//Draw the array of balloons on the JPanel
		for (int i = 0; i < imgBalloons.length; i++)
		{
			g2.drawImage(imgBalloons[i].getImage(), xBalloon[i], yBalloon[i], null);
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