/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.deadlockavoidance;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author sansa
 */
public class BankersAlgorithmAnimatorController implements Initializable {
    
    @FXML private HBox systemResourcesHBox;
    @FXML private HBox allocatedSystemResourcesHBox;
    @FXML private HBox availableSystemResourcesHBox;
    
    @FXML private HBox safeExecutionSequenceHBox;
    
    @FXML private TableView requiredSystemResourcesTableView;
    @FXML private TableView allocatedSystemResourcesTableView;
    @FXML private TableView moreRequiredSystemResourcesTableView;
    
    private ArrayList<SystemResourcePojo> systemResources;
    private ArrayList<SystemProcessPojo> systemProcesses;
    private Thread thread;
    
    private final String PROCESS_ID_TABLE_COLUMN_TITLE = "Process Id";
    private final String PROCESS_ID_KEY = "ProcessId";
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        System.out.println("Hello there...");
//        createSystemComponents();
//        updateSystemResourcesHBoxViews();
//        initiateSystemResourcesInstancesStatusTableViews();
//        updateSystemResourcesInstancesStatusTableViews();
        
//        BankersAlgorithm bankersAlgorithm = new BankersAlgorithm(systemResources, systemProcesses, this);
//        thread = new Thread(bankersAlgorithm);
//        thread.start();
//        System.out.println("Whats up...");
        systemResources = new ArrayList<>();
        systemProcesses = new ArrayList<>();
    }
    
    @FXML
    private void configureSystemResources(){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("ResourceConfigurationFXML.fxml"));
            Parent parent = fXMLLoader.load();
            ResourceConfigurationFXMLController resourceConfigurationFXMLController = fXMLLoader.getController();
            if(systemResources!=null){
                if(!systemResources.isEmpty()){
                    resourceConfigurationFXMLController.setSystemResourceInfoPojosObservableList(systemResources);
                }
            }
            Scene scene = new Scene(parent);
            Stage stage = new Stage(); 
            stage.setScene(scene);
            stage.showAndWait();
            systemResources.clear();
            systemProcesses.clear();
            ObservableList<SystemResourceInfoViewPojo> systemResourceInfoPojosObservableList = resourceConfigurationFXMLController.getSystemResourceInfoPojosObservableList();
            systemResourceInfoPojosObservableList
                    .stream()
                    .forEach(systemResourceInfoPojo->{
                       SystemResourcePojo systemResourcePojo = new SystemResourcePojo(systemResourceInfoPojo.getId(), systemResourceInfoPojo.getTotalInstances());
                       systemResources.add(systemResourcePojo);
                    });
            System.out.println("Following are created System Resources...");
            systemResources
                    .stream()
                    .forEach(systemResourcePojo->{
                        System.out.println("Created systemResourcePojo> ID=" + systemResourcePojo.getId() + ", Total Instances=" + systemResourcePojo.getTotalInstances() + ", Available Instances=" + systemResourcePojo.getAvailableInstances());
                    });
            initiateSystemResourcesInstancesStatusTableViews();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void addProcess(){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("ProcessCreationFXML.fxml"));
            Parent parent = fXMLLoader.load();
            ProcessCreationFXMLController processCreationFXMLController = fXMLLoader.getController();
            processCreationFXMLController.setSystemResources(systemResources, systemProcesses);
            Scene scene = new Scene(parent);
            Stage stage = new Stage(); 
            stage.setScene(scene);
            stage.showAndWait();
//            Get the latest status after creation of process
//            systemResources.clear();
//            systemResources.addAll(processCreationFXMLController.getSystemResources());
            System.out.println("After Process Creation in Mains... Updated Resources Allocation and Availability...");
            systemResources
                .stream()
                .forEach(systemResourcePojo->{
                    System.out.println("Updated systemResourcePojo> ID=" + systemResourcePojo.getId() + ", Total Instances=" + systemResourcePojo.getTotalInstances() + ", Available Instances=" + systemResourcePojo.getAvailableInstances());
                });
            if(processCreationFXMLController.getSystemProcessPojo()!=null){
                systemProcesses.add(processCreationFXMLController.getSystemProcessPojo());
            }
            updateSystemResourcesHBoxViews();
