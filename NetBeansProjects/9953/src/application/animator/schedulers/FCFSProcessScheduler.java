/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.schedulers;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;


/**
 *
 * @author sansa
 */
public class FCFSProcessScheduler extends ProcessScheduler implements Runnable{
    
    public FCFSProcessScheduler(LinkedList<ProcessPojo> processContainer, int timerCounter, ProcessSchedulerController processSchedulerController) {
        super(processContainer, timerCounter, processSchedulerController);
    }

    @Override
    public void run() {
        int totalProcesses = processContainer.size();
        
        do {            
            timerCounter++;
            processCloneFirst = getProcess(timerCounter);
            if (processCloneFirst!=null) {
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
            if (systemIdle) {
                if (readyQueue.isEmpty()) {continue;};
                systemIdle = false;
                processCloneSecond = readyQueue.get(0);
                readyQueue.remove(processCloneSecond);
//                Process started its execution. Set its initiation time
                processCloneSecond.setInitiationTime(timerCounter);
//                And so do the waiting time
//                processCloneSecond.setWaitingTime();
                System.out.println("Process " + processCloneSecond.getName() + " started its execution. Waiting Time:=" + processCloneSecond.getWaitingTime());
//                Update the Initiation time for the process execution in time log
//                Remove the graphical representation for the process from the Ready Queue 
//                Update the Waiting time for the process execution in time log
//                Add the graphical representation for the process to the Execution Shell 
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        processSchedulerController.updateInitiationTime(processCloneSecond.getName(), processCloneSecond.getInitiationTime());
                        processSchedulerController.removeGraphicalRepresentationProcessReadyQueue(processCloneSecond.getName());
//                        processSchedulerController.updateWaitingTime(processCloneSecond.getName(), processCloneSecond.getWaitingTime());
                        processSchedulerController.addGraphicalRepresentationProcessJobExecution(processCloneSecond.getName());
                    }
                });
                
            }
            processCloneSecond.execute();
//            Add the graphical representation for process execution to the Gannt Chart
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
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        processSchedulerController.updateCompletionTime(processCloneSecond.getName(), processCloneSecond.getCompletionTime());
                        processSchedulerController.removeGraphicalRepresentationProcessJobExecution(processCloneSecond.getName());
                        processSchedulerController.updateTurnAroundTime(processCloneSecond.getName(), processCloneSecond.getTurnAroundTime());
                        processSchedulerController.updateWaitingTime(processCloneSecond.getName(), processCloneSecond.getWaitingTime());
                    }
                });
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
//                    Logger.getLogger(FCFSProcessScheduler.class.getName()).log(Level.SEVERE, null, ex);
                }
//                Do something to let the process know it is completed
                finishedQueue.add(processCloneSecond);
                systemIdle = true;
            }
        } while (finishedQueue.size()<totalProcesses);
        System.out.println("First Come First Serve Algorithm Execution Completed...");
        processSchedulerController.updateStatus("First Come First Serve Algorithm Execution Completed...");
    }
    
    
    
}
