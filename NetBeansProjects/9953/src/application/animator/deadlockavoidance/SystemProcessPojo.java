/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.deadlockavoidance;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author sansa
 */
public class SystemProcessPojo {
    
    private String id;
//    Total number of instances of system resources required by the process to complete its execution
    private LinkedHashMap<String, Integer> totalResourceInstances;
//    Total number of instances of system resources allocated to the process at a timestamp
    private LinkedHashMap<String, Integer> allocatedResourceInstances;
//    Total number of more instances of system resources required by the process to complete its execution
    private LinkedHashMap<String, Integer> requiredResourceInstances;
//    Flag to determine whether process execution is completed
    private boolean finishedExecution;
    
    public SystemProcessPojo(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public LinkedHashMap<String, Integer> getTotalResourceInstances() {
        return totalResourceInstances;
    }

    public void setTotalResourceInstances(LinkedHashMap<String, Integer> totalResourceInstances) {
        this.totalResourceInstances = totalResourceInstances;
    }

    public LinkedHashMap<String, Integer> getAllocatedResourceInstances() {
        return allocatedResourceInstances;
    }

    public void setAllocatedResourceInstances(LinkedHashMap<String, Integer> allocatedResourceInstances) {
        this.allocatedResourceInstances = allocatedResourceInstances;
    }

    public LinkedHashMap<String, Integer> getRequiredResourceInstances() {
        return requiredResourceInstances;
    }

    public void setRequiredResourceInstances(LinkedHashMap<String, Integer> requiredResourceInstances) {
        this.requiredResourceInstances = requiredResourceInstances;
    }

    public void setFinishedExecution(boolean finishedExecution) {
        this.finishedExecution = finishedExecution;
    }

    public boolean isFinishedExecution() {
        return finishedExecution;
    }

    public void execute(ArrayList<SystemResourcePojo> systemResources){
        System.out.println(id + " execution ...");
        System.out.println(id + " more required instances...");
        requiredResourceInstances.entrySet()
                .stream()
                .forEach(System.out::println);
        System.out.println(id + " allocated instances (before execution)...");
        allocatedResourceInstances.entrySet()
                .stream()
                .forEach(System.out::println);
//        Allocate instances of system resources as per the required instances collection of system process
        requiredResourceInstances.entrySet()
                .stream()
                .forEach(requiredInstancesEntry->{
//                    Get the currently available instances for a system resource 
                    int availableInstances = systemResources
                            .stream()
                            .filter(systemResourcePojo->systemResourcePojo.getId().equalsIgnoreCase(requiredInstancesEntry.getKey()))
                            .findFirst()
                            .get()
                            .getAvailableInstances();
//                    Chech whether currently available instances of system resource is greater than the required instances by process
                    if(availableInstances>=requiredInstancesEntry.getValue()){
//                        Get currently allocated instances of system resource to process
                        int allocatedInstancesForProcess = allocatedResourceInstances.get(requiredInstancesEntry.getKey());
//                        Add required instances to allocated instances
                        allocatedInstancesForProcess += requiredInstancesEntry.getValue();
//                        Update allocated instances of system resource for process
                        allocatedResourceInstances.put(requiredInstancesEntry.getKey(), allocatedInstancesForProcess);
                    } 
                });
//        System resource instances allocation completed
        System.out.println(id + " allocated instances (during execution)...");
        allocatedResourceInstances.entrySet()
                .stream()
                .forEach(System.out::println);
                
//        Check whether all allocated system resource instances are equal to all required system resource instances 
        finishedExecution = true;
        System.out.println(id + " execution completed...");
    }
    
    public void clearMoreRequiredSystemResourceInstances(List<String> systemResourcesId){
//        Clear required instances of system resources for process
        systemResourcesId
                .stream()
                .forEach(systemResourcesIdElement->{
                    requiredResourceInstances
                            .entrySet()
                            .stream()
                            .filter(requiredInstancesEntry->requiredInstancesEntry.getKey().equalsIgnoreCase(systemResourcesIdElement))
                            .findFirst()
                            .get()
                            .setValue(0);
                });
    }
    
    public void releaseSystemResourceInstances(List<String> systemResourcesId){
        
        if(finishedExecution){
            
            systemResourcesId
                    .stream()
                    .forEach(systemResourcesIdElement->{
                    
                    allocatedResourceInstances
                            .entrySet()
                            .stream()
                            .filter(requiredInstancesEntry->requiredInstancesEntry.getKey().equalsIgnoreCase(systemResourcesIdElement))
                            .findFirst()
                            .get()
                            .setValue(0);
                    
                    });
            
        }
        
    }
    
    
    
}
