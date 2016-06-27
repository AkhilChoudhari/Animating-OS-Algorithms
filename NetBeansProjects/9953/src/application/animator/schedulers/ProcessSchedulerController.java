/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.schedulers;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sansa
 */
public class ProcessSchedulerController implements Initializable {
    
    private LinkedList<ProcessPojo> processPojoList;
//    A clonned version of above list of processPojos in order to maintain the data.
//    Why? After execution of any of the algorithms the list of process pojos is 
//    exhausted. Hence, keep a clone of list in order to refill the original process pojos list.
//    this collection will be used for refill purpose only. 
//    Work around for this is to click on Assign Processes labelled button after execution of each algorithm.
//    Why? -> So that processPojosList is reinitiallized and refilled with new pojos but with same parameters.
//    private LinkedList<ProcessPojo> processPojoListClonned;
    private ObservableList<ProcessInfoViewPojo> processInfoViewPojoList;
    
//    
    private LinkedList<StackPane> readyQueueProcessGraphicalComponentsList;
    private LinkedList<StackPane> currentExecutionProcessGrahicalComponentsList;
    
    @FXML private HBox processCompletionStatusHBox;
    @FXML private HBox readyQueueHBox;
    @FXML private HBox currentExecutionProcessHBox;
    @FXML private HBox ganntChartHBox;
    
    @FXML private TableView processTimeLogTableView;
    @FXML private TableColumn nameColumn;
    @FXML private TableColumn arrivalTimeColumn;
    @FXML private TableColumn burstTimeColumn;
    @FXML private TableColumn initiationTimeColumn;
    @FXML private TableColumn waitingTimeColumn;
    @FXML private TableColumn completionTimeColumn;
    @FXML private TableColumn turnAroundTimeColumn;
    
    @FXML private Label averageWaitingTimeLabel;
    @FXML private Label averageTurnAroundTimeLabel;
    
    @FXML private ComboBox<String> schedulerPolicyComboBox;
    @FXML private Label schedulerPolicyTitleLabel;
    @FXML private Label statusLabel;
    
//    @FXML private HBox schedulerPolicyTitleHBox;
    @FXML private Label timeSliceTitleLabel;
    @FXML private TextField timeSliceTextField;
    
    private Thread thread;

