/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solveur;

import solution.ensemble.Chaine;
import solution.ensemble.Cycle;
import instance.Instance;
import instance.reseau.Altruiste;
import instance.reseau.Paire;
import instance.reseau.Participant;
import instance.reseau.Transplantation;
import io.InstanceReader;
import io.exception.ReaderException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import solution.Solution;

/**
 *
 * @author Clem
 */
public class ArbreSolveur implements Solveur{

    @Override
    public String getNom() {
        return "Arbre Solveur";
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
            }
        }
        
        return c;
    }
    
    private LinkedList<Participant> rechercheBestChaine(Altruiste altruiste,int maxtailleChaine, LinkedList<Paire> availabe, LinkedList<Participant> chaine, Participant next) {
        
        chaine.add(next);
        // this.printCycle(cycle);
        /*
        if(next.getBeneficeVers(paire) >= 0) {
            return new LinkedList(chaine);
        }
        */
        
        if(chaine.size() >= maxtailleChaine) return new LinkedList(chaine); 
       
        LinkedList<Participant> temp = null;
        LinkedList<Participant> c = null;
        int benefChaine = 0,
                benefTemp = 0;
        
        //this.printPaireTransplantation(next);
        
        for(Transplantation t: next.getTransplantations()) {
            Paire p = t.getBeneficiaire();

            if(!chaine.contains(p) && availabe.contains(p) ) {
                temp = this.rechercheBestChaine(altruiste, maxtailleChaine, availabe, chaine, p);

                if( null != temp ){
                    /*
                    System.out.print("(" + Chaine.beneficeTotalChaine(temp) + ") : ");
                    this.printChaine(temp);
                    */
                    benefTemp = Chaine.beneficeTotalChaine(temp);
                    benefChaine = Chaine.beneficeTotalChaine(c);
                    if( benefChaine < benefTemp ) {
                        c = new LinkedList<>(temp);
                        benefChaine = benefTemp;
                    }
                }
                chaine.remove(p);
                
                // if(chaine.size() >= maxtailleChaine) return null;
            }
        }
        
        return c;
    }

    @Override
    public Solution solve(Instance instance) {
        
        Solution s = new Solution(instance);
        
        LinkedList<Altruiste> altruistes = instance.getAltruistes();
        LinkedList<Paire> pairesAvailable = instance.getPaires();
        
        int limiteTaille = 5;
        // int minTailleChaineCycle = (instance.getMaxChaines() > instance.getMaxCycles() )? instance.getMaxCycles() : instance.getMaxChaines();
        
        int tailleChaineLimite = limiteTaille;
        int tailleCycleLimite = limiteTaille;
        
        int maxTailleChaine = (instance.getMaxChaines() > tailleChaineLimite )? tailleChaineLimite: instance.getMaxChaines() ;
        int maxTailleCycle = (instance.getMaxCycles() > tailleCycleLimite)? tailleCycleLimite: instance.getMaxCycles();
        
        /*
        System.out.println("1 - taille Cycle : " + maxTailleCycle);
        System.out.println("1 - taille Chaine : " + maxTailleChaine);
        */
        
        LinkedList<Participant> participants = new LinkedList<>(pairesAvailable);
        participants.addAll(altruistes);
        
        int bestBenef = 0, tempBenef;
        LinkedList<Participant> bestEchanges = new LinkedList(), tempChaine;
        LinkedList<Paire> tempCycle;
        
        /* Cycles avec les plus gros bénéfices */
        while( ! pairesAvailable.isEmpty() && bestEchanges != null ) {
            
            bestEchanges = null;
            bestBenef = 0;
            
            for (Participant participant : participants) {
                
                if(participant instanceof Paire ) {
                    
                    Paire paire = (Paire) participant;
                    
                    tempCycle = new LinkedList<>();
                    
                    if( (tempCycle = this.rechercheBestCycle(paire, maxTailleCycle, new LinkedList<>(pairesAvailable), tempCycle, paire) ) != null ) {

                        tempBenef = Cycle.beneficeTotalCycle(tempCycle);

                        if( bestBenef < tempBenef ) {

                            bestEchanges = new LinkedList<>(tempCycle);
                            bestBenef = tempBenef;
                        }
                    }
                    
                } else if (participant instanceof Altruiste) {
                    
                    Altruiste altruiste = (Altruiste)participant;
                    tempChaine = new LinkedList<>();
                    tempChaine = this.rechercheBestChaine(altruiste, maxTailleChaine, pairesAvailable, tempChaine, altruiste);

                    if(tempChaine != null) {
                        tempBenef = Chaine.beneficeTotalChaine(tempChaine);
                        bestBenef = Chaine.beneficeTotalChaine(bestEchanges);

                        if(bestBenef < tempBenef){
                            bestEchanges = new LinkedList<>(tempChaine);
                            bestBenef = tempBenef;
                        }
                    }
                }
            }

            
            if( bestEchanges != null && ! bestEchanges.isEmpty() ) {
                
                Participant p = bestEchanges.getFirst();
                
                if(p instanceof Paire) {
                    LinkedList<Paire> bestCycle = new LinkedList<>();
                    
                    for (Participant part : bestEchanges) {
                        bestCycle.add((Paire)part);
                    }
                    
                    if(s.ajouterNouveauCycle(bestCycle)) {                        
                        pairesAvailable.removeAll(bestCycle);
                        participants.removeAll(bestEchanges);
                    }
                } else if(p instanceof Altruiste) {
                    
                    Altruiste a = (Altruiste)p;
                    
                    if(s.ajouterAltruisteNouvelleChaine(a)) {
                        participants.remove(a);
                        
                        for (int i = 1; i < bestEchanges.size(); i++) {
                            Paire paireBestCycle = (Paire)bestEchanges.get(i);
                            s.ajouterPaireChaineByAltruiste(a, paireBestCycle);
                            pairesAvailable.remove(paireBestCycle);
                            participants.remove(paireBestCycle);
                        }
                    }
                }
            }
        }
        
        // System.out.println(bestEchanges);
        
        for (Paire paire : pairesAvailable) {
            tempCycle = new LinkedList<>();
            if( this.rechercheFirstCycle(paire, maxTailleCycle, tempCycle, paire) ) {
                // System.out.println( participants.contains(paire));
                // this.printCycle(tempCycle);
            }
        }
        
        /*
        
        System.out.println("paires restantes : " + pairesAvailable.size() );
        System.out.println("altruistes restants : " + (participants.size() - pairesAvailable.size()));
        //System.out.println("Solution : " + s);
        
        */
        return s;
    }
    
    private void printPaireTransplantation(Participant p) {
        LinkedList<Transplantation> lt = p.getTransplantations();
        System.out.print("Trans (" + p + ") : [");
        if(!lt.isEmpty()) {
            Transplantation t = lt.pop();
            System.out.print(t.getBeneficiaire()+"("+t.getBenefice()+")");
            for (Transplantation t2 : lt) {
                System.out.print(", "+t2.getBeneficiaire()+"("+t2.getBenefice()+")");
            }
        }
        System.out.println("]");
    }
    
    private void printChaine(LinkedList<Participant> chaine) {
        if(null == chaine || chaine.isEmpty()) return;
        LinkedList<Participant> c = new LinkedList(chaine);
        
        System.out.print("[" + c.pop().toString());
        for (Participant paire : c) {
            System.out.print(", " + paire);
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
        // KEP_p100_n11_k5_l17
        try {
            InstanceReader read = new InstanceReader("instancesInitiales/KEP_p100_n11_k5_l17.txt");
            Instance i = read.readInstance();

            ArbreSolveur algoSimple = new ArbreSolveur();

            Solution simple = algoSimple.solve(i);

            System.out.println("solution valide : " + simple.check());
            
            System.out.println(simple.toString());

        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