//            initiateSystemResourcesInstancesStatusTableViews();
            updateSystemResourcesInstancesStatusTableViews();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML 
    private void removeProcess(){
        
    }
    
    @FXML
    private void configureSystemProcesses(){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("ProcessConfigurationFXML.fxml"));
            Parent parent = fXMLLoader.load();
            ProcessConfigurationFXMLController processConfigurationFXMLController = fXMLLoader.getController();
            if(systemResources!=null){
                if(!systemResources.isEmpty()){
                    processConfigurationFXMLController.setSystemResources(systemResources);
                }
            }
            Scene scene = new Scene(parent);
            Stage stage = new Stage(); 
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void initiateSimulation(){
        BankersAlgorithm bankersAlgorithm = new BankersAlgorithm(systemResources, systemProcesses, this);
        thread = new Thread(bankersAlgorithm);
        thread.start();
    }
    
    private void createSystemComponents(){
        
        systemResources = new ArrayList<>();
        systemProcesses = new ArrayList<>();
        
        SystemResourcePojo systemResourcePojoSR1 = new SystemResourcePojo("SR1",10);
        systemResources.add(systemResourcePojoSR1);
        
        SystemResourcePojo systemResourcePojoSR2 = new SystemResourcePojo("SR2",5);
        systemResources.add(systemResourcePojoSR2);
        
        SystemResourcePojo systemResourcePojoSR3 = new SystemResourcePojo("SR3",7);
        systemResources.add(systemResourcePojoSR3);
        
        SystemProcessPojo systemProcessPojoP1 = new SystemProcessPojo("P1");
        LinkedHashMap<String, Integer> totalResourceInstances = new LinkedHashMap<>();
        totalResourceInstances.put(systemResourcePojoSR1.getId(), 7);
        totalResourceInstances.put(systemResourcePojoSR2.getId(), 5);
        totalResourceInstances.put(systemResourcePojoSR3.getId(), 3);
        systemProcessPojoP1.setTotalResourceInstances(totalResourceInstances);
        LinkedHashMap<String, Integer> allocatedResourceInstances = new LinkedHashMap<>();
        allocatedResourceInstances.put(systemResourcePojoSR1.getId(), 0);
        allocatedResourceInstances.put(systemResourcePojoSR2.getId(), 1);
        allocatedResourceInstances.put(systemResourcePojoSR3.getId(), 0);
        systemProcessPojoP1.setAllocatedResourceInstances(allocatedResourceInstances);
        LinkedHashMap<String, Integer> requiredResourceInstances = new LinkedHashMap<>();
        requiredResourceInstances.put(systemResourcePojoSR1.getId(), 7);
        requiredResourceInstances.put(systemResourcePojoSR2.getId(), 4);
        requiredResourceInstances.put(systemResourcePojoSR3.getId(), 3);
        systemProcessPojoP1.setRequiredResourceInstances(requiredResourceInstances);
        systemProcesses.add(systemProcessPojoP1);

        SystemProcessPojo systemProcessPojoP2 = new SystemProcessPojo("P2");
        totalResourceInstances = new LinkedHashMap<>();
        totalResourceInstances.put(systemResourcePojoSR1.getId(), 3);
        totalResourceInstances.put(systemResourcePojoSR2.getId(), 2);
        totalResourceInstances.put(systemResourcePojoSR3.getId(), 2);
        systemProcessPojoP2.setTotalResourceInstances(totalResourceInstances);
        allocatedResourceInstances = new LinkedHashMap<>();
        allocatedResourceInstances.put(systemResourcePojoSR1.getId(), 2);
        allocatedResourceInstances.put(systemResourcePojoSR2.getId(), 0);
        allocatedResourceInstances.put(systemResourcePojoSR3.getId(), 0);
        systemProcessPojoP2.setAllocatedResourceInstances(allocatedResourceInstances);
        requiredResourceInstances = new LinkedHashMap<>();
        requiredResourceInstances.put(systemResourcePojoSR1.getId(), 1);
        requiredResourceInstances.put(systemResourcePojoSR2.getId(), 2);
        requiredResourceInstances.put(systemResourcePojoSR3.getId(), 2);
        systemProcessPojoP2.setRequiredResourceInstances(requiredResourceInstances);
        systemProcesses.add(systemProcessPojoP2);

        SystemProcessPojo systemProcessPojoP3 = new SystemProcessPojo("P3");
        totalResourceInstances = new LinkedHashMap<>();
        totalResourceInstances.put(systemResourcePojoSR1.getId(), 9);
        totalResourceInstances.put(systemResourcePojoSR2.getId(), 0);
        totalResourceInstances.put(systemResourcePojoSR3.getId(), 2);
        systemProcessPojoP3.setTotalResourceInstances(totalResourceInstances);
        allocatedResourceInstances = new LinkedHashMap<>();
        allocatedResourceInstances.put(systemResourcePojoSR1.getId(), 3);
        allocatedResourceInstances.put(systemResourcePojoSR2.getId(), 0);
        allocatedResourceInstances.put(systemResourcePojoSR3.getId(), 2);
        systemProcessPojoP3.setAllocatedResourceInstances(allocatedResourceInstances);
        requiredResourceInstances = new LinkedHashMap<>();
        requiredResourceInstances.put(systemResourcePojoSR1.getId(), 6);
        requiredResourceInstances.put(systemResourcePojoSR2.getId(), 0);
        requiredResourceInstances.put(systemResourcePojoSR3.getId(), 0);
        systemProcessPojoP3.setRequiredResourceInstances(requiredResourceInstances);
        systemProcesses.add(systemProcessPojoP3);

        SystemProcessPojo systemProcessPojoP4 = new SystemProcessPojo("P4");
        totalResourceInstances = new LinkedHashMap<>();
        totalResourceInstances.put(systemResourcePojoSR1.getId(), 2);
        totalResourceInstances.put(systemResourcePojoSR2.getId(), 2);
        totalResourceInstances.put(systemResourcePojoSR3.getId(), 2);
        systemProcessPojoP4.setTotalResourceInstances(totalResourceInstances);
        allocatedResourceInstances = new LinkedHashMap<>();
        allocatedResourceInstances.put(systemResourcePojoSR1.getId(), 2);
        allocatedResourceInstances.put(systemResourcePojoSR2.getId(), 1);
        allocatedResourceInstances.put(systemResourcePojoSR3.getId(), 1);
        systemProcessPojoP4.setAllocatedResourceInstances(allocatedResourceInstances);
        requiredResourceInstances = new LinkedHashMap<>();
        requiredResourceInstances.put(systemResourcePojoSR1.getId(), 0);
        requiredResourceInstances.put(systemResourcePojoSR2.getId(), 1);
        requiredResourceInstances.put(systemResourcePojoSR3.getId(), 1);
        systemProcessPojoP4.setRequiredResourceInstances(requiredResourceInstances);
        systemProcesses.add(systemProcessPojoP4);
        
        SystemProcessPojo systemProcessPojoP5 = new SystemProcessPojo("P5");
        totalResourceInstances = new LinkedHashMap<>();
        totalResourceInstances.put(systemResourcePojoSR1.getId(), 4);
        totalResourceInstances.put(systemResourcePojoSR2.getId(), 3);
        totalResourceInstances.put(systemResourcePojoSR3.getId(), 3);
        systemProcessPojoP5.setTotalResourceInstances(totalResourceInstances);
        allocatedResourceInstances = new LinkedHashMap<>();
        allocatedResourceInstances.put(systemResourcePojoSR1.getId(), 0);
        allocatedResourceInstances.put(systemResourcePojoSR2.getId(), 0);
        allocatedResourceInstances.put(systemResourcePojoSR3.getId(), 2);
        systemProcessPojoP5.setAllocatedResourceInstances(allocatedResourceInstances);
        requiredResourceInstances = new LinkedHashMap<>();
        requiredResourceInstances.put(systemResourcePojoSR1.getId(), 4);
        requiredResourceInstances.put(systemResourcePojoSR2.getId(), 3);
        requiredResourceInstances.put(systemResourcePojoSR3.getId(), 1);
        systemProcessPojoP5.setRequiredResourceInstances(requiredResourceInstances);
        systemProcesses.add(systemProcessPojoP5);
        
        systemResources
            .stream()
            .forEach(systemResourcePojo->{
//                System.out.println(systemResourcePojo.getId());
                systemProcesses
                .stream()
                .forEach(systemProcessPojo->{
//                    System.out.println(systemProcessPojo.getId());
                    systemProcessPojo.getAllocatedResourceInstances()
                            .entrySet()
                            .stream()
                            .filter(systemResourceEntry->systemResourceEntry.getKey().equalsIgnoreCase(systemResourcePojo.getId()))
                            .forEach(systemResourceEntryFiltered->{
//                                System.out.println(systemResourceEntryFiltered.getValue());
                                systemResourcePojo.updateInstancesAllocation(systemResourceEntryFiltered.getValue());
                            });
                });
            });
        
        printSystemResourceInstancesAvailability();
    }
    
    public void updateSystemResourcesInstancesAllocationForProcess(String inProcessId){
        
        systemProcesses
                .stream()
                .filter(systemProcessPojo->systemProcessPojo.getId().equalsIgnoreCase(inProcessId))
                .findFirst()
                .get()
                .getRequiredResourceInstances()
                .entrySet()
                .stream()
                .forEach(requiredResourceEntry->{
                    systemResources
                            .stream()
                            .filter(systemResourcePojo->systemResourcePojo.getId().equalsIgnoreCase(requiredResourceEntry.getKey()))
                            .findFirst()
                            .get()
                            .updateInstancesAllocation(requiredResourceEntry.getValue());
                });
                
        printSystemResourceInstancesAvailability();
        
        updateSystemResourcesHBoxViews();
        updateSystemResourcesInstancesStatusTableViews();
        
    }
    
    public void updateSystemResourcesInstancesAvailalibilityForProcess(String inProcessId){
        systemProcesses
                .stream()
                .filter(systemProcessPojo->systemProcessPojo.getId().equalsIgnoreCase(inProcessId))
                .findFirst()
                .get()
                .getAllocatedResourceInstances()
                .entrySet()
                .stream()
                .forEach(allocatedResourceEntry->{
                    systemResources
                            .stream()
                            .filter(systemResourcePojo->systemResourcePojo.getId().equalsIgnoreCase(allocatedResourceEntry.getKey()))
                            .findFirst()
                            .get()
                            .updateInstancesAvailability(allocatedResourceEntry.getValue());
                });
        
        printSystemResourceInstancesAvailability();
        
        updateSystemResourcesHBoxViews();
        updateSystemResourcesInstancesStatusTableViews();
        
        
        
    }
    
    private void printSystemResourceInstancesAvailability(){
        System.out.print("System Resources Instances availability current status :>>");
        systemResources
                .stream()
                .forEach(systemResourcePojo->{
                    System.out.print(systemResourcePojo.getId() + "::" + systemResourcePojo.getAvailableInstances() + "  ");
                });
        System.out.println();
    }
    
