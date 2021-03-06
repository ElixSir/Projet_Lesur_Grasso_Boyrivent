/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solution.ensemble;

import instance.reseau.Paire;
import java.io.PrintWriter;
import instance.Instance;
import instance.reseau.Participant;
import java.util.LinkedList;
import operateur.InterEchange;
import operateur.IntraEchange;

/**
 *
 * @author Bart
 */
public class Cycle extends Echanges {
    
    private int maxCycle;

    public Cycle(Instance i) {
        this.maxCycle = i.getMaxCycles();
    }
    
    public Cycle (Cycle c){
        this.maxCycle = c.getMaxCycle();
        this.setPaires(c.getPaires());
    }
    

    public Cycle(Instance i, LinkedList<Paire> cycle) {
        this(i);
        if(Cycle.checkCycleValide(cycle)) {
            this.setPaires(new LinkedList(cycle));
        }
    }
    
    public static boolean checkCycleValide(LinkedList<Paire> cycle) {
        if(null == cycle) return false;
        if( cycle.size() < 2 ) return false; 
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
    
   /* public int deltaBeneficeRemplacement(int position, Participant paireJ){
                if(this.paires.size() < 2){
            return -1;
        }
        
        int deltaCout = 0;

        Participant paireI = this.getCurrent(position);
        
        Participant avantI = this.getPrec(position);
        Participant apresI = this.getNext(position);
        
        //deltaCout =  this.DelBenefice(deltaCout, avantI.getBeneficeVers(paireI));
        //deltaCout =  this.DelBenefice(deltaCout, paireI.getBeneficeVers(apresI));
        deltaCout -= avantI.getBeneficeVers(paireI);
        deltaCout -= paireI.getBeneficeVers(apresI);
        System.out.println("Apres moins : "+ deltaCout);
        deltaCout +=  avantI.getBeneficeVers(paireJ);
        deltaCout += paireJ.getBeneficeVers(apresI);
        System.out.println(" Apres Plus : "+deltaCout);
        return deltaCout;
    }
    
        private int deltaBeneficeEchangeConsecutif(int positionI){
        
        if(this.paires.size() < 2){
            return -1;
        }
        
        if(this.paires.size() == 2){ // si un cycle de 2 
            return 0;
        }
        
        
        int deltaCout = 0;
        
        Participant paireI = this.getCurrent(positionI);
        Participant paireJ = this.getNext(positionI);
        Participant avantI = this.getPrec(positionI);
        Participant apresJ = this.getNext(positionI+1);
        
        
        deltaCout = this.DelBenefice(deltaCout, avantI.getBeneficeVers(paireI));
        deltaCout = this.DelBenefice(deltaCout, paireI.getBeneficeVers(paireJ));
        deltaCout = this.DelBenefice(deltaCout, paireJ.getBeneficeVers(apresJ));
        
        deltaCout = this.addBenefice(deltaCout, avantI.getBeneficeVers(paireJ));
        deltaCout = this.addBenefice(deltaCout, paireJ.getBeneficeVers(paireI));
        deltaCout = this.addBenefice(deltaCout, paireI.getBeneficeVers(apresJ));
        
        
        return deltaCout;
    }*/
        
    /*private int deltaBeneficeEchangeConsecutifBout(int positionI){
        
        if(this.paires.size() < 2){
            return -1;
        }
        
        if(this.paires.size() == 2){ // si un cycle de 2 
            return 0;
        }
        
        
        int deltaCout = 0;
        
        Participant paireI = this.getCurrent(positionI);
        Participant paireJ = this.getNext(positionI);
        Participant avantI = this.getPrec(positionI);
        Participant avantJ = this.getPrec(positionI-1);
        
        
        deltaCout = this.DelBenefice(deltaCout, avantI.getBeneficeVers(paireI));
        deltaCout = this.DelBenefice(deltaCout, paireI.getBeneficeVers(paireJ));
        deltaCout = this.DelBenefice(deltaCout, paireJ.getBeneficeVers(avantJ));
        
        deltaCout = this.addBenefice(deltaCout, avantI.getBeneficeVers(paireJ));
        deltaCout = this.addBenefice(deltaCout, paireJ.getBeneficeVers(paireI));
        deltaCout = this.addBenefice(deltaCout, paireI.getBeneficeVers(avantJ));
        
        
        return deltaCout;
    }*/
    
    
    public int deltaBeneficeEchange(int positionI, int positionJ, IntraEchange infos) {
        if(!isPositionInsertionValide(positionI)){
            System.out.println("posI Invalid");
            return -1;
        }
        if(!isPositionInsertionValide(positionJ)){
            System.out.println("posJ Invalid");
            return -1;
        }
        if(positionI == positionJ){
            System.out.println("posI = posJ");
            return -1;
        }
        if(!(positionI<positionJ)){
            System.out.println("!(positionI<positionJ)");
            return -1;
        }
        
        Cycle c = new Cycle(this);
        
        
        Paire paireI = infos.getClientI();
        Paire paireJ = infos.getClientJ();
        
        c.paires.set(positionI, paireJ);
        c.paires.set(positionJ, paireI);
        
        
         int beneficeTotal = 0;
        
        if( c.getSize() > 1) {
            
            Paire p = c.getFirstPaire();
            for(int i = 1; i < c.getSize(); i++) {
                Paire pcurr = c.get(i);
                beneficeTotal = c.addBenefice(beneficeTotal, p.getBeneficeVers(pcurr));
                p = pcurr;
            }
            beneficeTotal = c.addBenefice(beneficeTotal, c.getLastPaire().getBeneficeVers(c.getFirstPaire()));
        } 
        
        
        if( c.getSize() > c.maxCycle ) {
            System.err.println("[CALCUL CYCLE] : La taille totale est sup??rieur ?? la capacit?? : ( taille totale : " + this.getSize() + ", capacit?? : " + this.maxCycle  + " )" );
            return -1;
        }
        
        
        return beneficeTotal;
    }    

        
    @Override
    public boolean isPaireAjoutableFin(Paire p) {
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

    public Participant getNext(int position){
       // if(!this.isPositionInsertionValide(position)) return null;
        if(position >= this.paires.size()-1){
            return this.paires.get(position - (this.paires.size()-1)); // Si on d???passe on reviens au d???but + n 
        }
        return this.paires.get(position +1);
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
                beneficeTotal = this.addBenefice(beneficeTotal, p.getBeneficeVers(pcurr));
                p = pcurr;
            }
            beneficeTotal = this.addBenefice(beneficeTotal, this.getLastPaire().getBeneficeVers(this.getFirstPaire()));
        } 
        
        if( beneficeTotal != this.getBeneficeTotal()) {
            System.err.println(this.getSize() +"[CHECK - Cycle] : Le b??n??fice total calcul?? n'est pas correcte : ( classe : " + this.getBeneficeTotal() + ", calcul?? : " + beneficeTotal + " )");
            checker = false;
        }
        
        if( this.getSize() > this.maxCycle ) {
            System.err.println("[CHECK - Cycle] : La taille totale est sup??rieur ?? la capacit?? : ( taille totale : " + this.getSize() + ", capacit?? : " + this.maxCycle  + " )" );
            checker = false;
        }
        
        return checker;
    }

    
    public int deltaCoutInsertion(int position, Paire paireToAdd){
        if (!isPositionInsertionValide(position) || paireToAdd == null) return -1;
        int deltaBenefice = 0;
                
        if(this.paires.isEmpty()){
            //Pas de Benefice en plus car c'est la premi???re paire dans le cycle
        }
        else if(this.getSize() == 1)
        {
            Participant pFirst = this.paires.getFirst();
            
            deltaBenefice = this.addBenefice(deltaBenefice, pFirst.getBeneficeVers(paireToAdd));
            deltaBenefice = this.addBenefice(deltaBenefice, paireToAdd.getBeneficeVers(pFirst));
          
        }
        else{
            Participant pPrec = this.getPrec(position);
            Participant pCour = this.getCurrent(position);
            
            deltaBenefice = this.DelBenefice(deltaBenefice, pPrec.getBeneficeVers(pCour));
            deltaBenefice = this.addBenefice(deltaBenefice, pPrec.getBeneficeVers(paireToAdd));
            deltaBenefice = this.addBenefice(deltaBenefice, paireToAdd.getBeneficeVers(pCour));
        }
        
        return deltaBenefice;
    }
    

    
    @Override
    public String toString() {
        String res = "\nCycles(" + this.getBeneficeTotal() + ")[";
        
        LinkedList<Paire> paires = this.getPaires();
        
        res += paires.pop().toString();
        
        for (Paire paire : paires) {
            res += ", " + paire.toString();
        }
        
        return res + ']';
    }

