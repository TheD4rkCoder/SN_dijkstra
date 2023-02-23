package com.example.sn_dijkstra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;

public class HelloApplication extends Application {


    // http://www.cse.uaa.alaska.edu/~afkjm/csce201/handouts/LoopsImages.pdf
    static PixelReader pixelReader; // firs originalbild
    static PixelWriter pixelWriter; // firs Resultatbild
    static WritableImage wImage; // Resutatbild
    static Image image; // originalbild

    @Override
    public void start(Stage stage) throws IOException {

        loadImage("src/main/resources/com/example/sn_dijkstra/data/IMG_5.png");
        //loadImage("image.png");

        ///*
        wImage = Bildeditor.convertToPolarCoordinates(image, (int)image.getWidth()/2 - 20);
        double[][] filter = Bildeditor.generateGaussianFilter(51, 25);
        wImage = Bildeditor.applyFilter(wImage, filter);
        wImage = Bildeditor.applyFilter(wImage, Bildeditor.gradientFilterV);
        wImage = Bildeditor.applyFilter(wImage, filter);
        wImage = Bildeditor.saturateGrayscaleImage(wImage);
        wImage = Bildeditor.applyFilter(wImage, filter);
        //wImage = Bildeditor.invertColor(wImage);
        //wImage = Bildeditor.reduceResolution(wImage, 5);
        wImage = Bildeditor.saturateGrayscaleImage(wImage);
        //wImage = Bildeditor.applyFilter(wImage, Bildeditor.generateGaussianFilter(9, 5));
        //wImage = Bildeditor.saturateGrayscaleImage(wImage);
        saveImage(wImage);
        System.out.println("saved");
        //*/
        System.out.println("begin building graph");
        Long time = System.currentTimeMillis();
        ImageGraph imageGraph = new ImageGraph(wImage);
        System.out.println("graph built: " + (System.currentTimeMillis() - time) + "ms");

        Djikstra djikstra = new Djikstra(imageGraph);
        djikstra.startDjikstra();
        Boolean[][] shortestP = djikstra.getShortestPathTo((int)(wImage.getWidth() + 1), 0);
        System.out.println("finished");
        wImage = Djikstra.applyShortestPathToImage(wImage,shortestP);
        //saveImage(image1);
        showImage(stage);
    }



    static public void loadImage(String path) {
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream(path); // use other file names
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        image = new Image(inputstream);
        pixelReader = image.getPixelReader();
        //wImage = Bildeditor.applyGradiant(wImage, Bildeditor.gradientFilter);
    }
    static void showImage(Stage stage) {
        ImageView imageView = new ImageView();
        imageView.setImage(wImage);
        imageView.setFitWidth(600);
        imageView.setPreserveRatio(true);
        Group root = new Group(imageView);
        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    static public void saveImage(WritableImage image){
        File file = new File("image.png");
        try{
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch();
    }
}