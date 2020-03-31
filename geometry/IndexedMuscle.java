package geometry;

public class IndexedMuscle extends Muscle {

	private int index1, index2;
	
	// makes a muscle on the body with the indexed points
	public IndexedMuscle(SoftBody parent, int i1, int i2) {
		super(parent.getPoint(i1), parent.getPoint(i2));
		
		index1 = i1;
		index2 = i2;
	}
	
	// makes a muscle with given tension
	public IndexedMuscle(SoftBody parent, int i1, int i2, double d) {
		super(parent.getPoint(i1), parent.getPoint(i2), d);
		
		index1 = i1;
		index2 = i2;
	}
	
	// returns the indices of the muscle's points
	public int[] getIndices() {
		int[] indices = {index1, index2};
		return indices;
	}

}