    @Override 
    protected boolean isPaireInserable(Paire p) {

        for (int i = 0; i < this.getSize(); i++) {
            if (isPaireInserablePosition(i, p)) {
                return true;
            }

        }

        return false;
    }
    
    
    @Override
    protected int getMaxEchange() {
        return this.maxCycle;
    }
    
    @Override
    public int getSize() {
        return this.paires.size();
    }
    
    @Override
    public Paire getPrec(int position) {
        
        if (!this.isPositionInsertionValide(position)) {
            return null;
        }
        if (position == 0) {
            return this.paires.get(this.getSize()-1);
        }
        return this.paires.get(position - 1);
    }
    
    @Override
    public Participant getCurrent(int position) {
        if (position == this.getSize()) {
            position = 0;
        }
        if (!this.isPositionInsertionValide(position)) {
            return null;
        }
        return this.paires.get(position);
    }
    
    /**
     * Est compatible si l'?lement avant est compatible avec le premier et 
     * si le dernier ?lement est compatible avec le suivant
     * avec le suivant de la chaine
     *
     * @param position
     * @param longueur
     * @param pairesToAdd
     * @return
     */
    private boolean isCompatible(int positionInsertion, int longueur)
    {
        if(this.getSize() == 1) return true;
        Participant pPrec = this.getPrec(positionInsertion);
        Participant pFirst = this.getCurrent(positionInsertion);
        Participant pLast = this.getCurrent(positionInsertion + longueur-1);
        Participant pNext = this.getNext(positionInsertion+longueur-1);
        if(pPrec.getBeneficeVers(pFirst) != -1 
                && pLast.getBeneficeVers(pNext) != -1)
        {
            return true;
        }
        return false;
    }

    
    
