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
     * Vï¿½rifie qu'une paire puisse ï¿½tre insï¿½rï¿½e ï¿½ une certaine position
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

        if (this.deltaCoutInsertion(position, p) == -1) {
            return false;
        }

        return true;
    }
    
    
    /**
     * Vï¿½rifie qu'une paire soit insï¿½rable au moins ï¿½ une position dans l'Echanges
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
    
    /**
     * Ajoute le benefice au benefice total en testant les paramètres
     *
     * @param benefice
     * @param beneficeToAdd
     * @return
     */
    public int addBenefice(int benefice, int beneficeToAdd) {
        if (benefice == -1 || beneficeToAdd == -1) {
            return -1;
        }
        return benefice + beneficeToAdd;
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
        //on ne prend pas en compte l'altruiste donc la liste commence ï¿½ 1 donc dï¿½calage de 1

        if(position >= 0 && position < this.getSize()){ // && position < this.getSize() Si on met ça impossible d'inserer en 
            return true;
        }
        return false;
    }
   
    protected abstract int getMaxEchange();
    
    public abstract int deltaCoutInsertion(int position, Paire paireToAdd);  
        
    public abstract Participant getPrec(int position);
    
    public abstract Participant getCurrent(int position);
    
    public abstract Participant getNext(int position);
    
    /**
     * Obtiens la meilleure insertionPaire pour une paire donnï¿½e
     * @param paireToInsert
     * @return 
     */
    public InsertionPaire getMeilleureInsertion(Paire paireToInsert) {
        InsertionPaire insMeilleur = new InsertionPaire();
        if (!isPaireInserable(paireToInsert)) {
            return insMeilleur;
        }
        InsertionPaire insActu;
        for (int pos = 0; pos <= this.paires.size(); pos++) {
            insActu = new InsertionPaire(this, pos, paireToInsert);
            if (insActu.isMeilleur(insMeilleur)) {
                //problème : Les chaines ne rentrent jamais
                insMeilleur = insActu;
            }
        }

        return insMeilleur;
   
    }

     
        
        /**
     * Ins?re dans l'?change si il y a possibilit?
     *
     * @param infos
     * @return
     */
    public boolean doInsertion(InsertionPaire infos) {
        if (infos == null) {
            return false;
        }
        if (!infos.isMouvementRealisable()) {
            return false;
        }

        Paire paire = infos.getPaire();

        this.beneficeTotal += infos.getDeltaBenefice();
        this.paires.add(infos.getPosition(), paire);

        if (!this.check()) {
            System.out.println("Erreur doInsertion");
            System.out.println(infos);
            System.exit(-1); // Si erreur critique on arrete 
        }

        return true;
    }

    

    public LinkedList<Paire> getPaires() {
        return new LinkedList<>(paires);
    }
    
    
     
    private int deltaBeneficeEchangeConsecutif(int positionI){
        
        if(this.paires.size() < 2){
            return -1;
        }
        
        if(this.paires.size() == 2){ // si un cycle de 2 
            return 0;
        }
        
        
        int deltaCout = 0;
        
        Participant paireI = this.getCurrent(positionI);
        Participant paireJ = this.getNext(positionI);
        Participant avantI = this.getPrec(positionI);
        Participant apresJ = this.getNext(positionI+1);
        
        
        deltaCout -= avantI.getBeneficeVers(paireI);
        deltaCout -= paireI.getBeneficeVers(paireJ);
        deltaCout -= paireJ.getBeneficeVers(apresJ);
        
        deltaCout += avantI.getBeneficeVers(paireJ);
        deltaCout += paireJ.getBeneficeVers(paireI);
        deltaCout += paireI.getBeneficeVers(apresJ);
        
        
        return deltaCout;
    }
    
    private int deltaBeneficeRemplacement(int position, Participant paireJ){
        
        if(this.paires.size() < 2){
            return -1;
        }
        
        int deltaCout = 0;

        Participant paireI = this.getCurrent(position);
        
        Participant avantI = this.getPrec(position);
        Participant apresI = this.getNext(position);
        
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
        
        this.beneficeTotal = this.addBenefice(beneficeTotal, infos.getDeltaBenefice()) ; //MAJ cout total
        
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
            return -1;
        }
        if(!isPositionInsertionValide(positionJ)){
            //System.out.println("posJ Invalid");
            return -1;
        }
        if(positionI == positionJ){
            //System.out.println("posI = posJ");
            return -1;
        }
        if(!(positionI<positionJ)){
            //System.out.println("!(positionI<positionJ)");
            return -1;
        }
        
        
        if(positionJ-positionI == 1){
            //System.out.println("Cons?cutif");
            return deltaBeneficeEchangeConsecutif(positionI);
        }
        //System.out.println("Pas cons?cutif");
        return deltaBeneficeRemplacement(positionI,this.getCurrent(positionJ))+deltaBeneficeRemplacement(positionJ,this.getCurrent(positionI));
    }    
    
    
}
