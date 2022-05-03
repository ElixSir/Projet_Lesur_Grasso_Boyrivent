/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensemble;

import instance.reseau.Paire;
import instance.reseau.Participant;
import java.util.LinkedList;
import operateur.InsertionPaire;
import operateur.IntraEchangeCycle;

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
        if( null == p || !this.isPaireAjoutableFin(p) ) return false;
        
        this.beneficeTotal += this.deltaBenefice(p);
        this.paires.add(p);
                
        return true;
    }
    
    /**
     * V�rifie qu'une paire puisse �tre ins�r�e � une certaine position
     *
     * @param position
     * @param p
     * @return
     */
    protected boolean isPaireInserablePosition(int position, Paire p) {

        if (!isPositionInsertionValide(position)) {
            return false;
        }
        if (null == p || this.getSize() >= this.getMaxEchange()) {
            return false;
        }

        if (this.deltaCoutInsertion(position, p) >= Integer.MAX_VALUE) {
            return false;
        }

        return true;
    }
    
    
    /**
     * V�rifie qu'une paire soit ins�rable au moins � une position dans l'Echanges
     * @param p
     * @return 
     */
    protected abstract boolean isPaireInserable(Paire p);
    
    public abstract int deltaBenefice(Paire p);
    
    public abstract boolean isPaireAjoutableFin(Paire p);
    
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
    
    public abstract int getSize();
    
    public Paire get(int i) {
        return this.paires.get(i);
    }
    
    public int getBeneficeTotal() {
        return beneficeTotal;
    }

    
    protected boolean isPositionInsertionValide(int position){
        //on ne prend pas en compte l'altruiste donc la liste commence � 1 donc d�calage de 1
        if(position >= 0 && position < this.getSize()){
            return true;
        }
        return false;
    }
   
    
    
    public abstract int deltaCoutInsertion(int position, Paire paireToAdd);  
        
    public abstract Participant getPrec(int position);
    
    public abstract Participant getCurrent(int position);
    
    /**
     * Obtiens la meilleure insertionPaire pour une paire donn�e
     * @param paireToInsert
     * @return 
     */
    public InsertionPaire getMeilleureInsertion(Paire paireToInsert) {
        InsertionPaire insMeilleur = new InsertionPaire();

        if (!isPaireInserable(paireToInsert)) {
            return insMeilleur;
        }

        InsertionPaire insActu;
        for (int pos = 0; pos <= this.getSize(); pos++) {
            insActu = new InsertionPaire(this, pos, paireToInsert);
            if (insActu.isMeilleur(insMeilleur)) {
                insMeilleur = insActu;
            }
        }

        return insMeilleur;
   
    }

     public abstract Paire getNext(int position);
        
    


    public LinkedList<Paire> getPaires() {
        return new LinkedList<>(paires);
    }
}