    public int beneficeGlobal(int positionInsertion, int longueur) {
        if(this.getSize() == 1) return 0;
        if(isCompatible(positionInsertion, longueur))
        {
            int BeneficeTotalCalcule = 0;

            Paire p = this.getFirstPaire();
            for (int i = 1; i < this.getSize(); i++) {
                Paire pcurr = this.get(i);
                BeneficeTotalCalcule = this.addBenefice(BeneficeTotalCalcule, p.getBeneficeVers(pcurr));
                p = pcurr;
            }
            BeneficeTotalCalcule = this.addBenefice(BeneficeTotalCalcule, this.getLastPaire().getBeneficeVers(this.getFirstPaire()));
            return BeneficeTotalCalcule;
        }
        return -1;
    }
    
    public int BeneficeEchangeInter(int positionI, int positionJ, int longueurI, int longueurJ) {
        if (!isPositionInsertionValide(positionI)) {
            System.out.println("posI Invalid");
            return -1;
        }
        if (!isPositionInsertionValide(positionJ)) {
            System.out.println("posJ Invalid");
            return -1;
        }

        /*
        if(positionI == positionJ){
            System.out.println("posI = posJ");
            return -1;
        }
        if(!(positionI<positionJ)){
            System.out.println("!(positionI<positionJ)");
            return -1;
        }*/
        int beneficeTotal = 0;

        if (this.getSize() > 1) {

            Paire p = this.getFirstPaire();
            for (int i = 1; i < this.getSize(); i++) {
                Paire pcurr = this.get(i);
                beneficeTotal = this.addBenefice(beneficeTotal, p.getBeneficeVers(pcurr));
                p = pcurr;
            }
            beneficeTotal = this.addBenefice(beneficeTotal, this.getLastPaire().getBeneficeVers(this.getFirstPaire()));
        }

        if (this.getSize() > this.maxCycle) {
            System.err.println("[CALCUL CYCLE] : La taille totale est sup?rieur ? la capacit? : ( taille totale : " + this.getSize() + ", capacit? : " + this.maxCycle + " )");
            return -1;
        }

        return beneficeTotal;
    }
    
