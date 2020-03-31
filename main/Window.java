package main;

import java.awt.*;
import javax.swing.*;

import globals.Globals;

@SuppressWarnings("serial")
public class Window extends JPanel
{
	private Game game;
	
	//initializes aspects
	public Window()
	{
		this.setPreferredSize(new Dimension(Globals.PAGE_WIDTH, Globals.PAGE_HEIGHT));
        requestFocus();
        game = new Game(this);
	}
	
	//paints the window
	public void paintComponent(Graphics g)
	{
		game.draw(g);
	}
	
	//allows the window to be focused
	public boolean isFocusable()
    {
		return true;
    }
}
