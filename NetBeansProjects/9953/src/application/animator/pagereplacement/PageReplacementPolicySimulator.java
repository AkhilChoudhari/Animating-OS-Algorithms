/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.pagereplacement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author sansa
 */
public class PageReplacementPolicySimulator {
    
    public static void main(String[] args) {
        List<PagePojo> pagesSequence = new ArrayList<>();
        int totalPageFrames = 3;
        int totalPageRequests = 10;
        int minimumPageId = 0;
        int maximumPageId = 9;
        Random random = new Random();
        for(int totalPageRequestsCounter = 1; totalPageRequestsCounter<=totalPageRequests; totalPageRequestsCounter++){
            int pageRequestId = random.nextInt(maximumPageId - minimumPageId + 1) + minimumPageId;
            PagePojo pagePojo = new PagePojo(pageRequestId);
            pagesSequence.add(pagePojo);
        }
        
        pagesSequence
                .stream()
                .forEach(System.out::print);
        
        System.out.println();
        
//        FIFOPageReplacement fifoPageReplacement = new FIFOPageReplacement(totalPageFrames, pagesSequence);
//        Thread thread = new Thread(fifoPageReplacement);
//        thread.start();
        
//        LRUPageReplacement lRUPageReplacement = new LRUPageReplacement(totalPageFrames, pagesSequence);
//        Thread thread = new Thread(lRUPageReplacement);
//        thread.start();
        
    }
    
}
