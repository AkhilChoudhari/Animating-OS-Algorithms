/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.schedulers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javafx.application.Platform;

/**
 *
 * @author Pratik
 */
public class RoundRobinProcessSchedulerV2 extends ProcessScheduler implements Runnable{
    private int timeSlice;
    public RoundRobinProcessSchedulerV2(LinkedList<ProcessPojo> processContainer, int timerCounter, ProcessSchedulerController processSchedulerController) {
        super(processContainer, timerCounter, processSchedulerController);
        timeSlice = 2;
    }
    
    public int getTimeSlice() {
        return timeSlice;
    }

    public void setTimeSlice(int timeSlice) {
        this.timeSlice = timeSlice;
    }
    
    @Override
    public void run() {
        int totalProcesses=this.processContainer.size();
        int timeSliceCounter = 0;
        boolean switchProcess = false;
        String executionSequence = "";
        do {            
            timerCounter++;
//            processCloneFirst = getProcess(timerCounter);
//            if(processCloneFirst!=null){
//                System.out.println("Initially selected:>" + processCloneFirst.getName());
//                readyQueue.add(processCloneFirst);
//                processContainer.remove(processCloneFirst);
////                Add the graphical representation for the process to the Ready Queue 
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        processSchedulerController.addGraphicalRepresentationProcessReadyQueue(processCloneFirst.getName());
//                    }
//                });
//                System.out.println("Process: " + processCloneFirst.getName() + " is added to Ready Queue");
//                try {
//                    TimeUnit.SECONDS.sleep(1);
//                } catch (InterruptedException ex) {
//                }
//            }
            List<ProcessPojo> tempProcessPojos = getAllProcesses();
            if(tempProcessPojos!=null && !tempProcessPojos.isEmpty()){
                readyQueue.addAll(tempProcessPojos);
                processContainer.removeAll(tempProcessPojos);
                processCloneFirst = readyQueue.getFirst();
                System.out.println("Initially selected:>" + processCloneFirst.getName());
//                Add the graphical representation for the process to the Ready Queue 
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        processSchedulerController.addGraphicalRepresentationProcessReadyQueue(processCloneFirst.getName());
                    }
                });
                System.out.println("Process: " + processCloneFirst.getName() + " is added to Ready Queue");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                }
                
            }
            if(systemIdle||switchProcess){
                if(switchProcess){
                    switchProcess = false;
                    timeSliceCounter = 0;
                    System.out.println("Time to switch process...");
                    for(ProcessPojo processPojoCounter: readyQueue){
                        System.out.println(readyQueue.indexOf(processPojoCounter) + "::" + processPojoCounter.getName());
                    }
                    ProcessPojo tempProcessPojo = readyQueue.get(0);
                    readyQueue.remove(tempProcessPojo);
                    readyQueue.add(tempProcessPojo);
                    System.out.println("After Switching...");
                    for(ProcessPojo processPojoCounter: readyQueue){
                        System.out.println(readyQueue.indexOf(processPojoCounter) + "::" + processPojoCounter.getName());
                    }
                }
                if(readyQueue.size()==0){
                    continue;
                }
                processCloneSecond = readyQueue.get(0);
                System.out.println("Switched process:>" + processCloneSecond.getName());
                if(processCloneSecond.isExecutionInitiated()){
                System.out.println("Continuing " + processCloneSecond.getName() + " execution...");
                } else {
                    System.out.println("Initiating " + processCloneSecond.getName() + " execution...");
//                    Process started its execution. Set its initiation time
                    processCloneSecond.setInitiationTime(timerCounter);
////                    And so do the waiting time
//                    processCloneSecond.setWaitingTime();
//                    System.out.println("Process " + processCloneSecond.getName() + " started its execution. Waiting Time:=" + processCloneSecond.getWaitingTime());
//                    Update the Initiation time for the process execution in time log
//                    Update the Waiting time for the process execution in time log
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            processSchedulerController.updateInitiationTime(processCloneSecond.getName(), processCloneSecond.getInitiationTime());
//                            processSchedulerController.updateWaitingTime(processCloneSecond.getName(), processCloneSecond.getWaitingTime());
                        }
                    });
                    processCloneSecond.setExecutionInitiated(true);
                }
//                Remove the graphical representation for the process from the Ready Queue 
//                Add the graphical representation for the process to the Execution Shell 
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        processSchedulerController.removeGraphicalRepresentationProcessReadyQueue(processCloneSecond.getName());
                        processSchedulerController.addGraphicalRepresentationProcessJobExecution(processCloneSecond.getName());
                    }
                });
                systemIdle = false;
            }
            processCloneSecond.execute();
            
//              Add the graphical representation for process execution to the Gannt Chart
            Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("timerCounter>>" + timerCounter);
                        processSchedulerController.addGraphicalRepresentationProcessGanntChart(processCloneSecond.getName(),timerCounter);
                    }
                });
            executionSequence+= ">>" + processCloneSecond.getName();
            timeSliceCounter++;
            System.out.println("Process: " + processCloneSecond.getName() + " is executing...");
            if(processCloneSecond.getTimeLeft()==0){
                System.out.println("Process: " + processCloneSecond.getName() + " execution completed!");
//                Process completed its execution. Set its completion time 
                processCloneSecond.setCompletionTime(timerCounter);
//                And so do the turn around time
                processCloneSecond.setTurnAroundTime();
//                And so do the waiting time
                processCloneSecond.setWaitingTime();
//                Update the Completion time for the process execution in time log
//                Remove the graphical representation for the process from the Execution Shell
//                Update the Completion time for the process execution in time log
                finishedQueue.add(processCloneSecond);
                readyQueue.remove(processCloneSecond);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        processSchedulerController.updateCompletionTime(processCloneSecond.getName(), processCloneSecond.getCompletionTime());
                        processSchedulerController.removeGraphicalRepresentationProcessJobExecution(processCloneSecond.getName());
                        processSchedulerController.updateTurnAroundTime(processCloneSecond.getName(), processCloneSecond.getTurnAroundTime());
                        processSchedulerController.updateWaitingTime(processCloneSecond.getName(), processCloneSecond.getWaitingTime());
                    }
                });
                systemIdle = true;
                timeSliceCounter = 0;
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                }
            }   else if(timeSliceCounter == timeSlice){
                System.out.println("Time Slice Up!!!");
                switchProcess = true;
            }
            try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                }
        } while (this.finishedQueue.size()<totalProcesses);
        System.out.println(executionSequence);
        Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        processSchedulerController.updateStatus("Round Robin Algorithm Execution Completed...");
                    }
                });
    }
    
    private List<ProcessPojo> getAllProcesses(){
        System.out.println("::In getInitProcess method::");
        System.out.println("Processes in pool are...");
        this.processContainer
                .stream()
                .forEach(processPojo->{
                    System.out.println("Process Id:" + processPojo.getName() + ", Arrival Time: " + processPojo.getArrivalTime() + ", Burst Time:" + processPojo.getBurstTime());
                });
        int earliestArrivalTime = this.processContainer
                .stream()
                .mapToInt(processPojo->processPojo.getArrivalTime())
                .min()
                .orElse(0);
        System.out.println("earliestArrivalTime:>" + earliestArrivalTime);
        return this.processContainer
                .stream()
                .filter(processPojo->processPojo.getArrivalTime()==earliestArrivalTime&&processPojo.getArrivalTime()<=timerCounter)
                .collect(Collectors.toList());
    }
    
    
    
}
