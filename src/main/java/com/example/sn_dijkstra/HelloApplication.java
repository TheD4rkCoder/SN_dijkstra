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
import javax.sound.midi.Soundbank;

public class HelloApplication extends Application {


    // http://www.cse.uaa.alaska.edu/~afkjm/csce201/handouts/LoopsImages.pdf
    static PixelReader pixelReader; // firs originalbild
    static PixelWriter pixelWriter; // firs Resultatbild
    static WritableImage wImage; // Resutatbild
    static Image image; // originalbild

    @Override
    public void start(Stage stage) throws IOException {

        loadImage("src/main/resources/com/example/sn_dijkstra/data/IMG_3.jpg");
        //loadImage("image.png");
        System.out.println("begin converting image");
        long time = System.currentTimeMillis();
        wImage = Bildeditor.convertToPolarCoordinates(image, (int)image.getWidth()/2 - 20);
        WritableImage beginImage = Bildeditor.reduceResolution(wImage, (int) (wImage.getWidth() / 250));
        int filterSize = ((int) (wImage.getWidth() / 50) % 2 == 0) ? (int) (wImage.getWidth() / 50) + 1 : (int) (wImage.getWidth() / 50);
        double[][] filter = Bildeditor.generateGaussianFilter(filterSize, wImage.getWidth()/100);
        wImage = Bildeditor.applyFilter(wImage, filter);
        wImage = Bildeditor.applyFilter(wImage, Bildeditor.gradientFilterV);
        wImage = Bildeditor.applyFilter(wImage, filter);
        wImage = Bildeditor.saturateGrayscaleImage(wImage);
        wImage = Bildeditor.applyFilter(wImage, filter);

        //wImage = Bildeditor.invertColor(wImage);
        wImage = Bildeditor.reduceResolution(wImage, (int) (wImage.getWidth() / 250));
        wImage = Bildeditor.saturateGrayscaleImage(wImage);
        wImage = Bildeditor.applyFilter(wImage, Bildeditor.generateGaussianFilter(9, 5));
        wImage = Bildeditor.saturateGrayscaleImage(wImage);
        System.out.println("image converted: " + (System.currentTimeMillis() - time) + "ms");
        //wImage = Bildeditor.saturateGrayscaleImage(wImage);
        saveImage(wImage);
        System.out.println("begin building graph");
        time = System.currentTimeMillis();
        ImageGraph imageGraph = new ImageGraph(wImage);
        System.out.println("graph built: " + (System.currentTimeMillis() - time) + "ms");

        System.out.println("begin djikstra algorithm");
        time = System.currentTimeMillis();
        Djikstra djikstra = new Djikstra(imageGraph);
        djikstra.startDjikstra();
        Boolean[][] shortestP = djikstra.getShortestPathTo((int)(wImage.getWidth() + 1), 0);
        System.out.println("djikstra complete: " + (System.currentTimeMillis() - time) + "ms");
        wImage = Djikstra.applyShortestPathToImage(beginImage,shortestP);
        //saveImage(image1);
        showImage(stage, wImage);
        System.out.println("The read Path is the Shortest from left to right");
        int firstcolumn = 0, lastcolumn = 0;
        for (int i = 0; i < shortestP[0].length; i++) {
            if (shortestP[1][i]) {
                firstcolumn = i;
            }
            if (shortestP[shortestP.length-2][i]) {
                lastcolumn = i;
            }
        }

        System.out.println("y-difference between first and last column: " + Math.abs(firstcolumn-lastcolumn));
        System.out.println("or " + 100.0 * Math.abs(firstcolumn-lastcolumn)/shortestP[0].length + "% of height");
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
    static void showImage(Stage stage, Image image) {
        ImageView imageView = new ImageView();
        imageView.setImage(image);
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
            System.out.println("image saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch();
    }
}