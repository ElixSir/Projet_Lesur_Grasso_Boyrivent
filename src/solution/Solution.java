/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solution;

import ensemble.Chaine;
import ensemble.Cycle;
import instance.Instance;
import instance.reseau.Altruiste;
import instance.reseau.Paire;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.print.attribute.standard.MediaSize;



/**
 *
 * @author Bart
 */
public class Solution {

    private int beneficeTotal;
    private Instance instance;
    // private LinkedList<Chaine> chaines;
    
    private Map<Altruiste,Chaine> chaines;
    private LinkedList<Cycle> cycles;

    public Solution() {
        this.beneficeTotal = 0;
    }

    
    public Solution(Instance i) {
        this.beneficeTotal = 0;
        this.instance = i;
        chaines = new LinkedHashMap(); // a voir
        cycles = new LinkedList(); // a voir
    }
/*
    public Solution(Solution solution) {
        this.beneficeTotal = solution.beneficeTotal;
        this.instance = solution.instance;
        chaines = new LinkedList<Chaine>(); // a voir
        cycles = new LinkedList<Cycle>(); // a voir  
    }
*/
    public boolean ajouterAltruisteNouvelleChaine(Altruiste a) {
        if( null == a ) return false;
        
        Chaine c = new Chaine( this.instance, a );
        this.chaines.put(a, c);
        
        return true;
    }
    
    
    public boolean ajouterPaireNouveauCycle(Paire p){
        if(p == null) return false;
        
        Cycle c = new Cycle( this.instance );
        
        if(c.ajouterPaire(p)){
            this.cycles.add(c);
            
            return true;
        }
        
        return false;
    }
    
    public boolean ajouterPaireChaine(Paire p){
        if( null == p ) return false;
        
        int benefice = 0;
        
        for(Chaine c : this.chaines.values()){
            
            benefice = c.getBeneficeTotal();
            
            if(c.ajouterPaire(p)){
                
                this.beneficeTotal += c.getBeneficeTotal() - benefice;
                
                return true;
            }
        }

        return false;
    }
    
    public boolean ajouterPaireCycle(Paire p){
        if(null == p) return false;
        
        int benefice = 0;

        for(Cycle c : this.cycles){
            
            benefice = c.getBeneficeTotal();
            
            if(c.ajouterPaire(p)){
                
                this.beneficeTotal += c.getBeneficeTotal() - benefice;
                
                return true;
            }
        }

        return false;
    }
    
    public boolean ajouterPaireLastCycle(Paire p) {
        if(null == p || this.cycles.isEmpty()) return false;
        
        int benefice = 0;
        
        Cycle c = this.cycles.getLast();
        
        benefice = c.getBeneficeTotal();
        
        if(c.ajouterPaire(p)) {
            
            this.beneficeTotal += c.getBeneficeTotal() - benefice;
            
            return true;
        }

        return false;
    }
    
    public boolean ajouterPaireChaineByAltruiste(Altruiste a,Paire p) {
        if(null == p || null == a) return false;
        if( !this.chaines.containsKey(a) ) return false;
        
        Chaine c = this.chaines.get(a);
        int benefice = c.getBeneficeTotal();
        
        if(c.ajouterPaire(p)) {
            this.beneficeTotal += c.getBeneficeTotal() - benefice;
            return true;
        }
        
        return false;
    }
    
    public boolean ajouterNouveauCycle(LinkedList<Paire> cycle) {
        if( !Cycle.checkCycleValide(cycle) ) return false;
        
        Cycle c = new Cycle(instance, cycle);
        
        this.cycles.add(c);
        
        this.beneficeTotal += Cycle.beneficeTotalCycle(cycle);
                
        return true;
    }
    
    public void clean() {
        
        LinkedList<Altruiste> altruisteSupp = new LinkedList<>();
        LinkedList<Cycle> cycleSupp = new LinkedList<>();
        
        
        for (Altruiste a : this.chaines.keySet()) {
            Chaine c = this.chaines.get(a);
            if(c.getSize() <= 1) 
                //this.chaines.remove(a);
                altruisteSupp.add(a);
        }
        
        for (Cycle c : this.cycles) {
            if(c.getSize() <= 1) 
                //this.cycles.remove(c);
                cycleSupp.add(c);
        }
        
        
        for(Altruiste a: altruisteSupp){
            this.chaines.remove(a);
        }
        
        for(Cycle c: cycleSupp){
            this.cycles.remove(c);
        }
        
        
    }
    
    public int getBeneficeTotal() {
        return beneficeTotal;
    }

    public Instance getInstance() {
        return instance;
    }
    
