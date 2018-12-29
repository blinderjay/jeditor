/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jeditor;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.application.Application;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 *
 * @author blinderjay
 */
public class Jeditor extends Application {
    Parent root; 
    static Path path=null;   // //non-static variable path cannot be referenced from a static context ( return from Paths.get() methods)
      
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Jeditor.class.getResource("MainPage.fxml"));
        
        try(InputStream in = Jeditor.class.getResourceAsStream("MainPage.fxml")){
            root = (Parent)loader.load(in);
        }
        
        MainPageController mainpage = (MainPageController)loader.getController();
        mainpage.setEditor(this, stage);
        mainpage.setPath(path);
        
       //        Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        Scene scene = new Scene(root);
        
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
