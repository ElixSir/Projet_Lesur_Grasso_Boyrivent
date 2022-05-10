/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensemble;

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
    protected boolean isPaireInserable(Paire p) {

        for (int i = 0; i < this.getSize(); i++) {
            if (isPaireInserablePosition(i, p)) {
                return true;
            }

        }

        return false;
    }
    
    public int deltaCoutInsertion(int position, Paire paireToAdd){
        if(!this.isPaireAjoutableFin(paireToAdd)) return -1;
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
        else if(position == this.getSize()-1){
            pPrec = this.getPrec(position);
            deltaBenefice = this.addBenefice(deltaBenefice, pPrec.getBeneficeVers(paireToAdd));
        }
        else{
           //probleme ici avec get pPrec
           //position = 2 alors que seulement l'altruiste et la paire,
           //mais si dans le test pr�alable on fait size - 1, on ne
           //rentre m�me pas dans la boucle avec i car getSize - 1 = 0
           //voir classe Echange ligne 162
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
    
    
    public Participant getNext(int position){
        if(!this.isPositionInsertionValide(position)) return null;
        if(position == this.paires.size()){
            return null;
        }
        return this.paires.get(position +1);
    }
        
    public void printChaine(PrintWriter ecriture) {
        
        String s =  this.altruiste.getId() + "\t";
        for (int j = 0; j < this.getSize() - 1; j++) {
            Paire paire = this.get(j);
            s += paire.getId() + "\t";
        }

        ecriture.print(s + "\n");
    }

    @Override
    public boolean check() {
        boolean checker = true;
        
        int beneficeTotal = 0;
        
        if( this.getSize() > 1) {
            
            Altruiste a = this.getAltruiste();
            Paire p = this.getFirstPaire();
            beneficeTotal += a.getBeneficeVers(p);
            
            for(int i = 1; i < this.getSize() - 1; i++) {
                Paire pcurr = this.get(i);
                beneficeTotal += p.getBeneficeVers(pcurr);
                p = pcurr;
            }
        } 
        
        if( beneficeTotal != this.getBeneficeTotal()) {
            System.err.println("[CHECK - Chaine] : Le bénéfice total calculé n'est pas correcte : ( classe : " + this.getBeneficeTotal() + ", calculé : " + beneficeTotal + " )");
            checker = false;
        }
        
        if( this.getSize() > this.maxChaine ) {
            System.err.println("[CHECK - Chaine] : La taille totale est supérieur à la capacité : ( taille totale : " + this.getSize() + ", capacité : " + this.maxChaine  + " )" );
            checker = false;
        }
        
        return checker;
    }

    @Override
    public String toString(){
           String s = "Chaine{" + "maxChaine=" + maxChaine + ", altruiste=" + altruiste;
            
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
        if (!this.isPositionInsertionValide(position)) {
            return null;
        }
        return this.paires.get(position);
    }
}
