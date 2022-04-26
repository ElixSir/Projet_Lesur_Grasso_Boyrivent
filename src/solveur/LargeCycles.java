/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solveur;

import ensemble.Cycle;
import instance.Instance;
import instance.reseau.Altruiste;
import instance.reseau.Paire;
import instance.reseau.Participant;
import instance.reseau.Transplantation;
import io.InstanceReader;
import io.exception.ReaderException;
import java.util.LinkedList;
import java.util.List;
import solution.Solution;

/**
 *
 * @author Clem
 */
public class LargeCycles implements Solveur {

    @Override
    public String getNom() {
        return "Large Cycle";
    }
    
    private Paire getPlusGrandBenefice(List<Paire> P,Participant p) {
        Paire best = null;
        int max = 0, benef;
        
        for (Paire paire : P) {
            benef = p.getBeneficeVers(paire);
            if( benef != -1 && max < benef ){
                best = paire;
                max = benef;
            }
        }
        
        return best;
    }
    
    private boolean rechercheFirstCycle(Paire paire,int maxtailleCycle, LinkedList<Paire> cycle, Paire next) {
        
        cycle.add(next);
        // this.printCycle(cycle);
        
        if(next.getBeneficeVers(paire) >= 0) {
            return true;
        }
        
        if(cycle.size() >= maxtailleCycle) return false;
        
        for(Transplantation t: next.getTransplantations()) {
            Paire p = t.getBeneficiaire();

            if(!cycle.contains(p) ){

                if( this.rechercheFirstCycle(paire,maxtailleCycle, cycle, p) ){
                    return true;
                }
                
                if(cycle.size() >= maxtailleCycle) return false;
            }
        }
        
        return false;
    }
    
    private boolean rechercheBestCycleOld(Paire paire, int maxtailleCycle, LinkedList<Paire> cycle, Paire next) {
        // LinkedList<Paire> cycle = new LinkedList();
        cycle.add(next);
        // this.printCycle(cycle);
        
        if(next.getBeneficeVers(paire) >= 0) {
            // System.out.print("win (" + this.beneficeTotalCycle(cycle) + ") : ");
            // this.printCycle(cycle);
            return true;
        }
        
        if(cycle.size() >= maxtailleCycle) return false;
        
        LinkedList<Paire> temp = new LinkedList(cycle);
        LinkedList<Paire> curr = new LinkedList(cycle);
        boolean found = false;
        int benefCycle = Cycle.beneficeTotalCycle(cycle),
                benefTemp = 0;
        
        for(Transplantation t: next.getTransplantations()) {
            Paire p = t.getBeneficiaire();

            if(!temp.contains(p) ){

                if( this.rechercheBestCycleOld(paire,maxtailleCycle, temp, p) ){
                    benefTemp = Cycle.beneficeTotalCycle(temp);
                    if( benefCycle < benefTemp ) {
                        cycle = new LinkedList(temp);
                        benefCycle = benefTemp;
                        found = true;
                    }
                }
                
                temp = new LinkedList(curr);
                
                if(temp.size() >= maxtailleCycle) return false;
            }
        }
        //System.out.print("(" + benefCycle + ") : ");
        //this.printCycle(cycle);
        return found;
    }
    
    private LinkedList<Paire> rechercheBestCycle(Paire paire,int maxtailleCycle, LinkedList<Paire> availabe, LinkedList<Paire> cycle, Paire next) {
        
        cycle.add(next);
        // this.printCycle(cycle);
        
        if(next.getBeneficeVers(paire) >= 0) {
            return new LinkedList(cycle);
        }
        
        if(cycle.size() >= maxtailleCycle) return null;
        
        LinkedList<Paire> temp = null;
        LinkedList<Paire> c = null;
        int benefCycle = 0,
                benefTemp = 0;
        
        for(Transplantation t: next.getTransplantations()) {
            Paire p = t.getBeneficiaire();

            if(!cycle.contains(p) && availabe.contains(p) ) {
                temp = this.rechercheBestCycle(paire, maxtailleCycle, availabe, cycle, p);

                if( null != temp ){
                    benefTemp = Cycle.beneficeTotalCycle(temp);
                    benefCycle = Cycle.beneficeTotalCycle(c);
                    if( benefCycle < benefTemp ) {
                        c = temp;
                        benefCycle = benefTemp;
                    }
                }
                cycle.remove(p);
                
                if(cycle.size() >= maxtailleCycle) return null;
            }
        }
        
        return c;
    }
    
