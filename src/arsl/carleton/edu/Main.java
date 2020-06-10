package arsl.carleton.edu;

import java.io.File;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

	LogFileData fileData = null;
	String xVal, yVal;

	public static void main(String[] args) {
		launch(args); // It calls start method defined bellow...
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		final FileChooser fileChooser = new FileChooser();
		primaryStage.setTitle("CO2 Charter");

		// add GridPane Layout to give flexibility of rows and columns
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		// Add controls to input which cell will be charted
		Label cellXValue = new Label("Cell X Value:");
		grid.add(cellXValue, 0, 1);

		TextField xTextField = new TextField();
		grid.add(xTextField, 1, 1);

		Label cellYValue = new Label("Cell Y Value:");
		grid.add(cellYValue, 0, 2);

		TextField yTextField = new TextField();
		grid.add(yTextField, 1, 2);

		// select the log file
		Button logFileBtn = new Button("Choose Log");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(logFileBtn);
		grid.add(hbBtn, 1, 4);

		// start the charting

		Button genBtn = new Button("Chart Data");
		HBox hbBtn1 = new HBox(10);
		hbBtn1.setAlignment(Pos.BOTTOM_LEFT);
		hbBtn1.getChildren().add(genBtn);
		grid.add(hbBtn1, 0, 4);

		// set action for the selection button
		logFileBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {
					openFile(file);
				}
			}
		});

		// set action
		final Text actiontarget = new Text();
		grid.add(actiontarget, 0, 6);
		GridPane.setColumnSpan(actiontarget, 2);
		GridPane.setHalignment(actiontarget, HPos.RIGHT);
		actiontarget.setId("actiontarget");
		genBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				xVal = xTextField.getText();
				yVal = yTextField.getText();
				if (!xVal.isEmpty() && !yVal.isEmpty() && fileData != null) {
					String cellCoord = xVal + "," + yVal;
					List<DataPoint> array = fileData.getDataPoints(cellCoord);
					addChart(primaryStage, grid, array);
				} else {
					actiontarget.setFill(Color.FIREBRICK);
					actiontarget.setText("First select the cell and the log file");
				}
			}
		});

		Scene scene = new Scene(grid, 300, 275);
		primaryStage.setScene(scene);

		primaryStage.show();
	}

	private void openFile(File file) {
		try {
			LogFileParser logParser = new LogFileParser();
			fileData = logParser.parse(file);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void addChart(Stage stage, GridPane grid, List<DataPoint> datapoints) {

		if (grid.getChildren().size() > 7) {
			grid.getChildren().remove(7);
		}

		if (datapoints != null) {
			// defining a series
			XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();

			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			for (DataPoint dp : datapoints) {
				// populating the series with data
				series.getData().add(new XYChart.Data<String, Number>(dp.getTime(), dp.getValue()));
				if (dp.getValue() < min)
					min = dp.getValue();

				if (dp.getValue() > max)
					max = dp.getValue();
			}

			// defining the axes
			final CategoryAxis xAxis = new CategoryAxis();
			final NumberAxis yAxis = new NumberAxis(min, max, 1.0);

			xAxis.setLabel("CO2 Level");
			// creating the chart
			final BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);

			barChart.setTitle("Charting CO2 reading");
			barChart.getData().add(series);
			grid.add(barChart, 0, 7);
			// grid.autosize();
			stage.setMaximized(true);
		}
	}
}