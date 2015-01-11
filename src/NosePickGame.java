//Goal of this game is to aim finger(s) into nose in the alloted time

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;

import java.util.Random;

public class NosePickGame extends JPanel implements ActionListener
{

	//Static constants of the rectangles placed on the panels to give it the visual effect of a Nintendo DS screen
	public static final Rectangle2D.Double rect1 = new Rectangle2D.Double(WWTProgram.topX, WWTProgram.topY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2);
	public static final Rectangle.Double rect2 = new Rectangle2D.Double(WWTProgram.bottomX, WWTProgram.bottomY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2 - 27);

	//Declare global variables
	private ImageIcon[] imgString;
	private ImageIcon imgFireTip, imgString1, imgString2, imgBomb, imgTimeNumber, imgText, imgButton, imgNose, imgHand;
	private int COUNTER, cutCount, fireTip, onFire, xExplode , yExplode, xHand, yHand, xButton, yButton;
	private int xText, yText, textCounter, textPos, counter, color, fingerPoint, xDir;
	private int COUNTER_AMOUNT;
	private int[] xValues, yValues;
	private Timer tmrGame, tmrSeconds, tmrText;
	private Random rnd;
	private boolean  pressed;
	
	public static boolean win;

	public NosePickGame()
	{
		//Declare timers, x/y values, pictures, and text to their settings
		win = false;
		//ReadyScreen.win = false;
		
		tmrText = new Timer(15, this);
		tmrGame = new Timer(75, this);
		tmrSeconds = new Timer(400 + WWTProgram.SECONDS, this);

		rnd = new Random();

		xButton = 365;
		yButton = 450;
		xHand = 200;
		yHand = 480;

		color = rnd.nextInt(2);
		
		//Randomizes which type of hand will show up during the game (index finger, pinky finger, two fingers)
		fingerPoint = rnd.nextInt(2);

		xDir = fingerPoint + 1;

		xValues = new int[]{-5, 3, 2};
		yValues = new int[]{2, -3, 1};

		imgText = new ImageIcon("images\\game\\pickText.png");
		imgButton = new ImageIcon("images\\game\\a0.png");
		imgNose = new ImageIcon("images\\game\\nose.png");
		imgHand = new ImageIcon("images\\game\\hand" + fingerPoint + ".png");

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
		/* Animates the text on the screen, also checks which type of finger has been randomized, and sets if-statements according to
		 * that on the boundaries the user must succeed to get to win the game
		 */
		if (e.getSource() == tmrText)
		{
			if (pressed)
			{
				yHand -=  3;
				xDir = 0;

				if (yHand <= 450)
				{
					yHand = 450;

					if (fingerPoint == 0)
					{
						if (xHand + 10 >= 200 && xHand + 23 <= 223) //Left nostril
						{
							win = true;
							//ReadyScreen.win = true;
						}
						else if (xHand + 10 >= 220 && xHand + 23 <= 242) //Right nostril
						{
							win = true;
							//ReadyScreen.win = true;
						}
						else if (xHand + 23 >= 197 && xHand + 23 <= 207)
						{
							imgNose = new ImageIcon("images\\game\\noseHitLeft.png");
						}
						else if (xHand + 10 >= 233 && xHand + 10 <= 247)
						{
							imgNose = new ImageIcon("images\\game\\noseHitRight.png");
						}
						else if (xHand + 10 >= 200 && xHand + 23 <= 240)
						{
							imgNose = new ImageIcon("images\\game\\noseHitCentre.png");
						}
					}
					else if (fingerPoint == 1)
					{
						if ((xHand + 48 >= 200 && xHand + imgHand.getIconWidth() <= 223) || (xHand + 48 >= 220 && xHand + 
								imgHand.getIconWidth() <= 242))
						{
							win = true;
							//ReadyScreen.win = true;
						}
						else if (xHand + imgHand.getIconWidth() >= 197 && xHand + imgHand.getIconWidth() <= 207)
						{
							imgNose = new ImageIcon("images\\game\\noseHitLeft.png");
						}
						else if (xHand + 48 >= 233 && xHand + 48 <= 247)
						{
							imgNose = new ImageIcon("images\\game\\noseHitRight.png");
						}
						else if (xHand + 48 >= 200 && xHand + imgHand.getIconWidth() <= 240)
						{
							imgNose = new ImageIcon("images\\game\\noseHitCentre.png");
						}
					}
				}
			}

			if (xHand <= 141 || xHand + imgHand.getIconWidth() >= 300)
			{
				xDir = -xDir;
			}

			xHand += xDir;

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

			imgButton = new ImageIcon("images\\game\\a" + counter + ".png");

			counter++;

			if (counter >= 2)
			{
				counter = 0;
			}

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

	//Allows the user to press 'A' on the gameboy to launch the hand
	class MouseListener extends MouseAdapter
	{
		public void mouseReleased(MouseEvent e)
		{
			if (e.getX() >= xButton && e.getX() <= xButton + imgButton.getIconWidth() &&
					e.getY() >= yButton && e.getY() <= yButton + imgButton.getIconHeight())
			{
				pressed = true;
			}
		}
	}

	//Draws certain things on the JPanel
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.drawImage(new ImageIcon("images\\game\\top7.png").getImage(), WWTProgram.topX, WWTProgram.topY, null);
		g2.drawImage(new ImageIcon("images\\game\\nosePickBackground.png").getImage(), WWTProgram.bottomX + 1, WWTProgram.bottomY + 1, null);

		g2.setColor(Color.WHITE);
		g2.fill(new Rectangle2D.Double(140, 420, 165, 115));
		
		/* If the user successfully picks a nose, finger will be drawn behind nose. Otherwise, finger will be drawn in front of nose (as if user
		 * misses and hits nose roughly).
		 */
		if (win)
		{
			g2.drawImage(imgHand.getImage(), xHand, yHand, null);
		}
		
		g2.drawImage(imgNose.getImage(), (WWTProgram.FRAME_WIDTH - imgNose.getIconWidth())/2, 425, null);
		
		if (!win)
		{
			g2.drawImage(imgHand.getImage(), xHand, yHand, null);
		}
		
		g2.drawImage(new ImageIcon("images\\game\\gameBoy" + color + ".png").getImage(), WWTProgram.bottomX + 30, 
				WWTProgram.bottomY + 40, null);
		g2.drawImage(imgButton.getImage(), 365, 450, null);
		
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