import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.text.*;
import java.util.Collections;
import java.util.LinkedList;
import java.io.*;

public class MainScreen extends JPanel implements ActionListener
{
	public static final Rectangle2D.Double rect1 = new Rectangle2D.Double(WWTProgram.topX, WWTProgram.topY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2);

	public static final Rectangle.Double rect2 = new Rectangle2D.Double(WWTProgram.bottomX, WWTProgram.bottomY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2 - 17);

	public static final Rectangle.Double fadeRect = new Rectangle2D.Double(WWTProgram.topX, WWTProgram.topY, 
			WWTProgram.FRAME_WIDTH, WWTProgram.FRAME_HEIGHT);

	//Global variables
	private Timer tmrMain;
	private ImageIcon imgSubtitle, imgCharTitle, imgCharIcon; 
	private ImageIcon[] imgMainButton;
	private int imgCounter, strHeadWidth, strMainWidth, xMessage, aniCounter, fadeTrans, fadeTransBottom, colChangeCounter, colCounter;
	private int xMainIcon, yMainIcon, xMainTitle, yMainTitle, xSubTitle, ySubTitle, xCharIcon, yCharIcon, xCharTitle, yCharTitle;
	private int xConfirmBubble, yConfirmBubble, confirmBubbleWidth, confirmBubbleHeight,  xCancelBubble, yCancelBubble, cancelBubbleWidth, 
	cancelBubbleHeight, xStart, yStart, xCancel, yCancel, blueTint, pinkTint, yellowTint, xBlueScorePane, yBlueScorePane;
	private int[] xValues, yValues, xButton, yButton;
	private Font textFont;
	private FontMetrics fmHead, fmMain;
	private String headerText, mainText;
	private boolean opening, chosen;
	private boolean[] selected, held;
	private RoundRectangle2D.Double selectedOutline;
	private Ellipse2D.Double confirmBubble, cancelBubble;
	private Color[] colValue;
	private Color c;
	private int TEMPORARY_TIMER;
	private DecimalFormat fm;
	private File textFile;

	private Character chars;
	private LinkedList<Scores> players;