    @Override
    public String toString() {
        return "Solution" 
                + "\n\tbeneficeTotal=" + this.beneficeTotal 
                + "\n\tinstance=" + instance 
                + "\n\tchaines=" + chaines 
                + "\n\tcycles=" + cycles + '}';
    }

    public void printSolution(PrintWriter ecriture) {
        // Cout total de la solution
        ecriture.println("// Benefice total de la solution");
        ecriture.println(this.beneficeTotal);
        // Description de la solution
        // Cycles
        ecriture.println("// Description de la solution");
        ecriture.println("// Cycles");
        printCycles(ecriture); //Affiches les donneurs des différents cycles
        ecriture.print("\n");
        // Chaines
        ecriture.print("// Chaines");
        printChaines(ecriture); //Affiches les donneurs des différentes chaines
    }
    
    /**
     * //Affiches les donneurs des différents cycles de la solution
     *
     * @param sol
     * @param ecriture
     */
    private void printCycles(PrintWriter ecriture) {
        if (this.cycles != null) {

            for (int i = 0; i < cycles.size(); i++) {
                Cycle cycle = cycles.get(i);
                cycle.printCycle(ecriture);
                
            }
        }

    }
    
    /**
     * //Affiches les donneurs des différentes chaines de la solution
     *
     * @param sol
     * @param ecriture
     */
    private void printChaines(PrintWriter ecriture) {
        ecriture.print("\n");
        if (this.chaines != null) {
            
            for (Chaine chaine : this.chaines.values()) {
                chaine.printChaine(ecriture);
            }
        }

    }
      
    /**
     * Checker de la class Solution
     *
     * @return
     *
     * A modifier
     */
    public boolean check() {
        boolean checker = true;
        int nbTrouneeNotCheck = this.nbEchangesNotChecked();
        int coutTotalSolution = beneficeTotalSolution();
        
        if( nbTrouneeNotCheck != 0 ) {
            System.err.println("[CHECK - Solution] : " + nbTrouneeNotCheck + " échnages(s) non valides");
            checker = false;
        }
        
        if(coutTotalSolution != this.beneficeTotal) {
            System.err.println("[CHECK - Solution] : Le bénéfice totale de la solution n'est pas correcte : ( classe : " + this.beneficeTotal + ", calculé : " +  coutTotalSolution + " )");
            checker = false;
        }
        
        checker = checker && uniqParticipantInEchanges();
        
        return checker;
    }
    
    private int nbEchangesNotChecked() {
        int res = 0;
        
        for (Cycle cycle : this.cycles) {
            if(!cycle.check()) res++;
        }
        
        for (Chaine c : this.chaines.values()) {
            if(!c.check()) res++;
        }
        
        return res;
    }
    
    private int beneficeTotalSolution() {
        int benef = 0;
        
        for (Cycle cycle : this.cycles) {
            benef += cycle.getBeneficeTotal();
        }
        
        for (Chaine c : this.chaines.values()) {
            benef += c.getBeneficeTotal();
        }
        
        return benef;
    }
    
    private boolean uniqParticipantInEchanges() {
        boolean checker = true;
        
        List<Altruiste> altruistes = this.instance.getAltruistes();
        List<Paire> paires = this.instance.getPaires();
        
        for (Chaine c : this.chaines.values()) {
            if (! altruistes.remove(c.getAltruiste())){
                System.err.println("[CHECK - Solution] : L'altruiste est déjà présent dans la chaine :\n\t" + c.getAltruiste().toString() );
                checker = false;
            }
        }
        
        /*
        if(! altruistes.isEmpty()) {
            System.err.println("[CHECK - Solution] : Tout les altruistes ne sont pas présents dans la solution : " + altruistes.size() );
            checker = false;
        }
        */
        
        for (Chaine c : this.chaines.values()) {
            for (Paire paire : c.getPaires()) {
                if (! paires.remove(paire)){
                    System.err.println("[CHECK - Solution] : La paire est déjà présente dans la chaine :\n\t" + paire );
                    checker = false;
                }
            }
        }
        
        for (Cycle cycle : this.cycles) {
            for (Paire paire : cycle.getPaires()) {
                if (! paires.remove(paire)){
                    System.err.println("[CHECK - Solution] : La paire est déjà présente dans le cyle :\n\t" + paire );
                    checker = false;
                }
            }
        }
        
        /*
        if(! paires.isEmpty()) {
            System.err.println("[CHECK - Solution] : Toutes les paires ne sont pas présentes dans la solution : " + paires.size() );
            checker = false;
        }
        */
        
        return checker;
    }
}
