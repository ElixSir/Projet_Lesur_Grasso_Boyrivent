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
import java.util.LinkedList;

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
        return super.getSize() + 1;
    }
    
    @Override
    public boolean isPaireAjoutable(Paire p) {
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

    public void printChaine(PrintWriter ecriture) {
        
        String s = this.altruiste.getId() + "\t";
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
}
