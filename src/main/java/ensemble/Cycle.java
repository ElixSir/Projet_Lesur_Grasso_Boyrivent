/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensemble;

import instance.reseau.Paire;

/**
 *
 * @author Bart
 */
public class Cycle extends Echanges {
    private int maxCycle;
    

    public Cycle(int maxCycle) {
        this.maxCycle = maxCycle;
    }

    public int getMaxCycle() {
        return maxCycle;
    }
    
    public void setMaxCycle(int m){
        this.maxCycle = m;
    }
    
    public boolean ajouterPaire(Paire p){ // si chaine pas pleine et que la paire est pas en double alors on ajoute
        if(p == null) return false;
        if(this.paires.size()  >= this.maxCycle ) return false; // si la chaine est pleine alors return
        
        for(Paire pa: this.paires){
            if(pa.equals(p)){
                return false; // si la paire existe deja je n'ajoute pas
            }
        }
        
        this.paires.add(p);
        
        return true;
    }
    
}
