/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solution;

import instance.reseau.Paire;
import instance.reseau.Participant;
import java.util.LinkedList;
import operateur.InsertionPaire;
import operateur.InterEchange;
import operateur.IntraDeplacement;
import operateur.IntraEchange;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;

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
   /* protected boolean isPaireInserablePosition(int position, Paire[] p) {

        if (!isPositionInsertionValide(position)) {
            return false;
        }
        if (null == p || this.getSize() >= this.getMaxEchange()) {
            return false;
        }
        
        if (this.deltaBeneficeInsertion(position, p) == -1) {
            return false;
        }

        return true;
    }
    */
    
    /**
     * Vï¿½rifie qu'une paire soit insï¿½rable au moins ï¿½ une position dans l'Echanges
     * @param p
     * @return 
     */
    protected abstract boolean isPaireInserable(Paire[] p);
    
    public abstract int deltaBenefice(Paire p);
    
    public abstract boolean isPaireAjoutableFin(Paire p);
    
    public abstract boolean check();
    
    
    
    /**
     * set les paires (possible uniquement si la classe est Cycle)
     * @param paires
     * @return 
     */
    public boolean setPaires(LinkedList<Paire> paires) {//bizarre
        if( null == paires) return false;
        
        if( ! (this instanceof Cycle) ) return false;
            
        this.paires = paires;
        
        this.beneficeTotal = Cycle.beneficeTotalCycle(paires);
        
        return true;
    }
   
    /**
     * Récupére la paire à une certaine position d?un échange
     *
     * @param position
     * @return
     */
    public Paire getPairePosition(int position) {
        if (!isPositionValide(position)) {
            return null;
        }
        return this.getPaires().get(position);

    }
    
    /**
     * Ajoute le benefice au benefice total en testant les paramï¿½tres
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
    
    public int delBenefice(int benefice, int beneficeToAdd) {
        if (benefice == -1 || beneficeToAdd == -1) {
            return -1;
        }
        return benefice - beneficeToAdd;
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

    
    /**
     * renvoie un boolï¿½en indiquant si position correspond ï¿½ une position d'insertion valide
     * d?une paire dans l'echange (donc entre 0 et le nombre de paires).
     *
     * @param position
     * @return
     */
    public boolean isPositionInsertionValide(int position){

        if (position < 0 || position > this.paires.size()) {
            return false;
        }
        return true;
    }
    
    /**
     * renvoie un boolï¿½en indiquant si position correspond ï¿½ une position valide
     * d?une paire dans l'echange (donc entre 0 et le nombre de paire ?1).
     *
     * @param position
     * @return
     */
    public boolean isPositionValide(int position) {
        if (position < 0 || position > this.paires.size() - 1) {
            return false;
        }
        return true;
    }
    
    protected abstract int getMaxEchange();
    
    public abstract int deltaBeneficeInsertion(int position, Paire paireToAdd); 
   
    public abstract int beneficeGlobal(int positionInsertion, int longueur); 
        
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
        if (!isPaireInserable(new Paire[] {paireToInsert})) {
            return insMeilleur;
        }
        InsertionPaire insActu;
        for (int pos = 0; pos <= this.paires.size(); pos++) {
            insActu = new InsertionPaire(this, pos, paireToInsert);
            if (insActu.isMeilleur(insMeilleur)) {
                //problï¿½me : Les chaines ne rentrent jamais
                insMeilleur = insActu;
            }
        }

        return insMeilleur;
   
    }

    /** 
     * renvoie le meilleur opérateur intra-echange de type type pour cette
     * tournée
     * TODO : Augmenter la longueur pour l'opérateur (ici 1)
     * Il faut faire en sorte que l'on teste avec des ensemble de paires, longueur
     * supérieure à 1
     * @param type
     * @return
     */
    public OperateurLocal getMeilleurOperateurIntra(TypeOperateurLocal type) {
        OperateurLocal best = OperateurLocal.getOperateur(type);
        for (int i = 0; i < this.paires.size(); i++) {
            for (int j = 0; j < this.paires.size() + 1; j++) {
                OperateurLocal op = OperateurLocal.getOperateurIntra(type, this, i, j, 1, 0);
                if (op.isMeilleur(best)) {
                    best = op;
                }
            }
        }
        return best;
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

        this.beneficeTotal = this.addBenefice(beneficeTotal, infos.getDeltaBenefice());
        this.paires.add(infos.getPosition(), paire);

        if (!this.check()) {
            System.out.println("Erreur doInsertion");
            System.out.println(infos);
            System.exit(-1); // Si erreur critique on arrete 
        }

        return true;
    }
    
    /**
     * implémente le mouvement lié à l?opérateur de déplacement intra-echange
     * infos PositionJ est une case après le véritable emplacement d'insertion
     *
     * @param infos
     * @return
     */
    public boolean doDeplacement(IntraDeplacement infos) {
        if (infos == null || infos.getEchangeFini() == null) {
            return false;
        }
        if (!infos.isMouvementRealisable()) {
            return false;
        }
        if (!infos.isMouvementAmeliorant()) {
            return false;
        }

        //En voulant l'insérer à la positionJ, cela va décaler le reste de la list (incrément de 1 de le leur index)
        this.beneficeTotal += infos.getDeltaBenefice();

        Echanges echangeFini = infos.getEchangeFini();
        this.paires = echangeFini.getPaires();

        if (!this.check()) {
            System.out.println("Mauvais déplacement, " + this.toString());
            System.out.println(infos);
            System.exit(-1); //Termine le programme
        }

        return true;
    }

    

    public LinkedList<Paire> getPaires() {
        return new LinkedList<>(paires);
    }
    public LinkedList<Paire> getPairesReference() {
        return this.paires;
    }
    
    
    
    /**
     * renvoie le co?t engendr? par la suppression de la paire i ? la position
     * position de la tourn?e On fait trois trajets : on enl?ve la
     * transplantation de i-1 ? i, on enl?ve la transplantation de i ? i+1, on
     * ajoute la transplantation de i-1 ? i+1
     *
     * @param position
     * @return
     */
    public abstract int deltaBeneficeSuppression(int position);

    /**
     * Renvoie le co?t engendr? par le d?placement dans la m?me ?change de la
     * paire ? la position positionI avant le point ? la position positionJ.
     * Cette m?thode renverra -1 si une des positions pass?es en param?tre est
     * incorrecte, ou si les deux positions ne sont pas compatibles pour un
     * d?placement.
     *
     * @param positionI
     * @param positionJ
     * @return
     */
    /*public int deltaBeneficeDeplacement(int positionI, int positionJ) {
        if (positionDeplacementValides(positionI, positionJ)
                && this.deltaBeneficeInsertion(positionJ, new Paire[] {paires.get(positionI)}) != -1) {
            int deltaBeneficeDeplacement = this.deltaBeneficeSuppression(positionI)
                    + this.deltaBeneficeInsertion(positionJ, new Paire[]{paires.get(positionI)});
            return deltaBeneficeDeplacement;
        }
        return -1;
    }*/

    /**
     * Renvoie un boolean indiquant si : positionI, la position de la paire est
     * une position valide positionJ, la position d'insertion est possible
     * positionI et positionJ ne sont pas ?gaux positionI n'est pas coll? dans
     * la liste ? positionJ
     *
     * @param positionI
     * @param positionJ
     * @return
     */
    public boolean positionDeplacementValides(int positionI, int positionJ) {
        int difference = Math.abs(positionI - positionJ);

        if (this.isPositionInsertionValide(positionJ)
                && this.isPositionValide(positionI)) {
            if (positionI != positionJ && difference > 1) {
                return true;
            }
        }
        return false;
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
        
        
        deltaCout = this.delBenefice(deltaCout, avantI.getBeneficeVers(paireI));
        deltaCout = this.delBenefice(deltaCout, paireI.getBeneficeVers(paireJ));
        deltaCout = this.delBenefice(deltaCout, paireJ.getBeneficeVers(apresJ));
        
        deltaCout = this.addBenefice(deltaCout, avantI.getBeneficeVers(paireJ));
        deltaCout = this.addBenefice(deltaCout, paireJ.getBeneficeVers(paireI));
        deltaCout = this.addBenefice(deltaCout, paireI.getBeneficeVers(apresJ));
        
        
        return deltaCout;
    }
    
    protected abstract int deltaBeneficeRemplacement(int position, Participant paireJ);
    

    
    public boolean doEchangeCycle(IntraEchange infos){
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
            System.out.println("Mauvais ï¿½change des clients");
            System.out.println(infos);
            System.exit(-1); //Termine le programme
        }
        
        return true;
    }
    public abstract boolean doEchange(InterEchange infos);
    public abstract boolean doEchange(IntraEchange infos);

    public abstract int BeneficeEchange(int positionI, int positionJ, int longueurI, int longueurJ);

    public abstract int BeneficeEchangeInter(int positionI, int positionJ, int longueurI, int longueurJ);
    
    
  
    
}
