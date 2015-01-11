import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;

import java.io.*;
import java.util.Random;

public class ReadyScreen extends JPanel implements ActionListener
{

	//Static constants of the rectangles placed on the panels to give it the visual effect of a Nintendo DS screen
	public static final Rectangle2D.Double rect1 = new Rectangle2D.Double(WWTProgram.topX, WWTProgram.topY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2);
	public static final Rectangle.Double rect2 = new Rectangle2D.Double(WWTProgram.bottomX, WWTProgram.bottomY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2 - 27);

	public static final Rectangle.Double fadeRect = new Rectangle2D.Double(WWTProgram.topX, WWTProgram.topY, 
			WWTProgram.FRAME_WIDTH, WWTProgram.FRAME_HEIGHT);

	//Declare other static variables used in other game classes.
	public static int levelCounter, readyCounter, roundDigit1, roundDigit2;

	public static boolean gameover;
	public static Timer tmrReady, tmrReturn, tmrLose, tmrWin, tmrEliminate;

	//Declare global variables
	private Random rnd;
	private ImageIcon[] imgLives;
	private ImageIcon imgReady, imgTop, imgBottom, imgDigit1, imgDigit2, imgGameover;
	private int counter, xRound1, lives, livesCounter, livesPacer, xDir, topY, bottomY, readyX, readyY, gameIndex, amount, fadeTrans;
	private int[] xLives, check;
	private boolean entered, started, firstGame;
	private JPanel panel;
	private Timer tmrNewGame, tmrGameOver;
	private File file;
	private String score;

	public ReadyScreen()
	{
		//Declare timers, randomizers, files, x and y positions for visuals on the screen, and settings, etc
		rnd = new Random();

		file = new File("scores.txt");

		fadeTrans = 0;

		tmrNewGame = new Timer(1000, this);

		tmrReady = new Timer(75, this);
		tmrReturn = new Timer(75, this);
		tmrWin = new Timer(130, this);
		tmrLose = new Timer(130, this);
		tmrEliminate = new Timer(75, this);
		tmrGameOver = new Timer(75, this);

		check = new int[6];

		topY = WWTProgram.topY;
		bottomY = WWTProgram.bottomY;
		readyX = WWTProgram.topX;
		readyY = WWTProgram.topY;

		roundDigit1 = 0;
		roundDigit2 = 1;
		lives = 4;

		imgLives = new ImageIcon[lives];
		xLives = new int[lives];

		for (int i = 0; i < imgLives.length; i++)
		{
			imgLives[i] = new ImageIcon("images\\game\\lifeReady0.png");
			xLives[i] = 50 + (i * 85);
		}

		imgTop = new ImageIcon("images\\game\\topBackground.png");
		imgBottom = new ImageIcon("images\\game\\bottomBackground.png");
		imgReady = new ImageIcon("images\\game\\ready" + counter + ".png");

		imgDigit1 = new ImageIcon("images\\game\\round" + roundDigit1 + ".png");
		imgDigit2 = new ImageIcon("images\\game\\round" + roundDigit2 + ".png");
		imgGameover = new ImageIcon("");

		xRound1 = (WWTProgram.FRAME_WIDTH)/2 - (imgDigit1.getIconWidth() + imgDigit2.getIconWidth())/2;

		setLayout(null);
		setVisible(true);

		tmrReady.start();
	}

