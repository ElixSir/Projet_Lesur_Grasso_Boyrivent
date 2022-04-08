/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensemble;

import instance.reseau.Paire;
import java.util.LinkedList;

/**
 *
 * @author Bart
 */
public abstract class Echanges {
    protected int beneficeTotal;
    protected LinkedList<Paire> paires;
    
    public Echanges(){
        beneficeTotal = 0;
        paires = new LinkedList<Paire>();
    }

    public int getBeneficeTotal() {
        return beneficeTotal;
    }

    public LinkedList<Paire> getPaires() {
        return new LinkedList<>(paires);
    }
 
    public boolean ajouterPaire(Paire p){ // si chaine pas pleine et que la paire est pas en double alors on ajoute
        if(p == null) return false;
        if(!this.possibleAjouter()) return false; // si la chaine est pleine alors return
        
        for(Paire pa: this.paires){
            if(pa.equals(p)){
                return false; // si la paire existe deja je n'ajoute pas
            }
        }
        
        // Ajouter benefice de la paire
        
        this.paires.add(p);
        
        return true;
    }
    
    public abstract boolean possibleAjouter();

    public static void main(String[] args) {
        
    }
    
}
