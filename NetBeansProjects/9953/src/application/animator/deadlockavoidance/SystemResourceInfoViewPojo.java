/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.deadlockavoidance;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author sansa
 */
public class SystemResourceInfoViewPojo {
    
    private SimpleStringProperty id;
    private SimpleIntegerProperty totalInstances;
    
    public SystemResourceInfoViewPojo(String id, int totalInstances) {
        this.id = new SimpleStringProperty(id);
        this.totalInstances = new SimpleIntegerProperty(totalInstances);
    }

    public String getId() {
        return id.getValue();
    }

    public void setId(String id) {
        this.id.setValue(id);
    }

    public int getTotalInstances() {
        return totalInstances.getValue();
    }

    public void setTotalInstances(int totalInstances) {
        this.totalInstances.setValue(totalInstances);
    }
    
    
    
    
    

}