//    Displays graphical views for System Resources
    private void updateSystemResourcesHBoxViews(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                updateSystemResourcesHBoxView();
                updateAllocatedSystemResourcesHBoxView();
                updateAvailableSystemResourcesHBoxView();
            }
        });
        
    }
    
//    Displays graphical view for total System Resources
    private void updateSystemResourcesHBoxView(){
        systemResourcesHBox.getChildren().clear();
        systemResources
                .stream()
                .forEach(systemResourcePojo->{
                    
                    VBox vBox = new VBox();
                    vBox.setSpacing(5);
                    Rectangle systemResourceNameRectangle = new Rectangle(40, 40);                    
                    Label systemResourceNameLabel = new Label(systemResourcePojo.getId());
                    systemResourceNameLabel.setTextFill(Color.WHITE);
                    StackPane systemResourceNamestackPane = new StackPane(systemResourceNameRectangle, systemResourceNameLabel);
                    vBox.getChildren().add(systemResourceNamestackPane);
                    
                    Rectangle systemResourceInstancesRectangle = new Rectangle(30, 30);                    
                    Label systemResourceInstancesLabel = new Label(String.valueOf(systemResourcePojo.getTotalInstances()));
                    systemResourceInstancesLabel.setTextFill(Color.WHITE);
                    StackPane systemResourceInstancesstackPane = new StackPane(systemResourceInstancesRectangle, systemResourceInstancesLabel);
                    vBox.getChildren().add(systemResourceInstancesstackPane);
                    
                    systemResourcesHBox.getChildren().add(vBox);
                });
    }
    
