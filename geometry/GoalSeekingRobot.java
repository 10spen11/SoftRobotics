package geometry;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import java.util.Vector;

import globals.Vector2;
import globals.Globals;

public class GoalSeekingRobot extends SoftBody {

	public static final double STARTING_LEARNING_RATE = Globals.EPSILON;
	public static final double VARIATION = 30;
	protected static Color agentColor = new Color(0, 0, 255, 180);
	protected static Color goalColor = new Color(0, 155, 0, 180);

	private int seed;
	private Vector<Vector2> goalPoints;
	private Vector2 offset;
	private SoftBody goal;
	private long timeElapsed = 0;
	private int nextUpdateTime = 1000;
	private int iterationCount = 0;
	private double learningRate = STARTING_LEARNING_RATE;

	public GoalSeekingRobot(Vector<Vector2> goalPoints, Vector2 offset, int seed) {
		super(getStartingPoints(goalPoints, offset, new Random(seed)), agentColor);
		
		this.offset = offset.clone(); 
		this.seed = seed;
		
		// records the starting goal points for future use
		this.goalPoints = new Vector<Vector2>();
		for (int i = 0; i < goalPoints.size(); i++) {
			// add the goal points
			Vector2 point = goalPoints.elementAt(i).clone();
			this.goalPoints.add(point); // recorded without offset
		}

		goal = new SoftBody(getGoalPoints(), goalColor);
	}

	// returns the goal points, as recorded on initialization
	protected Vector<Vector2> getGoalPoints() {

		Vector<Vector2> wantedGoalPoints = new Vector<Vector2>();

		for (int i = 0; i < goalPoints.size(); i++) {
			wantedGoalPoints.add(goalPoints.elementAt(i).add(offset));
		}
		return wantedGoalPoints;
	}

	// gets the staring points based on the given goal points
	protected static Vector<Vector2> getStartingPoints(Vector<Vector2> goalPoints, Vector2 offset, Random rand) {
		
		Vector<Vector2> points = new Vector<Vector2>();
		for (int i = 0; i < goalPoints.size(); i++) {
			// adds randomness to the goal points
			Vector2 randAdded = goalPoints.elementAt(i).add(randomVector(rand));
			randAdded.plusEquals(offset);
			points.add(randAdded);
		}

		return points;
	}

	// returns a vector with randomess added from the default values provided
	protected static final Vector2 randomVector(Random rand) {
		return new Vector2(rand.nextDouble() * VARIATION - VARIATION / 2,
				rand.nextDouble() * VARIATION - VARIATION / 2);
	}

	// resets the robot, while keeping the muscles it already has
	public void reset() {

		// resets the points of the robot
		Vector<Vector2> resetPoints = getStartingPoints(goalPoints, offset, new Random(seed));
		for (int i = 0; i < points.length; i++) {
			points[i].copy(resetPoints.elementAt(i));
		}

		// resets the muscles, so that they can affect the reset points
		for (int i = 0; i < muscles.size(); i++) {
			muscles.elementAt(i).resetTension();
		}

		learningRate = STARTING_LEARNING_RATE;
	}

	// clones the GoalSeekingRobot
	// without copying the muscles
	public GoalSeekingRobot cloneMuscleless() {

		GoalSeekingRobot robot;
		robot = new GoalSeekingRobot(goalPoints, offset, seed);

		robot.points = new Vector2[points.length];
		for (int i = 0; i < robot.points.length; i++) { // coppies the agent's points
			robot.points[i] = points[i].clone();
		}
		robot.goal.points = new Vector2[goal.points.length];
		for (int i = 0; i < robot.goal.points.length; i++) { // coppies the goal points
			robot.goal.points[i] = goal.points[i].clone();
		}

		return robot;
	}

	// adds and returns a muscle that is predicted to help the most
	// the muscle that would add the most value is the one that moves two points
	// closer to where they need to be, that is:
	// the difference vectors for the two points on the muscle would
	// point in opposing directions along the line between them
	public IndexedMuscle addNextMuscle() {

		double maxHelpfulness = 0.0;
		IndexedMuscle bestMuscle = null;

		// the points arrays are the same length
		for (int i = 0; i < points.length; i++) {
			for (int j = i + 1; j < goal.points.length; j++) { // for each pair of indices

				double helpfulness = rateMuscleHelpfulness(i, j);
				if (maxHelpfulness < helpfulness) {
					maxHelpfulness = helpfulness;
					bestMuscle = new IndexedMuscle(this, i, j);
				}
			}
		}

		// if there's a helpful muscle, add it
		if (bestMuscle != null) {
			return addMuscle(bestMuscle);
		} else {
			return bestMuscle;
		}

	}
	
