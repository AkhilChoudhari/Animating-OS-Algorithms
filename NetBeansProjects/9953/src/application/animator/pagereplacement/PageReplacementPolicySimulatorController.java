/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.pagereplacement;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Pratik
 */
public class PageReplacementPolicySimulatorController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML private HBox pageReplacementGanntHBox;
    @FXML private Label pageReplacementPolicyTitleLabel;
    @FXML private Label totalPageHitsLabel;
    @FXML private Label totalPageFaultsLabel;
    @FXML private ComboBox<String> pageReplacementPolicyComboBox;
    @FXML private Label pageSequenceLabel;
    
    List<PagePojo> pagesSequence = new ArrayList<>();
    int totalPageFrames = 3;
    int totalPageRequests = 10;
    int minimumPageId = 0;
    int maximumPageId = 9;
    Random random = new Random();
    Thread thread;
    
    ObservableList<PageInfoViewPojo> pageInfoViewPojos;
    String pageSequenceText;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ObservableList<String> pageReplacementPoliciesList = FXCollections.observableArrayList();
        pageReplacementPoliciesList.add("First In First Out");
        pageReplacementPoliciesList.add("Least Recently Used");
        pageReplacementPolicyComboBox.setItems(pageReplacementPoliciesList);
        
        pageReplacementPolicyComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("Selected Page Replacement Policy:>" + newValue);
                pageReplacementPolicyTitleLabel.setText(newValue);
                pageReplacementGanntHBox.getChildren().clear();
            }
        });
        
        pageInfoViewPojos = FXCollections.observableArrayList();
        pageSequenceText = "";
//        pageReplacementPolicyTitleLabel.setText("First In First Out Page Replacement Policy");
//        FIFOPageReplacement fifoPageReplacement = new FIFOPageReplacement(totalPageFrames, pagesSequence);
//        Thread thread = new Thread(fifoPageReplacement);
//        thread.start();
        
