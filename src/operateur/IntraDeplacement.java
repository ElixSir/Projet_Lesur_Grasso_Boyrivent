/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operateur;

import instance.reseau.Paire;
import java.util.ArrayList;
import java.util.LinkedList;
import solution.Chaine;
import solution.Cycle;
import solution.Echanges;

/**
 *
 * @author felix
 */
public class IntraDeplacement extends OperateurIntraEchange{
    Echanges echangeFini;
    
    public IntraDeplacement() {
        super();
    }

    public IntraDeplacement(int positionI, int positionJ, Echanges echange, int longueurI) {
        super(positionI, positionJ, longueurI, echange);
    }
    
    
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
            return ech.beneficeGlobal(this.positionJ - 1, this.longueurI) - this.echange.getBeneficeTotal();
        } else {
            for (int i = 0; i < longueurI; i++) {
                paires.add(positionJ + i, pairesToAdd.get(i));
            }
            this.echangeFini = ech;
            this.deltaBenefice = ech.beneficeGlobal(this.positionJ, this.longueurI) - this.echange.getBeneficeTotal();
            return ech.beneficeGlobal(this.positionJ, this.longueurI);
        }
        
        
        //return c.deltaBeneficeDeplacement(this.positionI, this.positionJ);
        
    }

    @Override
    protected boolean doMouvement() {
        return false;
    }

    public Echanges getEchangeFini() {
        return echangeFini;
    }

    
}