	public MainScreen()
	{
		//Timers for the program
		tmrMain = new Timer(30, this);
		tmrMain.start();

		textFile = new File("scores.txt"); //Textfile to read from a file

		players = new LinkedList<Scores>(); //LinkedList of the 'Score' class
		
		/*Main screen has a small component showing the top 3 scores. The top 3 scores need to be updated everytime the Main Screen
		 * is in play. This being so, this JPanel needs to read from the scores file and sort the data by the highest score each time
		 * the JPanel shows up.
		 */
		
		if (textFile != null)
		{
			try
			{
				BufferedReader in = new BufferedReader(new FileReader(textFile));

				String line;
				String[] data;

				line = in.readLine();

				while (line != null)
				{
					data = line.split (", ");

					players.add(new Scores(data[0], Integer.parseInt(data[1])));

					line = in.readLine();
				}

				in.close();
			}
			catch(IOException e)
			{
				e.getMessage();
			}
		}

		//Sorts the data from the file. If two players have the same score, sort alphabetically (not case sensitive).
		Scores.sortOrder = Scores.SORT_BY_NAME;
		Collections.sort(players);
		Scores.sortOrder = Scores.SORT_BY_SCORE;
		Collections.sort(players, Collections.reverseOrder());

		//To make sure the location of the numbers are constant, sets the score until decimal format (e.g 5 will be 05)
		fm = new DecimalFormat("00");

		/* Following variables are several global integers/doubles for the x and y components of animation for several shape objects and
		 * ImageIcons.
		 */
		xBlueScorePane = -250;
		yBlueScorePane = 170;

		xConfirmBubble = -100;
		yConfirmBubble = -100;
		xCancelBubble = -100;
		yCancelBubble = -100;

		confirmBubble = new Ellipse2D.Double(xConfirmBubble, yConfirmBubble, confirmBubbleWidth, confirmBubbleHeight);
		cancelBubble = new Ellipse2D.Double(xCancelBubble, yCancelBubble, cancelBubbleWidth, cancelBubbleHeight);

		blueTint = 100;
		pinkTint = 30;
		yellowTint = 30;

		imgCounter = 1;

		xMainIcon = 40;
		xMainTitle = 155;
		yMainTitle = 65;
		xSubTitle = 20;
		ySubTitle = 30;

		xCharTitle = 20;
		yCharTitle = -80;
		xCharIcon = WWTProgram.FRAME_WIDTH;
		yCharIcon = 90;

		fadeTrans = 255;
		colCounter = 0;

		xMessage = WWTProgram.FRAME_WIDTH;

		//Default text for the scrolling message.
		headerText = "Welcome to WarioWare Touched!";
		mainText = " Poke someone below to play their mad microgames!";

		//Array of numbers for further animation
		xValues = new int[]{-3, 1, 2};
		yValues = new int[]{150, 151, 149};

		xButton = new int[]{10, 130, 250, 370};
		yButton = new int[4];

		//Booleans to check if certain image buttons have been clicked
		held = new boolean[4];
		selected = new boolean[4];

		selected[0] = true;

		//Change colours for the 'selection' outline, based on what box you pick, so the user can understand which screen they're currently on
		colValue = new Color[]{Color.RED, Color.CYAN, Color.WHITE};

		//Y-position for certain buttons
		for (int i = 0; i < yButton.length; i++)
		{
			yButton[i] = 616;
		}

		//Font style/ font size for the font
		textFont = new Font ("Bauhaus 93", Font.PLAIN, 18);

		//Font metrics for the program to understand certain String widths for later if-statements in the program
		fmHead = getFontMetrics(textFont);
		fmMain = getFontMetrics(textFont);
		strHeadWidth = fmHead.stringWidth(headerText);
		strMainWidth = fmMain.stringWidth(mainText);

		// Several images used in the program
		imgSubtitle = new ImageIcon("images\\main\\toySubtitle" + imgCounter + ".png");

		imgMainButton = new ImageIcon[4];
		imgMainButton[0] = new ImageIcon("images\\main\\toyroomButton.png");
		imgMainButton[1] = new ImageIcon("images\\main\\gamesButton.png");
		imgMainButton[2] = new ImageIcon("images\\main\\highScoreButton.png");
		imgMainButton[3] = new ImageIcon("images\\main\\optionsButton.png");

		imgCharTitle = new ImageIcon("");
		imgCharIcon = new ImageIcon("");

		selectedOutline = new RoundRectangle2D.Double(xButton[0], yButton[0], 
				imgMainButton[0].getIconWidth() - 1, imgMainButton[0].getIconHeight(), 15, 15);

		chars = new Character(0);

		setLayout(null);
		addMouseListener(new MouseListener());
		addMouseMotionListener(new MouseMotionListener());
		setVisible(true);
	}

