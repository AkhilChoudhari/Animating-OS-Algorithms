/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.schedulers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author sansa
 */
public class ProcessInfoViewPojo {
    
    private SimpleStringProperty name;
    private SimpleIntegerProperty arrivalTime;
    private SimpleIntegerProperty burstTime;
    private SimpleIntegerProperty initiationTime;
    private SimpleIntegerProperty waitingTime;
    private SimpleIntegerProperty completionTime;
    private SimpleIntegerProperty turnAroundTime;

    public ProcessInfoViewPojo(String name, int arrivalTime, int burstTime) {
        this.name = new SimpleStringProperty(name);
        this.arrivalTime = new SimpleIntegerProperty(arrivalTime);
        this.burstTime = new SimpleIntegerProperty(burstTime);
        this.initiationTime = new SimpleIntegerProperty(0);
        this.waitingTime = new SimpleIntegerProperty(0);
        this.completionTime = new SimpleIntegerProperty(0);
        this.turnAroundTime = new SimpleIntegerProperty(0);
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public int getArrivalTime() {
        return arrivalTime.getValue();
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime.setValue(arrivalTime);
    }

    public int getBurstTime() {
        return burstTime.getValue();
    }

    public void setBurstTime(int burstTime) {
        this.burstTime.setValue(burstTime);
    }

    public int getInitiationTime() {
        return initiationTime.getValue();
    }

    public void setInitiationTime(int initiationTime) {
        this.initiationTime.setValue(initiationTime);
    }

    public int getWaitingTime() {
        return waitingTime.getValue();
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime.setValue(waitingTime);
    }

    public int getCompletionTime() {
        return completionTime.getValue();
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime.setValue(completionTime);
    }

    public int getTurnAroundTime() {
        return turnAroundTime.getValue();
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime.setValue(turnAroundTime);
    }

}
