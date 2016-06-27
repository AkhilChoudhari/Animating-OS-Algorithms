/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.schedulers;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;

/**
 *
 * @author sansa
 */
public class RoundRobinProcessScheduler extends ProcessScheduler implements Runnable{
    
    private int timeSlice;
    
    public RoundRobinProcessScheduler(LinkedList<ProcessPojo> processContainer, int timerCounter, ProcessSchedulerController processSchedulerController) {
        super(processContainer, timerCounter, processSchedulerController);
        timeSlice = 2;
    }

    public int getTimeSlice() {
        return timeSlice;
    }
    
    @Override
    public void run() {
        int totalProcesses=this.processContainer.size();
        int timeSliceCounter = 0;
        int switchProcessIndex =0;
        boolean switchProcess = false;
        do {            
            timerCounter++;
            processCloneFirst = getProcess(timerCounter);
            
            if(processCloneFirst!=null){
                System.out.println("Initially selected:>" + processCloneFirst.getName());
                readyQueue.add(processCloneFirst);
                processContainer.remove(processCloneFirst);
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
//                    Logger.getLogger(FCFSProcessScheduler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(systemIdle||switchProcess){
                if(switchProcess){
                    switchProcess = false;
                    timeSliceCounter = 0;
                }
                if(readyQueue.size()==0){
                    continue;
                }
                if(switchProcessIndex<readyQueue.size()-1){
                    switchProcessIndex++;
                } else{
                    switchProcessIndex=0;
                }
                processCloneSecond = readyQueue.get(switchProcessIndex);
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
                        processSchedulerController.addGraphicalRepresentationProcessGanntChart(processCloneSecond.getName(),timerCounter);
                    }
                });
            timeSliceCounter++;
            System.out.println("Process: " + processCloneSecond.getName() + " is executing...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
//                    Logger.getLogger(FCFSProcessScheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
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
                switchProcessIndex--;
                systemIdle = true;
                timeSliceCounter = 0;
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
    //                    Logger.getLogger(FCFSProcessScheduler.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if(timeSliceCounter == timeSlice){
                switchProcess = true;
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
    //                    Logger.getLogger(FCFSProcessScheduler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } while (this.finishedQueue.size()<totalProcesses);
        System.out.println("Round Robin Algorithm Execution Completed...");
        processSchedulerController.updateStatus("Round Robin Algorithm Execution Completed...");
        
    }
    
    
    
}
