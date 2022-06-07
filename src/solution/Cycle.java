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
            return this.paires.get(position - (this.paires.size()-1)); // Si on d�passe on reviens au d�but + n 
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
            System.err.println(this.getSize() +"[CHECK - Cycle] : Le bénéfice total calculé n'est pas correcte : ( classe : " + this.getBeneficeTotal() + ", calculé : " + beneficeTotal + " )");
            checker = false;
        }
        
        if( this.getSize() > this.maxCycle ) {
            System.err.println("[CHECK - Cycle] : La taille totale est supérieur à la capacité : ( taille totale : " + this.getSize() + ", capacité : " + this.maxCycle  + " )" );
            checker = false;
        }
        
        return checker;
    }

    /**
     * renvoie le co�t engendr� par la suppression de la paire i � la position
     * position de la tourn�e On fait trois trajets : on enl�ve la
     * transplantation de i-1 � i, on enl�ve la transplantation de i � i+1, on
     * ajoute la transplantation de i-1 � i+1
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

            //on enl�ve le trajet de i-1 � i
            deltaBenefice = this.delBenefice(deltaBenefice, pPrec.getBeneficeVers(pCurr));
            //on enl�ve le trajet de i � i+1
            deltaBenefice = this.delBenefice(deltaBenefice, pCurr.getBeneficeVers(pSuiv));
            //on ajoute le trajet i-1 � i+1
            deltaBenefice = this.addBenefice(deltaBenefice, pPrec.getBeneficeVers(pSuiv));

        }

        return deltaBenefice;

    }
    
    
    public int deltaBeneficeInsertion(int position, Paire paireToAdd){
        if (!isPositionInsertionValide(position) || paireToAdd == null) return -1;
        int deltaBenefice = 0;
                
        if(this.paires.isEmpty()){
            //Pas de Benefice en plus car c'est la premi�re paire dans le cycle
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
     * Est compatible si l'�lement avant est compatible avec le premier et 
     * si le dernier �lement est compatible avec le suivant
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


    

}