	/* Timer for all the components and moving figures in the game. Integer values are continuously changing and being reset 
	 * in this Timer
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == tmrMain)
		{
			aniCounter++;
			TEMPORARY_TIMER++;

			colChangeCounter++;

			if (colChangeCounter >= 2)
			{
				c = colValue[colCounter];

				colChangeCounter = 0;
				colCounter++;

				if (colCounter > 2)
				{
					colCounter = 0;
				}
			}

			if (imgCounter > 2)
			{
				imgCounter = 0;
			}

			if (opening)
			{
				xMessage -= 2;
			}

			if (xMessage + strHeadWidth + strMainWidth <= 0 )
			{
				xMessage = WWTProgram.FRAME_WIDTH;
			}

			if (!opening)
			{
				fadeTrans -= 10;

				if (fadeTrans <= 0)
				{
					fadeTrans = 0;
					opening = true;
				}
			}

			if (TEMPORARY_TIMER >= 60)
			{
				if (opening)
				{
					chars.move();

					// If one of the characters are selected, change the Character picture and the text to the respective character
					if (chars.getSelected())
					{
						headerText = "[Super Zero!]";
						mainText = " \"I am Warior-Man! You never know what kind of touch control you will " +
								"have to use in my Super Zero microgame mix!\"";

						strHeadWidth = fmHead.stringWidth(headerText);
						strMainWidth = fmMain.stringWidth(mainText);

						fadeTransBottom += 10;

						xMainIcon -= 10;
						ySubTitle -= 10;
						xMainTitle += 25;

						yCharTitle += 10;
						xCharIcon -= 20;

						confirmBubbleWidth += 30;
						confirmBubbleHeight += 20;
						cancelBubbleWidth += 30;
						cancelBubbleHeight += 20;

						xBlueScorePane += 25;

						if (xMainIcon <= -100)
						{
							xMainIcon = -100;
						}
						if (ySubTitle <= -60)
						{
							ySubTitle = -60;
						}
						if (xMainTitle >= WWTProgram.FRAME_WIDTH)
						{
							xMainTitle = WWTProgram.FRAME_WIDTH;
						}

						if (yCharTitle >= 30)
						{
							yCharTitle = 30;
						}
						if (xCharIcon <= 195)
						{
							xCharIcon = 195;
						}

						if (confirmBubbleWidth >= 170)
						{
							confirmBubbleWidth = 170;
							xStart = xConfirmBubble + 50;
							yStart = yConfirmBubble + 45;
						}
						if (confirmBubbleHeight >= 70)
						{
							confirmBubbleHeight = 70;
						}
						if (cancelBubbleWidth >= 130)
						{
							cancelBubbleWidth = 130;
							xCancel = xCancelBubble + 23;
							yCancel = yCancelBubble + 27;
						}
						if (cancelBubbleHeight >= 40)
						{
							cancelBubbleHeight = 40;
						}

						if (xBlueScorePane >= -20)
						{
							xBlueScorePane = -20;
						}

						if (fadeTransBottom >= 100 && !chosen)
						{
							fadeTransBottom = 100;
						}
					}
					//If a character is not selected, set the pictures and text to its default settings
					else
					{
						xStart = -100;
						yStart = -100;
						xCancel = -100;
						yCancel = -100;

						headerText = "Welcome to WarioWare Touched!";
						mainText = " Poke someone below to play their mad microgames!";

						strHeadWidth = fmHead.stringWidth(headerText);
						strMainWidth = fmMain.stringWidth(mainText);

						fadeTransBottom -= 10;

						xMainIcon += 10;
						ySubTitle += 10;
						xMainTitle -= 25;

						yCharTitle -= 10;
						xCharIcon += 20;

						confirmBubbleWidth -= 30;
						confirmBubbleHeight -= 20;
						cancelBubbleWidth -= 30;
						cancelBubbleHeight -= 20;

						xBlueScorePane -= 25;

						if (xMainIcon >= 40)
						{
							xMainIcon = 40;
						}
						if (ySubTitle >= 30)
						{
							ySubTitle = 30;
						}
						if (xMainTitle <= 155)
						{
							xMainTitle = 155;
						}

						if (yCharTitle <= -80)
						{
							yCharTitle = -80;
						}
						if (xCharIcon >= WWTProgram.FRAME_WIDTH)
						{
							xCharIcon = WWTProgram.FRAME_WIDTH;
						}

						if (confirmBubbleWidth <= 0)
						{
							confirmBubbleWidth = 0;
						}
						if (confirmBubbleHeight <= 0)
						{
							confirmBubbleHeight = 0;
						}
						if (cancelBubbleWidth <= 0)
						{
							cancelBubbleWidth = 0;
						}
						if (cancelBubbleHeight <= 0)
						{
							cancelBubbleHeight = 0;
						}

						if (xBlueScorePane <= -250)
						{
							xBlueScorePane = -250;
						}

						if (fadeTransBottom <= 0 && !chosen)
						{
							fadeTransBottom = 0;
						}
					}

					/* When a character is selected, a bubble will continually increase in size until a certain amount. These two lines of
					 * of code ensure that those changes will be recognized when they're being drawn in the paint method
					 */
					confirmBubble = new Ellipse2D.Double(xConfirmBubble, yConfirmBubble, confirmBubbleWidth, confirmBubbleHeight);
					cancelBubble = new Ellipse2D.Double(xCancelBubble, yCancelBubble, cancelBubbleWidth, cancelBubbleHeight);

					blueTint += 25;
					pinkTint += 25;
					yellowTint += 25;

