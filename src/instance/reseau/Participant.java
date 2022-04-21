/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instance.reseau;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Bart
 */
public abstract class Participant {
    
    private final int id;
    
    /**
     * la clef représente le bénéficiaire
     */
    private Map<Paire, Transplantation> transplantations;
    
    public Participant(int id){
        this.id = id;
        this.transplantations = new LinkedHashMap<>();
    }
    
    public void ajouterTransplantation(Paire beneficiaire, int benefice) {
        if( 
                null == beneficiaire  ||
                benefice < 0
                ) return;
        
        Transplantation t = new Transplantation(this, beneficiaire, benefice);
        
        this.transplantations.put(beneficiaire, t);
    }
    
    /**
     * Donne le bénéfice de la transplantation de lui vers la paire
     * @param p
     * @return un bénéfice de -1 si aucune transplantation lui est associé 
     */
    public int getBeneficeVers( Paire p ) {
        Transplantation t = this.transplantations.get(p);
        
        if( t != null ) {
            return t.getBenefice();
        }
        
        return -1;
    }

    public int getId() {
        return id;
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

    @Override
    public String toString() {
        return "{" + "id=" + id + '}';
    }
    
    public static void main(String[] args) {
        
    }
    
    
}
