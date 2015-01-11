/**NAME: Alex Adusei
 * COURSE CODE: ICS4U1
 * DATE: Friday June 15 2012
 * PROGRAM: WariorWare: Touched
 */

import javax.swing.*;

public class WWTProgram
{
	//Declare static variables to be used in other classes
	public static final int EXTRA_HEIGHT = 8;
	
	public static final int rectWidth1 = 436;
	public static final int rectHeight1 = 337;
	public static final int rectWidth2 = 436;
	public static final int rectHeight2 = 310;

	public static final int FRAME_WIDTH = 445;
	public static final int FRAME_HEIGHT = 675 + EXTRA_HEIGHT;

	public static final int topX = 1;
	public static final int topY = 1;
	public static final int bottomX = 1;
	public static final int bottomY = FRAME_HEIGHT/2;
	
	public static final int SECONDS = 200;
	
	public static JFrame frame;

	public static void main(String[] args)
	{
		new WWTProgram();
	}

	public WWTProgram()
	{
		//Declare static frame, which will be used in about every class
		frame = new JFrame();
		
		frame.setContentPane(new TitleScreen()); //Takes user to the TitleScreen
		frame.setTitle("WaroiWare: Touched!");
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}