//    Displays graphical view for allocated System Resources
    private void updateAllocatedSystemResourcesHBoxView(){
        allocatedSystemResourcesHBox.getChildren().clear();
        systemResources
                .stream()
                .forEach(systemResourcePojo->{
                    
                    VBox vBox = new VBox();
                    vBox.setSpacing(5);
                    Rectangle systemResourceNameRectangle = new Rectangle(40, 40);                    
                    Label systemResourceNameLabel = new Label(systemResourcePojo.getId());
                    systemResourceNameLabel.setTextFill(Color.WHITE);
                    StackPane systemResourceNamestackPane = new StackPane(systemResourceNameRectangle, systemResourceNameLabel);
                    vBox.getChildren().add(systemResourceNamestackPane);
                    
                    Rectangle systemResourceInstancesRectangle = new Rectangle(30, 30);                    
                    Label systemResourceInstancesLabel = new Label(String.valueOf(systemResourcePojo.getAllocatedInstances()));
                    systemResourceInstancesLabel.setTextFill(Color.WHITE);
                    StackPane systemResourceInstancesstackPane = new StackPane(systemResourceInstancesRectangle, systemResourceInstancesLabel);
                    vBox.getChildren().add(systemResourceInstancesstackPane);
                    
                    allocatedSystemResourcesHBox.getChildren().add(vBox);
                });
    }
    