    @Override
    public Solution solve(Instance instance) {
        Solution s = new Solution(instance);
        LinkedList<Paire> paires = instance.getPaires();
        int maxTailleCycle = instance.getMaxCycles();
        
        int bestBenef = 0, tempBenef = 0;
        LinkedList<Paire> bestCycle = new LinkedList(), tempCycle;
        Paire bestP;
        
        /* Cycles avec les plus gros bénéfices */
        while( ! paires.isEmpty() && bestCycle != null ) {
            
            bestCycle = null;
            
            for (Paire paire : paires) {
            
                tempCycle = new LinkedList(); 
                
                if( (tempCycle = this.rechercheBestCycle(paire, maxTailleCycle, paires, tempCycle, paire) ) != null ) {
                    
                    tempBenef = Cycle.beneficeTotalCycle(tempCycle);
                
                    if( bestBenef < tempBenef ) {
                        bestCycle = tempCycle;
                        bestBenef = tempBenef;
                    }
                }
            }
            
            if( bestCycle != null && ! bestCycle.isEmpty() ) {
                if( s.ajouterNouveauCycle(bestCycle)) {
                    for (Paire paire : bestCycle) {
                            paires.remove(paire);
                    }
                }
            }
        }
        
        /* Plus longues chaines */
        for (Altruiste altruiste : instance.getAltruistes()) {
            s.ajouterAltruisteNouvelleChaine(altruiste);
            bestP = this.getPlusGrandBenefice(paires, altruiste);
            if( bestP != null ) {
                if(s.ajouterPaireChaineByAltruiste(altruiste, bestP) ) {
                    paires.remove(bestP);

                    while ( (bestP = this.getPlusGrandBenefice(paires, bestP)) != null ) {
                        if( !s.ajouterPaireChaineByAltruiste(altruiste, bestP)) {
                            break;
                        }
                        paires.remove(bestP);
                    }
                }
            }
        }
        
        /* Le reste des paires est ajouté dans des cycles */
        while( !paires.isEmpty() ) {
            
            bestP = paires.pop();
            
            if(!s.ajouterPaireCycle(bestP)) {
                s.ajouterPaireNouveauCycle(bestP);
            }
        }
        
        return s;
    }
    
    private boolean checkCycleValide(LinkedList<Paire> cycle) {
        if(null == cycle) return false;
        boolean checked = true;
        
        if(cycle.isEmpty()) 
            return checked;
        
        Paire first = cycle.getFirst(),
                last = cycle.getLast();
        
        if( !first.equals(last) && last.getBeneficeVers(first) == -1 ) {
            System.err.println("[Cycle] : le dernier n'est pas compatible avec le premier");
            checked = false;
        }
        
        Paire p = first;
        for (int i = 1; i < cycle.size(); i++) {
            Paire temp = cycle.get(i);
            if( p.getBeneficeVers(temp) == -1 ) {
                System.err.println("[Cycle] : deux paires ne sont pas compatibles");
                checked = false;
                break;
            }
            p = temp;
        }
        
        return checked;
    }
    
    private void printPaireTransplantation(Paire p) {
        LinkedList<Transplantation> lt = p.getTransplantations();
        System.out.print("Trans (" + p + ") : [");
        if(!lt.isEmpty()) {
            System.out.print(lt.pop().getBeneficiaire());
            for (Transplantation t2 : lt) {
                System.out.print(", "+t2.getBeneficiaire());
            }
        }
        System.out.println("]");
    }
    
    private void printCycle(LinkedList<Paire> cycle) {
        if(null == cycle || cycle.isEmpty()) return;
        LinkedList<Paire> c = new LinkedList(cycle);
        
        System.out.print("[" + c.pop().toString());
        for (Paire paire : c) {
            System.out.print(", " + paire);
        }
        System.out.println("]");
    }
    
    
    public static void main(String[] args) {
        // KEP_p9_n1_k3_l3
        try {
            InstanceReader read = new InstanceReader("instancesInitiales/KEP_p100_n11_k5_l17.txt");
            Instance i = read.readInstance();

            LargeCycles algoSimple = new LargeCycles();

            Solution simple = algoSimple.solve(i);

            //System.out.println("solution valide : " + simple.check());
            
            System.out.println(simple.toString());

        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
