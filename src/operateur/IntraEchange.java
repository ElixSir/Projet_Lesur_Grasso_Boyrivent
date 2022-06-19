/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operateur;

import instance.reseau.Paire;
import solution.ensemble.Chaine;
import solution.ensemble.Cycle;
import solution.ensemble.Echanges;

/**
 *
 * @author Bart
 */
public class IntraEchange extends OperateurIntraEchange {
    
    Echanges echangeFini;

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

    
    /**
     * Evalue le bénéfice de la fonction
     *
     * @return
     */
    @Override
    protected int evalBenefice() {
        if (echange == null) {
            return -1;
        }

    try{    
        
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
        System.out.println("PI" + positionI + "PJ" + positionJ + "LI" + longueurI + "LJ" + longueurJ);

        /////// Verification avant calcul
        if (positionI == positionJ) {
            System.out.println("positionI==positionJ");
            this.benefice = -1;
            return -1;
        }

        if (longueurI <= 0 || longueurJ <= 0) {
            System.out.println("longueurI<=0 ou longueurJ<=0");
            this.benefice = -1;
            return -1;
        }

        if (positionJ + longueurJ >= c.getPaires().size()) {
            System.out.println("posJ+longJ > size");
            this.benefice = -1;
            return -1;
        }

        if (positionI + longueurI >= c.getPaires().size() || positionI + longueurI >= positionJ) {
            System.out.println("posI+longI > size");
            this.benefice = -1;
            return -1;
        }

            
                //////////////////////////////////////////////
                
        for (int i = 0; i < longueurJ; i++) {
            c.getPairesReference().add(positionI + i, c.get(positionJ + i));
            c.getPairesReference().remove(positionJ + i + 1);

        }

        for (int i = 0; i < longueurI; i++) {
            c.getPairesReference().add(positionJ + longueurJ, c.get(positionI + longueurJ));
            c.getPairesReference().remove(positionI + longueurJ);

        }
        this.echangeFini = c;
        this.deltaBenefice = c.BeneficeEchange(positionI, positionJ, longueurI, longueurJ) - c.getBeneficeTotal();
        this.benefice = c.BeneficeEchange(positionI, positionJ, longueurI, longueurJ);

        return c.BeneficeEchange(positionI, positionJ, longueurI, longueurJ);
    }catch(Exception ex){ // si erreur alors pas de calcul
        this.benefice = -1;
        return -1;
    }
    }
    
    
    public boolean isMouvementAmeliorant(){
        if(deltaBenefice > 0)
            return true;
        return false;
    }

    public boolean isMouvementRealisable(){
            if(this.benefice == -1){
            return false;
        }
        return true;
    }

    public Echanges getEchangeFini() {
        return echangeFini;
    }
    
    
    
    @Override
    protected boolean doMouvement() {
        return this.echange.doEchange(this);
    }

}
