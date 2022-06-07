/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solution;

import instance.reseau.Paire;
import java.io.PrintWriter;
import instance.Instance;
import instance.reseau.Participant;
import java.util.LinkedList;
import operateur.InsertionPaire;
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
    
    public Cycle(Cycle c) {
        this.maxCycle = c.maxCycle;
        this.paires = c.getPaires();
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
        
        if(this.deltaBeneficeInsertion(position, p) >= Integer.MAX_VALUE) return false;
        
        
        return true;
    }
    
    public int deltaBeneficeRemplacement(int position, Participant paireJ){
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
            return this.paires.get(position - (this.paires.size()-1)); // Si on dï¿½passe on reviens au dï¿½but + n 
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
            System.err.println(this.getSize() +"[CHECK - Cycle] : Le bÃ©nÃ©fice total calculÃ© n'est pas correcte : ( classe : " + this.getBeneficeTotal() + ", calculÃ© : " + beneficeTotal + " )");
            checker = false;
        }
        
        if( this.getSize() > this.maxCycle ) {
            System.err.println("[CHECK - Cycle] : La taille totale est supÃ©rieur Ã  la capacitÃ© : ( taille totale : " + this.getSize() + ", capacitÃ© : " + this.maxCycle  + " )" );
            checker = false;
        }
        
        return checker;
    }

    /**
     * renvoie le coût engendré par la suppression de la paire i à la position
     * position de la tournée On fait trois trajets : on enlève la
     * transplantation de i-1 à i, on enlève la transplantation de i à i+1, on
     * ajoute la transplantation de i-1 à i+1
     *
     * @param position
     * @return
     */
    @Override
    public int deltaBeneficeSuppression(int position) {
        if (!this.isPositionValide(position)) {
            return -1;
        }
        int deltaBenefice = 0;
        if (paires.size() == 2) {
            //on enleve le benefice de la transplantation de l'altruiste vers la paire
            Participant pPrec = this.getPrec(position);
            Participant pCurr = this.getCurrent(position);
            deltaBenefice = this.delBenefice(deltaBenefice, pPrec.getBeneficeVers(pCurr));
            deltaBenefice = this.delBenefice(deltaBenefice, pCurr.getBeneficeVers(pPrec));
        } 
        else if(paires.size() > 2){

            Participant pPrec = this.getPrec(position);
            Participant pCurr = this.getCurrent(position);
            Participant pSuiv = this.getNext(position);

            //on enlève le trajet de i-1 à i
            deltaBenefice = this.delBenefice(deltaBenefice, pPrec.getBeneficeVers(pCurr));
            //on enlève le trajet de i à i+1
            deltaBenefice = this.delBenefice(deltaBenefice, pCurr.getBeneficeVers(pSuiv));
            //on ajoute le trajet i-1 à i+1
            deltaBenefice = this.addBenefice(deltaBenefice, pPrec.getBeneficeVers(pSuiv));

        }

        return deltaBenefice;

    }
    
    
    public int deltaBeneficeInsertion(int position, Paire paireToAdd){
        if (!isPositionInsertionValide(position) || paireToAdd == null) return -1;
        int deltaBenefice = 0;
                
        if(this.paires.isEmpty()){
            //Pas de Benefice en plus car c'est la premiï¿½re paire dans le cycle
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
            
            deltaBenefice = this.delBenefice(deltaBenefice, pPrec.getBeneficeVers(pCour));
            deltaBenefice = this.addBenefice(deltaBenefice, pPrec.getBeneficeVers(paireToAdd));
            deltaBenefice = this.addBenefice(deltaBenefice, paireToAdd.getBeneficeVers(pCour));
        }
        
        return deltaBenefice;
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
    
    /**
     * Est compatible si l'élement avant est compatible avec le premier et 
     * si le dernier élement est compatible avec le suivant
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

    
    @Override
    public String toString() {
        String res = "\n[";
        
        LinkedList<Paire> paires = this.getPaires();
        
        res += paires.pop().toString();
        
        for (Paire paire : paires) {
            res += ", " + paire.toString();
        }
        
        return res + ']';
    }

    @Override 
    protected boolean isPaireInserable(Paire[] p) {

        for (int i = 0; i < this.getSize(); i++) {
            /*if (isPaireInserablePosition(i, p)) {
                return true;
            }*/

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


    public int BeneficeEchange(int positionI, int positionJ, int longueurI, int longueurJ) {
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
            System.err.println("[CALCUL CYCLE] : La taille totale est supérieur à la capacité : ( taille totale : " + this.getSize() + ", capacité : " + this.maxCycle + " )");
            return -1;
        }

        return beneficeTotal;
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
            System.err.println("[CALCUL CYCLE] : La taille totale est supérieur à la capacité : ( taille totale : " + this.getSize() + ", capacité : " + this.maxCycle + " )");
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

        for (int i = 0; i < longueurJ; i++) {
            this.getPairesReference().add(positionI + i, this.get(positionJ + i));
            this.getPairesReference().remove(positionJ + i + 1);

        }

        for (int i = 0; i < longueurI; i++) {
            this.getPairesReference().add(positionJ + longueurJ, this.get(positionI + longueurJ));
            this.getPairesReference().remove(positionI + longueurJ);

        }

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
        /*       System.out.println("PositionI "+positionI);
        System.out.println("PositionJ " + positionJ);
        System.out.println("longueurI "+longueurI);
        System.out.println("longueurJ"+longueurJ);*/

        for (int i = 0; i < longueurJ; i++) {
            c.getPairesReference().add(positionI + i, c2.get(positionJ));
            c2.getPairesReference().remove(positionJ);

        }

        for (int i = 0; i < longueurI; i++) {
            c2.getPairesReference().add(positionJ + i, c.get(positionI + longueurJ));
            c.getPairesReference().remove(positionI + longueurJ);

        }

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
