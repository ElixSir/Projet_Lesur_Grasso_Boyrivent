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
import ilog.cplex.IloCplex;
import instance.Instance;
import instance.reseau.Altruiste;
import instance.reseau.Paire;
import instance.reseau.Participant;
import instance.reseau.Transplantation;
import io.InstanceReader;
import io.exception.ReaderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import solution.Solution;
import solution.ensemble.Echanges;

/**
 *
 * @author Clem
 */
public class PLNE implements Solveur{
    
    private IloCplex iloCplex;
    private IloIntVar[] x;
    private List<Echanges> echangesPossibles;
    private int nbAltruistes;
    private int nbPaires;

    @Override
    public String getNom() {
        return "PLNE";
    }
    
    private void contrainte1() throws IloException {
        
        int size = this.nbAltruistes + this.nbPaires;
        IloLinearNumExpr[] exprs = new IloLinearNumExpr[size];
        
        for (int i = 0; i < size; i++) {
            exprs[i] = this.iloCplex.linearNumExpr();
        }

        for (int i = 0; i < this.echangesPossibles.size(); i++) {
            Echanges e = this.echangesPossibles.get(i);
            
            if( e instanceof Chaine ) {
                int index = ((Chaine) e).getAltruiste().getId() - 1;
                exprs[index].addTerm(x[i], 1);
            } 
        
            for (Paire p : e.getPaires()) {
                int j = p.getId() - 1;
                
                exprs[j].addTerm(x[i], 1);
            }
        }
        for (IloLinearNumExpr expr : exprs) {
            this.iloCplex.addLe(expr, 1);
        }
    }
    
    private void objective() throws IloException {
        IloLinearNumExpr expr = this.iloCplex.linearNumExpr();
        int benefice;
        
        for (int i = 0; i < this.echangesPossibles.size(); i++) {
            Echanges e = this.echangesPossibles.get(i);
            benefice = e.getBeneficeTotal();
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
        this.objective();
        this.contrainte1();
    }

    @Override
    public Solution solve(Instance instance) {
        System.out.println("Free memory (%): " +  ((double)Runtime.getRuntime().freeMemory()/ (double)Runtime.getRuntime().maxMemory())*100 + "%");

        
        Solution s = new Solution(instance);
        
        this.nbAltruistes = instance.getNbAltruistes();
        this.nbPaires = instance.getNbPaires();
        
        int median =0 ;
        
        ArrayList<Integer> arrayT = new ArrayList();
        
        for (Paire paire : instance.getPaires()) {
            for (Transplantation t : paire.getTransplantations()) {
                arrayT.add(t.getBenefice());
            }
        }
        
        for (Altruiste a : instance.getAltruistes()) {
            for (Transplantation t : a.getTransplantations()) {
                arrayT.add(t.getBenefice());
            }
        }
        
        Collections.sort(arrayT);
        int lenght = arrayT.size();
        if(lenght%2 ==1){
            median = arrayT.get(lenght/2);
        } else {
            median = (arrayT.get(lenght/2) + arrayT.get(lenght/2 - 1) )/2;
        }
        
        
        int tailleLimiteCycle = 6, tailleLimiteChaine = 3,nbLimit = 3 * 1000000,nbEchanges =0,nbEchangesPrec =-1 ;
        RechercheRecursiveAllEchanges r;
        LinkedList<Echanges> resultats = new LinkedList<>();
        
        while (nbEchanges < nbLimit  && nbEchangesPrec != nbEchanges) {            
            r = new RechercheRecursiveAllEchanges(instance, tailleLimiteChaine, tailleLimiteCycle);
            
            resultats = r.recherche();
            
            nbEchangesPrec = nbEchanges;
            nbEchanges = resultats.size();
            tailleLimiteChaine++;
            r.clear();
        }
        
        this.echangesPossibles = new ArrayList();
        
        if( nbEchanges > 7*1000000 ) {
            for (Echanges res : resultats) {
                if( res instanceof Chaine && res.getBeneficeTotal() >= (median*(res.getSize()-1)))
                    this.echangesPossibles.add(res);
            }
        } else {
            this.echangesPossibles.addAll(resultats);
        }
        resultats.clear();
        resultats = null;
        
        try {
            this.buildModel();
            
            iloCplex.setParam( IloCplex.DoubleParam.TimeLimit, 15 * 60 );
            
            if(iloCplex.solve()){
                
                for (int i = 1; i < this.echangesPossibles.size(); i++) {
                    if(iloCplex.getValue(this.x[i]) >= 1)
                    {
                        Echanges e = this.echangesPossibles.get(i);
                        
                        s.ajouterNouvelEchange(e);
                    }
                }
                      
            }
        } catch (IloException ex) {
            Logger.getLogger(PLNE.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /* Clear & Clean */
        this.iloCplex.end();
        this.iloCplex = null;
        this.echangesPossibles.clear();
        s.clean();
        
        return s;
    }

    public static void main(String[] args) {
        // KEP_p9_n1_k3_l3
        // KEP_p9_n1_k3_l3
        // KEP_p100_n11_k5_l17
        // KEP_p50_n6_k5_l17
        // KEP_p100_n11_k3_l7
        // KEP_p250_n13_k3_l7
        
        // KEP_p250_n28_k3_l13
        // KEP_p250_n13_k5_l17
        // KEP_p250_n28_k3_l13
        // KEP_p250_n83_k3_l7
        // KEP_p250_n83_k3_l4
        // KEP_p250_n83_k5_l17
        
        // KEP_p250_n83_k3_l13 2577933840
        try {
            // InstanceReader read = new InstanceReader("instancesFinales/KEP_p250_n83_k5_l17.txt");
            // InstanceReader read = new InstanceReader("instancesFinales1/KEP_p250_n28_k3_l13.txt");
            InstanceReader read = new InstanceReader("instancesInitiales/KEP_p9_n0_k3_l0.txt");
            
            Instance i = read.readInstance();
            
            PLNE algoSimple = new PLNE();
            
            Solution simple = algoSimple.solve(i);

            System.out.println("solution valide : " + simple.check());
            
            System.out.println(simple.toString());

        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
