/** This JPanel was originally intended for prizes and minigames when the user scores a certain amount of points in the 
 * main game. Minigames would be little icons on the bottom screen the user could click and be brought to another screen to play.
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;

public class GameScreen extends JPanel implements ActionListener
{

	//Static constants of the rectangles placed on the panels to give it the visual effect of a Nintendo DS screen
	public static final Rectangle2D.Double rect1 = new Rectangle2D.Double(WWTProgram.topX, WWTProgram.topY,
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2);

	public static final Rectangle.Double rect2 = new Rectangle2D.Double(WWTProgram.bottomX, WWTProgram.bottomY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2 - 17);

	public static final Rectangle.Double fadeRect = new Rectangle2D.Double(WWTProgram.topX, WWTProgram.topY, 
			WWTProgram.FRAME_WIDTH, WWTProgram.FRAME_HEIGHT);

	// Declare Global variables
	private Timer tmrGame;
	private ImageIcon[] imgMainButton;
	private String headerText, mainText;
	private int fadeTrans, xMessage, strHeadWidth, strMainWidth, colChangeCounter, colCounter;
	private int[] xButton, yButton;
	private RoundRectangle2D.Double selectedOutline;
	private FontMetrics fmHead, fmMain;
	private boolean[] selected, held;
	private boolean chosen;
	private Font textFont;
	private Color[] colValue;
	private Color c;

	public GameScreen()
	{
		//Declare timer and set its interval. Declare and set all the other components, images, and string's default data.
		tmrGame = new Timer(30, this);
		tmrGame.start();

		fadeTrans = 255;

		xMessage = WWTProgram.FRAME_WIDTH;

		headerText = "[Game Room]";
		mainText = " Chuck your souvenirs into this room! You can come back anytime to mess around with 'em!";

		textFont = new Font ("Bauhaus 93", Font.PLAIN, 18);

		xButton = new int[]{10, 130, 250, 370};
		yButton = new int[4];

		held = new boolean[4];
		selected = new boolean[4];

		selected[1] = true;

		colValue = new Color[]{Color.RED, Color.CYAN, Color.WHITE};

		for (int i = 0; i < yButton.length; i++)
		{
			yButton[i] = 616;
		}

		fmHead = getFontMetrics(textFont);
		fmMain = getFontMetrics(textFont);
		strHeadWidth = fmHead.stringWidth(headerText);
		strMainWidth = fmMain.stringWidth(mainText);

		imgMainButton = new ImageIcon[4];
		imgMainButton[0] = new ImageIcon("images\\main\\toyroomButton.png");
		imgMainButton[1] = new ImageIcon("images\\main\\gamesButton.png");
		imgMainButton[2] = new ImageIcon("images\\main\\highScoreButton.png");
		imgMainButton[3] = new ImageIcon("images\\main\\optionsButton.png");

		selectedOutline = new RoundRectangle2D.Double(xButton[1], yButton[1], 
				imgMainButton[1].getIconWidth() - 1, imgMainButton[1].getIconHeight(), 15, 15);

		setLayout(null);
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
		
		// Timer changes certain x and y components for the images and text displayed in this screen
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

		/* If chosen is equal to true, the fadeTrans variable will continuously add up, and the screen will darken, to which when at its 
		 * maximum, it will make a choice based on which boolean is equal to true.
		 */
		
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
		public void mouseMoved(MouseEvent e){} 
	}

	// Method to draw certain things on the JPanel
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setStroke(new BasicStroke(7));

		g2.drawImage(new ImageIcon("images\\main\\topBackground.png").getImage(), WWTProgram.topX, WWTProgram.topY, null);
		g2.drawImage(new ImageIcon("images\\main\\bottomBackground2.png").getImage(), WWTProgram.bottomX, WWTProgram.bottomY, null);

		g2.drawImage(new ImageIcon("images\\main\\mainTitle.png").getImage(), WWTProgram.FRAME_WIDTH/2 - 271/2, 
				WWTProgram.rectHeight1/2 - 100, null);

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

		g2.setStroke(new BasicStroke(7));
		g2.setColor(new Color (15, 15, 15));
		g2.draw(rect1);
		g2.draw(rect2);

		g2.setColor(new Color(0, 0, 0, fadeTrans));
		g2.fill(fadeRect);
	}
}