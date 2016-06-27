/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.schedulers;

import java.net.URL;
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
public class ProcessCreationFXMLController implements Initializable {
    
    @FXML private TextField arrivalTimeTextField;
    @FXML private TextField burstTimeTextField;
    @FXML private Button doneButton;
//    
    @FXML private TableView<ProcessInfoViewPojo> processInfoTableView;
    @FXML private TableColumn<ProcessInfoViewPojo, String> nameTableColumn;
    @FXML private TableColumn<ProcessInfoViewPojo, Integer> arrivalTableColumn;
    @FXML private TableColumn<ProcessInfoViewPojo, Integer> burstTableColumn;
    
    private ObservableList<ProcessInfoViewPojo> processInfoPojosObservableList;
    
    private final String PROCESS = "Process";
    private int processCounter;

    public ObservableList<ProcessInfoViewPojo> getProcessInfoPojosObservableList() {
        return processInfoPojosObservableList;
    }

    public void setProcessInfoPojosObservableList(ObservableList<ProcessInfoViewPojo> processInfoPojosObservableList) {
        this.processInfoPojosObservableList.addAll(processInfoPojosObservableList);
        processCounter = processInfoPojosObservableList.size();
    }
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<ProcessInfoViewPojo, String>("name"));
        arrivalTableColumn.setCellValueFactory(new PropertyValueFactory<ProcessInfoViewPojo, Integer>("arrivalTime"));
        burstTableColumn.setCellValueFactory(new PropertyValueFactory<ProcessInfoViewPojo, Integer>("burstTime"));
        processInfoPojosObservableList = FXCollections.observableArrayList();
        processInfoTableView.setItems(processInfoPojosObservableList);
    }

    @FXML
    private void addProcess(){
        processCounter++;
        ProcessInfoViewPojo processInfoViewPojo = new ProcessInfoViewPojo(PROCESS + " " + processCounter, Integer.parseInt(arrivalTimeTextField.getText()), Integer.parseInt(burstTimeTextField.getText()));
        arrivalTimeTextField.setText("");
        burstTimeTextField.setText("");
        
        processInfoPojosObservableList.add(processInfoViewPojo);
    }
    
    @FXML
    private void removeProcess(){
        if(processInfoTableView.getSelectionModel().getSelectedItem()!=null){
            processInfoPojosObservableList.remove(processInfoTableView.getSelectionModel().getSelectedItem());
        } 
    }
    
    @FXML
    private void commitAndClose(){
        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();
    }
    
}
