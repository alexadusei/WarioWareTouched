import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

public class OptionScreen extends JPanel implements ActionListener
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
	private ImageIcon imgBird, imgYes, imgNo, imgOk;
	private ImageIcon[] imgMainButton;
	private String[] optionText, birdText;
	private String headerText, mainText, birdDirection, leftText, rightText;
	private int counter, yConfirmButton, xYes, xNo, xOk, yOk, xLeft, xRight, yWord, rc1Width, rc1Height, rc2Width, rc2Height, rcY;
	private int fadeTrans, xMessage, strHeadWidth, strMainWidth, colChangeCounter, colCounter, xOptionPane, greenTint, yellowTint, pinkTint, blueTint;
	private int xSpeech, ySpeech, speechWidth, speechHeight, speechDiameter, xRule;
	private int[] xButton, yButton, yOptionPane, optionPaneWidth, strOptionWidth, xText, yText;
	private RoundRectangle2D.Double selectedOutline;
	private Ellipse2D.Double speechBubble, speechOpening;
	private FontMetrics fmHead, fmMain;
	private FontMetrics[] fmOptions;
	private boolean[] selected, held, optSelected;
	private boolean chosen, picked, yesHeld, noHeld;
	private Font textFont;
	private Color[] colValue, optCols;
	private Color c, c1, c2;

	private File textFile; //Declare a file variable

	public OptionScreen()
	{
		/* Declare following x and y integer components, timers, intervals, Strings, arrays of text, x and y positions, colours, and JPanel
		 * settings for the visual components of the game. The textFile is for the 'scores' TXT file to read from a file and display 
		 * the information
		 */
		textFile = new File("scores.txt");

		tmrGame = new Timer(30, this);
		tmrGame.start();	

		c1 = Color.BLACK;
		c2 = Color.BLACK;

		leftText = "left";
		rightText = "right";

		xRule = -350;
		
		yWord = 540;
		xLeft = -100;
		xRight = -100;

		rc1Width = 65;
		rc1Height = 35;
		rc2Width = 85;
		rc2Height = 40;
		rcY = yWord - 30;

		birdText = new String[4];
		xText = new int[]{119, 83, 70, 120};
		yText = new int[]{388, 415, 444, 474};

		for (int i = 0; i < birdText.length; i++)
		{
			birdText[i] = "";
		}

		yConfirmButton = 500;
		xYes = -50;
		xNo = -50;
		
		yOk = 555;

		birdDirection = "Right";

		imgBird = new ImageIcon("images\\main\\bird" + birdDirection + counter + ".png");
		imgYes = new ImageIcon("images\\main\\yesButton0.png");
		imgNo = new ImageIcon("images\\main\\noButton0.png");
		imgOk = new ImageIcon("images\\main\\okButton0.png");

		xSpeech = (WWTProgram.FRAME_WIDTH - 350)/2 - 5;
		ySpeech = (WWTProgram.rectHeight1)  + (WWTProgram.rectHeight2 - 250)/2 - 20;

		speechBubble = new Ellipse2D.Double(xSpeech - 5, ySpeech, speechWidth, speechHeight);
		speechOpening = new Ellipse2D.Double(368 - 5, 553, speechDiameter, speechDiameter);

		fadeTrans = 255;
		xOptionPane = -20;

		greenTint = 50;
		yellowTint = 30;
		pinkTint = 30;
		blueTint = 100;

		xMessage = WWTProgram.FRAME_WIDTH;

		headerText = "[Options]";
		mainText = " It's always gotta be about you huh? Now you're learning! Set the game options just how you want 'em!";

		textFont = new Font ("Bauhaus 93", Font.PLAIN, 18);

		xButton = new int[]{10, 130, 250, 370};
		yButton = new int[4];

		held = new boolean[4];
		selected = new boolean[4];
		optSelected = new boolean[4];

		selected[3] = true;

		yOptionPane = new int[4];
		optionPaneWidth = new int[4];

		optionText = new String[]{"How To Play", "Change Hands", "Erase Data", "Title Screen"};

		for (int i = 0; i < yOptionPane.length; i++)
		{
			yOptionPane[i] = 353 + (65 * i);
			optionPaneWidth[i] = 350 - (50 * i);
		}

		colValue = new Color[]{Color.RED, Color.CYAN, Color.WHITE};
		optCols = new Color[]{new Color(0, greenTint, 0), new Color(255, yellowTint, 0), new Color(255, 0, pinkTint), new Color(0, blueTint, 255)};

		for (int i = 0; i < yButton.length; i++)
		{
			yButton[i] = 616;
		}

		strOptionWidth = new int[4];
		fmOptions = new FontMetrics[4];

		for (int i = 0; i < fmOptions.length; i++)
		{
			fmOptions[i] = getFontMetrics(new Font("Bauhaus 93", Font.PLAIN, 30));
			strOptionWidth[i] = fmOptions[i].stringWidth(optionText[i]);
		}

		xOk = xRule + 350/2 - imgOk.getIconWidth()/2;
		
		fmHead = getFontMetrics(textFont);
		fmMain = getFontMetrics(textFont);
		strHeadWidth = fmHead.stringWidth(headerText);
		strMainWidth = fmMain.stringWidth(mainText);

		imgMainButton = new ImageIcon[4];
		imgMainButton[0] = new ImageIcon("images\\main\\toyroomButton.png");
		imgMainButton[1] = new ImageIcon("images\\main\\gamesButton.png");
		imgMainButton[2] = new ImageIcon("images\\main\\highScoreButton.png");
		imgMainButton[3] = new ImageIcon("images\\main\\optionsButton.png");

		selectedOutline = new RoundRectangle2D.Double(xButton[3], yButton[3], 
				imgMainButton[3].getIconWidth() - 1, imgMainButton[3].getIconHeight(), 15, 15);

		setLayout(null);
		addMouseListener(new MouseListener());
		addMouseMotionListener(new MouseMotionListener());
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		/* Checks which of the option buttons was clicked. If it is the first three, the little bird to the right will undergo
		 * a speaking animation and certain bubbles and visual rectangles will be drawn on the screen. If these first three buttons
		 * weren't clicked (e.g, the TitleScreen button was clicked). The JPanel will fade to black and will return to the Title Screen without
		 * the bird talking.
		 */
		if (optSelected[0] || optSelected[1] || optSelected[2])
		{
			imgBird = new ImageIcon("images\\main\\bird" + birdDirection + counter + ".png");
			counter++;
			
			if (counter > 1)
			{
				counter = 0;
			}
		}
		else
		{
			imgBird = new ImageIcon("images\\main\\bird" + birdDirection + 0 + ".png");
		}

		//Other x/y values and color values for the visual aspects of the Screen
		greenTint += 20;
		yellowTint += 20;
		pinkTint += 50;
		blueTint += 20;

		if (greenTint >= 250)
		{
			greenTint = 125;
		}
		if (yellowTint >= 200)
		{
			yellowTint = 75;
		}
		if (pinkTint >= 250)
		{
			pinkTint = 30;
		}
		if (blueTint >= 175)
		{
			blueTint = 50;
		}

		if (!chosen)
		{
			fadeTrans -= 10;

			if (fadeTrans <= 0)
			{
				fadeTrans = 0;
			}
		}

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

		if (picked)
		{
			xOptionPane -= 30;

			if (optSelected[0])
			{
				xRule += 25;
			}
			else if (optSelected[1] || optSelected[2])
			{
				speechWidth += 30;
				speechHeight += 30;
				speechDiameter++;
			}

			if (xRule >= (WWTProgram.FRAME_WIDTH - 390)/2)
			{
				xRule = (WWTProgram.FRAME_WIDTH - 390)/2;
			}
			
			if (xOptionPane <= -400)
			{
				xOptionPane = -400;
			}

			if (speechWidth >= 350)
			{
				speechWidth = 350;

				// Lines of text will be changed according to which button was clicked
				if (optSelected[2])
				{
					birdText[0] = "Are you sure you want";
					birdText[1] = "to clear all your data? Scores";
					birdText[2] = "and rewards can't be recovered!";
					birdText[3] = "";

					xYes = 150;
					xNo = 250;
				}
				if (optSelected[1])
				{
					birdText[0] = "Select which hand you";
					birdText[1] = "play with and the mouse will";
					birdText[2] = "be adjusted to your good hand.";
					birdText[3] = "(Gameplay may vary)";

					xLeft = 100;
					xRight = 250;
				}
			}
			if (speechHeight >= 250)
			{
				speechHeight = 250;
			}
			if (speechDiameter >= 15)
			{
				speechDiameter = 15;
			}

			xOk = xRule + (350 - imgOk.getIconWidth())/2;
		}
		else
		{
			if (!optSelected[0])
			{
				xRule -= 25;
				xOk = xRule + (300 - imgOk.getIconWidth())/2;
			}
			
			for (int i = 0; i < birdText.length; i++)
			{
				birdText[i] = "";
			}

			xYes = -50;
			xNo = -50;
			xLeft = -100;
			xRight = -100;

			xOptionPane += 30;

			speechWidth -= 30;
			speechHeight -= 30;
			speechDiameter--;

			if (xRule <= -350)
			{
				xRule = -350;
			}
			
			if (xOptionPane >= -20)
			{
				xOptionPane = -20;
			}

			if (speechWidth <= 0)
			{
				speechWidth = 0;
			}
			if (speechHeight <= 0)
			{
				speechHeight = 0;
			}
			if (speechDiameter <= 0)
			{
				speechDiameter = 0;
			}
		}

		speechBubble = new Ellipse2D.Double(xSpeech, ySpeech, speechWidth, speechHeight);
		speechOpening = new Ellipse2D.Double(363, 553, speechDiameter, speechDiameter);

		//Screen blackens and goes to respective JPanel based on which button was clicked
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
				else if (selected[2])
				{
					WWTProgram.frame.setContentPane(new ScoreScreen());	
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

	/* Buttons will follow the if-statements and conditions set by the program to when it will be pressed down, when booleans will be
	 * set to true/false, and when to change screens. This includes the new buttons involved in this JPanel, such as OK, YES, and NO
	 * buttons which are illusive images.
	 */
	class MouseListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			for (int i = 0; i < yButton.length; i++)
			{
				//If-statement for Mode-buttons (Toyroom, Games, Options, Highscores, etc)
				if (e.getY() >= yButton[i] && e.getY() <= yButton[i] + imgMainButton[i].getIconHeight() &&
						e.getX() >= xButton[i] && e.getX() <= xButton[i] + imgMainButton[i].getIconWidth() 
						&& !selected[i] && !chosen && fadeTrans == 0 && !optSelected[1] && !optSelected[2])
				{
					yButton[i]++;
					held[i] = true;
				}
				
				//If-statement for yes/no buttons
				else if (e.getY() >= yConfirmButton && e.getY() <= yConfirmButton + imgYes.getIconHeight() &&
						e.getX() >= xYes && e.getX() <= xYes + imgYes.getIconWidth())
				{
					imgYes = new ImageIcon("images\\main\\yesButton1.png");
					yesHeld = true;
				}
				else if (e.getY() >= yConfirmButton && e.getY() <= yConfirmButton + imgNo.getIconHeight() &&
						e.getX() >= xNo && e.getX() <= xNo + imgNo.getIconWidth())
				{
					imgNo = new ImageIcon("images\\main\\noButton1.png");
					noHeld = true;
				}
				else if (e.getY() >= yOk && e.getY() <= yOk + imgOk.getIconHeight() &&
						e.getX() >= xOk && e.getX() <= xOk + imgOk.getIconWidth())
				{
					imgOk = new ImageIcon("images\\main\\OkButton1.png");
				}
			}
		}

		public void mouseReleased(MouseEvent e)
		{
			for (int i = 0; i < yButton.length; i++)
			{
				//If-statement for releasing upon mode buttons
				if (e.getY() >= yButton[i] && e.getY() <= yButton[i] + imgMainButton[i].getIconHeight() &&
						e.getX() >= xButton[i] && e.getX() <= xButton[i] + imgMainButton[i].getIconWidth() 
						&& !selected[i] && !chosen && fadeTrans == 0 && !optSelected[1] && !optSelected[2])
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
				
				//If-statement upon releasing on other buttons
				else if (e.getY() >= yOptionPane[i] && e.getY() <= yOptionPane[i] + 50 &&
						e.getX() >= xOptionPane && e.getX() <= xOptionPane + optionPaneWidth[i])
				{
					for (int k = 0; k < yOptionPane.length; k++)
					{
						optSelected[k] = false;
					}

					optSelected[i] = true;
					

					if (!picked)
					{
						picked = true;
					}

					if (optSelected[3])
					{
						chosen = true;
					}
				}
			}

			//If-statement upon releasing on yes/no buttons
			if (e.getY() >= yConfirmButton && e.getY() <= yConfirmButton + imgYes.getIconHeight() &&
					e.getX() >= xYes && e.getX() <= xYes + imgYes.getIconWidth() && yesHeld)
			{
				for (int k = 0; k < yButton.length; k++)
				{
					optSelected[k] = false;
				}
				
				imgYes = new ImageIcon("images\\main\\yesButton0.png");	

				if (textFile.exists())
				{
					textFile.delete();
				}
				try
				{
					textFile.createNewFile();
				} 
				catch (IOException ex)
				{
					ex.getMessage();
				}

				picked = false;
				yesHeld = false;
			}
			else if (e.getY() >= yConfirmButton && e.getY() <= yConfirmButton + imgNo.getIconHeight() &&
					e.getX() >= xNo && e.getX() <= xNo + imgNo.getIconWidth() && noHeld)
			{
				for (int k = 0; k < yOptionPane.length; k++)
				{
					optSelected[k] = false;
				}
				
				imgNo = new ImageIcon("images\\main\\noButton0.png");
				picked = false;
				noHeld = false;
			}
			else if (e.getY() >= yOk && e.getY() <= yOk + imgOk.getIconHeight() &&
					e.getX() >= xOk && e.getX() <= xOk + imgOk.getIconWidth())
			{
				for (int k = 0; k < yOptionPane.length; k++)
				{
					optSelected[k] = false;
				}
				
				imgOk = new ImageIcon("images\\main\\OkButton0.png");
				picked = false;
			}

			//If-statement for "left-right buttons". (Initially has no use. Humour is in irony of left/right hand...)
			if((e.getY() >= rcY && e.getY() <= rcY + rc1Height &&
					e.getX() >= xLeft && e.getX() <= xLeft + rc1Width) ||
					(e.getY() >= rcY && e.getY() <= rcY + rc2Height &&
					e.getX() >= xRight && e.getX() <= xRight + rc2Width))
			{
				for (int k = 0; k < yOptionPane.length; k++)
				{
					optSelected[k] = false;
				}
				
				picked = false;
			}
		}

		public void mouseClicked(MouseEvent e){}
	}

	/* If-statements for is the user clicks the buttons but drags the mouse off of them, which will set them all back to their inital pictures
	 * and their initial boolean settings
	 */
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
			
			if ((e.getY() <= yConfirmButton || e.getY() >= yConfirmButton + imgYes.getIconHeight()) ||
					(e.getX() <= xYes || e.getX() >= xYes + imgYes.getIconWidth()))
			{
				imgYes = new ImageIcon("images\\main\\yesButton0.png");
				yesHeld = false;
			}
			if ((e.getY() <= yConfirmButton || e.getY() >= yConfirmButton + imgNo.getIconHeight()) ||
					(e.getX() <= xNo || e.getX() >= xNo + imgNo.getIconWidth()))
			{
				imgNo = new ImageIcon("images\\main\\noButton0.png");
				noHeld = false;
			}
			if ((e.getY() <= yOk || e.getY() >= yOk + imgOk.getIconHeight()) ||
					(e.getX() <= xOk || e.getX() >= xOk + imgOk.getIconWidth()))
			{
				imgOk = new ImageIcon("images\\main\\OkButton0.png");
			}
		}
		public void mouseMoved(MouseEvent e)
		{
			if (e.getY() >= rcY && e.getY() <= rcY + rc1Height &&
					e.getX() >= xLeft && e.getX() <= xLeft + rc1Width)
			{
				c1 = new Color(255, 36, 150);
			}
			else if(e.getY() >= rcY && e.getY() <= rcY + rc2Height &&
					e.getX() >= xRight && e.getX() <= xRight + rc2Width)
			{
				c2 = new Color(0, 50, 255);
			}
			else
			{
				c1 = Color.BLACK;
				c2 = Color.BLACK;
			}
		} 
	}

	// Draws certain things on the screen with changing x/y components, and certain String text
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setStroke(new BasicStroke(7));

		g2.drawImage(new ImageIcon("images\\main\\topBackground1.png").getImage(), WWTProgram.topX, WWTProgram.topY, null);
		g2.drawImage(new ImageIcon("images\\main\\bottomBackground1.png").getImage(), WWTProgram.bottomX, WWTProgram.bottomY, null);

		g2.drawImage(new ImageIcon("images\\main\\mainTitle1.png").getImage(), WWTProgram.FRAME_WIDTH/2 - 305/2, 
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
		g2.setFont(new Font("Bauhaus 93", Font.PLAIN, 30));

		optCols = new Color[]{new Color(0, greenTint, 0), new Color(255, yellowTint, 0), new Color(255, 36, pinkTint), new Color(0, blueTint, 255)};

		for (int i = 0; i < yOptionPane.length; i++)
		{
			g2.setColor(Color.WHITE);
			g2.fill(new RoundRectangle2D.Double(xOptionPane, yOptionPane[i], optionPaneWidth[i], 50, 30, 30));

			g2.setColor(optCols[i]);

			g2.drawString(optionText[i], xOptionPane + (optionPaneWidth[i] - strOptionWidth[i])/2 + 5, yOptionPane[i] + 35);
			g2.draw(new RoundRectangle2D.Double(xOptionPane, yOptionPane[i], optionPaneWidth[i], 50, 30, 30));
		}

		g2.drawImage(imgBird.getImage(), 385, 565, null);

		g2.setStroke(new BasicStroke(5));

		g2.setColor(Color.WHITE);
		g2.fill(speechBubble);
		g2.fill(speechOpening);

		g2.setColor(new Color(0, 200, 255));
		g2.draw(speechBubble);
		g2.draw(speechOpening);

		g2.setFont(new Font("Bauhaus 93", Font.PLAIN, 20));

		for (int i = 0; i < birdText.length; i++)
		{
			g2.drawString(birdText[i], xText[i], yText[i]);
		}

		g2.drawImage(imgYes.getImage(), xYes, yConfirmButton, null);
		g2.drawImage(imgNo.getImage(), xNo, yConfirmButton, null);

		g2.setFont(new Font("Bauhaus 93", Font.PLAIN, 40));
		g2.setColor(c1);
		g2.drawString(leftText, xLeft, yWord);
		g2.setColor(c2);
		g2.drawString(rightText, xRight, yWord);

		g2.setStroke(new BasicStroke(2));
		g2.setColor(Color.WHITE);
		g2.draw(new Rectangle2D.Double(xLeft, rcY, rc1Width, rc1Height));
		g2.draw(new Rectangle2D.Double(xRight, rcY, rc2Width, rc2Height));
		
		g2.setStroke(new BasicStroke(7));
		g2.setColor(Color.WHITE);
		g2.fill(new RoundRectangle2D.Double(xRule, 352, 350, 250, 30, 30));
		g2.setColor(new Color(0, 200, 255));
		g2.draw(new RoundRectangle2D.Double(xRule, 352, 350, 250, 30, 30));
		
		g2.setFont(new Font("Bauhaus 93", Font.ITALIC, 40));
		g2.drawString("Instructions", xRule + 75, 390);
		
		g2.setFont(new Font("Bauhaus 93", Font.PLAIN, 18));
		g2.drawString("WarioWare: Touched! ™  consists entirely", xRule + 10, 420);
		g2.drawString("of \"microgames\": short activities that last", xRule + 10, 440);
		g2.drawString("for a few seconds. Your job's to figure out", xRule + 10, 460);
		g2.drawString("what you must do before time runs out!", xRule + 10, 480);
		g2.drawString("A word or phrase is usually there to help", xRule + 10, 500);
		g2.drawString("you (E.g \"Jump!\", \"Scratch!\", etc)", xRule + 10, 520);
		
		g2.drawImage(imgOk.getImage(), xOk, yOk, null);

		g2.setColor(new Color (15, 15, 15));
		g2.draw(rect1);
		g2.draw(rect2);

		g2.setColor(new Color(0, 0, 0, fadeTrans));
		g2.fill(fadeRect);
	}
}