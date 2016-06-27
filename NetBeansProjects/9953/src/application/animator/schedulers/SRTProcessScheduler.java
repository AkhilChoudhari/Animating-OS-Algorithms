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

//The implementation is PreEmptive
public class SRTProcessScheduler extends ProcessScheduler implements Runnable{

    public SRTProcessScheduler(LinkedList<ProcessPojo> processContainer, int timerCounter, ProcessSchedulerController processSchedulerController) {
        super(processContainer, timerCounter, processSchedulerController);
    }

    @Override
    public void run() {
        int totalProcesses = processContainer.size();
        boolean switchProcess = false;
        ProcessPojo processPojo = null;
        do {            
            timerCounter++;
            processCloneFirst = getProcess(timerCounter);
            if(processCloneFirst!=null){
                readyQueue.add(processCloneFirst);
                processContainer.remove(processCloneFirst);
                switchProcess = true;
//                  Add the graphical representation for the process to the Ready Queue 
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
                    if(processPojo!=null){
                        readyQueue.add(processPojo);
                    }
                }
                if(readyQueue.size()==0){
                    continue;
                }
                if(systemIdle){
                    systemIdle = false;
                }
                System.out.println("Currently ReadyQueue:>");
                readyQueue
                        .stream()
                        .forEach(
                                procesPojo->{
                                    System.out.print(procesPojo.getName() + ">>");
                                    System.out.println(procesPojo.getTimeLeft());
                                });
                processCloneSecond = readyQueue.get(0);
                int remainingTime = processCloneSecond.getTimeLeft();
                for(ProcessPojo processPojoCounter:readyQueue){
                    if(remainingTime > processPojoCounter.getTimeLeft()){
                        processCloneSecond = processPojoCounter;
                        remainingTime = processPojoCounter.getTimeLeft();
                    } else if(remainingTime == processPojoCounter.getTimeLeft()){
                        if(processPojoCounter.getArrivalTime() < processCloneSecond.getArrivalTime()){
                            System.out.println("Although remaning times of processes " + processCloneSecond.getName() + " and " + processPojoCounter.getName() + " are same...");
                            System.out.println("Arrival time of process " + processPojoCounter.getName() + " which is " + processPojoCounter.getArrivalTime() + " is less than that of process " + processCloneSecond.getName() + " whose arrival time is " + processCloneSecond.getArrivalTime());
                            processCloneSecond = processPojoCounter;
                            remainingTime = processPojoCounter.getTimeLeft();
                        }
//                        But what if even arrival times of processes are same? 
                    }
                    
                }
                if(processCloneSecond.isExecutionInitiated()){
                    System.out.println("Continuing " + processCloneSecond.getName() + " execution...");
                } else {
                    System.out.println("Initiating " + processCloneSecond.getName() + " execution...");
//                    Process started its execution. Set its initiation time
                    processCloneSecond.setInitiationTime(timerCounter);
//                    And so do the waiting time
//                    processCloneSecond.setWaitingTime();
                    System.out.println("Process " + processCloneSecond.getName() + " started its execution. Waiting Time:=" + processCloneSecond.getWaitingTime());
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
                readyQueue.remove(processCloneSecond);
//                Remove the graphical representation for the process from the Ready Queue 
//                Add the graphical representation for the process to the Execution Shell 
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        processSchedulerController.removeGraphicalRepresentationProcessReadyQueue(processCloneSecond.getName());
                        processSchedulerController.addGraphicalRepresentationProcessJobExecution(processCloneSecond.getName());
                    }
                });
            }
            
            processCloneSecond.execute();
//              Add the graphical representation for process execution to the Gannt Chart
            Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        processSchedulerController.addGraphicalRepresentationProcessGanntChart(processCloneSecond.getName(),timerCounter);
                    }
                });
            System.out.println("Process: " + processCloneSecond.getName() + " is executing...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
//                    Logger.getLogger(FCFSProcessScheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(processCloneSecond.getTimeLeft()==0){
//                  Process completed its execution. Set its completion time 
                processCloneSecond.setCompletionTime(timerCounter);
//                And so do the turn around time
                processCloneSecond.setTurnAroundTime();
//                And so do the waiting time
                processCloneSecond.setWaitingTime();
//                Update the Completion time for the process execution in time log
//                Remove the graphical representation for the process from the Execution Shell
//                Update the Completion time for the process execution in time log
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        processSchedulerController.updateCompletionTime(processCloneSecond.getName(), processCloneSecond.getCompletionTime());
                        processSchedulerController.removeGraphicalRepresentationProcessJobExecution(processCloneSecond.getName());
                        processSchedulerController.updateTurnAroundTime(processCloneSecond.getName(), processCloneSecond.getTurnAroundTime());
                        processSchedulerController.updateWaitingTime(processCloneSecond.getName(), processCloneSecond.getWaitingTime());
                    }
                });
                System.out.println("Process: " + processCloneSecond.getName() + " execution completed!");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
//                    Logger.getLogger(FCFSProcessScheduler.class.getName()).log(Level.SEVERE, null, ex);
                }
                finishedQueue.add(processCloneSecond);
                systemIdle = true;
                processPojo = null;
            } else{
                processPojo = processCloneSecond;
            }
        } while (finishedQueue.size()<totalProcesses);
        System.out.println("Shortest Remaining Time First Algorithm Execution Completed...");
        processSchedulerController.updateStatus("Shortest Remaining Time First Algorithm Execution Completed...");
    }
    
    
}
