/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author sansa
 */
public class Main extends Application {
    
    private AnchorPane anchorPane;
         
    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane anchorPane = (AnchorPane)FXMLLoader.load(getClass().getResource("mainFXML.fxml"));
        
//        ProgressIndicator progressIndicator = new ProgressIndicator(0);
//        progressIndicator.setMinHeight(100);
//        progressIndicator.setMinWidth(100);
//        anchorPane.getChildren().add(progressIndicator);
        
        
        
        Scene scene = new Scene(anchorPane);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
