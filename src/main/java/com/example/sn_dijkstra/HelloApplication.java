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

        loadImage();

        /*
        PixelWriter pw = wImage.getPixelWriter();
        for (int i = 0; i < 1900; i++) {
            for (int j = 0; j < 1900; j++) {
                pw.setColor(i,j,Color.DARKBLUE);
            }
        }

         */
        double[][] filter = Bildeditor.generateGaussianFilter(21, 18);
        wImage = Bildeditor.applyFilter(wImage, filter);
        wImage = Bildeditor.applyFilter(wImage, Bildeditor.gradientFilter);
        //wImage = Bildeditor.applyGradiant(wImage, filter);
        wImage = Bildeditor.invertColor(wImage);
        wImage = Bildeditor.reduceResolution(wImage, 25);
        ImageGraph imageGraph = new ImageGraph(wImage);
        //wImage = imageGraph.drawGraphImage(wImage);
        //saveImage(wImage);
        //wImage = Bildeditor.applyGradiant(wImage, Bildeditor.gradientFilterV);
        showImage(stage);

    }



    static public void loadImage() {
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream("src/main/resources/com/example/sn_dijkstra/data/IMG_1.jpg"); // use other file names
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        image = new Image(inputstream);
        pixelReader = image.getPixelReader();
        wImage = Bildeditor.convertToPolarCoordinates(image);
        //wImage = Bildeditor.applyGradiant(wImage, Bildeditor.gradientFilter);
        PixelWriter pixelWriter = wImage.getPixelWriter();
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