//    Displays graphical view for available System Resources
    private void updateAvailableSystemResourcesHBoxView(){
        availableSystemResourcesHBox.getChildren().clear();
        systemResources
                .stream()
                .forEach(systemResourcePojo->{
                    
                    VBox vBox = new VBox();
                    vBox.setSpacing(5);
                    Rectangle systemResourceNameRectangle = new Rectangle(40, 40);                    
                    Label systemResourceNameLabel = new Label(systemResourcePojo.getId());
                    systemResourceNameLabel.setTextFill(Color.WHITE);
                    StackPane systemResourceNamestackPane = new StackPane(systemResourceNameRectangle, systemResourceNameLabel);
                    vBox.getChildren().add(systemResourceNamestackPane);
                    
                    Rectangle systemResourceInstancesRectangle = new Rectangle(30, 30);                    
                    Label systemResourceInstancesLabel = new Label(String.valueOf(systemResourcePojo.getAvailableInstances()));
                    systemResourceInstancesLabel.setTextFill(Color.WHITE);
                    StackPane systemResourceInstancesstackPane = new StackPane(systemResourceInstancesRectangle, systemResourceInstancesLabel);
                    vBox.getChildren().add(systemResourceInstancesstackPane);
                    
                    availableSystemResourcesHBox.getChildren().add(vBox);
                });
    }
    
    private void initiateSystemResourcesInstancesStatusTableViews(){
        initiateTotalRequiredSystemResourcesInstancesTableView();
        initiateAllocatedSystemResourcesInstancesTableView();
        initiateRequiredSystemResourcesInstancesTableView();
    }
    
    private void initiateTotalRequiredSystemResourcesInstancesTableView(){
        TableColumn processNameTableColumn = new TableColumn(PROCESS_ID_TABLE_COLUMN_TITLE);
        processNameTableColumn.setPrefWidth(100);
        processNameTableColumn.setCellValueFactory(new MapValueFactory(PROCESS_ID_KEY));
        requiredSystemResourcesTableView.getColumns().add(processNameTableColumn);
        
        systemResources
                .stream()
                .forEach(systemResourcePojo->{
//                    System.out.println(systemResourcePojo.getId());
                    TableColumn tableColumn = new TableColumn(systemResourcePojo.getId());
                    tableColumn.setPrefWidth(50);
                    tableColumn.setCellValueFactory(new MapValueFactory(systemResourcePojo.getId()));
                    tableColumn.setCellFactory(new Callback<TableColumn<Map, Integer>, TableCell<Map, Integer>>() {
                        @Override
                        public TableCell<Map, Integer> call(TableColumn<Map, Integer> param) {
                            return new TextFieldTableCell<>(new StringConverter<Integer>() {
                                @Override
                                public String toString(Integer object) {
                                    return String.valueOf(object);
                                }

                                @Override
                                public Integer fromString(String string) {
                                    return Integer.parseInt(string);
                                }
                            });
                        }
                    });
                    requiredSystemResourcesTableView.getColumns().add(tableColumn);
                });
    }
    
    private void initiateAllocatedSystemResourcesInstancesTableView(){
        TableColumn processNameTableColumn = new TableColumn(PROCESS_ID_TABLE_COLUMN_TITLE);
        processNameTableColumn.setPrefWidth(100);
        processNameTableColumn.setCellValueFactory(new MapValueFactory(PROCESS_ID_KEY));
        allocatedSystemResourcesTableView.getColumns().add(processNameTableColumn);
        
        systemResources
                .stream()
                .forEach(systemResourcePojo->{
//                    System.out.println(systemResourcePojo.getId());
                    TableColumn<Map,Integer> tableColumn = new TableColumn<>(systemResourcePojo.getId());
                    tableColumn.setPrefWidth(50);
                    tableColumn.setCellValueFactory(new MapValueFactory(systemResourcePojo.getId()));
                    tableColumn.setCellFactory(new Callback<TableColumn<Map, Integer>, TableCell<Map, Integer>>() {
                        @Override
                        public TableCell<Map, Integer> call(TableColumn<Map, Integer> param) {
                            return new TextFieldTableCell<>(new StringConverter<Integer>() {
                                @Override
                                public String toString(Integer object) {
                                    return String.valueOf(object);
                                }

                                @Override
                                public Integer fromString(String string) {
                                    return Integer.parseInt(string);
                                }
                            });
                        }
                    });
                    allocatedSystemResourcesTableView.getColumns().add(tableColumn);
                });
    }
    
    private void initiateRequiredSystemResourcesInstancesTableView(){
        TableColumn processNameTableColumn = new TableColumn(PROCESS_ID_TABLE_COLUMN_TITLE);
        processNameTableColumn.setPrefWidth(100);
        processNameTableColumn.setCellValueFactory(new MapValueFactory(PROCESS_ID_KEY));
        moreRequiredSystemResourcesTableView.getColumns().add(processNameTableColumn);
        
        systemResources
                .stream()
                .forEach(systemResourcePojo->{
//                    System.out.println(systemResourcePojo.getId());
                    TableColumn<Map,Integer> tableColumn = new TableColumn<>(systemResourcePojo.getId());
                    tableColumn.setPrefWidth(50);
                    tableColumn.setCellValueFactory(new MapValueFactory(systemResourcePojo.getId()));
                    tableColumn.setCellFactory(new Callback<TableColumn<Map, Integer>, TableCell<Map, Integer>>() {
                        @Override
                        public TableCell<Map, Integer> call(TableColumn<Map, Integer> param) {
                            return new TextFieldTableCell<>(new StringConverter<Integer>() {
                                @Override
                                public String toString(Integer object) {
                                    return String.valueOf(object);
                                }

                                @Override
                                public Integer fromString(String string) {
                                    return Integer.parseInt(string);
                                }
                            });
                        }
                    });
                    moreRequiredSystemResourcesTableView.getColumns().add(tableColumn);
                });
    }
    
    private void updateSystemResourcesInstancesStatusTableViews(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                updateTotalRequiredSystemResourcesInstancesTableView();
                updateAllocatedSystemResourcesInstancesTableView();
                updateRequiredSystemResourcesInstancesTableView();
            }
        });
        
    }
    
    private void updateTotalRequiredSystemResourcesInstancesTableView(){
        requiredSystemResourcesTableView.getItems().clear();
        ObservableList<Map> requiredSystemResourcesTableViewItems = FXCollections.observableArrayList();
        systemProcesses
                .stream()
                .forEach(systemProcessPojo->{
                    HashMap entriesMap = new HashMap();
                    entriesMap.put(PROCESS_ID_KEY, systemProcessPojo.getId());
                    entriesMap.putAll(systemProcessPojo.getTotalResourceInstances());
                    requiredSystemResourcesTableViewItems.add(entriesMap);
                });
        requiredSystemResourcesTableView.getItems().addAll(requiredSystemResourcesTableViewItems);
    }
    
    private void updateAllocatedSystemResourcesInstancesTableView(){
        allocatedSystemResourcesTableView.getItems().clear();
        ObservableList<Map> allocatedSystemResourcesTableViewItems = FXCollections.observableArrayList();
        systemProcesses
                .stream()
                .forEach(systemProcessPojo->{
                    HashMap entriesMap = new HashMap();
                    entriesMap.put(PROCESS_ID_KEY, systemProcessPojo.getId());
                    entriesMap.putAll(systemProcessPojo.getAllocatedResourceInstances());
                    allocatedSystemResourcesTableViewItems.add(entriesMap);
                });
        allocatedSystemResourcesTableView.setItems(allocatedSystemResourcesTableViewItems);
    }
    
    private void updateRequiredSystemResourcesInstancesTableView(){
        moreRequiredSystemResourcesTableView.getItems().clear();
        ObservableList<Map> moreRequiredSystemResourcesTableViewItems = FXCollections.observableArrayList();
        systemProcesses
                .stream()
                .forEach(systemProcessPojo->{
                    HashMap entriesMap = new HashMap();
                    entriesMap.put(PROCESS_ID_KEY, systemProcessPojo.getId());
                    entriesMap.putAll(systemProcessPojo.getRequiredResourceInstances());
                    moreRequiredSystemResourcesTableViewItems.add(entriesMap);
                });
        moreRequiredSystemResourcesTableView.setItems(moreRequiredSystemResourcesTableViewItems);
    }
    
    public void updateSafeExecutionSequenceHBox(String inProcessId, boolean inIsSafe){
        if(inIsSafe){
            Circle circle = new Circle(20, Color.GREEN);
            Label label = new Label(inProcessId);
            StackPane stackPane = new StackPane(circle, label);
            safeExecutionSequenceHBox.getChildren().add(stackPane);
        } else {
            safeExecutionSequenceHBox.getChildren()
                    .stream()
                    .forEach(node->{
                        if(node instanceof StackPane){
                            ((StackPane)node).getChildren()
                                    .stream()
                                    .filter(childNode->childNode instanceof Circle)
                                    .forEach(circleChildNode->{
                                        ((Circle)circleChildNode).setFill(Color.RED);
                                    });
                        }
                    });
        }
        
    }
    
    
    
}
