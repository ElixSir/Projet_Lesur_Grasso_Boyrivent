/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solution.ensemble;

import instance.reseau.Paire;
import instance.reseau.Participant;
import java.util.LinkedList;
import operateur.InterDeplacement;
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
    


    public void setBeneficeToal(int b){
        this.beneficeTotal = b;
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

       /* if (this.deltaCoutInsertion(position, p) == -1) {
            return false;
        }*/

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
    public boolean setPaires(LinkedList<Paire> paires) {
        if( null == paires) return false;
        
        this.paires = paires;
        if( (this instanceof Cycle) ) {
            
            this.beneficeTotal = Cycle.beneficeTotalCycle(paires);
        } else if( this instanceof Chaine) {
            LinkedList<Participant> participants = new LinkedList<>(paires);
            Chaine c = (Chaine)this;
            participants.addFirst(c.getAltruiste());
            this.beneficeTotal = Chaine.beneficeTotalChaine(participants);
        }
        
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
    
    public int DelBenefice(int benefice, int beneficeToAdd) {
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
     * renvoie un bool?en indiquant si position correspond ? une position d'insertion valide
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
   
    protected abstract int getMaxEchange();
    
    public abstract int deltaCoutInsertion(int position, Paire paireToAdd);  
    
    public abstract boolean doEchange(InterEchange infos);
    public abstract boolean doEchange(IntraEchange infos);

    public abstract int BeneficeEchange(int positionI, int positionJ, int longueurI, int longueurJ);

    public abstract int BeneficeEchangeInter(int positionI, int positionJ, int longueurI, int longueurJ);
        
    public abstract Participant getPrec(int position);
    
    public abstract Participant getCurrent(int position);
    
    public abstract Participant getNext(int position);
    
    
    
    /**
     * renvoie le meilleur op?rateur inter-echange de type type pour cet
     * echange et l'echage autreEchange.
     *
     * @param autreTournee
     * @param type
     * @return
     */
    public OperateurLocal getMeilleurOperateurInter(Echanges autreEchange, TypeOperateurLocal type) {
        OperateurLocal best = OperateurLocal.getOperateur(type);
        if (autreEchange.equals(this)) {
            return best;
        }
        for (int i = 0; i < this.paires.size(); i++) {
            for (int j = 0; j < this.paires.size() + 1; j++) {
                OperateurLocal op = OperateurLocal.getOperateurInter(type, this, autreEchange, i, j, 1, 0);
                //TODO : changer les deux derniers parametres avec une boucle
                if (op.isMeilleur(best)) {
                    best = op;
                }
            }
        }     

        return best;
    }
    
    /**
     * impl?mente le mouvement li? ? l?op?rateur de d?placement intra-echange
     * infos PositionJ est une case apr?s le v?ritable emplacement d'insertion
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

        //En voulant l'ins?rer ? la positionJ, cela va d?caler le reste de la list (incr?ment de 1 de le leur index)
        this.beneficeTotal += infos.getDeltaBenefice();

        Echanges echangeFini = infos.getEchangeFini();
        this.paires = echangeFini.getPaires();

        if (!this.check()) {
            System.out.println("Mauvais d?placement, " + this.toString());
            System.out.println(infos);
            System.exit(-1); //Termine le programme
        }

        return true;
    }
    
    /**
     * impl?mente le mouvement li? ? l?op?rateur de d?placement inter-tourn?e
     * infos On prend l'?l?ment ? la position I de la tourn?e actuelle (this) et
     * on le met ? l'emplacement J de la tourn?e autreTournee
     *
     * @param infos
     * @return
     */
    public boolean doDeplacement(InterDeplacement infos) {

        if (infos == null) {
            return false;
        }
        if (!infos.isMouvementRealisable()) {
            return false;
        }
        if (!infos.isMouvementAmeliorant()) {
            return false;
        }

        Echanges autreEchange = infos.getAutreEchange();
        autreEchange.paires = infos.getAutreEchangeFini().getPaires();
        autreEchange.beneficeTotal = infos.getBeneficeAutreEchange();
        this.paires = infos.getEchangeFini().getPaires();
        this.beneficeTotal = infos.getBeneficeEchange();

        if (!this.check()) {
            System.out.println("Mauvais d?placement cet echange, " + this.toString());
            System.out.println(infos);
            System.exit(-1); //Termine le programme
        }
        if (!autreEchange.check()) {
            System.out.println("Mauvais d?placement autre echange, " + this.toString());
            System.out.println(infos);
            System.exit(-1); //Termine le programme
        }

        return true;
    }

    

    public LinkedList<Paire> getPaires() {
        return new LinkedList(paires);
    }
    
    public abstract int beneficeGlobal(int positionInsertion, int longueur); 
    
    public abstract int deltaBeneficeEchange(int positionI, int positionJ, IntraEchange i); 

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.beneficeTotal;
        //hash = 67 * hash + Objects.hashCode(this.paires);
        return hash;
    }
    
    /** 
     * renvoie le meilleur op?rateur intra-echange de type type pour cette
     * tourn?e
     * TODO : Augmenter la longueur pour l'op?rateur (ici 1)
     * Il faut faire en sorte que l'on teste avec des ensemble de paires, longueur
     * sup?rieure ? 1
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
    

    @Override
    public boolean equals(Object obj) {
        if(true)return false;
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Echanges other = (Echanges) obj;
        if (this.beneficeTotal != other.beneficeTotal) {
            return false;
        }
        
        if( this.paires.size() != other.paires.size() ) {
            return false;
        } else {
            int som1 = 0,som2 = 0;
            for (Paire p : paires) {
                som1 += p.getId();
            }
            
            for (Paire p : other.paires) {
                som2 += p.getId();
            }
            
            if(som1 != som2) return false;
            
            LinkedList<Paire> pObj = new LinkedList<>(other.paires);
            
            for (Paire p : paires) {
                pObj.remove(p);
            }
            
            if(! pObj.isEmpty() ) return false;
        }
        // Objects.equals(this.paires, other.paires);
        return true;
    }
    
    public Paire getPairePosition(int position) {
        if (!isPositionValide(position)) {
            return null;
        }
        return this.getPaires().get(position);

    }
    
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

        if (this.isPositionInsertionValide(positionJ) && this.isPositionValide(positionI)) {
            if (positionI != positionJ && difference > 1) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * renvoie un bool?en indiquant si position correspond ? une position valide
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
    
    public LinkedList<Paire> getPairesReference() {
        return this.paires;
    }
    
}
