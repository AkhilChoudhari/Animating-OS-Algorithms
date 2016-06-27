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
public class SJFProcessScheduler extends ProcessScheduler implements Runnable{
    
    public SJFProcessScheduler(LinkedList<ProcessPojo> processContainer, int timerCounter, ProcessSchedulerController processSchedulerController) {
        super(processContainer, timerCounter, processSchedulerController);
    }

    @Override
    public void run() {
        int totalProcesses = processContainer.size();
        do {            
            timerCounter++;
            if(timerCounter==0){
                processCloneFirst = getInitProcess(timerCounter);
            } else{
                processCloneFirst = getProcess(timerCounter);
            }
            
            if(processCloneFirst != null){
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
            if(systemIdle){
                if(readyQueue.size()==0){
                    continue;
                }
                systemIdle = false;
                processCloneSecond = readyQueue.get(0);
                int serviceTime = processCloneSecond.getBurstTime();
                for(ProcessPojo processPojo:readyQueue){
                    if(serviceTime > processPojo.getBurstTime()){
                        processCloneSecond = processPojo;
                        serviceTime = processPojo.getBurstTime();
                    }
                }
//                  Process started its execution. Set its initiation time
                processCloneSecond.setInitiationTime(timerCounter);
//                And so do the waiting time
//                processCloneSecond.setWaitingTime();
                System.out.println("Process " + processCloneSecond.getName() + " started its execution. Waiting Time:=" + processCloneSecond.getWaitingTime());
//                Update the Initiation time for the process execution in time log
//                Remove the graphical representation for the process from the Ready Queue 
//                Update the Waiting time for the process execution in time log
//                Add the graphical representation for the process to the Execution Shell 
                readyQueue.remove(processCloneSecond);
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
        System.out.println("Shortest Job First Algorithm Execution Completed...");
        processSchedulerController.updateStatus("Shortest Job First Algorithm Execution Completed...");
    }
    
//    Gets process from process repository which has least arrival time
//    and if arrival time of more than one process is same then,
//    it selects process with least burst time among them.
//    This method will be used for first process selection for execution. 
//    That is when time=0. And probably will be used only once. 
    public ProcessPojo getInitProcess(int inCurrentTime){
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
        int leastBurstTime = this.processContainer
                .stream()
                .filter(processPojoFiltered->processPojoFiltered.getArrivalTime()==earliestArrivalTime)
                .mapToInt(processPojo->processPojo.getBurstTime())
                .min()
                .orElse(0);  
        System.out.println("leastBurstTime:>" + leastBurstTime + " with respect to earliestArrivalTime:>>" + earliestArrivalTime);
        return this.processContainer
                .stream()
                .filter(processPojo->processPojo.getArrivalTime()==earliestArrivalTime)
                .filter(processPojo->processPojo.getArrivalTime()<=inCurrentTime)
                .filter(processPojo->processPojo.getBurstTime()==leastBurstTime)
                .findFirst()
                .orElse(null);
        
//        return this.processContainer
//                .stream()
//                .filter(processPojo->processPojo.getArrivalTime()<=inCurrentTime)
//                .findFirst()
//                .orElse(null);
//        add comparator implementation if necessary
                
    }
    
    
}
