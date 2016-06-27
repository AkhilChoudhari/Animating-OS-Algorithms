/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.deadlockavoidance;

/**
 *
 * @author sansa
 */
public class SystemResourcePojo {
    private String id;
//    Total number of instances of system resource present in a system
    private int totalInstances;
    private int allocatedInstances;
    private int availableInstances;

    public SystemResourcePojo(String id, int totalInstances) {
        this.id = id;
        this.totalInstances = totalInstances;
        this.allocatedInstances = 0;
        this.availableInstances = totalInstances;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalInstances() {
        return totalInstances;
    }
    
    public int getAllocatedInstances() {
        return allocatedInstances;
    }

    public int getAvailableInstances() {
        return availableInstances;
    }
    
    public void updateInstancesAllocation(int inAllocatedInstances){
        this.allocatedInstances += inAllocatedInstances;
        this.availableInstances = this.totalInstances - this.allocatedInstances;
    }
    
    public void updateInstancesAvailability(int inReleasedInstances){
        this.availableInstances += inReleasedInstances;
        this.allocatedInstances = this.totalInstances - this.availableInstances;
    }
}
