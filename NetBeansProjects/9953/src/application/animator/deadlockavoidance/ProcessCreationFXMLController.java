/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.deadlockavoidance;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sansa
 */
public class ProcessCreationFXMLController implements Initializable {
    
    @FXML private VBox availableSystemResourceInstancesVBox;
    @FXML private Button saveAndCloseButton;
    
    /**
     * Initializes the controller class.
     */
    
    private LinkedHashMap<String, Integer> totalRequiredResourceInstances;
    private LinkedHashMap<String, Integer> allocatedResourceInstances;
    private LinkedHashMap<String, Integer> moreRequiredResourceInstances;
    
    private final String PROCESS_TAG = "PROCESS ";
    private int processCounter;
    private SystemProcessPojo systemProcessPojo;

    public SystemProcessPojo getSystemProcessPojo() {
        return systemProcessPojo;
    }
    
    private ArrayList<SystemResourcePojo> systemResources;
    
    public ArrayList<SystemResourcePojo> getSystemResources() {
        return systemResources;
    }

    public void setSystemResources(ArrayList<SystemResourcePojo> systemResources, ArrayList<SystemProcessPojo> systemProcesses) {
        processCounter = systemProcesses.size();
        totalRequiredResourceInstances = new LinkedHashMap<>();
        allocatedResourceInstances = new LinkedHashMap<>();
        moreRequiredResourceInstances = new LinkedHashMap<>();
        this.systemResources = systemResources;
        systemResources
                .stream()
                .forEach(systemResourcePojo->{
                    
                    Rectangle idRectangle = new Rectangle(40, 30);
                    Label idLabel = new Label(systemResourcePojo.getId());
                    idLabel.setTextFill(Color.WHITE);
                    StackPane idStackPane = new StackPane(idRectangle, idLabel);
                    
                    Rectangle availableRectangle = new Rectangle(40, 30);
                    Label availableInstancesLabel = new Label(String.valueOf(systemResourcePojo.getAvailableInstances()));
                    availableInstancesLabel.setTextFill(Color.WHITE);
                    StackPane availableStackPane = new StackPane(availableRectangle, availableInstancesLabel);
                    
                    VBox vBox = new VBox();
                    vBox.setSpacing(5);
                    vBox.getChildren().addAll(idStackPane, availableStackPane);
                    
                    TextField totalRequiredSystemResourceInstancesTextField = new TextField();
                    totalRequiredSystemResourceInstancesTextField.setPromptText("Total Required Instances");
                    totalRequiredSystemResourceInstancesTextField.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            
                        }
                    });
                    
