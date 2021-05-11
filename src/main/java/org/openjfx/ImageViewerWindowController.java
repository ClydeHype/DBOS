package org.openjfx;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.When;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ImageViewerWindowController
{
    private final List<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;
    Thread thread;
    Parent root;
    Stage stage2 = new Stage();
    Scene scene;
    KeyFrame keyFrame;

    @FXML
    private Label label1;

    @FXML
    private Label delayLabel;

    @FXML
    private Button btnEndSlide;

    @FXML
    private Button setDelayBTN;

    @FXML
    private TextField setDelayField;

    @FXML
    private Button btnSetDelay;

    @FXML
    private ImageView imageView;

    @FXML
    Timeline timeline;

    @FXML
    private void handleBtnLoadAction()
    {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select image files");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                    "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
            List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

            if (!files.isEmpty()) {
                files.forEach((File f) ->
                {
                    images.add(new Image(f.toURI().toString()));
                });
                displayImage();
            }
        } catch(Exception e){
            label1.setText("No image chosen. Please choose an image.");
        }
    }

    @FXML
    private void handleBtnPreviousAction()
    {
        try {
            if (!images.isEmpty()) {
                currentImageIndex =
                        (currentImageIndex - 1 + images.size()) % images.size();
                displayImage();
            }
        } catch (Exception e){
            label1.setText("No image chosen. Please choose an image.");
        }
    }

    @FXML
    private void handleBtnNextAction()
    {
        try {
            if (!images.isEmpty()) {
                currentImageIndex = (currentImageIndex + 1) % images.size();
                displayImage();
            }
        } catch(Exception e){
            label1.setText("No image chosen. Please choose an image.");
        }
    }

    private void displayImage()
    {
        if (!images.isEmpty())
        {
            imageView.setImage(images.get(currentImageIndex));
        }
    }

    @FXML
    private void handleBtnStartSlide(ActionEvent event) {
        slideshowStart();
    }

    @FXML
    private void handleBtnEndSlide(ActionEvent event) {

        timeline.stop();

    }

    @FXML
    private void handleDelaySetBTN(ActionEvent event){

        displayDelayWindow();
    }

    @FXML
    private void handleSetDelay(ActionEvent event) {

        delayLabel.setText("Delay has been set. Close window.");

    }

    private void slideshowStart(){

        thread = new Thread();
        thread.start();

        if (!images.isEmpty()) {

            timeline = new Timeline(keyFrame = new KeyFrame(Duration.seconds(2), a -> {
                currentImageIndex++;
                displayImage();
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

        }

        else if (images.isEmpty()) {
            label1.setText("No image chosen. Please choose an image.");
        }

        else if(currentImageIndex == images.lastIndexOf(images)){
            currentImageIndex = 0;
            displayImage();
        }

    }

    private void displayDelayWindow(){

        try {
            root = FXMLLoader.load(getClass().getResource("delayPopUp.fxml"));

            scene = new Scene(root);

            stage2.setScene(scene);
            stage2.setTitle("");
            stage2.initModality(Modality.APPLICATION_MODAL);
            stage2.show();
        }
        catch(Exception q){
            System.out.println("Error occured in displaying DelayWindow");
        }

    }
}