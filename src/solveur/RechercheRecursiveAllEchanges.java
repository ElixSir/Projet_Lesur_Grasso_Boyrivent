/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solveur;

import instance.reseau.Participant;
import java.util.LinkedList;
import instance.Instance;
import instance.reseau.Altruiste;
import instance.reseau.Paire;
import instance.reseau.Transplantation;
import solution.ensemble.Cycle;
import solution.ensemble.Echanges;
import solution.ensemble.Chaine;

/**
 *
 * @author Clem
 */
public class RechercheRecursiveAllEchanges {
        
    public int maxTailleChaine;
    public int maxTailleCycle;
    
    public final LinkedList<Participant> participants;
    public final Instance instance;
    
    public RechercheRecursiveAllEchanges(Instance i) {
        
        this.maxTailleChaine = i.getMaxChaines();
        this.maxTailleCycle = i.getMaxCycles();
        
        this.participants = new LinkedList(i.getPaires());
        this.participants.addAll(i.getAltruistes());
        this.instance = i;
    }
    
    public RechercheRecursiveAllEchanges(Instance i, int tailleLimiteChaine, int tailleLimiteCycle) {
        this(i);
        
        this.maxTailleChaine = (i.getMaxChaines() > tailleLimiteChaine )? tailleLimiteChaine: i.getMaxChaines() ;
        this.maxTailleCycle = (i.getMaxCycles() > tailleLimiteCycle)? tailleLimiteCycle: i.getMaxCycles();
    }
    
    public RechercheRecursiveAllEchanges(Instance i, int tailleLimite) {
        this(i,tailleLimite,tailleLimite);
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
    
    
    public LinkedList<Echanges> recherche() {
        LinkedList<Echanges> echangesPossibles = new LinkedList<>();
        LinkedList<LinkedList<Participant>> recherche;
        
        LinkedList<Participant> temp;
        
        for (Participant p : participants) {

            temp = new LinkedList<>();
            recherche = new LinkedList<>();
            
            if( p instanceof Paire ) {
                this.rechercheCycle((Paire)p, maxTailleCycle, recherche, temp, p);
                
                for (LinkedList<Participant> e : recherche) {
                    LinkedList<Paire> ep = new LinkedList(e);
                    echangesPossibles.add( new Cycle(this.instance, ep ));
                }
            } else if( p instanceof Altruiste ) {
                Altruiste a = (Altruiste)p;
                this.rechercheChaine(a, maxTailleChaine, recherche, temp, p);
                
                for (LinkedList<Participant> e : recherche) {
                    Chaine c = new Chaine(this.instance, (Altruiste) e.pop());
                    
                    for (Participant paire : e) {
                        c.ajouterPaire((Paire)paire);
                    }
                    
                    echangesPossibles.add(c);
                }
            }
        }
        
        return echangesPossibles;
    }
}
