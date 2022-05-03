/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensemble;

import instance.reseau.Paire;
import java.util.LinkedList;
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
    
    public abstract Paire getNext(int position);
    
    
    public Paire getCurrent(int position){
        if(!this.isPositionInsertionValide(position)) return null;
        return this.paires.get(position);
    }


    public LinkedList<Paire> getPaires() {
        return new LinkedList<>(paires);
    }
    
    
    private int deltaBeneficeEchangeConsecutif(int positionI){
        
        if(this.paires.size() < 2){
            return -1;
        }
        
        if(this.paires.size() == 2){ // si un cycle de 2 
            Paire paireI = this.getCurrent(positionI);
            Paire paireJ = this.getNext(positionI);
            return paireI.getBeneficeVers(paireJ) + paireJ.getBeneficeVers(paireI);
        }
        
        
        int deltaCout = 0;
        
        Paire paireI = this.getCurrent(positionI);
        Paire paireJ = this.getNext(positionI);
        Paire avantI = this.getPrec(positionI);
        Paire apresJ = this.getNext(positionI+1);
        
        
        deltaCout -= avantI.getBeneficeVers(paireI);
        deltaCout -= paireI.getBeneficeVers(paireJ);
        deltaCout -= paireJ.getBeneficeVers(apresJ);
        
        deltaCout += avantI.getBeneficeVers(paireJ);
        deltaCout += paireJ.getBeneficeVers(paireI);
        deltaCout += paireI.getBeneficeVers(apresJ);
        
        
        return deltaCout;
    }
    
    private int deltaBeneficeRemplacement(int position, Paire paireJ){
        
        if(this.paires.size() < 2){
            return -1;
        }
        
        int deltaCout = 0;

        Paire paireI = this.getCurrent(position);
        
        Paire avantI = this.getPrec(position);
        Paire apresI = this.getNext(position);
        
        deltaCout-= avantI.getBeneficeVers(paireI);
        deltaCout-= paireI.getBeneficeVers(apresI);
        
        deltaCout+= avantI.getBeneficeVers(paireJ);
        deltaCout+= paireJ.getBeneficeVers(apresI);
        
        return deltaCout;
    }
    
    
    public boolean doEchangeCycle(IntraEchangeCycle infos){
        if(infos == null) return false;
        if(!infos.isMouvementRealisable()) return false; 
        
        int positionI = infos.getPositionI();
        int positionJ = infos.getPositionJ();
        
        Paire paireI = infos.getClientI();
        Paire paireJ = infos.getClientJ();
        
        this.paires.set(positionI, paireJ);
        this.paires.set(positionJ, paireI);
        
        this.beneficeTotal += infos.getDeltaBenefice(); //MAJ cout total
        
        if (!this.check()){
            System.out.println("Mauvais échange des clients");
            System.out.println(infos);
            System.exit(-1); //Termine le programme
        }
        
        return true;
    }
    
    public int deltaBeneficeEchange(int positionI, int positionJ) {
        if(!isPositionInsertionValide(positionI)){
            //System.out.println("posI Invalid");
            return Integer.MAX_VALUE;
        }
        if(!isPositionInsertionValide(positionJ)){
            //System.out.println("posJ Invalid");
            return Integer.MAX_VALUE;
        }
        if(positionI == positionJ){
            //System.out.println("posI = posJ");
            return Integer.MAX_VALUE;
        }
        if(!(positionI<positionJ)){
            //System.out.println("!(positionI<positionJ)");
            return Integer.MAX_VALUE;
        }
        
        
        if(positionJ-positionI == 1){
            //System.out.println("Consécutif");
            return deltaBeneficeEchangeConsecutif(positionI);
        }
        //System.out.println("Pas consécutif");
        return deltaBeneficeRemplacement(positionI,this.getCurrent(positionJ))+deltaBeneficeRemplacement(positionJ,this.getCurrent(positionI));
    }    
    
    
}
