/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solveur;

import ensemble.Chaine;
import ensemble.Cycle;
import instance.Instance;
import instance.reseau.Altruiste;
import instance.reseau.Paire;
import instance.reseau.Participant;
import instance.reseau.Transplantation;
import io.InstanceReader;
import io.exception.ReaderException;
import java.util.LinkedList;
import solution.Solution;

/**
 *
 * @author Clem
 */
public class EncoreUnArbre implements Solveur{

    @Override
    public String getNom() {
        return "Eh oui un arbre";
    }
    
    private void rechercheCycle(Paire paire,int maxtailleCycle, LinkedList<LinkedList<Participant>> echangesPossibles, LinkedList<Participant> cycle, Participant next) {
        
        cycle.add(next);
        // this.printCycle(cycle);
        
        if(next.getBeneficeVers(paire) >= 0) {
            echangesPossibles.add(new LinkedList(cycle));
        }
        
        if(cycle.size() >= maxtailleCycle) return;
        
        for(Transplantation t: next.getTransplantations()) {
            Paire p = t.getBeneficiaire();

            if( !cycle.contains(p) ) {
                this.rechercheCycle(paire, maxtailleCycle, echangesPossibles, cycle, p);

                cycle.remove(p);
            }
        }
    }
    
    private void rechercheChaine(Altruiste altruiste, int maxtailleChaine, LinkedList<LinkedList<Participant>> echangesPossibles, LinkedList<Participant> chaine, Participant next) {
        
        chaine.add(next);
        
        if(chaine.size() >= maxtailleChaine) {
            echangesPossibles.add(new LinkedList<>(chaine) );
            return;
        } else if( chaine.size() >= 2 ){
            echangesPossibles.add(new LinkedList<>(chaine) );
        }
        
        for(Transplantation t: next.getTransplantations()) {
            Paire p = t.getBeneficiaire();

            if(!chaine.contains(p) ) {
                this.rechercheChaine(altruiste, maxtailleChaine, echangesPossibles, chaine, p);

                chaine.remove(p);
            }
        }
    }
    
    private int getbenefEchange(LinkedList<Participant> ech) {
        
        LinkedList<Participant> echanges = new LinkedList<>(ech);
        
        if( echanges.isEmpty() ) return 0;
        
        Participant p = echanges.getFirst();
        
        if( p instanceof Paire ) {
            LinkedList<Paire> cycle = new LinkedList(echanges);
            return Cycle.beneficeTotalCycle(cycle);
        } else if ( p instanceof Altruiste ) {
            return Chaine.beneficeTotalChaine(echanges);
        }
        
        return 0;
    }
    
    private int getBenef(LinkedList<LinkedList<Participant>> ech) {
        int res = 0;
        
        for (LinkedList<Participant> e : ech) {
            res += this.getbenefEchange(e);
        }
        
        return res;
    }
    
    private class E {
        
        private LinkedList<Participant> echange;
        private LinkedList<LinkedList<Participant>> echangePossible;
        
        public E(LinkedList<Participant> echange, LinkedList<LinkedList<Participant>> allEchanges) {
            this.echange = echange;
            this.echangePossible = new LinkedList<>();
            this.ajouterEchangesPossible(allEchanges);
        }
        
        private void ajouterEchangesPossible(LinkedList<LinkedList<Participant>> all) {
            
            for (LinkedList<Participant> e : all) {
                for (Participant participant : e) {
                    if( !this.echange.contains(participant) ) {
                        this.echangePossible.add(new LinkedList(e));
                    }
                }
            }
        }
        
        public LinkedList<LinkedList<Participant>> getEchangesCompatible() {
            return new LinkedList(echangePossible);
        }
        
        public int nbEchangesCompatible() {
            return this.echange.size();
        }
    }
    
    
    private LinkedList<LinkedList<Participant>> bestEchangesValable(LinkedList<LinkedList<Participant>> ech) {
        LinkedList<LinkedList<Participant>> echangesBrut = new LinkedList<>(ech);
        LinkedList<E> echanges = new LinkedList<>();
        
        LinkedList<LinkedList<Participant>> bestEchangesValable = new LinkedList<>();
        
        for (LinkedList<Participant> echangeBrut : echangesBrut) {
            E e = new E(echangeBrut,echangesBrut);
            // System.out.println(":" + e.nbEchangesCompatible());
            echanges.add(e);
        }
        
        return bestEchangesValable;
    }

    @Override
    public Solution solve(Instance instance) {
        Solution s = new Solution(instance);
        
        LinkedList<Paire> paires = instance.getPaires();
        LinkedList<Altruiste> altruistes = instance.getAltruistes();
        
        int limiteTaille = 5;
        
        int tailleChaineLimite = limiteTaille;
        int tailleCycleLimite = limiteTaille;
        
        int maxTailleChaine = (instance.getMaxChaines() > tailleChaineLimite )? tailleChaineLimite: instance.getMaxChaines() ;
        int maxTailleCycle = (instance.getMaxCycles() > tailleCycleLimite)? tailleCycleLimite: instance.getMaxCycles();
        
        LinkedList<Participant> participants = new LinkedList<>(paires);
        participants.addAll(altruistes);
        
        LinkedList<LinkedList<Participant>> echangesPossibles = new LinkedList<>();
        
        LinkedList<Participant> temp;
        
        for (Participant p : participants) {

            temp = new LinkedList<>();
            
            if( p instanceof Paire ) {
                this.rechercheCycle((Paire)p, maxTailleCycle, echangesPossibles, temp, p);
            } else if( p instanceof Altruiste ) {
                Altruiste a = (Altruiste)p;
                this.rechercheChaine(a, maxTailleChaine, echangesPossibles, temp, p);
            }
        }
        
        s.clean();
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
        // KEP_p9_n1_k3_l3
        // KEP_p100_n11_k5_l17
        // KEP_p50_n6_k5_l17
        try {
            InstanceReader read = new InstanceReader("instancesInitiales/KEP_p100_n11_k5_l17.txt");
            Instance i = read.readInstance();

            EncoreUnArbre algoSimple = new EncoreUnArbre();

            Solution simple = algoSimple.solve(i);

            System.out.println("solution valide : " + simple.check());
            
            System.out.println(simple.toString());

        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
