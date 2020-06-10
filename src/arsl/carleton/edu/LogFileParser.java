package arsl.carleton.edu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class LogFileParser {

	final String requiredMsgType = "Y";
	final String requiredPortType = "out_c";
	final int msgTypePos = 2;
	final int timePos = 3;
	final int cellPos = 4;
	final int portPos = 5;
	final int readingPos = 6;

	LogFileData fileData = null;
	public LogFileData parse1(File f) throws Exception {
		
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = br.readLine();
				mainpulateLine(line);
			}
		}
		return fileData;
	}
	
	public LogFileData parse(File f) throws Exception {
		try (Stream<String> lines = Files.lines(Paths.get(f.getPath()), Charset.defaultCharset())) {
			  lines.forEachOrdered(line -> mainpulateLine(line));
			}
		return fileData;
	}


	private void mainpulateLine(String line) {
		String[] lineCompnents = line.split("/");
		if (lineCompnents[msgTypePos].trim().equals(requiredMsgType))
			if (lineCompnents[portPos].trim().equals(requiredPortType)) {

				DataPoint dp = new DataPoint(lineCompnents[timePos], Double.parseDouble(lineCompnents[readingPos]));
				if (fileData == null)
					fileData = new LogFileData();
				String cellCoord = extractCoord(lineCompnents[cellPos]);
				fileData.addDataToKey(cellCoord, dp);
			}
	}

	private String extractCoord(String dataPoint) {
		int beginIndex = dataPoint.indexOf('(');
		int endIndex = dataPoint.indexOf(')');
		if (beginIndex != -1 && endIndex != -1) {
			String coords = dataPoint.substring(beginIndex + 1, endIndex);
			dataPoint = coords;
		}
		return dataPoint;
	}
}