    public LinkedList<ProcessPojo> getProcessPojoList() {
        return processPojoList;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        processPojoList = new LinkedList<>();
//        ProcessPojo processPojo = new ProcessPojo("Process 1", 0, 5);
//        processPojoList.add(processPojo);
//        processPojo = new ProcessPojo("Process 2", 1, 8);
//        processPojoList.add(processPojo);
//        processPojo = new ProcessPojo("Process 3", 0, 6);
//        processPojoList.add(processPojo);
        
        readyQueueProcessGraphicalComponentsList = new LinkedList<>();
        currentExecutionProcessGrahicalComponentsList = new LinkedList<>();
        
        nameColumn.setCellValueFactory(new PropertyValueFactory<ProcessInfoViewPojo, String>("name"));
        arrivalTimeColumn.setCellValueFactory(new PropertyValueFactory<ProcessInfoViewPojo, Integer>("arrivalTime"));
        burstTimeColumn.setCellValueFactory(new PropertyValueFactory<ProcessInfoViewPojo, Integer>("burstTime"));
        initiationTimeColumn.setCellValueFactory(new PropertyValueFactory<ProcessInfoViewPojo, Integer>("initiationTime"));
        waitingTimeColumn.setCellValueFactory(new PropertyValueFactory<ProcessInfoViewPojo, Integer>("waitingTime"));
        completionTimeColumn.setCellValueFactory(new PropertyValueFactory<ProcessInfoViewPojo, Integer>("completionTime"));
        turnAroundTimeColumn.setCellValueFactory(new PropertyValueFactory<ProcessInfoViewPojo, Integer>("turnAroundTime"));
        
        processInfoViewPojoList = FXCollections.observableArrayList();
        processTimeLogTableView.setItems(processInfoViewPojoList);
        
        ObservableList<String> schedulerPolicyList = FXCollections.observableArrayList();
        schedulerPolicyList.add("First Come First Serve");
        schedulerPolicyList.add("Round Robin");
        schedulerPolicyList.add("Shortest Job First");
        schedulerPolicyList.add("Shortest Remaining Time");
        schedulerPolicyComboBox.setItems(schedulerPolicyList);
        
//        ProcessScheduler processScheduler = new FCFSProcessScheduler(processPojoList, processPojoList.get(0).getArrivalTime(),this);
//        ProcessScheduler processScheduler = new RoundRobinProcessScheduler(inputProcessContainer, inputProcessContainer.get(0).getArrivalTime(), this);
//        ProcessScheduler processScheduler = new SJFProcessScheduler(inputProcessContainer, inputProcessContainer.get(0).getArrivalTime(), this);
        
        schedulerPolicyComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                timeSliceTitleLabel.setText("");
                schedulerPolicyTitleLabel.setText(newValue);
                if(newValue.equalsIgnoreCase("Round Robin")){
                    timeSliceTextField.setDisable(false);
                    timeSliceTextField.setVisible(true);
                    
                } else {
                    timeSliceTextField.setDisable(true);
                    timeSliceTextField.setVisible(false);
                }
//                processPojoList.clear();
//                processInfoViewPojoList.clear();
//                ganntChartHBox.getChildren().clear();
//                processCompletionStatusHBox.getChildren().clear();
                
//                if(newValue.equalsIgnoreCase("First Come First Serve")){
//                    ProcessPojo processPojo = new ProcessPojo("Process 1", 0, 4);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 2", 1, 3);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 3", 2, 1);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 4", 3, 2);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 5", 4, 5);
//                    processPojoList.add(processPojo);
//                    
//                } else if(newValue.equalsIgnoreCase("Round Robin")){
////                    ProcessPojo processPojo = new ProcessPojo("Process 1", 0, 4);
////                    processPojoList.add(processPojo);
////                    processPojo = new ProcessPojo("Process 2", 1, 5);
////                    processPojoList.add(processPojo);
////                    processPojo = new ProcessPojo("Process 3", 2, 6);
////                    processPojoList.add(processPojo);
////                    processPojo = new ProcessPojo("Process 4", 4, 1);
////                    processPojoList.add(processPojo);
////                    processPojo = new ProcessPojo("Process 5", 6, 3);
////                    processPojoList.add(processPojo);
////                    processPojo = new ProcessPojo("Process 6", 7, 2);
//                    ProcessPojo processPojo = new ProcessPojo("Process 1", 0, 4);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 2", 1, 5);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 3", 2, 2);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 4", 3, 1);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 5", 4, 6);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 6", 6, 3);
//                    processPojoList.add(processPojo);
//                    
//                } else if(newValue.equalsIgnoreCase("Shortest Job First")){
//                    ProcessPojo processPojo = new ProcessPojo("Process 1", 1, 7);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 2", 2, 5);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 3", 3, 1);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 4", 4, 2);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 5", 5, 8);
//                    processPojoList.add(processPojo);
//                    
//                } else if(newValue.equalsIgnoreCase("Shortest Remaining Time")){
//                    ProcessPojo processPojo = new ProcessPojo("Process 1", 0, 7);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 2", 1, 5);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 3", 2, 3);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 4", 3, 1);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 5", 4, 2);
//                    processPojoList.add(processPojo);
//                    processPojo = new ProcessPojo("Process 6", 5, 1);
//                    processPojoList.add(processPojo);
//                }
                
                
//                Add process completion status graphical indicators to user interface. 
//                Also add elements to view pojo list
//                processPojoList
//                .stream()
//                .forEach(containerProcessPojo -> {
//
//                    VBox vBox = new VBox(5);
//                    vBox.getChildren().addAll(new Label(containerProcessPojo.getName()), containerProcessPojo.getProgressIndicator());
//
//                    processCompletionStatusHBox.getChildren().add(vBox);
//
//                    processInfoViewPojoList.add(
//                            new ProcessInfoViewPojo(
//                                    containerProcessPojo.getName(),
//                                    containerProcessPojo.getArrivalTime(),
//                                    containerProcessPojo.getBurstTime()
//                            ));
//                });

                
                
            }
        });
    }

    @FXML
    private void createRecreateProcesses(){
//        processCompletionStatusHBox.getChildren().clear();
        try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("ProcessCreationFXML.fxml"));
            Parent parent = fXMLLoader.load();
            ProcessCreationFXMLController processCreationFXMLController = fXMLLoader.getController();
            if(processInfoViewPojoList!=null || !processInfoViewPojoList.isEmpty()){
                processCreationFXMLController.setProcessInfoPojosObservableList(processInfoViewPojoList);
            }
            Scene scene = new Scene(parent);
            Stage stage = new Stage(); 
            stage.setScene(scene);
            stage.showAndWait();
            processPojoList.clear();
            processInfoViewPojoList.clear();
            processInfoViewPojoList.addAll(processCreationFXMLController.getProcessInfoPojosObservableList());
            processInfoViewPojoList
                    .stream()
                    .forEach(processInfoViewPojo->{
                        ProcessPojo processPojo = new ProcessPojo(processInfoViewPojo.getName(), processInfoViewPojo.getArrivalTime(), processInfoViewPojo.getBurstTime());
                        System.out.println("Created Process:>" + processInfoViewPojo.getName() + ", A:>" + processInfoViewPojo.getArrivalTime() + ", B:>" + processInfoViewPojo.getBurstTime());
                        processPojoList.add(processPojo);
                    });
            
