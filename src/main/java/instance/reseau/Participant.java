/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instance.reseau;

import java.util.Map;

/**
 *
 * @author Bart
 */
public abstract class Participant {
    private int id;
    //private Participant receveur;
     private Map<Participant, Transplantation> routes;
    
    public Participant(){
        id = 0;
        //receveur = null;
    }

    public int getId() {
        return id;
    }

    public Map<Participant, Transplantation> getRoutes() {
        return routes;
    }
    
    
    
}
