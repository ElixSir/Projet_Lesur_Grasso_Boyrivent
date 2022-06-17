/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solution.ensemble;

import instance.reseau.Altruiste;
import instance.reseau.Paire;
import java.io.PrintWriter;
import instance.Instance;
import instance.reseau.Participant;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import operateur.InterEchange;
import operateur.IntraEchange;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;

/**
 *
 * @author Bart
 */
public class Chaine extends Echanges {
    private int maxChaine;
    private Altruiste altruiste; 
    
    public Chaine(Instance i){
        super();
        this.maxChaine = i.getMaxChaines();
        altruiste = null;
    }
    
    public Chaine(Chaine ch) {
        this.maxChaine = ch.maxChaine;
        this.altruiste = ch.getAltruiste();
        this.paires = ch.getPaires();
    }
    
    public Chaine(Instance i, Altruiste altruiste){
        this(i);
        this.altruiste = altruiste;
    }
    
    public Chaine(Instance i, List<Participant> chaine){
        this(i);
        
        LinkedList<Participant> chaineToCopy = new LinkedList<>(chaine);
        // if(Chaine.checkValide(chaineToCopy)) {
            
            this.altruiste = (Altruiste)chaineToCopy.pop();
            
            LinkedList<Paire> paireChaine = new LinkedList<>();
            for (Participant p : chaineToCopy) {
                paireChaine.add((Paire)p);
            }
            
            this.setPaires(paireChaine);
        // }
    }
    
    public static boolean checkValide(LinkedList<Participant> chaine) {
        if(null == chaine) return false;
        LinkedList<Participant> chaineToCopy = new LinkedList<>(chaine);
        if(chaineToCopy.size() < 2) return false;
        Participant a = chaineToCopy.pop();
        if(!(a instanceof Altruiste)) return false; 
        
        Participant ptemp = chaineToCopy.pop();
        
        if(a.getBeneficeVers(ptemp) == -1) return false;        
        
        for (Iterator<Participant> iterator = chaineToCopy.iterator(); iterator.hasNext();) {
            Participant next = iterator.next();
            if(ptemp.getBeneficeVers(next) == -1) return false;
            ptemp = next;
        }
        
        return true;
    }
    
    public static int beneficeTotalChaine(LinkedList<Participant> chaine) {
        if( null == chaine ) return -1;
        if( chaine.size() < 2 ) return 0;
        
        Participant p = chaine.getFirst(),
                temp; 
        int beneficeTotal = 0;
        
        for (int i = 1; i < chaine.size(); i++) {
            temp = chaine.get(i);
            if( !(temp instanceof Paire) ) return -1;
            beneficeTotal += p.getBeneficeVers((Paire)temp);
            p = temp;
        }
        return beneficeTotal;
    }
    
    @Override
    public int getSize() {
        return this.paires.size() + 1;
    }
    
    @Override
    public boolean isPaireAjoutableFin(Paire p) {
        if( null == p || this.getSize()  >= this.maxChaine ) return false;
         
       
        if( this.getSize() == 1 ) {
            return this.altruiste.getBeneficeVers(p) >= 0;
        }
        
        Paire pLast = this.getLastPaire();
        
        return (pLast.getBeneficeVers(p) >= 0);
    }
    
    
    
    @Override
    public int deltaBenefice(Paire p) {
                
        if( this.getSize() == 1 ) {
            return this.altruiste.getBeneficeVers(p);
        }
        
        Paire pLast = this.getLastPaire();
        
        return pLast.getBeneficeVers(p);
    }
    
    public int getMaxChaine() {
        return maxChaine;
    }

    public Altruiste getAltruiste() {
        return altruiste;
    }

    @Override
    protected boolean isPaireInserable(Paire p) {

        for (int i = 0; i <= this.getSize(); i++) {
            if (isPaireInserablePosition(i, p)) {
                return true;
            }

        }

        return false;
    }
    
