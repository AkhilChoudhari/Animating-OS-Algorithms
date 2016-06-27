/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.schedulers;

import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 *
 * @author sansa
 */
public class ProcessScheduler {
    
    protected LinkedList<ProcessPojo> processContainer;
    protected LinkedList<ProcessPojo> readyQueue;
    protected LinkedList<ProcessPojo> finishedQueue;
    protected int timerCounter;
    protected ProcessPojo processCloneFirst;
    protected ProcessPojo processCloneSecond;
    protected boolean systemIdle;
    protected ProcessSchedulerController processSchedulerController;

    public ProcessScheduler(LinkedList<ProcessPojo> processContainer, int timerCounter, ProcessSchedulerController processSchedulerController) {
//        this.processContainer = processContainer;
        this.processContainer = new LinkedList<>();
        this.processContainer.addAll(processContainer);
//        What is logic behind setting timeCounter this way?
//        this.timerCounter = timerCounter-1;
        this.timerCounter = -1;
        this.systemIdle = true;
        this.readyQueue = new LinkedList<>();
        this.finishedQueue = new LinkedList<>();
        this.processSchedulerController = processSchedulerController;
    }
    
//    Gets the process from the ready queue which is ready for execution based on it's arrival time
    public ProcessPojo getProcess(int inCurrentTime){
        this.processContainer
                .stream()
                .mapToInt(processPojo->processPojo.getArrivalTime())
                .forEach(System.out::println);
        int earliestArrivalTime = this.processContainer
                .stream()
                .mapToInt(processPojo->processPojo.getArrivalTime())
                .min()
                .orElse(0);
        System.out.println("earliestArrivalTime:>" + earliestArrivalTime);
        return this.processContainer
                .stream()
                .filter(processPojo->processPojo.getArrivalTime()==earliestArrivalTime&&processPojo.getArrivalTime()<=inCurrentTime)
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
