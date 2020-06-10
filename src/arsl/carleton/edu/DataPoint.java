package arsl.carleton.edu;

public class DataPoint {
	double value;
	String time;
	
	public DataPoint() {
	}
	
	public DataPoint( String time, double value) {
		super();
		this.value = value;
		this.time = time;
	}
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
	@Override
	public String toString() {
		String output = time + ": " + value;
		return output;
	}

}