    public int deltaCoutInsertion(int position, Paire paireToAdd){
        if (!isPositionInsertionValide(position) || paireToAdd == null) return -1;
        int deltaBenefice = 0;
                
        Participant pPrec = null;
        Participant pCour = null;
        
        if(this.paires.isEmpty()){
            deltaBenefice = this.addBenefice(deltaBenefice, this.altruiste.getBeneficeVers(paireToAdd));
            return deltaBenefice;
        }
        else if(position == 0){
            pPrec = this.altruiste;
            pCour = this.getCurrent(position);
        }
        else if(position == this.paires.size()){
            pPrec = this.getPrec(position);
            deltaBenefice = this.addBenefice(deltaBenefice, pPrec.getBeneficeVers(paireToAdd));
        }
        else{
           //probleme ici avec get pPrec
           //position = 2 alors que seulement l'altruiste et la paire,
           //mais si dans le test pr�alable on fait size - 1, on ne
           //rentre m�me pas dans la boucle avec i car getSize - 1 = 0
           //voir classe Echange ligne 162
            pPrec = this.getPrec(position);
            pCour = this.getCurrent(position);
        }
        if (pPrec == null)
        {
            int size = this.getSize();
        }
        deltaBenefice = this.addBenefice(deltaBenefice, - pPrec.getBeneficeVers(pCour));
        deltaBenefice = this.addBenefice(deltaBenefice, pPrec.getBeneficeVers(paireToAdd));
        deltaBenefice = this.addBenefice(deltaBenefice, paireToAdd.getBeneficeVers(pCour));

        return deltaBenefice;
    }
    
    
    public Participant getNext(int position){
        if(!this.isPositionInsertionValide(position)) return null;
        if(position == this.paires.size()){
            return null;
        }
        return this.paires.get(position +1);
    }
        
    public void printChaine(PrintWriter ecriture) {
        
        String s =  this.altruiste.getId() + "\t";
        for (int j = 0; j < this.getSize()-1; j++) {
            Paire paire = this.get(j);
            s += paire.getId() + "\t";
        }

        ecriture.print(s + "\n");
    }

    public int deltaBeneficeRemplacement(int position, Participant paireJ){
        
        return 0;
    }
    
    @Override
    public boolean check() {
        boolean checker = true;
        
        int beneficeTotal = 0;
        
        if( this.getSize() > 1) {
            
            Altruiste a = this.getAltruiste();
            Paire p = this.getFirstPaire();
            beneficeTotal = this.addBenefice(beneficeTotal,a.getBeneficeVers(p));
            
            for(int i = 1; i < this.getSize() - 1; i++) {
                Paire pcurr = this.get(i);
                beneficeTotal = this.addBenefice(beneficeTotal, p.getBeneficeVers(pcurr)) ;
                p = pcurr;
            }
        } 
        
        if( beneficeTotal != this.getBeneficeTotal()) {
            System.err.println("[CHECK - Chaine] : Le bénéfice total calculé n'est pas correcte : ( classe : " + this.getBeneficeTotal() + ", calculé : " + beneficeTotal + " )");
            checker = false;
        }
        
        if( this.getSize() > this.maxChaine ) {
            System.err.println("[CHECK - Chaine] : La taille totale est supérieur à la capacité : ( taille totale : " + this.getSize() + ", capacité : " + this.maxChaine  + " )" );
            checker = false;
        }
        
        if( beneficeTotal == -1)
        {
            System.err.println("[CHECK - Chaine] : Le bénéfice total calculé n'est pas correcte : ( classe : " + this.getBeneficeTotal() + ", calculé : " + beneficeTotal + " )");
            checker = false;
        }
        
        return checker;
    }

    @Override
    public String toString(){
           String s = "Chaine(" + this.getBeneficeTotal() + "){" ;//+ "maxChaine=" + maxChaine + ", altruiste=" + altruiste + ", paires=";
           
           LinkedList<Paire> list = new LinkedList<>(this.getPaires());
           
           s += list.pop().toString();
            for(Paire p: list){
                s+= ", " + p.toString();
           }
           
           s += "}\n";
        return s;
    }

    
     public int deltaBeneficeEchange(int positionI, int positionJ, IntraEchange i) {

        return 0;
    }
     
    @Override
    protected int getMaxEchange() {
        return this.maxChaine;
    }
    
    @Override
    public Participant getPrec(int position) {
        if (!this.isPositionInsertionValide(position)) {
            return null;
        }
        if (position == 0) {
            return this.altruiste;
        }
        return this.paires.get(position - 1);
    }
    
    @Override
    public Participant getCurrent(int position) {
        if (!this.isPositionInsertionValide(position)) {
            return null;
        }
        return this.paires.get(position);
    }
    
    /**
     * Est compatible si l'?lement avant est compatible avec le premier et soit 
     * on ins?re ? la derni?re place, soit le dernier ?lement est compatible 
     * avec le suivant de la chaine
     * @param position
     * @param longueur
     * @param pairesToAdd
     * @return 
     */
    private boolean isCompatible(int positionInsertion, int longueur) {
        if (this.getSize() == 1) return true;
        Participant pPrec = this.getPrec(positionInsertion);

        Participant pFirst = this.getCurrent(positionInsertion);
        Participant pLast = this.getCurrent(positionInsertion + longueur - 1);
        Participant pNext = this.getNext(positionInsertion + longueur - 1);
        if (pPrec.getBeneficeVers(pFirst) != -1
                && ((pNext != null && pLast.getBeneficeVers(pNext) != -1) 
                    || pNext == null)) {
            return true;
        }
        return false;
    }
    
