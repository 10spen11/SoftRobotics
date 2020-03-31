package geometry;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import globals.GameElement;
import globals.Vector2;

public class Polygon implements GameElement {

	protected Vector2[] points;
	protected Color color;

	// creates a polygon from a vector of points
	public Polygon(Vector<Vector2> points, Color color) {
		this.color = color;
		this.points = new Vector2[points.size()];
		
		for (int i = 0; i < this.points.length; i++) { // takes each point, does not clone
			this.points[i] = points.elementAt(i);
		}
	}

	// creates a polygon from an array of points
	public Polygon(Vector2[] points, Color color) {
		this.color = color;
		this.points = new Vector2[points.length];
		
		for (int i = 0; i < this.points.length; i++) { // takes each point, does not clone
			this.points[i] = points[i];
		}
	}
	
	// returns a point at the index
	public Vector2 getPoint(int index) {
		return points[index];
	}

	// updates the polygon
	@Override
	public void update(long millis) {
		// do nothing
	}
	
	// draws the polygon
	@Override
	public void draw(Graphics g) {
		g.setColor(color);

		int[] xValues = new int[points.length];
		int[] yValues = new int[points.length];
		
		for (int i = 0; i < points.length; i++) {
			xValues[i] = (int) (points[i].x);
			yValues[i] = (int) (points[i].y);
		}
		
		g.fillPolygon(xValues, yValues, points.length);
	}
	
}
