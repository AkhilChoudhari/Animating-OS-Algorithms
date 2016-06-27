/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.pagereplacement;

import java.util.List;

/**
 *
 * @author Pratik
 */
public class PageReplacementPolicy {
    
    protected PagePojo[] pageFrames;
    protected List<PagePojo> pagesSequence;
    protected int totalPageHits;
    protected int totalPageFaults;
    protected int clock;
    
    protected int pageFrameEventIndex;
    
    protected PageReplacementPolicySimulatorController pageReplacementPolicySimulatorController;

    public PageReplacementPolicy(int inTotalPageFrames, List<PagePojo> pagesSequence, PageReplacementPolicySimulatorController inPageReplacementPolicySimulatorController) {
        this.pageFrames = new PagePojo[inTotalPageFrames];
        for(int arrayIndexCounter=0; arrayIndexCounter<inTotalPageFrames; arrayIndexCounter++){
            this.pageFrames[arrayIndexCounter] = null;
        }
        this.pagesSequence = pagesSequence;
        this.totalPageHits = 0;
        this.totalPageFaults = 0;
        this.clock = 0;
        this.pageReplacementPolicySimulatorController = inPageReplacementPolicySimulatorController;
        this.pageFrameEventIndex = 0;
    }

    public PagePojo[] getPageFrames() {
        return pageFrames;
    }

    public int getTotalPageHits() {
        return totalPageHits;
    }

    public void setTotalPageHits() {
        this.totalPageHits++;
    }

    public int getTotalPageFaults() {
        return totalPageFaults;
    }

    public void setTotalPageFaults() {
        this.totalPageFaults++;
    }
    
    
    
    
}
