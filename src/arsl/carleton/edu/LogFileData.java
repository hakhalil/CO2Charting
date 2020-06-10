package arsl.carleton.edu;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class LogFileData {
	TreeMap<String, ArrayList<DataPoint>> fileData = new TreeMap<String, ArrayList<DataPoint>>();
	
	public List<DataPoint> getDataPoints(String key) {
		return fileData.get(key);
	}
	
	public boolean hasKey(String key) {
		return fileData.containsKey(key);
	}
	
	public void addDataToKey(String key, DataPoint data) {
		if (!fileData.containsKey(key)) {
			ArrayList<DataPoint> arrayOfValues = new ArrayList<DataPoint>();
			arrayOfValues.add(data);
			fileData.put(key, arrayOfValues);
		} else {
			fileData.get(key).add(data);
		}
	}
	
}
