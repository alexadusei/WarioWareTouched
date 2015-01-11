//Goal of this game is to drag all money into the purse in alloted time

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;

import java.util.Random;

public class GreedyGame extends JPanel implements ActionListener
{

	//Static constants of the rectangles placed on the panels to give it the visual effect of a Nintendo DS screen
	public static final Rectangle2D.Double rect1 = new Rectangle2D.Double(WWTProgram.topX, WWTProgram.topY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2);
	public static final Rectangle.Double rect2 = new Rectangle2D.Double(WWTProgram.bottomX, WWTProgram.bottomY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2 - 27);

	//Declare global variables
	private ImageIcon[] imgString;
	private ImageIcon imgFireTip, imgString1, imgString2, imgBomb, imgTimeNumber, imgText, imgBag, imgBagWin;
	private int COUNTER, cutCount, fireTip, onFire, xExplode , yExplode, mouseX, mouseY;
	private int xText, yText, textCounter, textPos, xBag, yBag, bagCounter, bagWaiter, yumAmount, coins;
	private int COUNTER_AMOUNT;
	private int[] xValues, yValues;
	private Timer tmrGame, tmrSeconds, tmrText;
	private Random rnd;
	private static boolean win;
	private boolean inside;

	//Declare coin object from inner class Coin
	private Coin[] coin;

	public GreedyGame()
	{
		//Declare timers, x/y values, pictures, and text to their settings
		win = false;
		//ReadyScreen.win = false;
		
		tmrText = new Timer(15, this);
		tmrGame = new Timer(75, this);
		tmrSeconds = new Timer(600 + WWTProgram.SECONDS, this);

		rnd = new Random();

		xValues = new int[]{-5, 3, 2};
		yValues = new int[]{2, -3, 1};

		imgText = new ImageIcon("images\\game\\greedyText.png");
		imgBag = new ImageIcon("images\\game\\bag0.png");
		imgBagWin = new ImageIcon("images\\game\\bagWin0.png");

		//Randomize how many coins are going to show up (2 - 6)
		coin = new Coin[rnd.nextInt(5) + 2];

		for (int i = 0; i < coin.length; i++)
		{
			coin[i] = new Coin();
		}

		xText = (WWTProgram.FRAME_WIDTH - imgText.getIconWidth())/2;
		yText = 500;
		xBag = (WWTProgram.FRAME_WIDTH - imgBag.getIconWidth())/2;
		yBag = 520;

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
	}

