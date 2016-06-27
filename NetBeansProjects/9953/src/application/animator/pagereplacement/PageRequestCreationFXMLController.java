/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.pagereplacement;

import application.animator.schedulers.*;
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
public class PageRequestCreationFXMLController implements Initializable {
    
    @FXML private TextField pageRequestIdTextField;
    @FXML private Button doneButton;
//    
    @FXML private TableView<PageInfoViewPojo> pageRequestInfoTableView;
    @FXML private TableColumn<PageInfoViewPojo, Integer> pageRequestIdTableColumn;
    
    private ObservableList<PageInfoViewPojo> pageInfoPojosObservableList;
    
    private int pageCounter;

    public ObservableList<PageInfoViewPojo> getPageInfoPojosObservableList() {
        return pageInfoPojosObservableList;
    }

    public void setPageInfoPojosObservableList(ObservableList<PageInfoViewPojo> pageInfoPojosObservableList) {
        this.pageInfoPojosObservableList.addAll(pageInfoPojosObservableList);
        pageCounter = pageInfoPojosObservableList.size();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        pageRequestIdTableColumn.setCellValueFactory(new PropertyValueFactory<PageInfoViewPojo, Integer>("id"));
        pageInfoPojosObservableList = FXCollections.observableArrayList();
        pageRequestInfoTableView.setItems(pageInfoPojosObservableList);
    }

    @FXML
    private void addProcess(){
        pageCounter++;
        PageInfoViewPojo pageInfoViewPojo = new PageInfoViewPojo(Integer.parseInt(pageRequestIdTextField.getText()));
        pageRequestIdTextField.setText("");
        
        pageInfoPojosObservableList.add(pageInfoViewPojo);
    }
    
    @FXML
    private void removeProcess(){
        if(pageRequestInfoTableView.getSelectionModel().getSelectedItem()!=null){
            pageInfoPojosObservableList.remove(pageRequestInfoTableView.getSelectionModel().getSelectedItem());
        } 
    }
    
    @FXML
    private void commitAndClose(){
        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();
    }
    
}
