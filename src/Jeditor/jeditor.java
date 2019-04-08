/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jeditor;

        

import Jeditor.cloud.Go.Godrive;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.application.Application;
import javafx.application.Preloader;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 *
 * @author blinderjay
 */
public class jeditor extends  Application {
    Parent root; 
    static Path path=null;   // //non-static variable path cannot be referenced from a static context ( return from Paths.get() methods)
 
    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.TRANSPARENT);
        
        FXMLLoader loader = new FXMLLoader();
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(jeditor.class.getResource("MainPage.fxml"));
        
        try(InputStream in = jeditor.class.getResourceAsStream("MainPage.fxml")){
            root = (Parent)loader.load(in);
        }
        
        MainPageController mainpage = (MainPageController)loader.getController();
        mainpage.setEditor(this, stage);
        mainpage.setPath(path);
        
        
       //        Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        Scene scene = new Scene(root);
        mainpage.setScene(scene);
        scene.getStylesheets().add("/Jeditor/effect/css/AppleView.css");
        scene.setFill(Color.TRANSPARENT);
//        stage.setWidth(1200);
//        stage.setHeight(900);
        stage.setTitle("Jeditor : an open, free editor highly customizable");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Jeditor/image/icon/Jeditor.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        
        try{
            if(args!=null && !args[0].isEmpty() )
            path = Paths.get(args[0]);
        }catch(ArrayIndexOutOfBoundsException e){
            
        }        
        launch(args);
    }
     
}
