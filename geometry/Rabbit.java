package geometry;

import java.awt.Color;
import java.util.Random;
import java.util.Vector;

import globals.Vector2;

public class Rabbit extends SoftBody {

	protected static Color randomColor = new Color(0, 0, 255, 180);
	protected static Color nonRandomColor = new Color(0, 155, 0, 180);
	public static final int SEED = 0;
	public static final double VARIATION = 30;
	protected static Random rand = new Random(SEED);

	public Rabbit(boolean addRandomness) {
		super(getRabbitPoints(addRandomness), addRandomness ? randomColor : nonRandomColor);
		// TODO Auto-generated constructor stub
	}

	protected static Vector<Vector2> getRabbitPoints(boolean addRandomness) {
		Vector<Vector2> points = new Vector<Vector2>();

		if (addRandomness) {
			rand.setSeed(SEED);

			points.add(addRandomness(100, 20));
			points.add(addRandomness(150, 20));
			points.add(addRandomness(150, 120));
			points.add(addRandomness(170, 120));
			points.add(addRandomness(170, 20));
			points.add(addRandomness(220, 20));
			points.add(addRandomness(220, 120));
			points.add(addRandomness(270, 120));
			points.add(addRandomness(270, 270));
			points.add(addRandomness(190, 270));
			points.add(addRandomness(190, 220));
			points.add(addRandomness(130, 220));
			points.add(addRandomness(130, 270));
			points.add(addRandomness(40, 270));
			points.add(addRandomness(40, 120));
			points.add(addRandomness(100, 120));
		} else {
			points.add(new Vector2(100, 20));
			points.add(new Vector2(150, 20));
			points.add(new Vector2(150, 120));
			points.add(new Vector2(170, 120));
			points.add(new Vector2(170, 20));
			points.add(new Vector2(220, 20));
			points.add(new Vector2(220, 120));
			points.add(new Vector2(270, 120));
			points.add(new Vector2(270, 270));
			points.add(new Vector2(190, 270));
			points.add(new Vector2(190, 220));
			points.add(new Vector2(130, 220));
			points.add(new Vector2(130, 270));
			points.add(new Vector2(40, 270));
			points.add(new Vector2(40, 120));
			points.add(new Vector2(100, 120));
		}

		return points;
	}

	// returns a vector with randomess added from the default values provided
	private static Vector2 addRandomness(double x, double y) {
		return new Vector2(x + rand.nextDouble() * VARIATION - VARIATION / 2, y + rand.nextDouble() * VARIATION - VARIATION / 2);
	}

}
