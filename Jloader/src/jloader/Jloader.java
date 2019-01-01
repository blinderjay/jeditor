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
import javafx.geometry.Pos;
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
public class Jloader extends Preloader {

    ProgressBar bar;
    Stage stage;
    final int width = 800;
    final int height = 300;

    @Override
    public void start(Stage stage) throws Exception {
        
        this.stage = stage;
        bar = new ProgressBar(0);
        BorderPane p = new BorderPane();
        Text text = new Text("JEditor正在疯狂加载中!");
        
        final Scene scene = new Scene(p, width, height);

        p.setCenter(bar);
        p.setStyle("-fx-background-color:transparent;");
        p.setBottom(text);

        
        text.setFont(new Font(20));
        text.setFill(Color.CRIMSON);

        scene.setFill(null);
        stage.initStyle(StageStyle.TRANSPARENT);
//        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
//        stage.setX(primaryScreenBounds.getWidth() /2- width/2);
//        stage.setY(primaryScreenBounds.getHeight()/2 - height);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void handleStateChangeNotification(Preloader.StateChangeNotification scn) {
        if (scn.getType() == Preloader.StateChangeNotification.Type.BEFORE_START) {
                
            try {
                Thread.sleep(1024);
            } catch (InterruptedException ex) {
                Logger.getLogger(Jloader1.class.getName()).log(Level.SEVERE, null, ex);
            }  
 stage.hide();
        }
    }

  
    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
//        if (info instanceof ProgressNotification) {
//            //提取应用程序发送过来的进度值
//            double v = ((ProgressNotification) info).getProgress();
//            System.out.println("handleApplicationNotification="+v);
//            bar.setProgress(v);
//        } else if (info instanceof StateChangeNotification) {
//            //隐藏/或者关闭preloader
////            stage.hide();
////            stage.close();
//        }
    }
    
    @Override
    public void handleProgressNotification(Preloader.ProgressNotification pn) {
        System.out.println("handleProgressNotification=" + pn.getProgress());
        if (pn.getProgress() != 1.0) {
            bar.setProgress(pn.getProgress() / 2);
        }
//        bar.setProgress(pn.getProgress());
    }

}