	public void actionPerformed(ActionEvent e)
	{
		//Animate text. Also check if coins go inside bag, if so, animate bag briefly.
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

			if (inside)
			{
				bagWaiter++;

				if (bagWaiter >= 10)
				{
					if (yumAmount >= 10)
					{
						inside = false;
						bagCounter = 0;
						yumAmount = 0;
					}

					imgBag = new ImageIcon("images\\game\\Bag" + bagCounter + ".png");

					if (win)
					{
						yumAmount = 0;
						imgBagWin = new ImageIcon("images\\game\\bagWin" + bagCounter + ".png");
					}

					bagCounter++;
					yumAmount++;

					if (bagCounter >= 2)
					{
						bagCounter = 0;
					}	

					bagWaiter = 0;
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

	/* Check if user clicks coin, if so, set the coin's x/y to the mouse's x/y. Also check if the coin is within the boundaries of the game
	 * If this is also true, make the coin disappear and increase the integer variable 'coins'. When 'coins' reaches the amount of coins 
	 * generated at the beginning of this game, the user has won
	 */
	class MouseListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			for (int i = 0; i < coin.length; i++)
			{
				if (e.getY() >= coin[i].getY() && e.getY() <= coin[i].getY() + coin[i].getHeight() &&
						e.getX() >= coin[i].getX() && e.getX() <= coin[i].getX() + coin[i].getWidth() && !coin[i].getInside())
				{
					coin[i].setTouched(true);
				}
			}
		}

		public void mouseReleased(MouseEvent e)
		{
			for (int i = 0; i < coin.length; i++)
			{
				coin[i].setTouched(false);
			}
		}
	}

	class MouseMotionListener extends MouseMotionAdapter
	{
		public void mouseDragged(MouseEvent e)
		{

			for (int i = 0; i < coin.length; i++)
			{
				coin[i].place();

				if (coin[i].getX() + coin[i].getWidth() - 20 >= xBag && coin[i].getX() <= xBag + imgBag.getIconWidth() - 20 &&
						coin[i].getY() + coin[i].getHeight() - 20 >= yBag && coin[i].getY() <= yBag + imgBag.getIconHeight() - 20)
				{
					inside = true;
					coin[i].setInside(true);
					coin[i].setTouched(false);
					coin[i].setImage(new ImageIcon(""));
					coin[i].setX(500);
					imgBag = new ImageIcon("images\\game\\Bag0.png");
					bagCounter = 1;
					coins++;

					if (coins == coin.length)
					{
						win = true;
					//	ReadyScreen.win = true;
					}
				}

				if (coin[i].getX() <= 57)
				{
					coin[i].setX(57);
				}
				else if (coin[i].getX() + coin[i].getWidth() >= WWTProgram.FRAME_WIDTH - 64)
				{
					coin[i].setX(WWTProgram.FRAME_WIDTH - 64 - coin[i].getWidth());
				}
				else if (coin[i].getY() <= 365)
				{
					coin[i].setY(365);
				}
				else if (coin[i].getY() + coin[i].getHeight() >= WWTProgram.FRAME_HEIGHT - 55)
				{
					coin[i].setY(WWTProgram.FRAME_HEIGHT - 55 - coin[i].getHeight());
				}
			}

			mouseX = e.getX();
			mouseY = e.getY();
		}
		public void mouseMoved(MouseEvent e)
		{
			mouseX = e.getX();
			mouseY = e.getY();
		} 
	}

	//Draw certain things on the JPanel
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.drawImage(new ImageIcon("images\\game\\top8.png").getImage(), WWTProgram.topX, WWTProgram.topY, null);
		g2.drawImage(new ImageIcon("images\\game\\greedyBackground.png").getImage(), WWTProgram.bottomX , WWTProgram.bottomY + 1, null);

		g2.drawImage(imgFireTip.getImage(), 392 - 15 - fireTip, 623, null); // fireTip = 12

		for (int i = 0; i < imgString.length; i++)
		{
			g2.drawImage(imgString[i].getImage(), 339 - (54 * i) - 15, 635, null);
		}

		g2.drawImage(imgString1.getImage(), 58 - 15, 616, null);
		g2.drawImage(imgString2.getImage(), 37 - 15, 601 - onFire, null); // onFire = 14
		g2.drawImage(imgBomb.getImage(), 17 - 15 - xExplode, 612 - yExplode, null);
		g2.drawImage(imgTimeNumber.getImage(), 28, 577, null);

		g2.drawImage(imgBag.getImage(), xBag, yBag, null); 

		for (int i = 0; i < coin.length; i++)
		{
			coin[i].drawCoin(g2);
		}

		g2.drawImage(imgText.getImage(), xText, yText, null);

		if (win)
		{
			g2.drawImage(imgBagWin.getImage(), xBag - 50, 450, null);
		}

		g2.setStroke(new BasicStroke(7));
		g2.setColor(new Color (25, 25, 25));
		g2.draw(rect1);
		g2.draw(rect2);
	}

	//The inner class, Coin.  
	class Coin
	{
		//Declare global variables.
		int x, y, width, height;
		ImageIcon imgCoin;
		Random rnd;
		private boolean touched, inside;

		//Declare no-args constructor
		public Coin()
		{
			rnd = new Random();
			x = rnd.nextInt(285) + 60;
			y = rnd.nextInt(115) + 370;
			imgCoin = new ImageIcon("images\\game\\coin" + rnd.nextInt(3) + ".png");
			width = imgCoin.getIconWidth();
			height = imgCoin.getIconHeight();
		}

		//Mutator methods for the inner class
		public void setX (int x)
		{
			this.x = x;
		}

		public void setY (int y)
		{
			this.y = y;
		}

		public void setTouched (boolean touched)
		{
			this.touched = touched;
		}

		public void setInside(boolean inside)
		{
			this.inside = inside;
		}

		public void setImage(ImageIcon img)
		{
			this.imgCoin = img;
		}

		//Accessor methods for the inner class
		public int getX()
		{
			return this.x;
		}

		public int getY()
		{
			return this.y;
		}

		public int getWidth()
		{
			return width;
		}

		public int getHeight()
		{
			return height;
		}

		public boolean getTouched()
		{
			return this.touched;
		}

		public boolean getInside()
		{
			return this.inside;
		}

		//Used to place coins to the x/y of the mouse when clicked
		public void place()
		{
			if (touched)
			{
				this.x = mouseX - width/2;
				this.y = mouseY - width/2;
			}
		}

		//Method to draw coins
		public void drawCoin(Graphics2D g2)
		{
			g2.drawImage(imgCoin.getImage(), x, y, width, height, null);
		}
	}
	
	public static boolean getWin(){
		return win;
	}

}