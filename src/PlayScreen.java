//This screen is the JPanel used when playing the main game of this program

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class PlayScreen extends JPanel implements ActionListener
{
	
	//Static constants of the rectangles placed on the panels to give it the visual effect of a Nintendo DS screen
	public static final Rectangle2D.Double rect1 = new Rectangle2D.Double(WWTProgram.topX, WWTProgram.topY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2);
	public static final Rectangle.Double rect2 = new Rectangle2D.Double(WWTProgram.bottomX, WWTProgram.bottomY, 
			WWTProgram.FRAME_WIDTH - 9, WWTProgram.FRAME_HEIGHT/2 - 27);

	//Declare static and global variables
	public static boolean start;
	public static JPanel bottomPanel, topPanel;
	public static JLayeredPane layeredpane;
	private Timer tmrPlay;

	public PlayScreen()
	{
		//Declare the timer and set its interval
		tmrPlay = new Timer(75, this);

		//Declare the top screen, which will always stay the same (the ReadyScreen)
		topPanel = new ReadyScreen();
		topPanel.setOpaque(false);
		topPanel.setBounds(0, 0, 439, 649);

		/* Declare the layered pane, used to contain multiple panels at the same time (two in this case). Set its bounds, and the panel's 
		 * location 0 being the root panel, and the following panels piling on top of those panels.
		 */
		layeredpane = new JLayeredPane();
		layeredpane.setBounds(0, 0, 439, 649);
		layeredpane.add(topPanel, new Integer(1));

		//Set the settings for this current panel, and add the layered pane to this panel.
		setLayout(null);
		add(layeredpane);
		setVisible(true);

		tmrPlay.start(); //Start timer
	}

	/* When the animation of the ReadyScreen is finished, move the top panel so that the user can click what's on the bottom panel, instead of the
	 * top one
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == tmrPlay)
		{
			if (ReadyScreen.levelCounter >= 9)
			{
				start = true;
				ReadyScreen.tmrReady.stop();
				ReadyScreen.levelCounter = 0;
				topPanel.setBounds(-436, 0, 436, 647);
			}
			
			repaint();
		}
	}
}