/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.pagereplacement;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author Pratik
 */
public class FIFOPageReplacement extends PageReplacementPolicy implements Runnable{

    int currentReplaceIndex = 0;
    
    public FIFOPageReplacement(int inTotalPageFrames, List<PagePojo> pagesSequence, PageReplacementPolicySimulatorController inPageReplacementPolicySimulatorController) {
        super(inTotalPageFrames, pagesSequence, inPageReplacementPolicySimulatorController);
    }

    @Override
    public void run() {
        for(PagePojo inputSequencepagePojo:this.pagesSequence){
            this.clock++;
            for(int pageFramesIndexCounter=0; pageFramesIndexCounter<this.pageFrames.length; pageFramesIndexCounter++){
                if(this.pageFrames[pageFramesIndexCounter]==null){
                    this.pageFrames[pageFramesIndexCounter] = inputSequencepagePojo;
                    setTotalPageFaults();
                    System.out.println("Page Fault for Page Request::" + inputSequencepagePojo.getId() + ", Empty Memory Page Fault Index:>" + pageFramesIndexCounter);
                    
                        //                    Reset the page frame location pointer
//                    if(currentReplaceIndex == this.pageFrames.length-1){
//                        currentReplaceIndex = 0;
//                    } else{
//                        currentReplaceIndex++;
//                    }
                    this.pageFrameEventIndex = pageFramesIndexCounter;
                    Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    pageReplacementPolicySimulatorController.updatePageReplacementGraphicalRepresentation(inputSequencepagePojo.getId(), pageFrames, false, pageFrameEventIndex);
                                    pageReplacementPolicySimulatorController.updateTotalPageFaultsGraphicalRepresentation(totalPageFaults);
                                }
                            });
                    break;
                } else {
                    if(this.pageFrames[pageFramesIndexCounter].getId()==inputSequencepagePojo.getId()){
//                    It is a page hit
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
                    } else{
                        if(pageFramesIndexCounter == this.pageFrames.length-1){
//                            Using the 'currentReplaceIndex' to keep track of position to be replaced (First Input among the others)
                            this.pageFrames[currentReplaceIndex] = inputSequencepagePojo;
                            System.out.println("Page Fault for Page Request::" + inputSequencepagePojo.getId() + ", Replace Memory Page Fault Index:>" + currentReplaceIndex);
                            
                            setTotalPageFaults();
                            this.pageFrameEventIndex = currentReplaceIndex;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    pageReplacementPolicySimulatorController.updatePageReplacementGraphicalRepresentation(inputSequencepagePojo.getId(), pageFrames, false, pageFrameEventIndex);
                                    pageReplacementPolicySimulatorController.updateTotalPageFaultsGraphicalRepresentation(totalPageFaults);
                                }
                            });
                            if(currentReplaceIndex == this.pageFrames.length-1){
                                currentReplaceIndex = 0;
                            } else{
                                currentReplaceIndex++;
                            }
                            break;
                        } else {
//                            try {
//                                Thread.currentThread().sleep(1000);
//                            } catch (InterruptedException ex) {
//                            }
//                            Skip the current page frame location until reaches the last index
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
                System.out.print(pageFramePojo.getId() + "\t");
            }
            System.out.println();
            
        }
    }
    
}
