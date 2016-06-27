/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.deadlockavoidance;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author sansa
 */
public class ProcessConfigurationFXMLController implements Initializable {
    
    @FXML private TableView resourcesTableView;
    
    @FXML private TableColumn totalRequiredResourcesTableColumn;
    @FXML private TableColumn allocatedResourcesTableColumn;
    @FXML private TableColumn moreRequiredResourcesTableColumn;
    
    private ArrayList<SystemResourcePojo> systemResources;
    
    public ArrayList<SystemResourcePojo> getSystemResources() {
        return systemResources;
    }

    public void setSystemResources(ArrayList<SystemResourcePojo> systemResources) {
        System.out.println("Setting System Resources...");
        this.systemResources = systemResources;
        systemResources
                    .stream()
                    .forEach(systemResourcePojo->{
                        System.out.println("Creating Columns for systemResourcePojo id:>" + systemResourcePojo.getId());
                        TableColumn totalRequiredResourcesChildTableColumn = new TableColumn(systemResourcePojo.getId());
                        totalRequiredResourcesChildTableColumn.setPrefWidth(systemResourcePojo.getId().length()*10);
                        totalRequiredResourcesChildTableColumn.setCellValueFactory(new MapValueFactory(systemResourcePojo.getId()));
                        totalRequiredResourcesTableColumn.getColumns().add(totalRequiredResourcesChildTableColumn);
                        
                        TableColumn allocatedResourcesChildTableColumn = new TableColumn(systemResourcePojo.getId());
                        allocatedResourcesChildTableColumn.setPrefWidth(systemResourcePojo.getId().length()*10);
                        allocatedResourcesChildTableColumn.setCellValueFactory(new MapValueFactory(systemResourcePojo.getId()));
                        allocatedResourcesTableColumn.getColumns().add(allocatedResourcesChildTableColumn);
                        
                        TableColumn moreRequiredResourcesChildTableColumn = new TableColumn(systemResourcePojo.getId());
                        moreRequiredResourcesChildTableColumn.setPrefWidth(systemResourcePojo.getId().length()*10);
                        moreRequiredResourcesChildTableColumn.setCellValueFactory(new MapValueFactory(systemResourcePojo.getId()));
                        moreRequiredResourcesTableColumn.getColumns().add(moreRequiredResourcesChildTableColumn);
                        
                    });
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if(systemResources == null){
            systemResources = new ArrayList<>();
        }
        
    }
    
    @FXML
    private void addSystemProcess(){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("ProcessCreationFXML.fxml"));
            Parent parent = fXMLLoader.load();
            ProcessCreationFXMLController processCreationFXMLController = fXMLLoader.getController();
//            processCreationFXMLController.setSystemResources(systemResources);
            Scene scene = new Scene(parent);
            Stage stage = new Stage(); 
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void removeSystemProcess(){
        
    }
    
    

    
    
}
