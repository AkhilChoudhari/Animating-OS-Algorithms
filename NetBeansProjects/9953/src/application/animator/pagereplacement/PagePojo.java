/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.pagereplacement;

/**
 *
 * @author Pratik
 */
public class PagePojo {
    
    private int id;
    private int clockCounter;
    private boolean pageHit;

    public PagePojo(int id) {
        this.id = id;
        this.clockCounter = 0;
        this.pageHit = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClockCounter() {
        return clockCounter;
    }

    public void setClockCounter(int clockCounter) {
        this.clockCounter = clockCounter;
    }

    public void setPageHit() {
        this.pageHit = true;
    }

    @Override
    public String toString() {
        return id + "\t";
    }

    
    
    
    
}
