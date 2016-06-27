/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.deadlockavoidance;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sansa
 */
public class ResourceConfigurationFXMLController implements Initializable {
    
    @FXML private TextField totalInstancesTextField;
    @FXML private Button doneButton;
//    
    @FXML private TableView<SystemResourceInfoViewPojo> systemResourceInfoTableView;
    @FXML private TableColumn<SystemResourceInfoViewPojo, String> idTableColumn;
    @FXML private TableColumn<SystemResourceInfoViewPojo, Integer> totalInstancesTableColumn;
    
    private ObservableList<SystemResourceInfoViewPojo> systemResourceInfoPojosObservableList;
    
    private int systemResourceCounter;
    private final String SYSTEM_RESOURCE = "SR ";

    public ObservableList<SystemResourceInfoViewPojo> getSystemResourceInfoPojosObservableList() {
        return systemResourceInfoPojosObservableList;
    }

    public void setSystemResourceInfoPojosObservableList(ArrayList<SystemResourcePojo> systemResources) {
        systemResources
                .stream()
                .forEach(systemResourcePojo->{
                    SystemResourceInfoViewPojo systemResourceInfoViewPojo = new SystemResourceInfoViewPojo(systemResourcePojo.getId(), systemResourcePojo.getTotalInstances());
                    this.systemResourceInfoPojosObservableList.add(systemResourceInfoViewPojo);
                });
        systemResourceCounter = systemResourceInfoPojosObservableList.size();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        idTableColumn.setCellValueFactory(new PropertyValueFactory<SystemResourceInfoViewPojo, String>("id"));
        totalInstancesTableColumn.setCellValueFactory(new PropertyValueFactory<SystemResourceInfoViewPojo, Integer>("totalInstances"));
        systemResourceInfoPojosObservableList = FXCollections.observableArrayList();
        systemResourceInfoTableView.setItems(systemResourceInfoPojosObservableList);
    }

    @FXML
    private void addSystemResource(){
        if(!totalInstancesTextField.getText().equalsIgnoreCase("")){
            SystemResourceInfoViewPojo pageInfoViewPojo = new SystemResourceInfoViewPojo(SYSTEM_RESOURCE + (++systemResourceCounter), Integer.parseInt(totalInstancesTextField.getText()));
            systemResourceInfoPojosObservableList.add(pageInfoViewPojo);
            totalInstancesTextField.setText("");
        }
    }
    
    @FXML
    private void removeSystemResource(){
        if(systemResourceInfoTableView.getSelectionModel().getSelectedItem()!=null){
            systemResourceInfoPojosObservableList.remove(systemResourceInfoTableView.getSelectionModel().getSelectedItem());
        } 
    }
    
    @FXML
    private void commitAndClose(){
        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();
    }
    
}