	/* Multiple timers in this JPanel will be used for different animation and conditions in this game. Such as winning a microgame, losing a
	 * game, running out of lives, proceeding to the next screen, etc.
	 */
	public void actionPerformed(ActionEvent e)
	{
		/* This timer is just used to randomize a new game behind this JPanel. It is set so that the same game will not be played twice in a row
		 * unless all the other games have been played first. This timer only runs through itself once and then stops.
		 */
		if (e.getSource() == tmrNewGame)
		{
			do
			{
				gameIndex = rnd.nextInt(6);
			}
			while (check[gameIndex] == 1);

			check[gameIndex]++;
			amount++;

			if (amount >= 6)
			{
				for (int i = 0; i < check.length; i++)
				{
					check[i] = 0;
				}
			}

			//Declare the new panel the 'panel' variable will have
			if (gameIndex == 0)
			{
				panel = new TapGame();
			}
			else if(gameIndex == 1)
			{
				panel = new GreedyGame();
			}
			else if(gameIndex == 2)
			{
				panel = new BubbleGame();
			}
			else if (gameIndex == 3)
			{
				panel = new BalloonGame();
			}
			else if (gameIndex == 4)
			{
				panel = new NosePickGame();
			}
			else if (gameIndex == 5)
			{
				panel = new PinGame();
			}

			//Sets the location on top of the Jframe
			panel.setBounds(0, 0, 439, 649);

			//If it is not the very first game, remove the JPanel that's already on the layered pane
			if (firstGame)
			{
				PlayScreen.layeredpane.remove(1);
			}

			//Add the randomized game panel to the bottom of the layered pane (behind this JPanel)
			PlayScreen.layeredpane.add(panel, new Integer(0));	
			firstGame = true; //Set firstGame to true after the very first game has been randomized

			//Stop the timer. It only needs one run-through when started.
			tmrNewGame.stop();
		}
		// The GameOver timer simply darkens the screen and exits the program when the game is finished.
		else if (e.getSource() == tmrGameOver)
		{
			fadeTrans += 10;

			if (fadeTrans >= 255)
			{
				fadeTrans = 255;
				tmrEliminate.stop();
				System.exit(0);	
			}
		}
		/* The Return timer is the timer that brings the user back to this screen after a minigame has been played. This screen will determine
		 * if the user completed the conditions of the minigame, and won, or did not complete the conditions of the game and lost. Based on that,
		 * this timer will start the Win or Lose timer.
		 */
		else if (e.getSource() == tmrReturn) // RETURN
		{
			//x and y components to bringing back the background images and animation of the character in play.
			imgReady = new ImageIcon("images\\game\\ready0.png");

			topY += 50;
			bottomY -= 50;
			readyX -= 90;
			readyY -= 90;

			if (readyX <= WWTProgram.topX)
			{
				readyX = WWTProgram.topX;
			}
			if (readyY <= WWTProgram.topY)
			{
				readyY = WWTProgram.topY;
			}

			if (topY >= WWTProgram.topY)
			{
				topY = WWTProgram.topY;
			}

			if (bottomY <= WWTProgram.bottomY)
			{

				bottomY = WWTProgram.bottomY;

				started = false;

				boolean win = false;


				if (gameIndex == 0)
				{
					win = TapGame.getWin();
				}
				else if(gameIndex == 1)
				{
					win = GreedyGame.getWin();
				}
				else if(gameIndex == 2)
				{
					win = BubbleGame.getWin();
				}
				else if (gameIndex == 3)
				{
					win = BalloonGame.getWin();
				}
				else if (gameIndex == 4)
				{
					win = NosePickGame.getWin();
				}
				else if (gameIndex == 5)
				{
					win = PinGame.getWin();
				}

				if (win)
				{
					readyCounter = 0;
					tmrWin.start();
				}
				else
				{
					readyCounter = 0;
					tmrLose.start();
				}
				tmrReturn.stop();
			}
		}
		
		//The timer for if the user one the previous minigame. The 'winning' animation will proceed to play, and the user will lose no lives.
		else if (e.getSource() == tmrWin && entered)
		{
			readyCounter++;

			imgReady = new ImageIcon("images\\game\\win" + counter + ".png");
			imgDigit1 = new ImageIcon("images\\game\\round" + roundDigit1 + ".png");
			imgDigit2 = new ImageIcon("images\\game\\round" + roundDigit2 + ".png");

			for (int i = 0; i < imgLives.length; i++)
			{
				imgLives[i] = new ImageIcon("images\\game\\lifeWin" + livesCounter + ".png");
			}

			livesCounter++;
			counter++;

			if (livesCounter >= 4)
			{
				livesCounter = 0;
			}

			if (counter >= 2)
			{
				counter = 0;
			}

			if (readyCounter == 20)
			{				
				tmrWin.stop();
				counter = 0;
				livesCounter = 0;
				readyCounter = 0;
				PlayScreen.start = false;
				imgReady = new ImageIcon("images\\game\\ready" + counter + ".png");

				roundDigit2++;

				if (roundDigit2 >= 10)
				{
					roundDigit2 = 0;
					roundDigit1++;

					if (roundDigit1 >= 10)
					{
						roundDigit1 = 9;
						roundDigit2 = 9;
					}
				}

				imgDigit1 = new ImageIcon("images\\game\\round" + roundDigit1 + ".png");
				imgDigit2 = new ImageIcon("images\\game\\round" + roundDigit2 + ".png");

				tmrReady.start();
			}
		}
		
		/* The Losing Timer if the user lost the previous minigame. The 'losing' animation will begin to play, with one of the player's lives
		 * leaving the game screen. If the lives is less than 0, the game will be over.
		 */
		else if (e.getSource() == tmrLose && entered)
		{
			readyCounter++;

			imgReady = new ImageIcon("images\\game\\lose" + counter + ".png");	
			imgDigit1 = new ImageIcon("images\\game\\round" + roundDigit1 + ".png");
			imgDigit2 = new ImageIcon("images\\game\\round" + roundDigit2 + ".png");

			if (readyCounter >= 4)
			{
				if (readyCounter == 4)
				{
					lives--;
					tmrEliminate.start();
				}

				counter++;
			}

			for (int i = 0; i < lives; i++)
			{
				imgLives[i] = new ImageIcon("images\\game\\lifeLose" + livesCounter + ".png");
			}

			livesCounter++;

			if (livesCounter >= 2)
			{
				livesCounter = 0;
			}

			if (counter >= 1)
			{
				counter = 1;
			}

			if (readyCounter == 20)
			{
				readyCounter = 0;
				counter = 0;
				imgReady = new ImageIcon("images\\game\\ready" + counter + ".png");
				PlayScreen.start = false;
				tmrLose.stop();
				tmrReady.start();
			}

		}
		/* This screen eliminates one of the lives from the view from the game. Once there are no more lives on the screen, the game will stop
		 * with a 'GAMEOVER' picture over the bottom screen and prompt the user for a user name to write his/her score to the file.
		 */
		else if (e.getSource() == tmrEliminate)
		{
			imgLives[lives] = new ImageIcon("images\\game\\lifeEliminate1.png");

			xDir += 2;
			xLives[lives] += xDir;

			if (xLives[lives] >= WWTProgram.FRAME_WIDTH)
			{
				tmrEliminate.stop();
				xDir = 0;

				if (lives <= 0)
				{
					imgGameover = new ImageIcon("images\\game\\gameOver.png");
					tmrEliminate.stop();
					tmrLose.stop();

					score = Integer.toString(roundDigit1) + Integer.toString(roundDigit2);

					String name = JOptionPane.showInputDialog(null, "Enter your name to be inputted for a highscore:", "High Score", 
							JOptionPane.INFORMATION_MESSAGE);

					if(name != null)
					{
						try
						{
							BufferedWriter out = new BufferedWriter(new FileWriter(file, true));

							out.write(name + ", ");
							out.write(score + "\n");
							out.close();
						}
						catch(IOException ex)
						{
							JOptionPane.showMessageDialog(null, ex.getMessage() + "!", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}

					gameover = true;
					tmrGameOver.start();
				}
			}	
		}
		
		/* The Ready Timer. This timer will always be played after a win/lose timer (or on the very first play). This timer plays the
		 * Generate Timer to randomize a new minigame behind this JPanel (away from view), and it will play the animation for the character
		 * to initiate the user into the new minigame. This timer will be played only when the user has enough lives to play. That being said,
		 * whenever this timer is starting, it will increase the 'level' of the game and the images will change to its corresponding level.
		 */
		else if (e.getSource() == tmrReady)
		{
			if (!started)
			{
				tmrNewGame.start();
				started = true;
			}

			if (!entered)
			{
				entered = true;
			}

			readyCounter ++;
			livesPacer++;

			if (livesPacer >= 2)
			{
				for (int i = 0; i < imgLives.length; i++)
				{
					imgLives[i] = new ImageIcon("images\\game\\lifeReady" + livesCounter + ".png");
				}

				livesCounter ++;

				if (livesCounter >= 2)
				{
					livesCounter = 0;
				}

				livesPacer = 0;
			}

			if (readyCounter >= 20)
			{

				if (counter >= 1)
				{
					imgDigit1 = new ImageIcon("");
					imgDigit2 = new ImageIcon("");

					for (int i = 0; i < imgLives.length; i++)
					{
						imgLives[i] = new ImageIcon("");
					}
				}

				if (counter >= 3)
				{
					topY = -imgTop.getIconHeight();
					bottomY = WWTProgram.FRAME_HEIGHT;
				}

				if (!PlayScreen.start)
				{
					imgReady = new ImageIcon("images\\game\\ready" + counter + ".png");
					counter++;
					levelCounter++;
				}
				if (counter >= 9)
				{
					readyX = WWTProgram.topX + 377;
					readyY = WWTProgram.topY + 305;
					imgReady = new ImageIcon("images\\game\\ready0.png");
					counter = 0;
				}
			}

			repaint();
		}
	}

	//Draws all of the images, titles, and components to this JPanel
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.drawImage(imgTop.getImage(), WWTProgram.topX, topY, null);
		g2.drawImage(imgBottom.getImage(), WWTProgram.bottomX, bottomY, null);

		g2.drawImage(imgReady.getImage(), readyX, readyY, null);

		g2.drawImage(imgDigit1.getImage(), xRound1 - 10, 265, null);
		g2.drawImage(imgDigit2.getImage(), xRound1 + imgDigit1.getIconWidth(), 265, null);

		for (int i = 0; i < imgLives.length; i++)
		{
			g2.drawImage(imgLives[i].getImage(), xLives[i], 20, null);
		}

		g2.drawImage(imgGameover.getImage(), (WWTProgram.FRAME_WIDTH - 360)/2, 450, null);

		g2.setStroke(new BasicStroke(7));
		g2.setColor(new Color (25, 25, 25));
		g2.draw(rect1);
		g2.draw(rect2);

		g2.setColor(new Color(0, 0, 0, fadeTrans));
		g2.fill(fadeRect);

	}
}