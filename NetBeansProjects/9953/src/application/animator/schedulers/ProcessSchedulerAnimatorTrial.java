/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.schedulers;

import java.util.LinkedList;

/**
 *
 * @author sansa
 */
public class ProcessSchedulerAnimatorTrial {
    
    public static void main(String[] args) {
        LinkedList<ProcessPojo> inputProcessContainer = new LinkedList<>();
        ProcessPojo processPojo = new ProcessPojo("Process 1", 0, 5);
        inputProcessContainer.add(processPojo);
        processPojo = new ProcessPojo("Process 2", 17, 8);
        inputProcessContainer.add(processPojo);
        processPojo = new ProcessPojo("Process 3", 3, 10);
        inputProcessContainer.add(processPojo);
//        ProcessScheduler processScheduler = new FCFSProcessScheduler(inputProcessContainer, inputProcessContainer.get(0).getArrivalTime());
    }
    
}
