/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operateur;

import instance.reseau.Paire;
import java.util.ArrayList;
import java.util.LinkedList;
import solution.ensemble.Chaine;
import solution.ensemble.Cycle;
import solution.ensemble.Echanges;

/**
 *
 * @author felix
 */
public class InterDeplacement extends OperateurInterEchange{
    Echanges echangeFini;
    Echanges autreEchangeFini;
    
    public InterDeplacement() {
    }

    public InterDeplacement(Echanges echange, Echanges autreEchange, int positionI, int positionJ, int longueurI) {
        super(echange, autreEchange, positionI, positionJ, longueurI, 0);
        this.benefice = evalBenefice();
    }

    /**
     * renvoie le coût engendré par la suppression de l'ensemble de paire commençant
     * a la position i
     * position de l'echange On fait trois etapes : on enlève la transplantation de i-1
     * à i, puis de i à i+1, et enfin on ajoute la transplantation de i-1 à i+1
     *
     */
    
    protected int evalBeneficeEchange() {
        if (this.echange == null || !this.echange.isPositionValide(positionI)) {
            return -1;
        }
        
        Echanges ech;

        if (this.echange instanceof Cycle) {//fait un échange si écart de 2 entre i et j, pareil sur cvrp

            ech = new Cycle((Cycle) this.echange);

        } else {
            ech = new Chaine((Chaine) this.echange);
        }

        LinkedList<Paire> paires = ech.getPairesReference();
        
        if (longueurI <= 0 || longueurJ <= 0) {
            this.benefice = -1;
            this.beneficeEchange = -1;
            this.beneficeAutreEchange = -1;
            return -1;
        }

        if (positionJ + longueurJ >= paires.size()) {
            this.benefice = -1;
            this.beneficeEchange = -1;
            this.beneficeAutreEchange = -1;
            return -1;
        }

        if (positionI + longueurI >= paires.size()) {
            this.benefice = -1;
            this.beneficeEchange = -1;
            this.beneficeAutreEchange = -1;
            return -1;
        }

        for (int i = positionI; i < positionI + longueurI; i++) {
            paires.remove(i);
        }
      
        this.echangeFini = ech;
        this.deltaBeneficeEchange = ech.beneficeGlobal(this.positionI, 1) - this.echange.getBeneficeTotal();
        return ech.beneficeGlobal(this.positionI, 1);
       

    }

    /**
     * calculera le coût d?insertion de l'ensemble pairesToAdd si celui-ci peut être
     * ajouté dans l'echange .Si l'ensemble ne peut pas être ajouté dans l'echange
     * alors la méthode renverra -1.
     *
     * @return
     */
    
    protected int evalBeneficeAutreEchange() {
        if (this.autreEchange == null
                || !this.autreEchange.isPositionInsertionValide(positionJ)) {
            return -1;
        }
        Echanges autreEch;
        
        
        if (this.autreEchange instanceof Cycle) {//fait un échange si écart de 2 entre i et j, pareil sur cvrp

            autreEch = new Cycle((Cycle) this.autreEchange);

        } else {
            autreEch = new Chaine((Chaine) this.autreEchange);
        }
        //on prend les paires à ajouter du premier echange
        LinkedList<Paire> pairesEchange = this.echange.getPaires();
        LinkedList<Paire> pairesAutreEchange = autreEch.getPairesReference();
        ArrayList<Paire> pairesToAdd = new ArrayList<Paire>();

        
        if (longueurI <= 0 || longueurJ < 0) {
            this.benefice = -1;
            this.beneficeEchange = -1;
            this.beneficeAutreEchange = -1;
            return -1;
        }

        if (positionJ + longueurJ >= pairesEchange.size() || positionJ + longueurJ >= pairesAutreEchange.size()) {
            this.benefice = -1;
            this.beneficeEchange = -1;
            this.beneficeAutreEchange = -1;
            return -1;
        }

        if (positionI + longueurI >= pairesEchange.size() || positionJ + longueurJ >= pairesAutreEchange.size()) {
            this.benefice = -1;
            this.beneficeEchange = -1;
            this.beneficeAutreEchange = -1;
            return -1;
        }
        
        
        for (int i = positionI; i < positionI + longueurI; i++) {
            pairesToAdd.add(pairesEchange.get(i));
        }
        
        
        for (int i = 0; i < longueurI; i++) {
            pairesAutreEchange.add(positionJ + i, pairesToAdd.get(i));
        }
        this.autreEchangeFini = autreEch;
        this.deltaBeneficeAutreEchange = autreEch.beneficeGlobal(this.positionJ, this.longueurI) - this.autreEchange.getBeneficeTotal();
        return autreEch.beneficeGlobal(this.positionJ, this.longueurI);
  
        
    }
    

    public boolean isMouvementAmeliorant(){
        if(deltaBeneficeEchange+deltaBeneficeAutreEchange > 0)
            return true;
        return false;
    }

    
     public boolean isMouvementRealisable(){
            if(this.benefice == -1){
            return false;
        }
        return true;
    }
    
    @Override
    protected boolean doMouvement() {
        return this.echange.doDeplacement(this);
    }

    public Echanges getEchangeFini() {
        return echangeFini;
    }

    public Echanges getAutreEchangeFini() {
        return autreEchangeFini;
    }

    @Override
    public String toString() {
        return "InterDeplacement{" + super.toString() + '}';
    }

    @Override
    protected int evalBenefice() {
        int beneficeTotal = -1;
        this.beneficeEchange = this.evalBeneficeEchange();
        this.beneficeAutreEchange = evalBeneficeAutreEchange();

        if (beneficeEchange == -1 || this.beneficeAutreEchange == -1) {
            beneficeTotal = -1;
            this.deltaBenefice = -1;
        } else {
            beneficeTotal = this.beneficeEchange + this.beneficeAutreEchange;
            this.deltaBenefice = this.deltaBeneficeEchange + this.deltaBeneficeAutreEchange;
        
        }
        return beneficeTotal;
    }
    
    
    
}