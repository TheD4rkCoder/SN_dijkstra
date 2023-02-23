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

        //loadImage("src/main/resources/com/example/sn_dijkstra/data/IMG_1.jpg");
        loadImage("image.png");

        /*
        wImage = Bildeditor.convertToPolarCoordinates(image);
        double[][] filter = Bildeditor.generateGaussianFilter(21, 18);
        wImage = Bildeditor.applyFilter(wImage, filter);
        wImage = Bildeditor.applyFilter(wImage, Bildeditor.gradientFilter);
        wImage = Bildeditor.invertColor(wImage);
        wImage = Bildeditor.reduceResolution(wImage, 10);
        saveImage(wImage);
        System.out.println("saved");
        */
        System.out.println("begin building graph");
        Long time = System.currentTimeMillis();
        ImageGraph imageGraph = new ImageGraph(image);
        System.out.println("graph built: " + (System.currentTimeMillis() - time) + "ms");

        Djikstra djikstra = new Djikstra(imageGraph);
        djikstra.startDjikstra();
        Boolean[][] shortestP = djikstra.getShortestPathTo((int)(image.getWidth() + 1), 0);
        System.out.println("finished");
        wImage = Djikstra.applyShortestPathToImage(image,shortestP);
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