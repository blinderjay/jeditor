/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jloader;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.concurrent.Task;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Simple Preloader Using the ProgressBar Control
 *
 * @author blinderjay
 */
public class Jloader1 extends Preloader {

    ProgressBar bar;
    Stage stage;
    final int width = 500;
    final int height = 100;

    private Scene createPreloaderScene() {
        bar = new ProgressBar();
        BorderPane p = new BorderPane();
        p.setCenter(bar);
        return new Scene(p, 300, 150);
    }

    private Scene createPreloaderScene1() {
        bar = new ProgressBar();
        BorderPane p = new BorderPane();
        p.setCenter(bar);

        Text text = new Text("显示提示信息成功!");
        text.setFont(new Font(12));
        text.setFill(Color.GREEN);
        VBox box = new VBox();
        box.getChildren().add(text);
        box.setStyle("-fx-background:transparent;");
p.setStyle("-fx-background:transparent;");
        p.setBottom(box);

        final Scene scene = new Scene(p, width, height);
        scene.setFill(null);

        return scene;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
//        stage.initStyle(StageStyle.UNDECORATED);
//       stage.initStyle(StageStyle.TRANSPARENT);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getWidth() /2- width/2);
        stage.setY(primaryScreenBounds.getHeight()/2 - height);
        stage.setScene(createPreloaderScene1());
        stage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification scn) {
        if (scn.getType() == StateChangeNotification.Type.BEFORE_START) {
     stage.hide();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Jloader1.class.getName()).log(Level.SEVERE, null, ex);
            }
       
        }
    }

    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        bar.setProgress(pn.getProgress());
    }

}
