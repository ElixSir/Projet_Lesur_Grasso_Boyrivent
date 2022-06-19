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
public class IntraDeplacement extends OperateurIntraEchange{
    Echanges echangeFini;
    
    public IntraDeplacement() {
        super();
    }

    public IntraDeplacement(int positionI, int positionJ, Echanges echange, int longueurI, int longueurJ) {
        super(positionI, positionJ, echange, longueurI, longueurJ);
    }
    
    /**
     * Evalue le bénéfice de l'opérateur
     *
     * @return
     */
    @Override
    protected int evalBenefice() {
        if (echange == null || !echange.positionDeplacementValides(this.positionI, this.positionJ)) {
            return -1;
        }
        Echanges ech;
        
        if(this.echange instanceof Cycle)
        {//fait un échange si écart de 2 entre i et j, pareil sur cvrp
            
            ech = new Cycle((Cycle) this.echange);
            
        }
        else {
            ech = new Chaine((Chaine) this.echange);
        }
            
        LinkedList<Paire> paires = ech.getPairesReference();
        ArrayList<Paire> pairesToAdd = new ArrayList<Paire>();
        
        if (longueurI <= 0 || longueurJ < 0) {
            System.out.println("ALED");
            System.out.println("longueurI<=0 ou longueurJ<=0");
            System.out.println(this);
            System.out.println(longueurI);
            System.out.println(longueurJ);
            
            this.benefice = -1;
            return -1;
        }

        if (positionJ + longueurJ >= paires.size()) {
//            System.out.println("posJ+longJ > size");
            this.benefice = -1;
            return -1;
        }

        if (positionI + longueurI >= paires.size() || positionI + longueurI >= positionJ) {
//            System.out.println("posI+longI > size");
            this.benefice = -1;
            return -1;
        }  

        for(int i = positionI; i < positionI + longueurI; i++)
        {
            pairesToAdd.add(paires.get(i));
            paires.remove(i);
        }

        if (positionJ > positionI) {
            for (int i = 0; i < longueurI; i++) {
                paires.add(positionJ + i - longueurI,pairesToAdd.get(i));
            }
            //on insère une place avant la positionJ
            this.echangeFini = ech;
            this.deltaBenefice = ech.beneficeGlobal(this.positionJ - 1, this.longueurI) - this.echange.getBeneficeTotal();

            return ech.beneficeGlobal(this.positionJ - 1, this.longueurI) - this.echange.getBeneficeTotal();
        } else {
            for (int i = 0; i < longueurI; i++) {
                paires.add(positionJ + i, pairesToAdd.get(i));
            }
            this.echangeFini = ech;
            this.deltaBenefice = ech.beneficeGlobal(this.positionJ, this.longueurI) - this.echange.getBeneficeTotal();
            return ech.beneficeGlobal(this.positionJ, this.longueurI);
        }
        
        
        
    }
    
    
    public boolean isMouvementAmeliorant(){
        if(deltaBenefice > 0)
            return true;
        return false;
    }

    public boolean isMouvementRealisable(){
            if(this.benefice == -1){ // changer ici si le déplacement se fait pas ELIE
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

    
}