    public int BeneficeEchange(int positionI, int positionJ, int longueurI, int longueurJ) {
        if (!isPositionInsertionValide(positionI)) {
            System.out.println("posI Invalid");
            return -1;
        }
        if (!isPositionInsertionValide(positionJ)) {
            System.out.println("posJ Invalid");
            return -1;
        }

        int beneficeTotal = 0;

        if (this.getSize() > 1) {

            Paire p = this.getFirstPaire();
            for (int i = 1; i < this.getSize(); i++) {
                Paire pcurr = this.get(i);
                beneficeTotal = this.addBenefice(beneficeTotal, p.getBeneficeVers(pcurr));
                p = pcurr;
            }
            beneficeTotal = this.addBenefice(beneficeTotal, this.getLastPaire().getBeneficeVers(this.getFirstPaire()));
        }

        if (this.getSize() > this.maxCycle) {
            System.err.println("[CALCUL CYCLE] : La taille totale est sup?rieur ? la capacit? : ( taille totale : " + this.getSize() + ", capacit? : " + this.maxCycle + " )");
            return -1;
        }

        return beneficeTotal;
    }
    
    public boolean doEchange(IntraEchange infos) {
        if (infos == null) {
            return false;
        }
        if (!infos.isMouvementRealisable()) {
            return false;
        }

        int positionI = infos.getPositionI();
        int positionJ = infos.getPositionJ();
        int longueurI = infos.getLongueurI();
        int longueurJ = infos.getLongueurJ();

        if (positionI > positionJ) { // Echanger dans un sens ou un autre ne change rien
            int saveJ = positionJ; // Si I>J j'inverse pour que mon calcul ne change pas 
            positionJ = positionI;
            positionI = saveJ;

            int savelJ = longueurJ;
            longueurJ = longueurI;
            longueurI = savelJ;
        }

        this.paires = infos.getEchangeFini().paires;
        this.beneficeTotal = infos.getBenefice(); //MAJ cout total

        if (!this.check()) {
            System.out.println("Mauvais ?change des clients");
            System.out.println(infos);
            System.exit(-1); //Termine le programme
        }

        return true;
    }

    public boolean doEchange(InterEchange infos) {
        if (infos == null) {
            return false;
        }
        if (!infos.isMouvementRealisable()) {
            return false;
        }

        Echanges c = infos.getEchange();
        Echanges c2 = infos.getAutreEchange();

        int positionI = infos.getPositionI();
        int positionJ = infos.getPositionJ();
        int longueurI = infos.getLongueurI();
        int longueurJ = infos.getLongueurJ();

        if (positionI > positionJ) { // Echanger dans un sens ou un autre ne change rien
            int saveJ = positionJ; // Si I>J j'inverse pour que mon calcul ne change pas 
            positionJ = positionI;
            positionI = saveJ;

            int savelJ = longueurJ;
            longueurJ = longueurI;
            longueurI = savelJ;
        }

        c.paires = infos.getEchangeFini().getPaires();
        c2.paires = infos.getAutreEchangeFini().getPaires();
        
        c.beneficeTotal = infos.getBeneficeEchange();
        c2.beneficeTotal = infos.getBeneficeAutreEchange();

        if (!this.check()) {
            System.out.println("Mauvais ?change des clients");
            System.out.println(infos);
            System.out.println("");
            System.exit(-1); //Termine le programme
        }

        return true;
    }

}
