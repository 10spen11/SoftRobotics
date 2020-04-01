package main;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Timer;

import geometry.GoalSeekingRobot;
import geometry.Rabbit;
import globals.Globals;

public class Game {
	private Timer timer;
	private Window window;

	private long time;

	private Vector<GoalSeekingRobot> agents;

	public Game(Window window) {
		this.window = window;

		agents = new Vector<GoalSeekingRobot>();
		agents.add(new Rabbit());
		// add more agents here

		time = System.currentTimeMillis();
		timer = new Timer(1000 / Globals.FRAME_RATE, new FrameListener());
		timer.start();
	}

	// updates all of the elements of the game
	public void update(long millis) {
		for (GoalSeekingRobot agent : agents) {
			agent.update(millis);
		}
	}

	// draws all of the game elements
	public void draw(Graphics g) {
		g.clearRect(0, 0, Globals.PAGE_WIDTH, Globals.PAGE_HEIGHT);
		
		for (GoalSeekingRobot agent : agents) {
			agent.draw(g);
		}
	}

	// class to contain listener for each frame
	private class FrameListener implements ActionListener {
		// listener controlled by timer each frame
		public void actionPerformed(ActionEvent event) {
			long newTime = System.currentTimeMillis();

			update(newTime - time);
			window.repaint();

			time = newTime;
		}
	}
}
