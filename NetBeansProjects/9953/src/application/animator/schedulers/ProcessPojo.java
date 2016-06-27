/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.schedulers;

import javafx.scene.control.ProgressIndicator;

/**
 *
 * @author sansa
 */
public class ProcessPojo {
    
    private String name;
    private int arrivalTime;
    protected int burstTime;
    private int initiationTime;
    private int completionTime;
    protected int waitingTime;
    protected int turnAroundTime;
    private int timeLeft;
    private ProgressIndicator progressIndicator;
    
    private boolean executionInitiated;
    
    public ProcessPojo(String name, int arrivalTime, int burstTime) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = this.timeLeft = burstTime;
        
        this.progressIndicator = new ProgressIndicator(0.0);
        this.progressIndicator.setPrefWidth(100);
        this.progressIndicator.setPrefHeight(100);
        
        this.initiationTime = 0;
        this.completionTime = 0;
        this.waitingTime = 0;
        this.turnAroundTime = 0;
        
        this.executionInitiated = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getInitiationTime() {
        return initiationTime;
    }

    public void setInitiationTime(int initiationTime) {
        this.initiationTime = initiationTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime + 1;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime() {
//        this.waitingTime = this.initiationTime - this.arrivalTime;
        this.waitingTime = this.turnAroundTime - this.burstTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setTurnAroundTime() {
        this.turnAroundTime = this.completionTime - this.arrivalTime;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public void setProgressIndicator(ProgressIndicator progressIndicator) {
        this.progressIndicator = progressIndicator;
    }

    public boolean isExecutionInitiated() {
        return executionInitiated;
    }

    public void setExecutionInitiated(boolean executionInitiated) {
        this.executionInitiated = executionInitiated;
    }
    
    private void updateExecutionProgress(double inProgressIndex){
        this.progressIndicator.setProgress(inProgressIndex);
    }
    
    public void execute(){
        this.timeLeft--;
        updateExecutionProgress((double)(this.burstTime - this.timeLeft)/this.burstTime);
    }
    
    public void calculateExecutionParameters(){
//        Calculate total waiting time and total turn around time here
        
    }
    
}
