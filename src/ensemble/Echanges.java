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
        paires = new LinkedList();
    }
    
    
    public boolean ajouterPaire(Paire p) {
        if( null == p || !this.isPaireAjoutable(p) ) return false;
        
        this.beneficeTotal += this.deltaBenefice(p);
        this.paires.add(p);
                
        return true;
    }
    
    protected abstract boolean isPaireInserable(int position, Paire p);
    
    
    public abstract int deltaBenefice(Paire p);
    
    public abstract boolean isPaireAjoutable(Paire p);
    
    public abstract boolean check();
    
    /**
     * set les paires (possible uniquement si la classe est Cycle)
     * @param paires
     * @return 
     */
    protected boolean setPaires(LinkedList<Paire> paires) {
        if( null == paires) return false;
        
        if( ! (this instanceof Cycle) ) return false;
            
        this.paires = paires;
        
        this.beneficeTotal = Cycle.beneficeTotalCycle(paires);
        
        return true;
    }
    
    protected Paire getLastPaire() {
        return this.paires.getLast();
    }
    
    protected Paire getFirstPaire() {
        return this.paires.getFirst();
    }
    
    public int getSize() {
        return this.paires.size();
    }
    
    public Paire get(int i) {
        return this.paires.get(i);
    }
    
    public int getBeneficeTotal() {
        return beneficeTotal;
    }

    
    protected boolean isPositionInsertionValide(int position){
        if(0 <= position && position <= this.getSize()){
            return true;
        }
        return false;
    }
    
    public abstract int deltaCoutInsertion(int position, Paire paireToAdd);  
        
    public Paire getPrec(int position){
        if(!this.isPositionInsertionValide(position)) return null;
        return this.paires.get(position -1);
    }
    
    public Paire getCurrent(int position){
        if(!this.isPositionInsertionValide(position)) return null;
        return this.paires.get(position);
    }
    
        

    public LinkedList<Paire> getPaires() {
        return new LinkedList<>(paires);
    }
}