//                Add process completion status graphical indicators to user interface. 
//                Also add elements to view pojo list
//                processPojoList
//                .stream()
//                .forEach(containerProcessPojo -> {
//
//                    VBox vBox = new VBox(5);
//                    vBox.getChildren().addAll(new Label(containerProcessPojo.getName()), containerProcessPojo.getProgressIndicator());
//                    
//                    processCompletionStatusHBox.getChildren().add(vBox);
//                });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void initiateSimulation(){
        processCompletionStatusHBox.getChildren().clear();
//        Add process completion status graphical indicators to user interface. 
//        Also add elements to view pojo list
        processPojoList
        .stream()
        .forEach(containerProcessPojo -> {

            VBox vBox = new VBox(5);
            vBox.getChildren().addAll(new Label(containerProcessPojo.getName()), containerProcessPojo.getProgressIndicator());
            
            processCompletionStatusHBox.getChildren().add(vBox);
        });
        ganntChartHBox.getChildren().clear();
        
        String selectedSchedulerPolicy = schedulerPolicyComboBox.getSelectionModel().getSelectedItem();
        ProcessScheduler processScheduler = null;
        
        if(selectedSchedulerPolicy.equalsIgnoreCase("First Come First Serve")){
            processScheduler = new FCFSProcessScheduler(processPojoList, processPojoList.get(0).getArrivalTime(), this);
        } else if(selectedSchedulerPolicy.equalsIgnoreCase("Round Robin")){
            processScheduler = new RoundRobinProcessSchedulerV2(processPojoList, processPojoList.get(0).getArrivalTime(), this);
            ((RoundRobinProcessSchedulerV2)processScheduler).setTimeSlice(Integer.parseInt(timeSliceTextField.getText()));
            timeSliceTitleLabel.setText("(Time Slice:" + ((RoundRobinProcessSchedulerV2)processScheduler).getTimeSlice() +")");
        } else if(selectedSchedulerPolicy.equalsIgnoreCase("Shortest Job First")){
            processScheduler = new SJFProcessScheduler(processPojoList, processPojoList.get(0).getArrivalTime(), this);
        } else if(selectedSchedulerPolicy.equalsIgnoreCase("Shortest Remaining Time")){
            processScheduler = new SRTProcessScheduler(processPojoList, processPojoList.get(0).getArrivalTime(), this);
        }
        thread = new Thread((Runnable) processScheduler);
        thread.start();
    }
    
    public void addGraphicalRepresentationProcessReadyQueue(String inProcessName){
        Sphere sphere = new Sphere(40);
        Label label = new Label(inProcessName);
        StackPane stackPane = new StackPane(sphere, label);
        readyQueueProcessGraphicalComponentsList.add(stackPane);
        readyQueueHBox.getChildren().clear();
        readyQueueHBox.getChildren().addAll(readyQueueProcessGraphicalComponentsList);
    }
    
    public void removeGraphicalRepresentationProcessReadyQueue(String inProcessName){
        LinkedList<StackPane> tempStackPanes = new LinkedList<>();
        readyQueueHBox.getChildren()
                .stream()
                .forEach(node->{
                    if(node instanceof StackPane){
                        ((StackPane) node).getChildren()
                                .stream()
                                .forEach((Node subNode)->{
                                    if(subNode instanceof Label){
                                        if(((Label) subNode).getText().equalsIgnoreCase(inProcessName)){
                                            tempStackPanes.add((StackPane) node);
                                        }
                                    }
                                });
                    }
                });
        
        readyQueueProcessGraphicalComponentsList.removeAll(tempStackPanes);
        readyQueueHBox.getChildren().removeAll(tempStackPanes);
    }
    
    public void addGraphicalRepresentationProcessJobExecution(String inProcessName){
        currentExecutionProcessGrahicalComponentsList.clear();
        Sphere sphere = new Sphere(40);
        Label label = new Label(inProcessName);
        StackPane stackPane = new StackPane(sphere, label);
        currentExecutionProcessGrahicalComponentsList.add(stackPane);
        currentExecutionProcessHBox.getChildren().clear();
        currentExecutionProcessHBox.getChildren().addAll(currentExecutionProcessGrahicalComponentsList);
    }
    
    public void removeGraphicalRepresentationProcessJobExecution(String inProcessName){
        LinkedList<StackPane> tempStackPanes = new LinkedList<>();
        currentExecutionProcessHBox.getChildren()
                .stream()
                .forEach(node->{
                    if(node instanceof StackPane){
                        ((StackPane) node).getChildren()
                                .stream()
                                .forEach((Node subNode)->{
                                    if(subNode instanceof Label){
                                        if(((Label) subNode).getText().equalsIgnoreCase(inProcessName)){
                                            tempStackPanes.add((StackPane) node);
                                        }
                                    }
                                });
                    }
                });
        currentExecutionProcessGrahicalComponentsList.removeAll(tempStackPanes);
        currentExecutionProcessHBox.getChildren().removeAll(tempStackPanes);
    }
    
    public void addGraphicalRepresentationProcessGanntChart(String inProcessName, int inClockCounterValue){
        Ellipse processNameEllipse = new Ellipse(15, 50);
//        String[] inProcessNameChars = inProcessName.split("");
//        String managedProcessName = "";
//        for(String singleChar:inProcessNameChars){
//            managedProcessName += singleChar + "\n";
//        }
//        Label label = new Label(managedProcessName);
        Label processNameLabel = new Label("P" + "\n" + inProcessName.split(" ")[1]);
        processNameLabel.setTextFill(Color.WHITE);
        StackPane processNameStackPane = new StackPane(processNameEllipse, processNameLabel);
        
        Rectangle clockCounterValueRectangle = new Rectangle(20, 20);
        Label clockCounterValueLabel = new Label(String.valueOf(inClockCounterValue));
        clockCounterValueLabel.setTextFill(Color.WHITE);
        StackPane clockCounterValueStackPane = new StackPane(clockCounterValueRectangle, clockCounterValueLabel);
        
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(5, 2, 5, 2));
        vBox.getChildren().addAll(processNameStackPane, clockCounterValueStackPane);
