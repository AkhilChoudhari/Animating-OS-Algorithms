/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.deadlockavoidance;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javafx.application.Platform;

/**
 *
 * @author sansa
 */
public class BankersAlgorithm implements Runnable{
    
    private ArrayList<SystemResourcePojo> systemResources;
    private ArrayList<SystemProcessPojo> systemProcesses;
    private BankersAlgorithmAnimatorController bankersAlgorithmAnimatorController;
    
//    Temp ListIterator to iterate through processes. Used in getFeasibleProcess() method.
//    Purpose to maintain the iteration index so as to continue with the iteration 
//    where it was left off after getting feasible process (iteration is broke after getting feasible process)
    private ListIterator<SystemProcessPojo> systemProcessListIterator;
    
    public BankersAlgorithm(ArrayList<SystemResourcePojo> systemResources, ArrayList<SystemProcessPojo> systemProcesses) {
        this.systemResources = systemResources;
        this.systemProcesses = systemProcesses;
        this.systemProcessListIterator = null;
    }

    public BankersAlgorithm(ArrayList<SystemResourcePojo> systemResources, ArrayList<SystemProcessPojo> systemProcesses, BankersAlgorithmAnimatorController bankersAlgorithmAnimatorController) {
        this.systemResources = systemResources;
        this.systemProcesses = systemProcesses;
        this.bankersAlgorithmAnimatorController = bankersAlgorithmAnimatorController;
        this.systemProcessListIterator = null;
    }
    
    private String getFeasibleProcess(){
//        System.out.println("While getting feasible process...");
        String outSystemProcessId = null;
        if(systemProcessListIterator==null){
            systemProcessListIterator = systemProcesses.listIterator();
        }
        while (systemProcessListIterator.hasNext()) {
            SystemProcessPojo systemProcessPojo = systemProcessListIterator.next();
//            System.out.println("Performing check for process:" + systemProcessPojo.getId());
            int feasibleProcessCounter = 0;
            ListIterator<SystemResourcePojo> systemResourcesListIterator = systemResources.listIterator();
            while (systemResourcesListIterator.hasNext()) {
                SystemResourcePojo systemResourcePojo = systemResourcesListIterator.next();
                if(systemProcessPojo.getRequiredResourceInstances().get(systemResourcePojo.getId())<=systemResourcePojo.getAvailableInstances()){
                    feasibleProcessCounter++;
                } else {
                    break;
                }
            }
            if(feasibleProcessCounter == systemResources.size() && !systemProcessPojo.isFinishedExecution()){
                outSystemProcessId = systemProcessPojo.getId();
//                Reset the Temp ListIterator here...
                if (!systemProcessListIterator.hasNext()) {
                    systemProcessListIterator = null;
                }
                break;
            } else if(systemProcesses
                    .stream()
                    .filter(systemResourcPojoFiltered->!systemResourcPojoFiltered.isFinishedExecution())
                    .count()
                    >0 && !systemProcessListIterator.hasNext()){
                systemProcessListIterator = systemProcesses.listIterator();
                
            }
            
            
        }
        
        return outSystemProcessId;
    }
    
    @Override
    public void run() {
        String safeSequence = "";
        do {            
//            Get id of feasible process 
            String feasibleProcessId = getFeasibleProcess();
            if(feasibleProcessId!=null){
                SystemProcessPojo systemProcessPojo = systemProcesses
                        .stream()
                        .filter(systemProcessPojoFilter->systemProcessPojoFilter.getId().equalsIgnoreCase(feasibleProcessId) && !systemProcessPojoFilter.isFinishedExecution())
                        .findFirst()
                        .orElse(null);
                if(systemProcessPojo != null){
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ex) {
                    }
                    System.out.println("Selected Feasible Process::" + feasibleProcessId);
//                    Allocate instances of system resources to process and execute process
                    systemProcesses
                        .stream()
                        .filter(systemProcessPojoFilter->systemProcessPojoFilter.getId().equalsIgnoreCase(feasibleProcessId) && !systemProcessPojoFilter.isFinishedExecution())
                        .findFirst()
                        .get()
                        .execute(systemResources);
                    try {
                        TimeUnit.SECONDS.sleep(systemProcesses.size());
                    } catch (InterruptedException ex) {
                    }
//                    Update system resources for allocation of instances to executing process
                    bankersAlgorithmAnimatorController.updateSystemResourcesInstancesAllocationForProcess(feasibleProcessId);
                    try {
                        TimeUnit.SECONDS.sleep(systemResources.size());
                    } catch (InterruptedException ex) {
                    }
//                    Clear required instances of system resources of executing process
                    systemProcesses
                        .stream()
                        .filter(systemProcessPojoFilter->systemProcessPojoFilter.getId().equalsIgnoreCase(feasibleProcessId))
                        .findFirst()
                        .get()
                        .clearMoreRequiredSystemResourceInstances(systemResources
                                        .stream()
                                        .map(systemResourcePojo->systemResourcePojo.getId())
                                        .collect(Collectors.toList()));
                    try {
                        TimeUnit.SECONDS.sleep(systemResources.size());
                    } catch (InterruptedException ex) {
                    }
//                    Check whether process finished otherwise flag Unsafe state for system
                    if(systemProcesses
                        .stream()
                        .filter(systemProcessPojoFilter->systemProcessPojoFilter.getId().equalsIgnoreCase(feasibleProcessId))
                        .findFirst()
                        .get()
                        .isFinishedExecution()){
                        System.out.println("After Execution:>" + feasibleProcessId);
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException ex) {
                        }
//                        
                        bankersAlgorithmAnimatorController.updateSystemResourcesInstancesAvailalibilityForProcess(feasibleProcessId);
//                        Release instances of system resources and update system resource collection with allocated instances for process
                        systemProcesses
                        .stream()
                        .filter(systemProcessPojoFilter->systemProcessPojoFilter.getId().equalsIgnoreCase(feasibleProcessId) && systemProcessPojoFilter.isFinishedExecution())
                        .findFirst()
                        .get()
                        .releaseSystemResourceInstances(systemResources
                                .stream()
                                .map(systemResourcePojo->systemResourcePojo.getId())
                                .collect(Collectors.toList()));
                        try {
                            TimeUnit.SECONDS.sleep(systemResources.size());
                        } catch (InterruptedException ex) {
                        }
                    } else {
                        System.err.println("Process " + systemProcessPojo.getId() + " did not finished execution. System reached Unsafe State!!!");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                bankersAlgorithmAnimatorController.updateSafeExecutionSequenceHBox(feasibleProcessId, false);
                            }
                        });
                        break;
                    }
                    safeSequence += "->" + systemProcessPojo.getId();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            bankersAlgorithmAnimatorController.updateSafeExecutionSequenceHBox(feasibleProcessId, true);
                        }
                    });
                }
            } else {
                System.err.println("System reached Unsafe State!!!");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        bankersAlgorithmAnimatorController.updateSafeExecutionSequenceHBox(feasibleProcessId, false);
                    }
                });
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
            }
        } while (systemProcesses
                .stream()
                .filter(systemProcessPojo->!systemProcessPojo.isFinishedExecution())
                .count()>0);
        System.out.println();
        System.out.println("Banker's Algorithm execution completed!");
        System.out.println("safeSequence:>" + safeSequence);
    }
    
    
    
}
