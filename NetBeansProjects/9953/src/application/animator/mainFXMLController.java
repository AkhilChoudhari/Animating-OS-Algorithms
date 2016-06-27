/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator;

import application.animator.schedulers.ProcessSchedulerController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author sansa
 */
public class mainFXMLController implements Initializable {
    
    @FXML
    private void populateSchedulerAnimator(ActionEvent event) {
        try {
            System.out.println("populateSchedulerAnimator: You clicked me!");
            Parent parent = FXMLLoader.load(getClass().getResource("schedulers/ProcessSchedulerFXML.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage(); 
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
//            Logger.getLogger(mainFXMLController.class.getName()).log(Level.SEVERE, null, ex);
              ex.printStackTrace();
        }
        
    }
    
    @FXML
    private void launchPageReplacementAnimator(ActionEvent event) {
        try {
            System.out.println("launchPageReplacementSimulator : You clicked me!");
            Parent parent = FXMLLoader.load(getClass().getResource("pagereplacement/PageReplacementPolicySimulator.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage(); 
            stage.setScene(scene);
            stage.showAndWait();
            
        } catch (IOException ex) {
        }
    }
    
    @FXML
    private void launchBankersAlgorithmAnimator(ActionEvent actionEvent){
        try {
            System.out.println("launchBankersAlgorithmAnimator : You clicked me!");
            Parent parent = FXMLLoader.load(getClass().getResource("deadlockavoidance/BankersAlgorithmAnimator.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage(); 
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
