/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solution;

import instance.reseau.Altruiste;
import instance.reseau.Paire;
import java.io.PrintWriter;
import instance.Instance;
import instance.reseau.Participant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import operateur.InsertionPaire;

/**
 *
 * @author Bart
 */
public class Chaine extends Echanges {
    private int maxChaine;
    private Altruiste altruiste; 
    
    public Chaine(Instance i){
        super();
        this.maxChaine = i.getMaxChaines();
        altruiste = null;
    }
    
    public Chaine(Chaine ch) {
        this.maxChaine = ch.maxChaine;
        this.altruiste = ch.getAltruiste();
        this.paires = ch.getPaires();
    }
    
    public Chaine(Instance i, Altruiste altruiste){
        this(i);
        this.altruiste = altruiste;
    }
    
    public static int beneficeTotalChaine(LinkedList<Participant> chaine) {
        if( null == chaine ) return -1;
        if( chaine.size() < 2 ) return 0;
        
        Participant p = chaine.getFirst(),
                temp; 
        int beneficeTotal = 0;
        
        for (int i = 1; i < chaine.size(); i++) {
            temp = chaine.get(i);
            if( !(temp instanceof Paire) ) return -1;
            beneficeTotal += p.getBeneficeVers((Paire)temp);
            p = temp;
        }
        return beneficeTotal;
    }
    
    @Override
    public int getSize() {
        return this.paires.size() + 1;
    }
    
    @Override
    public boolean isPaireAjoutableFin(Paire p) {
        if( null == p || this.getSize()  >= this.maxChaine ) return false;
         
       
        if( this.getSize() == 1 ) {
            return this.altruiste.getBeneficeVers(p) >= 0;
        }
        
        Paire pLast = this.getLastPaire();
        
        return (pLast.getBeneficeVers(p) >= 0);
    }
    
    
    
    @Override
    public int deltaBenefice(Paire p) {
                
        if( this.getSize() == 1 ) {
            return this.altruiste.getBeneficeVers(p);
        }
        
        Paire pLast = this.getLastPaire();
        
        return pLast.getBeneficeVers(p);
    }
    
    public int getMaxChaine() {
        return maxChaine;
    }

    public Altruiste getAltruiste() {
        return altruiste;
    }

    @Override
    protected boolean isPaireInserable(Paire[] p) {

        for (int i = 0; i <= this.getSize(); i++) {
            /*if (isPaireInserablePosition(i, p)) {
                return true;
            }*/

        }

        return false;
    }
    
    public int deltaBeneficeInsertion(int position, Paire paireToAdd){
        if (!isPositionInsertionValide(position) || paireToAdd == null) return -1;
        int deltaBenefice = 0;
                
        Participant pPrec = null;
        Participant pCour = null;
        
        if(this.paires.isEmpty()){
            deltaBenefice = this.addBenefice(deltaBenefice, this.altruiste.getBeneficeVers(paireToAdd));
            return deltaBenefice;
        }
        else if(position == 0){
            pPrec = this.altruiste;
            pCour = this.getCurrent(position);
        }
        else if(position == this.paires.size()){
            pPrec = this.getPrec(position);
            deltaBenefice = this.addBenefice(deltaBenefice, pPrec.getBeneficeVers(paireToAdd));
        }
        else{
            pPrec = this.getPrec(position);
            pCour = this.getCurrent(position);
        }
        if (pPrec == null)
        {
            int size = this.getSize();
        }
        deltaBenefice = this.addBenefice(deltaBenefice, - pPrec.getBeneficeVers(pCour));
        deltaBenefice = this.addBenefice(deltaBenefice, pPrec.getBeneficeVers(paireToAdd));
        deltaBenefice = this.addBenefice(deltaBenefice, paireToAdd.getBeneficeVers(pCour));

        return deltaBenefice;
    }
    
    public int beneficeGlobal(int position, int longueur) {
        if (this.getSize() == 1) return 0;
        if (isCompatible(position, longueur)) {
            int beneficeTotalCalcule = 0;

            Altruiste a = this.getAltruiste();
            Paire p = this.getFirstPaire();
            beneficeTotalCalcule = this.addBenefice(beneficeTotalCalcule, a.getBeneficeVers(p));

            for (int i = 1; i < this.paires.size(); i++) {
                Paire pcurr = this.get(i);
                beneficeTotalCalcule = this.addBenefice(beneficeTotalCalcule, p.getBeneficeVers(pcurr));
                p = pcurr;
            }
            
            return beneficeTotalCalcule;
        }
        return -1;
    }
    
