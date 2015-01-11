import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import javax.swing.*;

public class ScoreScreen extends JPanel implements ActionListener
{

	//Static constants of the rectangles placed on the panels to give it the visual effect of a Nintendo DS screen
	public static final Rectangle2D.Double rect1 = new Rectangle2D.Double(WWTProgram.topX, WWTProgram.topY,
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2);

	public static final Rectangle.Double rect2 = new Rectangle2D.Double(WWTProgram.bottomX, WWTProgram.bottomY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2 - 17);

	public static final Rectangle.Double fadeRect = new Rectangle2D.Double(WWTProgram.topX, WWTProgram.topY, 
			WWTProgram.FRAME_WIDTH, WWTProgram.FRAME_HEIGHT);

	//Declare global variables
	private Timer tmrGame;
	private ImageIcon[] imgMainButton;
	private String headerText, mainText, scoreText;
	private int fadeTrans, xMessage, strHeadWidth, strMainWidth, strScoreWidth, colChangeCounter, colCounter, yellowTint;
	private int[] xButton, yButton;
	private RoundRectangle2D.Double selectedOutline;
	private FontMetrics fmHead, fmMain, fmScore;
	private boolean[] selected, held;
	private boolean chosen;
	private Font textFont, scoreFont;
	private Color[] colValue;
	private Color c;
	private JTextArea txtOutput;
	private File textFile;
	
	private LinkedList<Scores> scores;

	public ScoreScreen()
	{
		/* Declare following x and y integer components, timers, intervals, Strings, arrays of text, x and y positions, colours, and JPanel
		 * settings for the visual components of the game. The textFile is for the 'scores' TXT file to read from a file and display 
		 * the information
		 */
		tmrGame = new Timer(30, this);
		tmrGame.start();
		
		scores = new LinkedList<Scores>();
		
		textFile = new File("scores.txt");
		
		txtOutput = new JTextArea(20, 70);
		txtOutput.setFont(new Font("Courier New", Font.BOLD, 12));
		txtOutput.setForeground(new Color(0, 150, 255));
		txtOutput.setMargin(new Insets(5, 5, 5, 5));
		txtOutput.setEditable(false);
		
		txtOutput.setBounds(WWTProgram.FRAME_WIDTH/2 - 375/2, 45, 370, 230);
		txtOutput.setText(String.format("%-2s%-10s%-25s%5s\n\n", "", "PLACE", "NAME", "SCORE"));
		
		/* Data in this game can be continuously updated and changed while being played. This being so, the program needs to update
		 * its information for the score-board every time this JPanel is clicked. That being so, this JPanel will read from the textfile
		 * 'score' every time the Score button is clicked. The data from the comma-delimited file will be saved into a LinkedList of Score
		 * objects and outputted to a JTextArea after it is sorted by name and high score.
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
					
					scores.add(new Scores(data[0], Integer.parseInt(data[1])));
					
					line = in.readLine();
				}
				
				in.close();
			}
			catch(IOException e)
			{
				txtOutput.setText(e.getMessage() + "!");
			}
		}
		
		//Sorts by score. If two players have the same score, sort by name.
		Scores.sortOrder = Scores.SORT_BY_NAME;
		Collections.sort(scores);
		Scores.sortOrder = Scores.SORT_BY_SCORE;
		Collections.sort(scores, Collections.reverseOrder());
		
		//Output scores in highest order to the JTextArea
		for (int i = 0; i < scores.size(); i++)
		{
			txtOutput.append(String.format("%-2s%-10s%-30s", "", i + 1, scores.get(i).toString()));
		}

		//More x/y components and string settings for the visual aspect of the JPanel
		fadeTrans = 255;
		yellowTint = 0;

		xMessage = WWTProgram.FRAME_WIDTH;

		headerText = "[High Scores]";
		mainText = " Use this room to view your high scores! Show off your hard-earned points to the losers!";
		scoreText = "Highscores";

		textFont = new Font ("Bauhaus 93", Font.PLAIN, 18);
		scoreFont = new Font("Forte", Font.PLAIN, 38);

		xButton = new int[]{10, 130, 250, 370};
		yButton = new int[4];

		held = new boolean[4];
		selected = new boolean[4];

		selected[2] = true;

		colValue = new Color[]{Color.RED, Color.CYAN, Color.WHITE};

		for (int i = 0; i < yButton.length; i++)
		{
			yButton[i] = 616;
		}

		fmHead = getFontMetrics(textFont);
		fmMain = getFontMetrics(textFont);
		fmScore = getFontMetrics(scoreFont);
		strHeadWidth = fmHead.stringWidth(headerText);
		strMainWidth = fmMain.stringWidth(mainText);
		strScoreWidth = fmScore.stringWidth(scoreText);

		imgMainButton = new ImageIcon[4];
		imgMainButton[0] = new ImageIcon("images\\main\\toyroomButton.png");
		imgMainButton[1] = new ImageIcon("images\\main\\gamesButton.png");
		imgMainButton[2] = new ImageIcon("images\\main\\highScoreButton.png");
		imgMainButton[3] = new ImageIcon("images\\main\\optionsButton.png");

		selectedOutline = new RoundRectangle2D.Double(xButton[2], yButton[2], 
				imgMainButton[2].getIconWidth() - 1, imgMainButton[2].getIconHeight(), 15, 15);

		//Settings for the JPanel
		setLayout(null);
		add(txtOutput);
		addMouseListener(new MouseListener());
		addMouseMotionListener(new MouseMotionListener());
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		/* When the boolean 'chosen' is set to false, the fadeTrans variable will continuously decrease, as if a black screen is lightning up again. 
		 * This will continue until the fadeTrans variable is set to 0.
		 */
		if (!chosen)
		{
			fadeTrans -= 10;

			if (fadeTrans <= 0)
			{
				fadeTrans = 0;
			}
		}

		// Timer allows the x/y components and string values to change continuously as the program runs
		xMessage -= 2;

		if (xMessage + strHeadWidth + strMainWidth <= 0 )
		{
			xMessage = WWTProgram.FRAME_WIDTH;
		}

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
		
		yellowTint += 25;

		if (yellowTint >= 250)
		{
			yellowTint = 0;
		}

		//Screen blacks out and changes JPanel based on which button was clicked.
		if (chosen)
		{
			fadeTrans += 10;

			if (fadeTrans >= 255)
			{
				fadeTrans = 255;

				if (selected[0])
				{
					WWTProgram.frame.setContentPane(new MainScreen());
				}
				else if (selected[1])
				{
					WWTProgram.frame.setContentPane(new GameScreen());	
				}
				else if (selected[3])
				{
					WWTProgram.frame.setContentPane(new OptionScreen());	
				}
				else
				{
					WWTProgram.frame.setContentPane(new TitleScreen());	
				}

				WWTProgram.frame.validate();
				tmrGame.stop();
			}
		}

		repaint();
	}

