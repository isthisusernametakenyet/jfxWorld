package se.jerka.ops.model;

public class Score {
	
	private int value;
	private Position position;
	
	public Score(int value, Position position) {
		this.value = value;
		this.position = position;
	}
	
	public void add(int num) {
		value += num;
	}
	
	public int value() {
		return value;
	}
	
	public Position position() {
		return position;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}
	
}