//        pageReplacementPolicyTitleLabel.setText("Least Recently Used Page Replacement Policy");
//        LRUPageReplacement lRUPageReplacement = new LRUPageReplacement(totalPageFrames, pagesSequence, this);
//        Thread thread = new Thread(lRUPageReplacement);
//        thread.start();
    }

    @FXML
    private void createRecreatePageRequests(){
        try {
            pageInfoViewPojos.clear();
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("PageRequestCreationFXML.fxml"));
            Parent parent = fXMLLoader.load();
            PageRequestCreationFXMLController pageRequestCreationFXMLController = fXMLLoader.getController();
            if(pagesSequence!=null){
                if(!pagesSequence.isEmpty()){
                    pagesSequence
                            .stream()
                            .forEach(pagePojo->{
                                PageInfoViewPojo pageInfoViewPojo = new PageInfoViewPojo(pagePojo.getId());
                                pageInfoViewPojos.add(pageInfoViewPojo);
                            });
                    pageRequestCreationFXMLController.setPageInfoPojosObservableList(pageInfoViewPojos);
                }
            }
            
            Scene scene = new Scene(parent);
            Stage stage = new Stage(); 
            stage.setScene(scene);
            stage.showAndWait();
            pageInfoViewPojos.clear();
            pagesSequence.clear();
            pageInfoViewPojos = pageRequestCreationFXMLController.getPageInfoPojosObservableList();
            pageSequenceText = "Page Requests <";
            pageInfoViewPojos
                    .stream()
                    .forEach(pageInfoViewPojo->{
                        PagePojo pagePojo = new PagePojo(pageInfoViewPojo.getId());
                        pagesSequence.add(pagePojo);
                        pageSequenceText += " " + pagePojo.getId();
                        System.out.print(pagePojo);
                    });
            System.out.println();
            pageSequenceText += " >";
            pageSequenceLabel.setText(pageSequenceText);
        
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void initiateSimulation(ActionEvent actionEvent){
//        String pageSequenceText = "Page Requests <";
//        pagesSequence.clear();
//        for(int totalPageRequestsCounter = 1; totalPageRequestsCounter<=totalPageRequests; totalPageRequestsCounter++){
//            int pageRequestId = random.nextInt(maximumPageId - minimumPageId + 1) + minimumPageId;
//            PagePojo pagePojo = new PagePojo(pageRequestId);
//            pageSequenceText += " " + pageRequestId;
//            pagesSequence.add(pagePojo);
//        }
//        pageSequenceText += " >";
//        pagesSequence
//                .stream()
//                .forEach(System.out::print);
//        
//        System.out.println();
        
        pageReplacementGanntHBox.getChildren().clear();
//        pageSequenceLabel.setText(pageSequenceText);
        totalPageHitsLabel.setText("");
        totalPageFaultsLabel.setText("");
        
        String selectedPageReplacementPolicy = pageReplacementPolicyComboBox.getSelectionModel().getSelectedItem();
        PageReplacementPolicy pageReplacementPolicy = null;
        if(selectedPageReplacementPolicy.equalsIgnoreCase("First In First Out")){
            pageReplacementPolicy = new FIFOPageReplacement(totalPageFrames, pagesSequence, this);
        } else if(selectedPageReplacementPolicy.equalsIgnoreCase("Least Recently Used")){
            pageReplacementPolicy = new LRUPageReplacement(totalPageFrames, pagesSequence, this);
        }
        
        thread = new Thread((Runnable) pageReplacementPolicy);
        thread.start();
        
    }
    
    @FXML
    private void pauseSimulation(ActionEvent actionEvent){
        
    }
    
    @FXML
    private void resumeSimulation(ActionEvent actionEvent){
        
    }
    
    @FXML
    private void stopSimulation(ActionEvent actionEvent){
        
    }
    
    public void updatePageReplacementGraphicalRepresentation(int inPageRequestId, PagePojo[] inPageFrames, boolean inPageHit, int pageFrameEventIndex){
//        VBox vBox = new VBox();
//        Label label = new Label(String.valueOf(inPageRequestId));
        VBox vBox = new VBox();
        
        Label pageRequestLabel = new Label("Page:" + String.valueOf(inPageRequestId));
        vBox.getChildren().add(pageRequestLabel);
        
        for(int pageFrameIndexCounter = 0; pageFrameIndexCounter < inPageFrames.length; pageFrameIndexCounter++){
            if(inPageFrames[pageFrameIndexCounter]!=null){
                Rectangle rectangle = new Rectangle(40, 40);
                rectangle.setStroke(Color.BLUE);
                if(pageFrameIndexCounter == pageFrameEventIndex && inPageHit){
                    rectangle.setFill(Color.GREEN);
                } else if(pageFrameIndexCounter == pageFrameEventIndex && !inPageHit){
                    rectangle.setFill(Color.RED);
                }
                Label label = new Label(String.valueOf(inPageFrames[pageFrameIndexCounter].getId()));
                label.setTextFill(Color.WHITE);
                StackPane stackPane = new StackPane(rectangle, label);
                vBox.getChildren().add(stackPane);
            } else{
                Rectangle rectangle = new Rectangle(40, 40);
                rectangle.setStroke(Color.BLUE);
                Label label = new Label("NULL");
                label.setTextFill(Color.WHITE);
                StackPane stackPane = new StackPane(rectangle, label);
                vBox.getChildren().add(stackPane);
            }
        }
        
        Label pageEventLabel = new Label(inPageHit?"Hit":"Fault");
        vBox.getChildren().add(pageEventLabel);
        
        pageReplacementGanntHBox.getChildren().add(vBox);
        
    }
    
    public void updateTotalPageHitsGraphicalRepresentation(int inTotalPageHits){
        totalPageHitsLabel.setText(String.valueOf(inTotalPageHits));
    }
    
    public void updateTotalPageFaultsGraphicalRepresentation(int inTotalPageFaults){
        totalPageFaultsLabel.setText(String.valueOf(inTotalPageFaults));
    }
    
}
