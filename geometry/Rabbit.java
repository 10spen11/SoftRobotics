package geometry;

import java.util.Vector;

import globals.Vector2;

public class Rabbit extends GoalSeekingRobot {

	public Rabbit() {
		super(getRabbitPoints());
	}

	protected static Vector<Vector2> getRabbitPoints() {
		Vector<Vector2> points = new Vector<Vector2>();

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

		return points;
	}

}
