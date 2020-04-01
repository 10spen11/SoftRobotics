package geometry;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import globals.Vector2;

// SoftBody represents a soft robot to be controlled by muscles,
// with points as its bounding box. It represents a simplified version
// of the soft robots discussed in Spielberg et al. Specifically,
// the learning is reduced to the muscles/ actuators that can be acted on,
// rather than the complete model of the soft body. Optimization can then
// be performed on the actuators in the system.
public class SoftBody extends Polygon {

	protected Vector<IndexedMuscle> muscles;
	
	public SoftBody(Vector<Vector2> points, Color color) {
		super(points, color);
		// TODO Auto-generated constructor stub
		muscles = new Vector<IndexedMuscle>();
	}

	public SoftBody(Vector2[] points, Color color) {
		super(points, color);
		// TODO Auto-generated constructor stub
		muscles = new Vector<IndexedMuscle>();
	}
	
	// adds a muscle to the system
	public IndexedMuscle addMuscle(IndexedMuscle muscle) {
		
		int[] indices = muscle.getIndices();
		for (int i = 0; i < muscles.size(); i++) {
			int[] tempIndices = muscles.elementAt(i).getIndices();
			if (indices[0] == tempIndices[0] && indices[1] == tempIndices[1]) {
				return null; // the muscle already exists!
			}
		}
		muscles.add(muscle);
		return muscle;
	}
	
	// adds a muscle to the system based on the indexes of the points to act upon and the tension
	public IndexedMuscle addMuscle(int p1, int p2, double d) {
		return addMuscle(new IndexedMuscle(this, p1, p2, d));
	}

	// updates the SoftBody
	@Override
	public void update(long millis) {
		super.update(millis);
		// updates all muscles
		for (Muscle muscle : muscles) {
			muscle.update(millis);
		}
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		// draws all muscles
		for (Muscle muscle : muscles) {
			muscle.draw(g);
		}
	}

}
