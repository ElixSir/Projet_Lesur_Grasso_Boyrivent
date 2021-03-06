/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solution;

import solution.ensemble.Chaine;
import solution.ensemble.Cycle;
import solution.ensemble.Echanges;
import instance.Instance;
import instance.reseau.Altruiste;
import instance.reseau.Paire;
import instance.reseau.Participant;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import operateur.OperateurInterEchange;
import operateur.OperateurIntraEchange;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;



/**
 *
 * @author Bart
 */
public class Solution {

    private int beneficeTotal;
    private Instance instance;
    
    private Map<Altruiste,Chaine> chaines;
    private LinkedList<Cycle> cycles;
    private LinkedList<Echanges> echanges;

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
    
    public boolean ajouterNouvelEchange(Echanges e) {
        if( null == e ) {
            System.out.println("[SOLUTION] null");
            return false;
        }
        
        if(e instanceof Chaine) {
            Chaine c = (Chaine) e;
            
            if(this.chaines.containsKey(c.getAltruiste()))
                System.out.println("[SOLUTION] double chaines");
                
            this.chaines.put(c.getAltruiste(), c);
        } else if (e instanceof Cycle) {
            this.cycles.add((Cycle)e);
        } else {
            System.out.println("[SOLUTION] aucun type");
        }
        
        this.beneficeTotal += e.getBeneficeTotal();
        
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
    
    public int getCoutTotal() {
        return beneficeTotal;
    }
    
    public LinkedList<Cycle> getCycles() {
        return new LinkedList(this.cycles);
    }
    
    public LinkedList<Chaine> getChaines() {
        return new LinkedList(this.chaines.values());
    }

    public Instance getInstance() {
        return instance;
    }
    
    @Override
    public String toString() {
        return "Solution" 
                + "\n\tbeneficeTotal=" + this.beneficeTotal 
                + "\n\tinstance=" + instance 
                + "\n\tchaines=" + chaines.toString()
                + "\n\tcycles=" + cycles.toString() + '}';
    }

    public void printSolution(PrintWriter ecriture) {
        // Cout total de la solution
        ecriture.println("// Cout total de la solution");
        ecriture.println(this.beneficeTotal);
        // Description de la solution
        // Cycles
        ecriture.println("// Description de la solution");
        ecriture.println("// Cycles");
        printCycles(ecriture); //Affiches les donneurs des diff??rents cycles
        ecriture.print("\n");
        // Chaines
        ecriture.print("// Chaines");
        printChaines(ecriture); //Affiches les donneurs des diff??rentes chaines
    }
    
    /**
     * //Affiches les donneurs des diff??rents cycles de la solution
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
     * //Affiches les donneurs des diff??rentes chaines de la solution
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
            System.err.println("[CHECK - Solution] : " + nbTrouneeNotCheck + " ??chnages(s) non valides");
            checker = false;
        }
        
        if(coutTotalSolution != this.beneficeTotal) {
            System.err.println("[CHECK - Solution] : Le b??n??fice totale de la solution n'est pas correcte : ( classe : " + this.beneficeTotal + ", calcul?? : " +  coutTotalSolution + " )");
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
    
    public void creerEchangesVides() {
        LinkedList listeParticipantUtilises = new LinkedList<Participant>();
        for(Chaine ch : this.chaines.values())
        {
            listeParticipantUtilises.add(ch.getAltruiste());
            for(Participant p : ch.getPaires())
            {
                listeParticipantUtilises.add(p);
            }
        }
        for(Cycle cy : this.cycles)
        {
            for(Participant p : cy.getPaires())
            {
                listeParticipantUtilises.add(p);
            }
        }
        
        System.out.println(listeParticipantUtilises);
        for (Participant p : this.instance.getAltruistes()) {
            if(!listeParticipantUtilises.contains(p)){
                this.ajouterAltruisteNouvelleChaine((Altruiste) p);
            }
        }
        for (Participant p : this.instance.getPaires()) {
            if (!listeParticipantUtilises.contains(p)) {

                this.ajouterPaireNouveauCycle((Paire) p);
            }
        }
    }
    
    /**
     * Renvoie le meilleur op?rateur intra-echange de type type dans la
     * solution.
     *
     * @param type
     * @return
     */
    private OperateurLocal getMeilleurOperateurIntra(TypeOperateurLocal type) {
        OperateurLocal best = OperateurLocal.getOperateur(type);
        OperateurLocal operateurTournee;
        for (Chaine ch : this.chaines.values()) {
            operateurTournee = ch.getMeilleurOperateurIntra(type);
            if (operateurTournee.isMeilleur(best)) {
                best = operateurTournee;
            }
        }
        for (Cycle c : this.cycles) {
            operateurTournee = c.getMeilleurOperateurIntra(type);
            if (operateurTournee.isMeilleur(best)) {
                best = operateurTournee;
            }
        }
        
        return best;
    }

    /**
     * Renvoie le meilleur op?rateur inter-echange de type type dans la
     * solution.
     *
     * @param type
     * @return
     */
    private OperateurLocal getMeilleurOperateurInter(TypeOperateurLocal type) {
        OperateurLocal best = OperateurLocal.getOperateur(type);
        OperateurLocal operateurTournee;
        for (Chaine ch : this.chaines.values()) {
            for (Chaine autreCh : this.chaines.values()) {

                operateurTournee = ch.getMeilleurOperateurInter(autreCh, type);
                if (operateurTournee.isMeilleur(best)) {
                    best = operateurTournee;
                }
            }

        }
        for (Cycle cy : this.cycles) {
            for (Cycle autreCy : this.cycles) {

                operateurTournee = cy.getMeilleurOperateurInter(autreCy, type);
                if (operateurTournee.isMeilleur(best)) {
                    best = operateurTournee;
                }
            }

        }
        
        return best;
    }
    
    /**
     * renvoie le meilleur op?rateur de recherche locale de type type dans la
     * solution
     *
     * @param type
     * @return
     */
    public OperateurLocal getMeilleurOperateurLocal(TypeOperateurLocal type) {
        OperateurLocal operateurLocal = OperateurLocal.getOperateur(type);
        if (operateurLocal instanceof OperateurIntraEchange) {
            operateurLocal = this.getMeilleurOperateurIntra(type);
        } else if (operateurLocal instanceof OperateurInterEchange) {
            operateurLocal = this.getMeilleurOperateurInter(type);
        }

        return operateurLocal;
    }
    
    /**
     * impl?mente le mouvement li? ? l?op?rateur de recherche locale infos s?il
     * est r?alisable
     *
     * @param infos
     * @return boolean
     */
    public boolean doMouvementRechercheLocale(OperateurLocal infos) {
        if (infos != null && infos.isMouvementAmeliorant() && infos.doMouvementIfRealisable()) {
            this.beneficeTotal += infos.getDeltaBenefice();
            System.out.println("Check solution : " + this.check());
            return true;
        }
        return false;
    }
    
    private boolean uniqParticipantInEchanges() {
        boolean checker = true;
        
        List<Altruiste> altruistes = this.instance.getAltruistes();
        List<Paire> paires = this.instance.getPaires();
        
        for (Chaine c : this.chaines.values()) {
            if (! altruistes.remove(c.getAltruiste())){
                System.err.println("[CHECK - Solution] : L'altruiste est d??j?? pr??sent dans la chaine :\n\t" + c.getAltruiste().toString() );
                checker = false;
            }
        }
        
        /*
        if(! altruistes.isEmpty()) {
            System.err.println("[CHECK - Solution] : Tout les altruistes ne sont pas pr??sents dans la solution : " + altruistes.size() );
            checker = false;
        }
        */
        
        for (Chaine c : this.chaines.values()) {
            for (Paire paire : c.getPaires()) {
                if (! paires.remove(paire)){
                    System.err.println("[CHECK - Solution] : La paire est d??j?? pr??sente dans la chaine :\n\t" + paire );
                    checker = false;
                }
            }
        }
        
        for (Cycle cycle : this.cycles) {
            for (Paire paire : cycle.getPaires()) {
                if (! paires.remove(paire)){
                    System.err.println("[CHECK - Solution] : La paire est d??j?? pr??sente dans le cyle :\n\t" + paire );
                    checker = false;
                }
            }
        }
        
        /*
        if(! paires.isEmpty()) {
            System.err.println("[CHECK - Solution] : Toutes les paires ne sont pas pr??sentes dans la solution : " + paires.size() );
            checker = false;
        }
        */
        
        return checker;
    }
}
