package geometry;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Vector;

import globals.Vector2;
import globals.Globals;

public class GoalSeekingRabbit extends Rabbit {

	public static final double STARTING_LEARNING_RATE = Globals.EPSILON;
	
	protected SoftBody goal;
	protected long timeElapsed = 0;
	protected int nextUpdateTime = 1000;
	protected int iterationCount = 0;
	public double learningRate = STARTING_LEARNING_RATE;

	public GoalSeekingRabbit() {
		super(true);
		goal = new Rabbit(false);
	}
	
	// resets the rabbit, while keeping the muscles it already has
	public void reset() {
		
		// resets the points of the rabbit
		Vector<Vector2> rabbitPoints = getRabbitPoints(true);
		for (int i = 0; i < points.length; i++) {
			points[i].copy(rabbitPoints.elementAt(i));
		}
		
		// resets the muscles, so that they can affect the reset points
		for (int i = 0; i < muscles.size(); i++) {
			muscles.elementAt(i).resetTension();
		}
		
		learningRate = STARTING_LEARNING_RATE;
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

	// rates how helpful it would be to add a muscle with the given indices
	private double rateMuscleHelpfulness(int i, int j) {

		/*
		GoalSeekingRabbit rabbit = this.cloneMuscleless();
		rabbit.addMuscle(i, j, 1.0);
		IndexedMuscle muscle = rabbit.muscles.elementAt(0);
		return Math.abs(getTensionModifier(muscle));
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
	private double getTensionModifier(IndexedMuscle muscle) {
		
		// simulates the muscle system
		// slightly disturbs the muscle, extending it
		int[] indices = muscle.getIndices();
		GoalSeekingRabbit rabbit = this.cloneMuscleless();
		// simulates increasing muscle extension by small amount
		rabbit.addMuscle(indices[0], indices[1], 1.0);
		rabbit.muscles.elementAt(0).alterTension(Globals.EPSILON);
		rabbit.muscles.elementAt(0).update(1); // makes the muscle extend
		
		// compares the affect of extending the muscle slightly with doing nothing
		double costDifference = rabbit.cost() - cost(); // compares costs
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
	
	// clones the GoalSeekingRabbit
	// without copying the muscles
	public GoalSeekingRabbit cloneMuscleless() {
		
		GoalSeekingRabbit rabbit = new GoalSeekingRabbit();
		for (int i = 0; i < rabbit.points.length; i++) { // coppies the rabbit points
			rabbit.points[i].copy(points[i]);
		}
		for (int i = 0; i < rabbit.goal.points.length; i++) { // coppies the goal points
			rabbit.goal.points[i].copy(goal.points[i]);
		}
		return rabbit;
	}

	@Override
	public void draw(Graphics g) {
		// draws the goal
		goal.draw(g);
		super.draw(g);
		
		g.setColor(Color.black);
		g.setFont(new Font("Consolas", Font.PLAIN, 20));
		g.drawString("Iteration: " + iterationCount, 90, 350);
		String costString = String.format("Cost: %.2f", cost());
		g.drawString(costString , 90, 380);
	}

	@Override
	public void update(long millis) {

		
		double priorCost = cost();
		
		// updates the muscles with respect to the goal
		for (int i = 0; i < muscles.size(); i++) {
			IndexedMuscle muscle = muscles.elementAt(i);
			muscle.alterTension(getTensionModifier(muscle));
		}
		
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