                    TextField moreRequiredSystemResourceInstancesTextField = new TextField();
                    moreRequiredSystemResourceInstancesTextField.setPromptText("More Required Instances");
                    moreRequiredSystemResourceInstancesTextField.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            
                        }
                    });
                    
                    TextField allocatedSystemResourceInstancesTextField = new TextField();
                    allocatedSystemResourceInstancesTextField.setPromptText("Allocated Instances");
                    allocatedSystemResourceInstancesTextField.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            int totalRequiredSystemResourceInstances = Integer.parseInt(totalRequiredSystemResourceInstancesTextField.getText());
                            int allocatedSystemResourceInstance = Integer.parseInt(newValue);
                            int moreRequiredSystemResourceInstances = totalRequiredSystemResourceInstances - allocatedSystemResourceInstance;
                            moreRequiredSystemResourceInstancesTextField.setText(String.valueOf(moreRequiredSystemResourceInstances));
                            availableInstancesLabel.setText(String.valueOf(systemResourcePojo.getAvailableInstances() - allocatedSystemResourceInstance));
                            
                            totalRequiredResourceInstances.put(systemResourcePojo.getId(), totalRequiredSystemResourceInstances);
                            allocatedResourceInstances.put(systemResourcePojo.getId(), allocatedSystemResourceInstance);
                            moreRequiredResourceInstances.put(systemResourcePojo.getId(), moreRequiredSystemResourceInstances);
                            
                            
                        }
                    });
                    
                    HBox hBox = new HBox();
                    hBox.setSpacing(10);
                    hBox.getChildren().addAll(vBox, totalRequiredSystemResourceInstancesTextField, allocatedSystemResourceInstancesTextField, moreRequiredSystemResourceInstancesTextField);
                    
                    availableSystemResourceInstancesVBox.getChildren().add(hBox);
                    
                });
        
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        totalRequiredResourceInstances = new LinkedHashMap<>();
        allocatedResourceInstances = new LinkedHashMap<>();
        moreRequiredResourceInstances = new LinkedHashMap<>();
        processCounter = 0;
        systemProcessPojo = null;
    }

    @FXML
    private void saveAndClose(){
        systemProcessPojo = new SystemProcessPojo(PROCESS_TAG + (++processCounter));
        availableSystemResourceInstancesVBox
                .getChildren()
                .stream()
                .filter(childNode->childNode instanceof HBox)
                .forEach(hboxNode->{
                    ((HBox)hboxNode).getChildren()
                            .stream()
                            .filter(filterHboxChildNode->filterHboxChildNode instanceof VBox)
                            .forEach(vboxChildNode->{
                               StackPane idStackPane = (StackPane) ((VBox)vboxChildNode).getChildren().get(0);
                               Node idLabelNode = idStackPane
                                       .getChildren()
                                       .stream()
                                       .filter(idStackPaneFilterNode->idStackPaneFilterNode instanceof Label)
                                       .findFirst()
                                       .get();
                                
                               String id = ((Label)idLabelNode).getText();
                               
                               StackPane availableStackPane = (StackPane) ((VBox)vboxChildNode).getChildren().get(1);
                               Node availableLabelNode = availableStackPane
                                       .getChildren()
                                       .stream()
                                       .filter(availableStackPaneFilterNode->availableStackPaneFilterNode instanceof Label)
                                       .findFirst()
                                       .get();
                                int availableUpdated = Integer.parseInt(((Label)availableLabelNode).getText());
                                
//                                System.out.println("Process id:" + id + ", Available:" + available);
                                int available = systemResources
                                        .stream()
                                        .filter(systemResourcePojo->systemResourcePojo.getId().equalsIgnoreCase(id))
                                        .findFirst()
                                        .get()
                                        .getAvailableInstances();
                                
                                systemResources
                                        .stream()
                                        .filter(systemResourcePojo->systemResourcePojo.getId().equalsIgnoreCase(id))
                                        .findFirst()
                                        .get()
                                        .updateInstancesAllocation(available - availableUpdated);
                                
                               
                            });
                });
        
        System.out.println("After Process Creation... Updated Resources Allocation and Availability...");
        systemResources
                .stream()
                .forEach(systemResourcePojo->{
                    System.out.println("Updated systemResourcePojo> ID=" + systemResourcePojo.getId() + ", Total Instances=" + systemResourcePojo.getTotalInstances() + ", Available Instances=" + systemResourcePojo.getAvailableInstances());
                });
        
        System.out.println("Total System Resource Required Instances are as follows:-");
        totalRequiredResourceInstances
                .entrySet()
                .stream()
                .forEach(totalRequiredResourceInstancesEntry->{
                    System.out.println("Resource Id:" + totalRequiredResourceInstancesEntry.getKey() + ", Total Required Instances:" + totalRequiredResourceInstancesEntry.getValue());
                });
        systemProcessPojo.setTotalResourceInstances(totalRequiredResourceInstances);
        
        System.out.println("Allocated System Resource Instances are as follows:-");
        allocatedResourceInstances
                .entrySet()
                .stream()
                .forEach(allocatedResourceInstancesEntry->{
                    System.out.println("Resource Id:" + allocatedResourceInstancesEntry.getKey() + ", Allocated Instances:" + allocatedResourceInstancesEntry.getValue());
                });
        systemProcessPojo.setAllocatedResourceInstances(allocatedResourceInstances);
        
        System.out.println("More System Resource Required Instances are as follows:-");
        moreRequiredResourceInstances
                .entrySet()
                .stream()
                .forEach(moreRequiredInstancesEntry->{
                    System.out.println("Resource Id:" + moreRequiredInstancesEntry.getKey() + ", More Required Instances:" + moreRequiredInstancesEntry.getValue());
                });
        systemProcessPojo.setRequiredResourceInstances(moreRequiredResourceInstances);
        
        Stage stage = (Stage) saveAndCloseButton.getScene().getWindow();
        stage.close();
    }
    
}
