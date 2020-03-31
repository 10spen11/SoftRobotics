package globals;

public class Vector2 {
	public double x, y;

	// creates a default 2D vector
	public Vector2() {
		x = 0;
		y = 0;
	}

	// creates a vector with defined dimension
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// returns the sum of this vector and another
	public boolean equals(Vector2 vec) {
		return equals(this, vec);
	}

	// coppies a vector onto this one, then returns itself
	public Vector2 copy(Vector2 vec) {
		this.x = vec.x;
		this.y = vec.y;
		
		return this;
	}

	// returns the sum of this vector and another
	public Vector2 add(Vector2 vec) {
		return add(this, vec);
	}

	// returns the difference of this vector and another
	public Vector2 sub(Vector2 vec) {
		return sub(this, vec);
	}

	// scales this vector by a constant
	// then returns itself
	public Vector2 mult(double scale) {
		return new Vector2(x * scale, y * scale);
	}

	// returns the Euclidean distance from this vector to another
	public double dist(Vector2 vec) {
		return dist(this, vec);
	}

	// returns the squared magnitude of the vector
	public double magSquared() {
		return this.dot(this);
	}

	// returns the magnitude of the vector
	public double mag() {
		return Math.sqrt(magSquared());
	}

	// returns the angle (in radians) of the vector on the unit circle
	public double angle() {
		return Math.atan2(y, x);
	}

	// normalizes the vector, returning itself
	// no changes are made to the zero vector
	public Vector2 normalize() {
		// guard against zero vector
		if (this.equals(zero()))
			return this;

		this.timesEquals(1.0 / this.mag());
		return this;
	}

	// returns the sum of this vector and another, stored as this vector
	public Vector2 plusEquals(Vector2 vec) {
		this.x += vec.x;
		this.y += vec.y;
		return this;
	}

	// returns the difference of this vector and another, stored as this vector
	public Vector2 minusEquals(Vector2 vec) {
		this.x -= vec.x;
		this.y -= vec.y;
		return this;
	}

	// returns the difference of this vector and another, stored as this vector
	public Vector2 timesEquals(double scale) {
		this.x *= scale;
		this.y *= scale;
		return this;
	}

	// adds a scalar quantity to the magnitude of the vector 
	// without changing its direction
	// if the vector would have negative magnitude, 
	// it instead becomes the zero vector
	public Vector2 addToMag(double sum) {
		double magnitude2 = mag() + sum;
		return setMag(magnitude2);
	}

	// sets the magnitude of the vector without changing its direction
	// if the vector would have negative magnitude, 
	// it instead becomes the zero vector
	public Vector2 setMag(double newMag) {
		double magnitude = mag();

		if (magnitude == 0) {
			return this; // cannot extend zero vector
		} else if (newMag < 0) {
			this.x = 0;
			this.y = 0;
			return this;
		} else {
			double quotient = newMag / magnitude;
			return this.timesEquals(quotient);
		}
	}

	// returns the dot product of this and another vector
	public double dot(Vector2 other) {
		return Vector2.dot(this, other);
	}

	// returns the vector projection of this vector onto another
	public Vector2 projection(Vector2 other) {
		Vector2 proj = other.clone();
		double scale = this.dot(other) / other.dot(other);
		return proj.mult(scale);
	}

	// returns the vector rejection of this vector from another
	public Vector2 rejection(Vector2 other) {
		// this minus its projection onto the other
		return this.sub(this.projection(other));
	}

	// rotates the vector a specified number of radians counterclockwise
	public Vector2 rotate(double radians) {
		double newAngle = angle() + radians;
		double magnitude = mag();
		this.x = magnitude * Math.cos(newAngle);
		this.y = magnitude * Math.sin(newAngle);
		return this;
	}

	// returns the unit vector in the direction of this vector
	public Vector2 unit() {
		Vector2 unitVector = this.clone();
		unitVector.setMag(1);
		return unitVector;
	}

	// clones the vector
	public Vector2 clone() {
		return new Vector2(x, y);
	}

	// returns the String representation of the Vector2
	public String toString() {
		return "<" + x + ", " + y + ">";
	}

	// returns whether two vectors are equal
	public static boolean equals(Vector2 vec1, Vector2 vec2) {
		return vec1.x == vec2.x && vec1.y == vec2.y;
	}

	// returns the sum of two vectors
	public static Vector2 add(Vector2 vec1, Vector2 vec2) {
		return new Vector2(vec1.x + vec2.x, vec1.y + vec2.y);
	}

	// returns the difference of two vectors
	public static Vector2 sub(Vector2 vec1, Vector2 vec2) {
		return new Vector2(vec1.x - vec2.x, vec1.y - vec2.y);
	}

	// returns the Euclidean distance between two vectors
	public static double dist(Vector2 vec1, Vector2 vec2) {
		Vector2 diff = vec1.sub(vec2);
		return diff.mag();
	}

	// returns the dot product of two vectors
	public static double dot(Vector2 vec1, Vector2 vec2) {
		return vec1.x * vec2.x + vec1.y * vec2.y;

	}

	// returns the zero vector
	public static Vector2 zero() {
		return new Vector2(0, 0);
	}

}
