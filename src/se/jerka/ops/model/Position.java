package se.jerka.ops.model;

import se.jerka.ops.model.Location.Direction;

public class Position {
	
	public static final int X_MIN = 0;
	public static final int X_MAX = 8;
	public static final int Y_MIN = 0;
	public static final int Y_MAX = 10;
	public static final String EMPTY = "[_____]";
	
	private int x;
	private int y;
	
	public Position(int x, int y) {
		set(x, y);
	}
	
	public void set(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public Position nextPosition(Direction direction) {
		switch (direction) {
		case NORTH:
			return BigBang.createPosition(x, y-1);
		case WEST:
			return BigBang.createPosition(x-1, y);
		case EAST:
			return BigBang.createPosition(x+1, y);
		case SOUTH:
			return BigBang.createPosition(x, y+1);
		default:
			throw new IllegalArgumentException();
		}
	}
	
	public boolean isPossible() {
		return horizontalBorder() && verticalBorder();
	}
	
	private boolean horizontalBorder() {
		return y >= Position.Y_MIN && y < Position.Y_MAX;
	}
	
	private boolean verticalBorder() {
		return x >= Position.X_MIN && x < Position.X_MAX;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	@Override
	public String toString () {
		return "( " + Integer.toString(x()) + ", " + Integer.toString(y()) + " )";
	}
}