//        VBox.setMargin(vBox, new Insets(10, 10, 10, 10));
        ganntChartHBox.getChildren().add(vBox);
    }
    
//    Updates process execution initiation time of individual process within the time log table.
    public void updateInitiationTime(String inProcessName, int inInitiationtime){
        ObservableList<ProcessInfoViewPojo> tempProcessInfoViewPojoList = FXCollections.observableArrayList(processInfoViewPojoList);
        processInfoViewPojoList.clear();
        tempProcessInfoViewPojoList
                .stream()
                .forEach(processInfoViewPojo->{
                    if(processInfoViewPojo.getName().equalsIgnoreCase(inProcessName)){
//                        System.out.println("Found Record..." + inWaitingtime);
                        processInfoViewPojo.setInitiationTime(inInitiationtime);
                    }
                    processInfoViewPojoList.add(processInfoViewPojo);
                });
        
    }
    
//    Updates waiting time of individual process within the time log table.
    public void updateWaitingTime(String inProcessName, int inWaitingtime){
        ObservableList<ProcessInfoViewPojo> tempProcessInfoViewPojoList = FXCollections.observableArrayList(processInfoViewPojoList);
        processInfoViewPojoList.clear();
        tempProcessInfoViewPojoList
                .stream()
                .forEach(processInfoViewPojo->{
                    if(processInfoViewPojo.getName().equalsIgnoreCase(inProcessName)){
//                        System.out.println("Found Record..." + inWaitingtime);
                        processInfoViewPojo.setWaitingTime(inWaitingtime);
                    }
                    processInfoViewPojoList.add(processInfoViewPojo);
                });
        
        
        updateAverageWaitingTime();
        
    }
    
