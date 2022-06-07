/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operateur;

import instance.reseau.Paire;
import solution.Chaine;
import solution.Cycle;
import solution.Echanges;

/**
 *
 * @author Bart
 */
public class IntraEchange extends OperateurIntraEchange {

    public IntraEchange() {
        super();
    }

    public IntraEchange(Echanges echange, int positionI, int positionJ, int longueurI, int longueurJ) {
        super(positionI, positionJ, echange, longueurI, longueurJ);
    }

    @Override
    public String toString() {
        return "IntraEchange{" + "positionI=" + positionI + ", positionJ=" + positionJ + ", coutDeplacement=" + deltaBenefice + ", paireI=" + paireI + ", paireJ=" + paireJ + ",echange=" + this.echange + '}';
    }

    /*public boolean isTabou(OperateurLocal operateur) {
        if(operateur == null) return false;
        if(!(operateur instanceof IntraEchange)) return false;
        if(operateur.tournee == null || operateur.clientI == null || operateur.clientJ == null) return false;
        if(!this.tournee.equals(operateur.tournee)) return false;
        if(this.clientI.equals(operateur.clientI) && this.clientJ.equals(operateur.clientJ))
            return true;
        if(this.clientI.equals(operateur.clientJ) && this.clientJ.equals(operateur.clientI))
            return true;
        
        return false;
    }*/
    @Override
    protected int evalBenefice() {
        if (echange == null) {
            return -1;
        }

        Echanges c;

        if (this.echange instanceof Cycle) {
            c = new Cycle((Cycle) this.echange);
        } else {
            c = new Chaine((Chaine) this.echange);
        }

        int positionI = getPositionI();
        int positionJ = getPositionJ();
        int longueurI = getLongueurI();
        int longueurJ = getLongueurJ();

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

        for (int i = 0; i < longueurJ; i++) {
            c.getPairesReference().add(positionI + i, c.get(positionJ + i));
            c.getPairesReference().remove(positionJ + i + 1);

        }

        for (int i = 0; i < longueurI; i++) {
            c.getPairesReference().add(positionJ + longueurJ, c.get(positionI + longueurJ));
            c.getPairesReference().remove(positionI + longueurJ);

        }

        this.deltaBenefice = c.BeneficeEchange(positionI, positionJ, longueurI, longueurJ) - c.getBeneficeTotal();
        this.benefice = c.BeneficeEchange(positionI, positionJ, longueurI, longueurJ);

        return c.BeneficeEchange(positionI, positionJ, longueurI, longueurJ);

    }

    @Override
    protected boolean doMouvement() {
        return this.echange.doEchange(this);
    }

}