					if (blueTint >= 175)
					{
						blueTint = 50;
					}
					if (pinkTint >= 200)
					{
						pinkTint = 30;
					}
					if (yellowTint >= 200)
					{
						yellowTint = 30;
					}

				}
			}

			/* When the user clicks any of the buttons, the variable 'fadeTrans' will increase continuously. This allows the transparent 
			 * rectangle drawn over the whole JPanel to become opaque over a short amount of time. When the opaqueness variable reaches
			 * 255, the program will have to make a decision, based on what button was clicked. The JFrame will be replaced with the new
			 * JPanel and the JFrame will be validated while the timer stops to prevent complications from the previous JPanel
			 */
			if (chosen)
			{
				fadeTrans += 10;

				if (fadeTrans >= 255)
				{
					fadeTrans = 255;

					if (selected[1])
					{
						WWTProgram.frame.setContentPane(new GameScreen());
					}
					else if (selected[2])
					{
						WWTProgram.frame.setContentPane(new ScoreScreen());	
					}
					else if (selected[3])
					{
						WWTProgram.frame.setContentPane(new OptionScreen());	
					}
					else
					{
						WWTProgram.frame.setContentPane(new PlayScreen());	
					}

					WWTProgram.frame.validate();
					tmrMain.stop();
				}
			}

			if (aniCounter >= 2)
			{
				imgSubtitle = new ImageIcon("images\\main\\toySubtitle" + imgCounter + ".png");
				xMainIcon += xValues[imgCounter];
				yMainIcon = yValues[imgCounter];

				imgCounter++;
				aniCounter = 0;
			}

			repaint();
		}
	}
	
	
	class MouseListener extends MouseAdapter
	{
		/* When any of the buttons are clicked (Toyrooms, Games, Scores, Options, etc), the buttons will have a 'pressed' effect
		 * and a pressed boolean respective to which button was pressed will be set to true. This is needed to that the user cannot release
		 * the mouse on another button, which will make him/her go to that JPanel screen instead of the one he/she initially clicked
		 */
		
		public void mousePressed(MouseEvent e)
		{
			for (int i = 0; i < yButton.length; i++)
			{
				if (e.getY() >= yButton[i] && e.getY() <= yButton[i] + imgMainButton[i].getIconHeight() &&
						e.getX() >= xButton[i] && e.getX() <= xButton[i] + imgMainButton[i].getIconWidth() && !selected[i] 
								&& !chars.getSelected() && !chosen && fadeTrans == 0)
				{
					yButton[i]++;
					held[i] = true;
				}
			}
		}

		/* When the user released the mouse, and the user is still within the bounds of the original button he/she clicked, the button
		 * will appear to rise back up and the 'pressed' boolean of the respective button will be reset back to false. All the 'selected'
		 * boolean will be set to false except for the button that was clicked. The colorful outline will be switched to the button that
		 * was clicked and the 'chosen' boolean will be set to true. In the timer, if chosen is set to true, the screen will fade black. 
		 * When the screen is completely black, the JPanel will be replaced with whatever 'selected' boolean is set to true.
		 */
		public void mouseReleased(MouseEvent e)
		{
			for (int i = 0; i < yButton.length; i++)
			{
				if (e.getY() >= yButton[i] && e.getY() <= yButton[i] + imgMainButton[i].getIconHeight() &&
						e.getX() >= xButton[i] && e.getX() <= xButton[i] + imgMainButton[i].getIconWidth() && !selected[i] 
								&& !chars.getSelected() && !chosen && fadeTrans == 0)
				{
					yButton[i]--;
					held[i] = false;

					for (int k = 0; k < yButton.length; k++)
					{
						selected[k] = false;
					}

					selected[i] = true;

					selectedOutline = new RoundRectangle2D.Double(xButton[i], yButton[i], 
							imgMainButton[i].getIconWidth() - 1, imgMainButton[i].getIconHeight(), 15, 15);

					chosen = true;
				}
			}
		}

		/* This method deals with actions on the first 'click', regardless if the mouse is holding it down or releasing it much later than
		 * its press. This is initially only dealing with the characters. If the mouse clicks within the boundaries of the character, the bottom
		 * screen will slightly darken to put emphasis on the character clicked (a separate rectangle for the bottom screen and transparency
		 * variable is used for this). Two bubbles will begin to appear: a Confirm bubble and a Cancel bubble. Both bubbles set their x and y
		 * locations near the character, depending on where he moved. The program will check if he's on the upper or lower half of the bottom
		 * screen to make sure the bubbles don't pass the boundaries of the bottom screen, and it also checks if the character is on the left 
		 * or right side. The bubble's width and height will continue to increase until a certain size.
		 */
		public void mouseClicked(MouseEvent e)
		{
			if (e.getY() >= chars.getY() && e.getY() <= chars.getY() + chars.getHeight() &&
					e.getX() >= chars.getX() && e.getX() <= chars.getX() + chars.getWidth())
			{
				if (!chars.getSelected())
				{
					if (chars.getX() <= WWTProgram.FRAME_WIDTH/2)
					{
						xConfirmBubble = chars.getX() + chars.getWidth();
					}
					else
					{
						xConfirmBubble = chars.getX() - 170;
					}

					if (chars.getY() <= (WWTProgram.rectHeight1)  + (WWTProgram.rectHeight2 - chars.getHeight())/2 - 20)
					{
						yConfirmBubble = chars.getY();
					}
					else
					{
						yConfirmBubble = chars.getY() - 100;
					}

					xCancelBubble = xConfirmBubble + 20;
					yCancelBubble = yConfirmBubble + 77;

					xMessage = WWTProgram.FRAME_WIDTH;
				}

				chars.setSelected(true); //Set the 'selected' boolean of the character to true so certain actions can take place

				//Set the picture and text to the respective character that was selected.
				if (chars.getImageNum() == 0)
				{
					imgCharTitle = new ImageIcon("images\\main\\wariomanTitle.png");
					imgCharIcon = new ImageIcon("images\\main\\warioManIcon.png");
				}
			}

			/* If the user clicks the confirm button, set the 'chosen' boolean once again to true, so the screen will fade. Futher action from
			 * there, will be taken.
			 */
			if (e.getY() >= yConfirmBubble && e.getY() <= yConfirmBubble + confirmBubbleHeight &&
					e.getX() >= xConfirmBubble && e.getX() <= xConfirmBubble + confirmBubbleWidth && !chosen)
			{
				chosen = true;
			}
			/* If the user, for any reason, decides to click the cancel button, set the boolean back to false, decrease the size of the 
			 * bubbles until they are nowhere to be seen, and re-light the bottom screen again to lose focus on the character that was
			 * selected. Also change the pictures and scroll-text back to its default settings.
			 */
			else if (e.getY() >= yCancelBubble && e.getY() <= yCancelBubble + cancelBubbleHeight &&
					e.getX() >= xCancelBubble && e.getX() <= xCancelBubble + cancelBubbleWidth)
			{
				if (chars.getSelected())
				{
					xMessage = WWTProgram.FRAME_WIDTH;
				}

				chars.setSelected(false); //Set the char method boolean back to false.
			}
		}
	}

	class MouseMotionListener extends MouseMotionAdapter
	{
		/* If a button was to be clicked, and while clicking on it, the user decides to move out of the bounds of the buttons, the button 
		 * that was pressed down will return to its initial position, with its 'pressed' boolean set back to false. This is to ensure the
		 * button will return back to its initial position even if its not successfully pressed after being pushed down
		 */
		public void mouseDragged(MouseEvent e)
		{
			for (int i = 0; i < yButton.length; i++)
			{

				if ((e.getY() <= yButton[i] || e.getY() >= yButton[i] + imgMainButton[i].getIconHeight()) ||
						(e.getX() <= xButton[i] || e.getX() >= xButton[i] + imgMainButton[i].getIconWidth()))
				{
					if (held[i])
					{
						yButton[i]--;
						held[i] = false;
					}
				}
			}
		}
	}

	/* Paint method. Draws all the visual aspects of the program on the JPanel. Most shape objects and images have their own variables for
	 * x, y, width, height, and transparency due to these values constantly changing.
	 */
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.drawImage(new ImageIcon("images\\main\\topBackground.png").getImage(), WWTProgram.topX, WWTProgram.topY, null);
		g2.drawImage(new ImageIcon("images\\main\\bottomBackground.png").getImage(), WWTProgram.bottomX, WWTProgram.bottomY, null);

		g2.drawImage(new ImageIcon("images\\main\\mainTitle.png").getImage(), xMainTitle, yMainTitle, null);
		g2.drawImage(new ImageIcon("images\\main\\mainIcon.png").getImage(), xMainIcon, yMainIcon, null);
		g2.drawImage(imgSubtitle.getImage(), xSubTitle, ySubTitle, null);

		g2.drawImage(imgCharTitle.getImage(), xCharTitle, yCharTitle, null);
		g2.drawImage(imgCharIcon.getImage(), xCharIcon, yCharIcon, null);

		g2.setFont(new Font("Bauhaus 93", Font.PLAIN, 18));

		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(22));
		g2.draw(new Line2D.Double(0, 300, WWTProgram.FRAME_WIDTH, 300));
		g2.setColor(Color.YELLOW);
		g2.drawString(headerText, xMessage, 306);
		g2.setColor(Color.WHITE);
		g2.drawString(mainText, xMessage + strHeadWidth, 306);

		g2.setColor(new Color (15, 15, 15));
		g2.setStroke(new BasicStroke(35));
		g2.draw(new Line2D.Double(0, 631, WWTProgram.FRAME_WIDTH, 631));

		for (int i = 0; i < imgMainButton.length; i++)
		{
			g2.drawImage(imgMainButton[i].getImage(), xButton[i], yButton[i], null); //10, 130, 250, 370
		}

		g2.setColor(c);
		g2.setStroke(new BasicStroke(1));
		g2.draw(selectedOutline);

		if (!chosen)
		{
			g2.setColor(new Color(0, 0, 0, fadeTransBottom));
			g2.fill(rect2);	
		}

		chars.drawCharacter(g2);

		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(7));
		g2.fill(confirmBubble);
		g2.fill(cancelBubble);

		g2.setFont(new Font("Bauhaus 93", Font.PLAIN, 30));
		g2.setColor(new Color(0, blueTint, 255));
		g2.drawString("Start!", xStart, yStart);
		g2.draw(confirmBubble);

		g2.setFont(new Font("Bauhaus 93", Font.PLAIN, 23));
		g2.setColor(new Color(255, 36, pinkTint)); //124
		g2.drawString("Forget it", xCancel, yCancel);
		g2.draw(cancelBubble);

		g2.setColor(new Color(255, yellowTint, 0));
		g2.setFont(new Font("Forte", Font.PLAIN, 40));
		g2.drawString("Highscores", xBlueScorePane + 24, 163);

		g2.setColor(Color.BLUE);
		g2.fill(new RoundRectangle2D.Double(xBlueScorePane, yBlueScorePane, 205, 27, 20, 20));
		g2.fill(new RoundRectangle2D.Double(xBlueScorePane, yBlueScorePane + 35, 230, 27, 20, 20));
		g2.fill(new RoundRectangle2D.Double(xBlueScorePane, yBlueScorePane + 70, 255, 27, 20, 20));

		g2.setStroke(new BasicStroke(1));
		g2.setFont(new Font("Bauhaus 93", Font.PLAIN, 13));

		/* Array for the score visual components. Colors are determined based on what 'line' of scores is being draw. 1st Place score 
		 * component is green, 2nd place is orange, 3rd is magenta, etc
		 */
		for (int i = 0; i < 3; i++)
		{
			g2.drawImage(new ImageIcon("images\\main\\imgScorePane" + (i + 1) + ".png").getImage(), 
					xBlueScorePane + 20, yBlueScorePane + (i * 35) + 3, null);

			if (i == 0)
			{
				g2.setColor(Color.GREEN);
			}
			else if(i == 1)
			{
				g2.setColor(Color.ORANGE);
			}
			else
			{
				g2.setColor(Color.MAGENTA);

			}
		}
		
		//Statement on place-colours above also applies to place-fonts for text.
		for (int i = 0; i < players.size(); i++)
		{
			g2.setColor(Color.WHITE);
			g2.drawString(players.get(i).getName(), xBlueScorePane + 58, yBlueScorePane + (i * 35) + 17);

			if (i == 0)
			{
				g2.setColor(Color.GREEN);
			}
			else if(i == 1)
			{
				g2.setColor(Color.ORANGE);
			}
			else
			{
				g2.setColor(Color.MAGENTA);

			}

			g2.drawString(fm.format((players.get(i).getScore())), xBlueScorePane + 168 + (i * 25), yBlueScorePane + (i * 35) + 17);
			
			if (i >= 2)
			{
				break;
			}
		}

		g2.setStroke(new BasicStroke(7));
		g2.setColor(new Color (15, 15, 15));
		g2.draw(rect1);
		g2.draw(rect2);

		//Black fading rectangle over all JPanels. Will only be drawn when chosen is true or the opening is false.
		g2.setColor(new Color(0, 0, 0, fadeTrans));
		if (!opening || chosen)
		{
			g2.fill(fadeRect);
		}
	}
}