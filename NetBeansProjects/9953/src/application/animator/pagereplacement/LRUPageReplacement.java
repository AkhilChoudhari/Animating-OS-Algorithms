/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.pagereplacement;

import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;

/**
 *
 * @author Pratik
 */
public class LRUPageReplacement extends PageReplacementPolicy implements Runnable{

    public LRUPageReplacement(int inTotalPageFrames, List<PagePojo> pagesSequence, PageReplacementPolicySimulatorController inPageReplacementPolicySimulatorController) {
        super(inTotalPageFrames, pagesSequence, inPageReplacementPolicySimulatorController);
    }
    
    @Override
    public void run() {
        for(PagePojo inputSequencepagePojo:this.pagesSequence){
            this.clock++;
            for(int pageFramesIndexCounter=0; pageFramesIndexCounter<this.pageFrames.length; pageFramesIndexCounter++){
                if(this.pageFrames[pageFramesIndexCounter] == null){
                    inputSequencepagePojo.setClockCounter(clock);
                    this.pageFrames[pageFramesIndexCounter] = inputSequencepagePojo;
                    setTotalPageFaults();
                    System.out.println("Page Fault for Page Request::" + inputSequencepagePojo.getId() + ", Empty Memory Page Fault Index:>" + pageFramesIndexCounter);
                    this.pageFrameEventIndex = pageFramesIndexCounter;
                    Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    pageReplacementPolicySimulatorController.updatePageReplacementGraphicalRepresentation(inputSequencepagePojo.getId(), pageFrames, false, pageFrameEventIndex);
                                    pageReplacementPolicySimulatorController.updateTotalPageFaultsGraphicalRepresentation(totalPageFaults);
                                }
                            });
                    
                    break;
                } else{
                    if(this.pageFrames[pageFramesIndexCounter].getId()==inputSequencepagePojo.getId()){
//                    It is a page hit
                        this.pageFrames[pageFramesIndexCounter].setClockCounter(clock);
                        setTotalPageHits();
                        System.out.println("Page Hit for Page Request::" + inputSequencepagePojo.getId());
                        this.pageFrameEventIndex = pageFramesIndexCounter;
                        Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    pageReplacementPolicySimulatorController.updatePageReplacementGraphicalRepresentation(inputSequencepagePojo.getId(), pageFrames, true, pageFrameEventIndex);
                                    pageReplacementPolicySimulatorController.updateTotalPageHitsGraphicalRepresentation(totalPageHits);
                                }
                            });
                        
                        break;
                    } else {
//                        Replace the page only if the pointer reaches the last index of pageFrames
                        if(pageFramesIndexCounter == this.pageFrames.length-1){
//                           Replace the least recently used page within the frame
                            int replaceIndex = getLeastRecentlyUsedPageIndex(this.pageFrames);
                            System.out.println("Page Fault for Page Request::" + inputSequencepagePojo.getId() + ", Replace Memory Page Fault Index:>" + replaceIndex);
                            inputSequencepagePojo.setClockCounter(clock);
                            this.pageFrames[replaceIndex] = inputSequencepagePojo;
                            setTotalPageFaults();
                            this.pageFrameEventIndex = replaceIndex;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    pageReplacementPolicySimulatorController.updatePageReplacementGraphicalRepresentation(inputSequencepagePojo.getId(), pageFrames, false, pageFrameEventIndex);
                                    pageReplacementPolicySimulatorController.updateTotalPageFaultsGraphicalRepresentation(totalPageFaults);
                                }
                            });
                            break;
                        } else{
//                            Continue to interate until reaches last index of the pageFrames or until match is found(Page Hit)
                            continue;
                        }
                    }
                }
            }
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException ex) {
            }
            System.out.print("After Event>>");
            for(PagePojo pageFramePojo: this.pageFrames){
                if(pageFramePojo!=null)
                System.out.print(pageFramePojo.getId() + ":" + pageFramePojo.getClockCounter() + "\t");
            }
            System.out.println();
        }
        
    }
    
    private int getLeastRecentlyUsedPageIndex(PagePojo[] inPageFrames){
        int leastRecentTime = 0;
        int recentlyUsedPageIndex = 0;
        for(int pageFramesIndexCounter=0; pageFramesIndexCounter<inPageFrames.length; pageFramesIndexCounter++){
            if(pageFramesIndexCounter == 0){
                leastRecentTime = inPageFrames[pageFramesIndexCounter].getClockCounter();
            } else {
                if(inPageFrames[pageFramesIndexCounter].getClockCounter() < leastRecentTime){
                    leastRecentTime = inPageFrames[pageFramesIndexCounter].getClockCounter();
                    recentlyUsedPageIndex = pageFramesIndexCounter;
                }
            }
            
        }
        return recentlyUsedPageIndex;        
    }
    
    
    
}
