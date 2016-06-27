/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.animator.pagereplacement;

import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author sansa
 */
public class PageInfoViewPojo {
    
    private SimpleIntegerProperty id;
    
    public PageInfoViewPojo(int id) {
        this.id = new SimpleIntegerProperty(id);
    }
    
    public void setId(int id) {
        this.id.setValue(id);
    }
    
    public int getId() {
        return id.getValue();
    }
    

}
