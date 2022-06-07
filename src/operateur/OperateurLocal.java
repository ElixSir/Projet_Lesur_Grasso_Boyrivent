/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operateur;

import solution.Echanges;
import instance.reseau.Paire;


/**
 *
 * @author lucas
 */
public abstract class OperateurLocal extends Operateur{
    
    protected int positionI;
    protected int positionJ;
    protected Paire paireI;
    protected Paire paireJ;
    protected int longueurI;

    public OperateurLocal() {
        this.positionI = -1;
        this.positionJ = -1;      
    }
    
    public OperateurLocal(Echanges echange, int positionI, int positionJ, int longueurI) { 
        super(echange);
        this.positionI = positionI;
        this.positionJ = positionJ;
        this.longueurI = longueurI;
        this.paireI = this.echange.getPairePosition(positionI);
        this.paireJ = this.echange.getPairePosition(positionJ);
    }
    
    public int getPositionI() {
        return positionI;
    }

    public int getPositionJ() {
        return positionJ;
    }

    public Paire getClientI() {
        return this.paireI;
    }

    public Paire getClientJ() {
        return this.paireJ;
    }
     
    
    public static OperateurLocal getOperateur(TypeOperateurLocal type){
        switch(type){
            case INTER_DEPLACEMENT:
                return new InterDeplacement();
            case INTER_ECHANGE:
                return null;//new InterEchange();
            case INTRA_DEPLACEMENT:
                return new IntraDeplacement();
            case INTRA_ECHANGE:
                return new IntraEchange();
            default:
                return null;
        }
    }
    
    public static OperateurIntraEchange getOperateurIntra(TypeOperateurLocal type, Echanges echange, int positionI, int positionJ, int longueurI, int longueurJ) {
        switch(type) {
            case INTRA_DEPLACEMENT:
                return new IntraDeplacement(positionI, positionJ, echange, longueurI);
            case INTRA_ECHANGE:
                return new IntraEchange(echange, positionI, positionJ, longueurI);
            default:
                return null;
        }
    }
    
     public static OperateurInterEchange getOperateurInter(TypeOperateurLocal type, Echanges echange, Echanges autreEchange, int positionI, int positionJ, int longueurI, int longueurJ) {
        switch(type) {
            case INTER_DEPLACEMENT:
                return new InterDeplacement(echange, autreEchange, positionI, positionJ, longueurI);
            case INTER_ECHANGE:
                return null;//new InterEchange(echange, autreEchange, positionI, positionJ);
            default:
                return null;
        }
    }

  //  public abstract boolean isTabou(OperateurLocal operateur); // � Ajouter dans le futur
     
     

}
