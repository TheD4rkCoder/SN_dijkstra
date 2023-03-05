package com.example.sn_dijkstra;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;

public class HelloApplication extends Application {
    static Image image; // Originalbild
    static WritableImage wImage; // Resultat-bild mit dem gearbeitet wird

    @Override
    public void start(Stage stage) throws IOException {
        loadImage("src/main/resources/com/example/sn_dijkstra/data/IMG_2.png");

        System.out.println("begin converting image");
        long time = System.currentTimeMillis();
        wImage = ImageEditor.convertToPolarCoordinates(image, (int)image.getWidth()/2 - 20);
        WritableImage beginImage = ImageEditor.reduceResolution(wImage, (int) (wImage.getWidth() / 200));
        wImage = ImageEditor.invertColor(wImage);
        int filterSize = ((int) (wImage.getWidth() / 50) % 2 == 0) ? (int) (wImage.getWidth() / 50) + 1 : (int) (wImage.getWidth() / 50);
        double[][] filter = ImageEditor.generateGaussianFilter(filterSize, wImage.getWidth()/100);
        wImage = ImageEditor.applyFilter(wImage, filter);
        wImage = ImageEditor.applyFilter(wImage, ImageEditor.gradientFilterV);
        wImage = ImageEditor.reduceResolution(wImage, (int) (wImage.getWidth() / 200));
        wImage = ImageEditor.saturateGrayscaleImage(wImage);
        wImage = ImageEditor.applyFilter(wImage, ImageEditor.generateGaussianFilter(9, 5));
        wImage = ImageEditor.saturateGrayscaleImage(wImage);
        wImage = ImageEditor.invertColor(wImage);
        System.out.println("image converted: " + (System.currentTimeMillis() - time) + "ms");

        System.out.println("begin building graph");
        time = System.currentTimeMillis();
        ImageGraph imageGraph = new ImageGraph(wImage);
        System.out.println("graph built: " + (System.currentTimeMillis() - time) + "ms");

        System.out.println("begin dijkstra algorithm");
        time = System.currentTimeMillis();
        Dijkstra dijkstra = new Dijkstra(imageGraph);
        dijkstra.startDijkstra();
        Boolean[][] shortestP = dijkstra.getShortestPathTo((int)(wImage.getWidth() + 1), 0);
        System.out.println("dijkstra complete: " + (System.currentTimeMillis() - time) + "ms");

        wImage = Dijkstra.applyShortestPathToImage(beginImage,shortestP); // zeichnet eine rote Linie für den kürzesten Weg
        wImage = ImageEditor.convertToKartesianImage(wImage);
        saveImage(wImage); // speichert das Bild in "image.png" im root ordner
        showImage(stage, wImage); // öffnet ein Fenster und zeigt das übergebene Bild
        int firstColumn = 0, lastColumn = 0;
        for (int i = 0; i < shortestP[0].length; i++) {
            if (shortestP[1][i]) {
                firstColumn = i;
            }
            if (shortestP[shortestP.length-2][i]) {
                lastColumn = i;
            }
        }
        System.out.println("y-difference between first and last column: " + Math.abs(firstColumn-lastColumn));
        System.out.println("or " + 100.0 * Math.abs(firstColumn-lastColumn)/shortestP[0].length + "% of height");
    }



    static public void loadImage(String path) {
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream(path); // use other file names
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        image = new Image(inputstream);
        //wImage = ImageEditor.applyGradiant(wImage, ImageEditor.gradientFilter);
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