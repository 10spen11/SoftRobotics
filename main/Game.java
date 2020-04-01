package main;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import geometry.Rabbit;
import globals.Globals;

public class Game {
	private Timer timer;
	private Window window;

	private long time;

	private Rabbit goalRabbit;

	public Game(Window window) {
		this.window = window;

		goalRabbit = new Rabbit();

		time = System.currentTimeMillis();
		timer = new Timer(1000 / Globals.FRAME_RATE, new FrameListener());
		timer.start();
	}

	// updates all of the elements of the game
	public void update(long millis) {
		goalRabbit.update(millis);
	}

	public void draw(Graphics g) {
		g.clearRect(0, 0, Globals.PAGE_WIDTH, Globals.PAGE_HEIGHT);

		goalRabbit.draw(g);
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
