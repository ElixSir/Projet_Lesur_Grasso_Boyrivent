/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operateur;

import solution.ensemble.Chaine;
import solution.ensemble.Cycle;
import solution.ensemble.Echanges;

public class InterEchange extends OperateurInterEchange {

    Echanges echangeFini;
    Echanges autreEchangeFini;

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

    @Override
    protected int evalBenefice() {
        if (echange == null) {
            this.benefice = -1;
            this.beneficeAutreEchange = -1;
            return -1;
        }
        if (autreEchange == null) {
            this.benefice = -1;
            this.beneficeAutreEchange = -1;
            return -1;
        }
        try {

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

            ////////////////////////////////////// Verification compatibilité avant calcul ////////////////
            if (positionJ + longueurJ > c.getSize()) {
                this.benefice = -1;
                return -1;
            }

            if (c.getPrec(positionI).getBeneficeVers(c2.get(positionJ)) == -1) {
                this.benefice = -1;
                this.beneficeAutreEchange = -1;
                return -1;
            }
            if (c2.get(positionJ + longueurJ - 1).getBeneficeVers(c.get(positionI + longueurI)) == -1) {
                this.benefice = -1;
                this.beneficeAutreEchange = -1;
                return -1;
            }

            if (c2.getPrec(positionJ).getBeneficeVers(c.get(positionI)) == -1) {
                this.benefice = -1;
                this.beneficeAutreEchange = -1;
                return -1;
            }

            if (c.get(positionI + longueurI - 1).getBeneficeVers(c2.get(positionJ + longueurJ)) == -1) {
                this.benefice = -1;
                this.beneficeAutreEchange = -1;
                return -1;
            }

            ///////////////////////////////////////
            //calcul
            for (int i = 0; i < longueurJ; i++) {
                c.getPairesReference().add(positionI + i, c2.get(positionJ));
                c2.getPairesReference().remove(positionJ);

            }

            for (int i = 0; i < longueurI; i++) {
                c2.getPairesReference().add(positionJ + i, c.get(positionI + longueurJ));
                c.getPairesReference().remove(positionI + longueurJ);

            }

            this.echangeFini = c;
            this.autreEchangeFini = c2;

            // resultats
            this.deltaBeneficeEchange = c.BeneficeEchangeInter(positionI, positionJ, longueurI, longueurJ) - c.getBeneficeTotal();
            this.deltaBeneficeAutreEchange = c2.BeneficeEchangeInter(positionI, positionJ, longueurI, longueurJ) - c2.getBeneficeTotal();
            this.beneficeEchange = c.BeneficeEchangeInter(positionI, positionJ, longueurI, longueurJ);
            this.beneficeAutreEchange = c2.BeneficeEchangeInter(positionI, positionJ, longueurI, longueurJ);

            this.deltaBenefice = this.deltaBeneficeEchange + this.deltaBeneficeAutreEchange;

            int benef = c.BeneficeEchange(positionI, positionJ, longueurI, longueurJ) + c2.BeneficeEchange(this.positionI, this.positionJ, this.longueurI, this.longueurJ);
            this.benefice = this.deltaBenefice + this.beneficeAutreEchange;

            if (this.beneficeEchange == -1 || this.beneficeAutreEchange == -1) {
                this.benefice = -1;
                return -1;
            }

            return benef;

        } catch (Exception ex) { // si erreur quelquonque je fais pas de calcul
            this.beneficeEchange = -1;
            this.beneficeAutreEchange = -1;
            return -1;
        }

    }

    public boolean isMouvementAmeliorant() {
        if (deltaBenefice + deltaBeneficeAutreEchange > 0) {
            return true;
        }
        return false;
    }

    public boolean isMouvementRealisable() {
        if (this.beneficeEchange == -1 || this.beneficeAutreEchange == -1) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean doMouvement() {
        return this.echange.doEchange(this);
    }

    public Echanges getEchangeFini() {
        return this.echangeFini;
    }

    public Echanges getAutreEchangeFini() {
        return this.autreEchangeFini;
    }

}
