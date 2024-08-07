/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bao2;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author Maiikol
 */
public class Graficador extends Application{
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Line Chart Sample");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        xAxis.setLabel("Number of Month");
        //creating the chart
        LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setTitle("Stock Monitoring, 2010");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");
        //populating the series with data
        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 14));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 24));
        series.getData().add(new XYChart.Data(5, 34));
        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.setAnimated(false);
        lineChart.getData().add(series);
        saveAsPng(scene, "c:\\temp\\chart.png");
        stage.setScene(scene);
        saveAsPng(scene, "c:\\temp\\chart1.png");
        //stage.show();
        System.out.println("After show");
    }
        
    public void saveAsPng(Scene scene, String path) {
        WritableImage image = scene.snapshot(null);
        File file = new File(path);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    public void graph(Stage stage) throws IOException {
        //Preparing ObservbleList object
        ObservableList<PieChart.Data>pieChartData =
        FXCollections.observableArrayList(
        new PieChart.Data("Iphone 5S", 13),
        new PieChart.Data("Samsung Grand", 25),
        new PieChart.Data("MOTO G", 10),
        new PieChart.Data("Nokia Lumia", 22));
        //Creating a Pie chart
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Mobile Sales");
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(50);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(180);
        //Creating a Group object
        Scene scene = new Scene(new Group(), 595, 400);
        stage.setTitle("Charts Example");
        ((Group) scene.getRoot()).getChildren().add(pieChart);
        //Saving the scene as image
        WritableImage image = scene.snapshot(null);
        String path = "tempPieChart.png";
        File file = new File(path);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", file);
        System.out.println("Image Saved");
    }

}
    
   
