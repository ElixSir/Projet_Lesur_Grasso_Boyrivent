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
    protected int id;
    //private Participant receveur;
    protected Map<Participant, Transplantation> transplantations;
    
    public Participant(){
        id = 0;
        //receveur = null;
    }

    public int getId() {
        return id;
    }

    public Map<Participant, Transplantation> getTransplantations() {
        return transplantations;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Participant other = (Participant) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
    
}
