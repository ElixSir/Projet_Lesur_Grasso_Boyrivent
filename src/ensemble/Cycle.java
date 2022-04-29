/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensemble;

import instance.reseau.Paire;
import java.io.PrintWriter;
import instance.Instance;
import java.util.LinkedList;

/**
 *
 * @author Bart
 */
public class Cycle extends Echanges {
    
    private int maxCycle;

    public Cycle(Instance i) {
        this.maxCycle = i.getMaxCycles();
    }
    
    public Cycle(Instance i, LinkedList<Paire> cycle) {
        this(i);
        if(Cycle.checkCycleValide(cycle)) {
            this.setPaires(new LinkedList(cycle));
        }
    }
    
    public static boolean checkCycleValide(LinkedList<Paire> cycle) {
        if(null == cycle) return false;
        boolean checked = true;
        
        if(cycle.isEmpty()) 
            return checked;
        
        Paire first = cycle.getFirst(),
                last = cycle.getLast();
        
        if( !first.equals(last) && last.getBeneficeVers(first) == -1 ) {
            checked = false;
        }
        
        Paire p = first;
        for (int i = 1; i < cycle.size(); i++) {
            Paire temp = cycle.get(i);
            if( p.getBeneficeVers(temp) == -1 ) {
                checked = false;
                break;
            }
            p = temp;
        }
        
        return checked;
    }
    
    public static int beneficeTotalCycle(LinkedList<Paire> cycle) {
        if( null == cycle ) return -1;
        if( cycle.isEmpty() ) return 0;
        int benefice =0;
        
        Paire p = cycle.getFirst();
        for (int i = 1; i < cycle.size(); i++) {
            Paire temp = cycle.get(i);
            benefice += p.getBeneficeVers(temp);
            p = temp;
        }
        benefice += cycle.getLast().getBeneficeVers(cycle.getFirst());
        
        return benefice;
    }
    
    protected boolean isPaireInserable(int position, Paire p){
        
        if(!isPositionInsertionValide(position)) return false;
        if( this.getSize() >= this.maxCycle ) return false;
        
        if(this.deltaCoutInsertion(position, p) >= Integer.MAX_VALUE) return false;
        
        
        return true;
    }
    
    @Override
    public boolean isPaireAjoutable(Paire p) {
        if( null == p || this.getSize()  >= this.maxCycle ) return false;
        
        if( this.getSize() == 0 ) return true;
        
        Paire pLast = this.getLastPaire(),
                pFirst = this.getFirstPaire();
        
        return (pLast.getBeneficeVers(p) >= 0 && p.getBeneficeVers(pFirst) >= 0 );
    }
    
    @Override
    public int deltaBenefice(Paire p) {
        if( this.getSize() == 0 ) return 0;
        
        Paire first = this.getFirstPaire(),
                last = this.getLastPaire();
        
        int benefPrec = last.getBeneficeVers(p),
                benefFirst = p.getBeneficeVers(first),
                benefSuppr = 0;
        
        if( this.getSize() > 1 ) {
            benefSuppr = last.getBeneficeVers(first);
        }
        
        return benefFirst + benefPrec - benefSuppr;
    }

    public int getMaxCycle() {
        return maxCycle;
    }

    public void printCycle(PrintWriter ecriture) {
        String s = "";

        for (int j = 0; j < this.getSize(); j++) {
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
            
            Paire p = this.getFirstPaire();
            for(int i = 1; i < this.getSize(); i++) {
                Paire pcurr = this.get(i);
                beneficeTotal += p.getBeneficeVers(pcurr);
                p = pcurr;
            }
            beneficeTotal += this.getLastPaire().getBeneficeVers(this.getFirstPaire());
        } 
        
        if( beneficeTotal != this.getBeneficeTotal()) {
            System.err.println(this.getSize() +"[CHECK - Cycle] : Le bénéfice total calculé n'est pas correcte : ( classe : " + this.getBeneficeTotal() + ", calculé : " + beneficeTotal + " )");
            checker = false;
        }
        
        if( this.getSize() > this.maxCycle ) {
            System.err.println("[CHECK - Cycle] : La taille totale est supérieur à la capacité : ( taille totale : " + this.getSize() + ", capacité : " + this.maxCycle  + " )" );
            checker = false;
        }
        
        return checker;
    }

    
    public int deltaCoutInsertion(int position, Paire paireToAdd){
        int deltaBenefice = 0;
                
        if(this.paires.isEmpty()){
            //Pas de Benefice en plus car c'est la premi�re paire dans le cycle
        }
        else{
            Paire pPrec = this.getPrec(position);
            Paire pCour = this.getCurrent(position);
            
            deltaBenefice -= pPrec.getBeneficeVers(pCour);
            deltaBenefice += pPrec.getBeneficeVers(paireToAdd);
            deltaBenefice += paireToAdd.getBeneficeVers(pCour);
        }
        
        return deltaBenefice;
    }
    
    @Override
    public String toString() {
        String res = "\n[";
        
        LinkedList<Paire> paires = this.getPaires();
        
        res += paires.pop().toString();
        
        for (Paire paire : paires) {
            res += ", " + paire.toString();
        }
        
        return res + ']';
    }
    
}
