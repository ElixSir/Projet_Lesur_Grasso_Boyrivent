/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operateur;


import instance.reseau.Paire;
import solution.Chaine;
import solution.Cycle;
import solution.Echanges;


public class InterEchange extends OperateurInterEchange {

    public InterEchange() {
        super();
    }

    public InterEchange(Echanges echange, Echanges autreechange, int positionI, int positionJ, int longueurI, int longueurJ) {
        super(echange, autreechange, positionI, positionJ, longueurI, longueurJ);
    }

    @Override
    public String toString() {
        return "InterEchange{" + "positionI=" + positionI + ", positionJ=" + positionJ + ", coutDeplacement=" + deltaBenefice + ", paireI=" + paireI + ", paireJ=" + paireJ + ",echange=" + this.echange + ",auteechange=" + this.autreEchange + '}';
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
        if (autreEchange == null) {
            return -1;
        }

        Echanges c;
        Echanges c2;

        if (this.echange instanceof Cycle) {
            c = new Cycle((Cycle) this.echange);
        } else {
            c = new Chaine((Chaine) this.echange);
        }

        if (this.autreEchange instanceof Cycle) {
            c2 = new Cycle((Cycle) this.autreEchange);
        } else {
            c2 = new Chaine((Chaine) this.autreEchange);
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

        if (c.getPrec(positionI).getBeneficeVers(c2.get(positionJ)) == -1) {
            System.out.println("Ici1");
            return -1;
        }
        if (c2.get(positionJ + longueurJ - 1).getBeneficeVers(c.get(positionI + longueurI)) == -1) {
            System.out.println("Ici2");
            return -1;
        }

        if (c2.getPrec(positionJ).getBeneficeVers(c.get(positionI)) == -1) {
            System.out.println("Ici3");
            return -1;
        }

        if (c.get(positionI + longueurI - 1).getBeneficeVers(c2.get(positionJ + longueurJ)) == -1) {
            System.out.println("Ici4");
            return -1;
        }

        for (int i = 0; i < longueurJ; i++) {
            c.getPairesReference().add(positionI + i, c2.get(positionJ));
            c2.getPairesReference().remove(positionJ);

        }

        for (int i = 0; i < longueurI; i++) {
            c2.getPairesReference().add(positionJ + i, c.get(positionI + longueurJ));
            c.getPairesReference().remove(positionI + longueurJ);

        }

        this.deltaBenefice = c.BeneficeEchangeInter(positionI, positionJ, longueurI, longueurJ) - c.getBeneficeTotal();
        this.deltaAutreBenefice = c2.BeneficeEchangeInter(positionI, positionJ, longueurI, longueurJ) - c2.getBeneficeTotal();
        this.beneficeEchange = c.BeneficeEchangeInter(positionI, positionJ, longueurI, longueurJ);
        this.beneficeAutreEchange = c2.BeneficeEchangeInter(positionI, positionJ, longueurI, longueurJ);

        int benef = 0;//c.deltaBeneficeEchange(positionI, positionJ, longueurI, longueurJ) + c2.deltaBeneficeEchange(this.positionI, this.positionJ, this.longueurI, this.longueurJ);

        if (this.beneficeEchange == -1 || this.beneficeAutreEchange == -1) {
            return -1;
        }

        return benef;

    }

    @Override
    protected boolean doMouvement() {
        return this.echange.doEchange(this);
    }

}
