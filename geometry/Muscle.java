package geometry;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import globals.GameElement;
import globals.Vector2;

public class Muscle implements GameElement {

	public static final double CONVERGENCE_RATE = 1.0;
	public static final double TENSION_RATE = 0.02;
	public static final double MAX_TENSION = 5.0;
	public static final double MIN_TENSION = 0.1;

	protected Vector2 point1, point2;
	// tension roughly represents the ratio between ending and starting lengths of
	// muscle
	private double currentTension, desiredTension;

	// creates a muscle between two points
	public Muscle(Vector2 point1, Vector2 point2) {
		this.point1 = point1;
		this.point2 = point2;
		currentTension = 1.0f;
		desiredTension = 1.0f;
	}

	// creates a muscle between two points, specifying the desired tension
	public Muscle(Vector2 point1, Vector2 point2, double tension) {
		this.point1 = point1;
		this.point2 = point2;
		currentTension = 1.0f;
		setTension(tension);
	}

	// adds or subtracts tension, based on 
	public void multTension(double toMult) {
		setTension(desiredTension * Math.pow(toMult, TENSION_RATE)); // scales down the tension
	}

	// sets the tension that the muscle seeks for
	public void setTension(double tension) {
		if (tension < MIN_TENSION)
			desiredTension = MIN_TENSION;
		else if (tension > MAX_TENSION)
			desiredTension = MAX_TENSION;
		else
			desiredTension = tension;
	}
	
	// sets the current tension to 0
	public void resetTension() {
		currentTension = 1.0;
		desiredTension = 1.0;
	}
	
	// returns the difference between the two points in the muscle
	public Vector2 difference() {
		return point2.sub(point1);
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.red);
		((Graphics2D) g).setStroke(new BasicStroke(6));
		g.drawLine((int) point1.x, (int) point1.y, (int) point2.x, (int) point2.y);
	}

	// stretches or contracts the muscle
	@Override
	public void update(long millis) {

		Vector2 diff = difference(); // line from 1 to 2

		// how much to stretch or contract the muscle
		double scale = desiredTension - currentTension; // how much more it needs to go
		// prevent it from going all the way at once
		scale *= 1.0 - Math.pow(1.0 - CONVERGENCE_RATE, millis);
		diff.timesEquals(scale / 2); // difference tension would make to each point

		// record the distortion
		currentTension += scale * currentTension;

		// push or pull both points equally
		point2.plusEquals(diff);
		point1.minusEquals(diff);

	}

}