//    Updates average waiting time on the user interface
    private void updateAverageWaitingTime(){
        int totalWaitingTime = processInfoViewPojoList
                .stream()
                .mapToInt(processInfoViewPojo->processInfoViewPojo.getWaitingTime())
                .sum();
        int totalProcess = processInfoViewPojoList.size();
        double averageWaitingTime = (double) totalWaitingTime / totalProcess;
        String averageWaitingTimeString = new DecimalFormat("0.00").format(averageWaitingTime);
        averageWaitingTimeLabel.setText(String.valueOf(averageWaitingTimeString));
    }
    
//    Updates process execution completion time of individual process within the time log table.
    public void updateCompletionTime(String inProcessName, int inCompletiontime){
        ObservableList<ProcessInfoViewPojo> tempProcessInfoViewPojoList = FXCollections.observableArrayList(processInfoViewPojoList);
        processInfoViewPojoList.clear();
        tempProcessInfoViewPojoList
                .stream()
                .forEach(processInfoViewPojo->{
                    if(processInfoViewPojo.getName().equalsIgnoreCase(inProcessName)){
//                        System.out.println("Found Record..." + inWaitingtime);
                        processInfoViewPojo.setCompletionTime(inCompletiontime);
                    }
                    processInfoViewPojoList.add(processInfoViewPojo);
                });
        
    }
    
//    Updates turn around time of individual process within the time log table.
    public void updateTurnAroundTime(String inProcessName, int inTurnAroundtime){
        ObservableList<ProcessInfoViewPojo> tempProcessInfoViewPojoList = FXCollections.observableArrayList(processInfoViewPojoList);
        processInfoViewPojoList.clear();
        tempProcessInfoViewPojoList
                .stream()
                .forEach(processInfoViewPojo->{
                    if(processInfoViewPojo.getName().equalsIgnoreCase(inProcessName)){
//                        System.out.println("Found Record..." + inWaitingtime);
                        processInfoViewPojo.setTurnAroundTime(inTurnAroundtime);
                    }
                    processInfoViewPojoList.add(processInfoViewPojo);
                });
        
        updateAverageTurnAroundTime();
    }
    
//    Updates average turn around time on the user interface
    private void updateAverageTurnAroundTime(){
        int totalTurnAroundTime = processInfoViewPojoList
                .stream()
                .mapToInt(processInfoViewPojo->processInfoViewPojo.getTurnAroundTime())
                .sum();
        int totalProcess = processInfoViewPojoList.size();
        double averageTurnAroundTime = (double) totalTurnAroundTime / totalProcess;
        String averageTurnAroundTimeString = new DecimalFormat("0.00").format(averageTurnAroundTime);
        averageTurnAroundTimeLabel.setText(String.valueOf(averageTurnAroundTimeString));
    }
    
    public void updateStatus(String inStatus){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                statusLabel.setText(inStatus);
            }
        });
        
    }
}