	/* Buttons will follow the if-statements and conditions set by the program to when it will be presesd down, when booleans will be
	 * set to true/false, and when to change screens.
	 */
	class MouseListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			for (int i = 0; i < yButton.length; i++)
			{
				if (e.getY() >= yButton[i] && e.getY() <= yButton[i] + imgMainButton[i].getIconHeight() &&
						e.getX() >= xButton[i] && e.getX() <= xButton[i] + imgMainButton[i].getIconWidth()
						&& !selected[i] && !chosen && fadeTrans == 0)
				{
					yButton[i]++;
					held[i] = true;
				}
			}
		}

		public void mouseReleased(MouseEvent e)
		{
			for (int i = 0; i < yButton.length; i++)
			{
				if (e.getY() >= yButton[i] && e.getY() <= yButton[i] + imgMainButton[i].getIconHeight() &&
						e.getX() >= xButton[i] && e.getX() <= xButton[i] + imgMainButton[i].getIconWidth() 
						&& !selected[i] && !chosen && fadeTrans == 0)
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
	}

	class MouseMotionListener extends MouseMotionAdapter
	{
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

	// Method to draw certain things on the JPanel
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setStroke(new BasicStroke(7));

		g2.drawImage(new ImageIcon("images\\main\\topBackground1.png").getImage(), WWTProgram.topX, WWTProgram.topY, null);
		g2.drawImage(new ImageIcon("images\\main\\bottomBackground1.png").getImage(), WWTProgram.bottomX, WWTProgram.bottomY, null);

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
		
		g2.setColor(new Color(255, 36, yellowTint));
		g2.setFont(scoreFont);
		g2.drawString("Highscores", WWTProgram.FRAME_WIDTH/2 - strScoreWidth/2, 31);
		
		g2.setStroke(new BasicStroke(15));
		g2.draw(new RoundRectangle2D.Double(WWTProgram.FRAME_WIDTH/2 - 375/2, 45, 370, 230, 5, 5));
		
		g2.drawImage(new ImageIcon("images\\main\\mainTitle1.png").getImage(), (436 - 305)/2 - 10, (WWTProgram.rectHeight1)  +
				(WWTProgram.rectHeight2 - 156)/2 - 20, null);

		g2.setColor(c);
		g2.setStroke(new BasicStroke(1));
		g2.draw(selectedOutline);
		
		g2.setStroke(new BasicStroke(7));
		g2.setColor(new Color (15, 15, 15));
		g2.draw(rect1);
		g2.draw(rect2);

		g2.setColor(new Color(0, 0, 0, fadeTrans));
		g2.fill(fadeRect);
	}
}