    public int beneficeGlobal(int position, int longueur) {
        if (this.getSize() == 1) return 0;
        if (isCompatible(position, longueur)) {
            int beneficeTotalCalcule = 0;

            Altruiste a = this.getAltruiste();
            Paire p = this.getFirstPaire();
            beneficeTotalCalcule = this.addBenefice(beneficeTotalCalcule, a.getBeneficeVers(p));

            for (int i = 1; i < this.paires.size(); i++) {
                Paire pcurr = this.get(i);
                beneficeTotalCalcule = this.addBenefice(beneficeTotalCalcule, p.getBeneficeVers(pcurr));
                p = pcurr;
            }
            
            return beneficeTotalCalcule;
        }
        return -1;
    }

    public boolean doEchange(IntraEchange infos) {
        if (infos == null) {
            return false;
        }
        if (!infos.isMouvementRealisable()) {
            return false;
        }

        /// CODE HERE
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

        System.out.println("PositionI " + positionI);
        System.out.println("PositionJ " + positionJ);
        System.out.println("longueurI " + longueurI);
        System.out.println("longueurJ" + longueurJ);

        this.paires = infos.getEchangeFini().paires;
        this.beneficeTotal = infos.getBenefice();

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
    
    public int BeneficeEchange(int positionI, int positionJ, int longueurI, int longueurJ) {
        if (!isPositionInsertionValide(positionI)) {
            System.out.println("posI Invalid");
            return -1;
        }
        if (!isPositionInsertionValide(positionJ)) {
            System.out.println("posJ Invalid");
            return -1;
        }
        if (positionI == positionJ) {
            System.out.println("posI = posJ");
            return -1;
        }
        /* if(!(positionI<positionJ)){
            System.out.println("!(positionI<positionJ)");
            return -1;
        }*/

        int beneficeTotal = 0;

        if (this.getSize() > 1) {

            Altruiste a = this.getAltruiste();
            Paire p = this.getFirstPaire();
            beneficeTotal = this.addBenefice(beneficeTotal, a.getBeneficeVers(p));

            for (int i = 1; i < this.getSize() - 1; i++) {
                Paire pcurr = this.get(i);
                beneficeTotal = this.addBenefice(beneficeTotal, p.getBeneficeVers(pcurr));
                p = pcurr;
            }
        }

        if (this.getSize() > this.maxChaine) {
            System.err.println("[CALCUL CYCLE] : La taille totale est sup?rieur ? la capacit? : ( taille totale : " + this.getSize() + ", capacit? : " + this.maxChaine + " )");
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

        /* if(!(positionI<positionJ)){
            System.out.println("!(positionI<positionJ)");
            return -1;
        }*/
        int beneficeTotal = 0;

        if (this.getSize() > 1) {

            Altruiste a = this.getAltruiste();
            Paire p = this.getFirstPaire();
            beneficeTotal = this.addBenefice(beneficeTotal, a.getBeneficeVers(p));

            for (int i = 1; i < this.getSize() - 1; i++) {
                Paire pcurr = this.get(i);
                beneficeTotal = this.addBenefice(beneficeTotal, p.getBeneficeVers(pcurr));
                p = pcurr;
            }
        }

        if (this.getSize() > this.maxChaine) {
            System.err.println("[CALCUL CYCLE] : La taille totale est sup?rieur ? la capacit? : ( taille totale : " + this.getSize() + ", capacit? : " + this.maxChaine + " )");
            return -1;
        }

        return beneficeTotal;
    }
    
    /** 
     * renvoie le meilleur op?rateur intra-echange de type type pour cette
     * tourn?e
     * TODO : Augmenter la longueur pour l'op?rateur (ici 1)
     * Il faut faire en sorte que l'on teste avec des ensemble de paires, longueur
     * sup?rieure ? 1
     * @param type
     * @return
     */
    public OperateurLocal getMeilleurOperateurIntra(TypeOperateurLocal type) {
        OperateurLocal best = OperateurLocal.getOperateur(type);
        for (int i = 0; i < this.paires.size(); i++) {
            for (int j = 0; j < this.paires.size() + 1; j++) {
                OperateurLocal op = OperateurLocal.getOperateurIntra(type, this, i, j, 1, 0);
                if (op.isMeilleur(best)) {
                    best = op;
                }
            }
        }
        return best;
    }
    
}