    /**
     * Est compatible si l'Èlement avant est compatible avec le premier et soit 
     * on insËre ‡ la derniËre place, soit le dernier Èlement est compatible 
     * avec le suivant de la chaine
     * @param position
     * @param longueur
     * @param pairesToAdd
     * @return 
     */
    private boolean isCompatible(int positionInsertion, int longueur) {
        if (this.getSize() == 1) return true;
        Participant pPrec = this.getPrec(positionInsertion);

        Participant pFirst = this.getCurrent(positionInsertion);
        Participant pLast = this.getCurrent(positionInsertion + longueur - 1);
        Participant pNext = this.getNext(positionInsertion + longueur - 1);
        if (pPrec.getBeneficeVers(pFirst) != -1
                && ((pNext != null && pLast.getBeneficeVers(pNext) != -1) 
                    || pNext == null)) {
            return true;
        }
        return false;
    }
    
    /**
     * renvoie le co˚t engendrÈ par la suppression de la paire i ‡ la position
     * position de la tournÈe On fait trois trajets : on enlËve la
     * transplantation de i-1 ‡ i, on enlËve la transplantation de i ‡ i+1, on
     * ajoute la transplantation de i-1 ‡ i+1
     * @param position
     * @return
     */
    @Override
    public int deltaBeneficeSuppression(int position) {
        if (!this.isPositionValide(position)) {
            return -1;
        }
        int deltaBenefice = 0;
        if (paires.size() == 1) {
            //on enleve le benefice de la transplantation de l'altruiste vers la paire
            deltaBenefice = this.delBenefice(deltaBenefice, this.altruiste.getBeneficeVers(this.getFirstPaire()));
        } else {

            Participant pPrec = this.getPrec(position);
            Participant pCurr = this.getCurrent(position);
            Participant pSuiv = this.getNext(position);

            //on enlËve le trajet de i-1 ‡ i
            deltaBenefice = this.delBenefice(deltaBenefice, pPrec.getBeneficeVers(pCurr));
            //on enlËve le trajet de i ‡ i+1
            deltaBenefice = this.delBenefice(deltaBenefice, pCurr.getBeneficeVers(pSuiv));
            //on ajoute le trajet i-1 ‡ i+1
            deltaBenefice = this.addBenefice(deltaBenefice, pPrec.getBeneficeVers(pSuiv));

        }

        return deltaBenefice;

    }
    
    
    public Participant getNext(int position){
        if(!this.isPositionInsertionValide(position)) return null;
        if(position+1 >= this.paires.size()){
            return null;
        }
        return this.paires.get(position +1);
    }
        
    public void printChaine(PrintWriter ecriture) {
        
        String s =  this.altruiste.getId() + "\t";
        for (int j = 0; j < this.getSize()-1; j++) {
            Paire paire = this.get(j);
            s += paire.getId() + "\t";
        }

        ecriture.print(s + "\n");
    }

    public int deltaBeneficeRemplacement(int position, Participant paireJ){
        
        return 0;
    }
    
    @Override
    public boolean check() {
        boolean checker = true;
        
        int beneficeTotal = 0;
        
        if( this.getSize() > 1) {
            
            Altruiste a = this.getAltruiste();
            Paire p = this.getFirstPaire();
            beneficeTotal = this.addBenefice(beneficeTotal,a.getBeneficeVers(p));
            
            for(int i = 1; i < this.getSize() - 1; i++) {
                Paire pcurr = this.get(i);
                beneficeTotal = this.addBenefice(beneficeTotal, p.getBeneficeVers(pcurr)) ;
                p = pcurr;
            }
        } 
        
        if( beneficeTotal != this.getBeneficeTotal()) {
            System.err.println("[CHECK - Chaine] : Le b√©n√©fice total calcul√© n'est pas correcte : ( classe : " + this.getBeneficeTotal() + ", calcul√© : " + beneficeTotal + " )");
            checker = false;
        }
        
        if( this.getSize() > this.maxChaine ) {
            System.err.println("[CHECK - Chaine] : La taille totale est sup√©rieur √† la capacit√© : ( taille totale : " + this.getSize() + ", capacit√© : " + this.maxChaine  + " )" );
            checker = false;
        }
        
        if( beneficeTotal == -1)
        {
            System.err.println("[CHECK - Chaine] : Le b√©n√©fice total calcul√© n'est pas correcte : ( classe : " + this.getBeneficeTotal() + ", calcul√© : " + beneficeTotal + " )");
            checker = false;
        }
        
        return checker;
    }

    @Override
    public String toString(){
           String s = "Chaine{" + "maxChaine=" + maxChaine + ", altruiste=" + altruiste + ", paires=";
            
            for(Paire p: this.getPaires()){
                s+= p.getId()+",";
            }
            
        return s;
    }

    @Override
    protected int getMaxEchange() {
        return this.maxChaine;
    }
    
    @Override
    public Participant getPrec(int position) {
        if (!this.isPositionInsertionValide(position)) {
            return null;
        }
        if (position == 0) {
            return this.altruiste;
        }
        return this.paires.get(position - 1);
    }
    
    @Override
    public Participant getCurrent(int position) {
        if (!this.isPositionValide(position)) {
            return null;
        }
        return this.paires.get(position);
    }


}
