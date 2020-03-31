package main;

import javax.swing.JFrame;

import globals.Globals;

public class SoftRobotLearning
{
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Magestic");
		
		Window panel = new Window();
		frame.getContentPane().add(panel);
		
		configureFrame(frame);
	}
	
	public static void configureFrame(JFrame frame)
	{
		frame.setBounds(325, 75, Globals.PAGE_WIDTH, Globals.PAGE_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
