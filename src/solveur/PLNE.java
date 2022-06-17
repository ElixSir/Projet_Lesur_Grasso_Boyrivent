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
    
    private void printPourcent(int cur, int fin) {
        float pourcent = cur / fin;
        String bar = "";
        for (int i = 0; i < 20; i++) {
            if( i < pourcent * 20 ) 
                bar += "#";
            else 
                bar += " ";
        }
        System.out.print("[" + bar + "] " + pourcent * 100 + "%\r");
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
        
        System.out.println("[PLNE]: " + instance.getNom());
        
        this.nbAltruistes = instance.getNbAltruistes();
        this.nbPaires = instance.getNbPaires();

        
        int tailleLimite = 5;
        
        int p = this.nbPaires;
        int n = this.nbAltruistes;
        int l = instance.getMaxChaines();
        int maxBe =0;
        int minBe =Integer.MAX_VALUE;        
        
        float sump = 0;
        float suma = 0;
        float sumbet = 0;
        float nbt = 0;
        float moyt = 0;
        int median =0 ;
        
        ArrayList<Integer> arrayT = new ArrayList();
        
        for (Paire paire : instance.getPaires()) {
            sump += paire.getTransplantations().size();
            for (Transplantation t : paire.getTransplantations()) {
                arrayT.add(t.getBenefice());
                sumbet += t.getBenefice();
                nbt++;
                if(maxBe < t.getBenefice())
                    maxBe = t.getBenefice();
                if(minBe > t.getBenefice())
                    minBe = t.getBenefice();
            }
        }
        
        for (Altruiste a : instance.getAltruistes()) {
            suma += a.getTransplantations().size();
            for (Transplantation t : a.getTransplantations()) {
                arrayT.add(t.getBenefice());
                sumbet += t.getBenefice();
                nbt++;
                if(maxBe < t.getBenefice())
                    maxBe = t.getBenefice();
                if(minBe > t.getBenefice())
                    minBe = t.getBenefice();
            }
        }
        
        Collections.sort(arrayT);
        int lenght = arrayT.size();
        if(lenght%2 ==1){
            median = arrayT.get(lenght/2);
        } else {
            median = (arrayT.get(lenght/2) + arrayT.get(lenght/2 - 1) )/2;
        }
        
        moyt=(sumbet/nbt);
        System.out.println("moy: "+(suma + sump)/(this.nbPaires + this.nbAltruistes));
        System.out.println("sump: "+sump/this.nbPaires);
        System.out.println("suma: "+suma/this.nbAltruistes);
        System.out.println("max: "+maxBe);
        System.out.println("min: "+minBe);
        System.out.println("moybet: "+moyt);
        System.out.println("median: "+median);
        
        
        int tailleLimiteCycle = 6, tailleLimiteChaine = 3,nbLimit = 3 * 1000000,nbEchanges =0,nbEchangesPrec =-1 ;
        RechercheRecursiveAllEchanges r;
        LinkedList<Echanges> resultats = new LinkedList<>();
        
        while (nbEchanges < nbLimit  && nbEchangesPrec != nbEchanges) {            
            r = new RechercheRecursiveAllEchanges(instance, tailleLimiteChaine, tailleLimiteCycle);
            
            resultats = r.recherche();
            System.out.println((tailleLimiteChaine) + " " + resultats.size() + " " );
            
            nbEchangesPrec = nbEchanges;
            // nbEchanges = this.echangesPossibles.size();
            nbEchanges = resultats.size();
            tailleLimiteChaine++;
            r.clear();
        }
        this.echangesPossibles = new ArrayList();
        
        getStats(resultats,tailleLimiteChaine, tailleLimiteCycle, instance,moyt,median);
        
        
        if( nbEchanges > 7*1000000 ) {
            for (Echanges res : resultats) {
                if( res instanceof Chaine && res.getBeneficeTotal() >= (median*(res.getSize()-1)))
                    this.echangesPossibles.add(res);
            }
        } else {
            this.echangesPossibles = new ArrayList<>(resultats);
        }
        resultats.clear();
        resultats = null;
        
        getStats(this.echangesPossibles,tailleLimiteChaine, tailleLimiteCycle, instance,moyt,median);
        
        // System.exit(0);
        try {
            long time = System.currentTimeMillis();
            this.buildModel();
            time = System.currentTimeMillis() - time;
            System.out.println("fin build (" + time + " s)");
            
            // iloCplex.exportModel("model_" + instance.getNom() + ".lp");
            
            iloCplex.setParam( IloCplex.DoubleParam.TimeLimit, 15 * 60 );
            
            if(iloCplex.solve()){
                /*
                System.out.println("ok");
                System.out.println(iloCplex.getStatus());
                System.out.println(iloCplex.getObjValue());
                System.out.println(iloCplex.getBestObjValue());
                */
                //System.out.println(iloCplex.getValue(var));
                
                // int nbParticipants = this.nbAltruistes + this.nbPaires;
                for (int i = 1; i < this.echangesPossibles.size(); i++) {
                    if(iloCplex.getValue(this.x[i]) >= 1)
                    {
                        Echanges e = this.echangesPossibles.get(i);
                        
                        if(s.ajouterNouvelEchange(e)) {
                        }
                    }
                }
                      
            }
        } catch (IloException ex) {
            Logger.getLogger(PLNE.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // System.out.println("[PLNE]: " + instance.getNom());
        this.iloCplex.end();
        this.iloCplex = null;
        this.echangesPossibles.clear();
        s.clean();
        
        int tot = 0;
        int totMoyT = 0;
        int totMedian = 0;
        
        for (Chaine c : s.getChaines()) {
            tot++;
            if(c.getSize() == instance.getMaxChaines() && (c.getBeneficeTotal()) < (instance.getNbPaires() + instance.getNbAltruistes())/2 ) {
                System.err.println(c.toString());
            }
            
            if(c.getBeneficeTotal() < (instance.getNbPaires() + instance.getNbAltruistes())/2) {
                // System.err.println("Général\t" +c.toString());
            }
            
            if(c.getBeneficeTotal() < ((instance.getNbPaires()*c.getSize())/tailleLimiteChaine)/2 ) {
                System.err.println("Taille\t"+c.toString());
            }
            
            if(c.getBeneficeTotal() < (moyt*c.getSize()) ) {
                // System.err.println("MoyT\t"+c.toString());
                totMoyT++;
            }
            
            if(c.getBeneficeTotal() < (median*(c.getSize()-1)) ) {
                System.err.println("median\t"+c.toString());
                totMedian++;
            }
        }
        if(totMoyT > 0)
            // System.err.println("totMoyT\t" + totMoyT + "/" + tot);
        
        if(totMedian > 0)
            System.err.println("totMedian\t" + totMedian + "/" + tot);
        
        
        
        /*
        
        for (Chaine chaine : s.getChaines()) {
            System.out.println(chaine.getBeneficeTotal() + "\t" + chaine.getSize() + "\t" + chaine.getBeneficeTotal()/chaine.getSize() + "\t" + chaine.getSize()/chaine.getBeneficeTotal());
        }
        
        for (Cycle cycle : s.getCycles()) {
            System.out.println(cycle.getBeneficeTotal() + "\t" + cycle.getSize() + "\t" + cycle.getBeneficeTotal()/ cycle.getSize() + "\t" + cycle.getSize()/cycle.getBeneficeTotal()); 
        }
        */
        return s;
    }

    private void getStats(List<Echanges> ech,int tailleLimiteChaine, int tailleLimiteCycle, Instance i, float moyt, int median) {
        int taille = Math.max(tailleLimiteChaine, tailleLimiteCycle);
        int[] sum = new int[taille];
        int[] sumInfMoit = new int[taille];
        int[] sumTaille = new int[taille];
        int[] sumInfmoy = new int[taille];
        int[] sumInfmed = new int[taille];
        
        for (Echanges e : ech) {
            sum[e.getSize()]++;
            if(e.getBeneficeTotal() < (i.getNbPaires() + i.getNbAltruistes())/2) {
                sumInfMoit[e.getSize()]++;
            }
            
            if(e.getBeneficeTotal() < ((i.getNbPaires()*e.getSize())/tailleLimiteChaine)/2 ) {
                sumTaille[e.getSize()]++;
            }
            if(e.getBeneficeTotal()< (e.getSize()*moyt)) {
                sumInfmoy[e.getSize()]++;
            }
            
            if(e.getBeneficeTotal()< ((e.getSize()-1)*median)) {
                sumInfmed[e.getSize()]++;
            }
        }
        
        String str = "taille\t|";
        
        for (int j = 2; j < sum.length; j++) {
            str += "\t" + j + "\t|";
        }
        
        System.out.println(str);
        
        str = "nb\t|";
        
        for (int j = 2; j < sum.length; j++) {
            str += "\t" + sum[j] + "\t|";
        }
        
        System.out.println(str);
        
        str = "nbInfMo\t|";
        
        for (int j = 2; j < sum.length; j++) {
            str += "\t" + sumInfMoit[j] + "\t|";
        }
        
        System.out.println(str);
        
        str = "nbTaill\t|";
        
        for (int j = 2; j < sum.length; j++) {
            str += "\t" + sumTaille[j] + "\t|";
        }
        
        System.out.println(str);
        
        
        str = "InfmoyT\t|";
        
        for (int j = 2; j < sum.length; j++) {
            str += "\t" + sumInfmoy[j] + "\t|";
        }
        
        System.out.println(str);
        
        str = "InfmedT\t|";
        
        for (int j = 2; j < sum.length; j++) {
            str += "\t" + sumInfmed[j] + "\t|";
        }
        
        System.out.println(str);
        
        
        
        
        /*
        */
        System.out.println("Fin de recherche des echanges possiblie pour une taille d'échange de " + (tailleLimiteChaine - 1) + ". "
                + this.echangesPossibles.size() + " trouvées");
        // System.out.println(this.echangesPossibles.size());
        
        // if(true) return s;
        System.out.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory());
        System.out.println("Free memory (%): " + ((double) Runtime.getRuntime().freeMemory() / (double) Runtime.getRuntime().maxMemory()) * 100 + "% - " + Runtime.getRuntime().freeMemory() / 1000000 + " " + Runtime.getRuntime().maxMemory() / 1000000);
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
            
            /*
            for (Chaine c : simple.getChaines()) {
                if(c.getSize() == 4) {
                    System.out.println(c.toString());
                }
            }
            */

            System.out.println("solution valide : " + simple.check());
            
            System.out.println(simple.toString());
            

        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