	// rates how helpful a muscle is expected to be

	// rates how helpful it would be to add a muscle with the given indices
	private double rateMuscleHelpfulness(int i, int j) {

		/*
		GoalSeekingRobot robot = cloneMuscleless();
		robot.addMuscle(i, j, 1.0);
		return getTensionModifier(robot.muscles.elementAt(0));
		*/
		
		// checks all muscles to see if this one already exists
		for (int k = 0; k < muscles.size(); k++) {
			int[] tempIndices = muscles.elementAt(k).getIndices();
			if (i == tempIndices[0] && j == tempIndices[1]) {
				return 0; // the muscle already exists, so adding it would be useless
			}
		}

		Vector2 muscleMovement = points[i].sub(points[j]);
		Vector2 toGoal1, toGoal2;

		// finds how each point would react to optimal muscle movement
		toGoal1 = goal.points[i].sub(points[i]);
		toGoal2 = goal.points[j].sub(points[j]);

		toGoal1 = toGoal1.projection(muscleMovement);
		toGoal2 = toGoal2.projection(muscleMovement);

		// if the best muscle movement for each are in opposite directions,
		// then the muscle would be helpful
		return -toGoal1.dot(toGoal2);
		

	}

	// gets the tension that a given muscle should add next
	// independently calculates the gradient with respect to this
	// degree of freedom
	private double getTensionModifier(IndexedMuscle muscle) {

		// simulates the muscle system
		// slightly disturbs the muscle, extending it
		int[] indices = muscle.getIndices();
		GoalSeekingRobot robot = this.cloneMuscleless();
		// simulates increasing muscle extension by small amount
		robot.addMuscle(indices[0], indices[1], 1.0);
		robot.muscles.elementAt(0).alterTension(Globals.EPSILON);
		robot.muscles.elementAt(0).update(1); // makes the muscle extend

		// compares the affect of extending the muscle slightly with doing nothing
		double costDifference = robot.cost() - cost(); // compares costs
		double slope = costDifference / Globals.EPSILON;

		// go the opposite direction of increasing cost
		return -slope * learningRate;
	}

	private double cost() {
		double sum = 0;
		for (int i = 0; i < points.length; i++) {
			sum += goal.points[i].sub(points[i]).mag();
		}
		return sum;
	}

	@Override
	public void draw(Graphics g) {
		// draws the goal
		goal.draw(g);
		super.draw(g);

		g.setColor(Color.black);
		g.setFont(new Font("Consolas", Font.PLAIN, 20));
		g.drawString("Iteration: " + iterationCount, (int) (90 + offset.x), (int) (350 + offset.y));
		String costString = String.format("Cost: %.2f", cost());
		g.drawString(costString, (int) (90 + offset.x), (int) (380 + offset.y));
	}

	@Override
	public void update(long millis) {

		double priorCost = cost();

		Vector<Double> tensionModifiers = new Vector<Double>();
		double magnitudeSquared = 0;

		// calculates the global gradient
		for (int i = 0; i < muscles.size(); i++) {
			IndexedMuscle muscle = muscles.elementAt(i);
			double tension = getTensionModifier(muscle);
			tensionModifiers.add(tension);
			magnitudeSquared += tension * tension;
		}
		double magnitude = Math.sqrt(magnitudeSquared); // the magnitude of the gradient
		// updates the muscles with respect to the goal
		// normalizes the gradient before multiplying by the learning rate
		if (magnitude != 0) { // gradient is not a complete standstill
			for (int i = 0; i < tensionModifiers.size(); i++) {
				IndexedMuscle muscle = muscles.elementAt(i);
				muscle.alterTension(tensionModifiers.elementAt(i) / magnitude * learningRate);
			}
		}
		/*
		 * for (int i = 0; i < muscles.size(); i++) { IndexedMuscle muscle =
		 * muscles.elementAt(i); muscle.alterTension(getTensionModifier(muscle)); }
		 */

		// records time passed
		timeElapsed += millis;

		// periodically resets muscles, and adds a new one
		if (timeElapsed > nextUpdateTime) {
			timeElapsed -= nextUpdateTime;
			nextUpdateTime += 50;
			iterationCount++;

			if (addNextMuscle() == null) {
				System.out.print("Done!");
				nextUpdateTime = 999999999;
			}
			reset();
		}

		super.update(millis);

		// updates the goal
		goal.update(millis);

		// checks how the costs compare, to see how to adapt the learning rate
		double updatedCost = cost();
		if (priorCost > updatedCost) { // adapt the learning rate
			learningRate *= 1.2;
		} else {
			learningRate *= 0.5;
		}
	}

}
