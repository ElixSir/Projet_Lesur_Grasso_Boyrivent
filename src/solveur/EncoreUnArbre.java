/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solveur;

import solution.ensemble.Chaine;
import solution.ensemble.Cycle;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import instance.Instance;
import instance.reseau.Altruiste;
import instance.reseau.Paire;
import instance.reseau.Participant;
import instance.reseau.Transplantation;
import io.InstanceReader;
import io.exception.ReaderException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import solution.Solution;

/**
 *
 * @author Clem
 */
public class EncoreUnArbre implements Solveur{
    
    private IloCplex iloCplex;
    private IloIntVar[] x;
    private LinkedList<Paire> paires;
    private LinkedList<Altruiste> altruistes;
    private LinkedList<LinkedList<Participant>> echangesPossibles;
    private int nbAltruistes;
    private int nbPaires;

    @Override
    public String getNom() {
        return "Eh oui un arbre";
    }
    
    private void contrainte1() throws IloException {
        LinkedList<Integer> echangesPaire;
        LinkedList<Participant> temp;
        
        for (Paire p : this.paires) {
            echangesPaire = new LinkedList();
            
            for (int i = 0; i < this.echangesPossibles.size(); i++) {
                temp = this.echangesPossibles.get(i);
                if( temp.contains(p) ) {
                    echangesPaire.add(i);
                }
            }

            
            IloLinearNumExpr expr = this.iloCplex.linearNumExpr();
            for (Integer i : echangesPaire) {
                expr.addTerm(x[i], 1);
            }
            
            this.iloCplex.addLe(expr, 1);
        }
    }
    
    private void contrainte2() throws IloException {
        LinkedList<Integer> chainesDonneurAltruiste;
        LinkedList<Participant> temp;
        
        for (Altruiste a : this.altruistes) {
            chainesDonneurAltruiste = new LinkedList();
            
            for (int i = 0; i < this.echangesPossibles.size(); i++) {
                temp = this.echangesPossibles.get(i);
                Participant tempFirstE = temp.getFirst();
                if( tempFirstE instanceof Altruiste && tempFirstE.equals(a) ) {
                    chainesDonneurAltruiste.add(i);
                }
            }
            
            IloLinearNumExpr expr = this.iloCplex.linearNumExpr();
            for (Integer i : chainesDonneurAltruiste) {
                expr.addTerm(x[i], 1);
            }
            
            this.iloCplex.addLe(expr, 1);
        }
    }
    
    private void objective() throws IloException {
        IloLinearNumExpr expr = this.iloCplex.linearNumExpr();
        int benefice;
        
        for (int i = 0; i < this.echangesPossibles.size(); i++) {
            LinkedList<Participant> e = this.echangesPossibles.get(i);
            benefice = this.getbenefEchange(e);
            expr.addTerm(x[i], benefice);
        }
        this.iloCplex.addMaximize(expr);
    }
    
    private void defXVarDecision() throws IloException {
        int nbEchanges = this.echangesPossibles.size();
        
        this.x = new IloIntVar[nbEchanges];
        
        for (int i = 0; i < nbEchanges; ++i) {
            x[i] = iloCplex.boolVar("x" + i );
        }
    }
    
    private void buildModel() throws IloException {
        this.iloCplex = new IloCplex();
        this.defXVarDecision();
        // System.out.println("Fin X var");
        this.objective();
        // System.out.println("Fin objective");
        this.contrainte1();
        // System.out.println("Fin contrainte 1");
        this.contrainte2();
        // System.out.println("Fin contrainte 2");
    }

    @Override
    public Solution solve(Instance instance) {
        
        Solution s = new Solution(instance);
        
        this.altruistes = instance.getAltruistes();
        this.paires = instance.getPaires();

        int tailleLimite = 5;

        RechercheRecursiveAllEchanges r = new RechercheRecursiveAllEchanges(instance, tailleLimite);

        this.echangesPossibles = r.recherche();
        
        /*
        */
        System.out.println("Fin de recherche des echanges possiblie pour une taille d'échange de " + tailleLimite + ". " 
                + this.echangesPossibles.size() + " trouvées");
        
        try {
            this.buildModel();
            System.out.println("fin build");
            
            iloCplex.exportModel("model_" + instance.getNom() + ".lp");
            iloCplex.setParam(IloCplex.DoubleParam.TimeLimit, 60);
            
            if(iloCplex.solve()){
                System.out.println("ok");
                System.out.println(iloCplex.getStatus());
                System.out.println(iloCplex.getObjValue());
                System.out.println(iloCplex.getBestObjValue());
                //System.out.println(iloCplex.getValue(var));
                
                int nbParticipants = this.nbAltruistes + this.nbPaires;
                for (int i = 1; i < this.echangesPossibles.size(); i++) {
                    if(iloCplex.getValue(this.x[i]) >= 1)
                    {
                        LinkedList<Participant> e = new LinkedList(this.echangesPossibles.get(i));
                        
                        if( e.getFirst() instanceof Altruiste) {
                            Altruiste a = (Altruiste) e.pop();
                            
                            s.ajouterAltruisteNouvelleChaine(a);
                            
                            for (Participant participant : e) {
                                s.ajouterPaireChaineByAltruiste(a, (Paire)participant);
                            }
                        } else {
                            LinkedList<Paire> ep = new LinkedList(e);
                            s.ajouterNouveauCycle(ep);
                        }
                    }
                }
                      
            }
        } catch (IloException ex) {
            Logger.getLogger(EncoreUnArbre.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        s.clean();
        return s;
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
