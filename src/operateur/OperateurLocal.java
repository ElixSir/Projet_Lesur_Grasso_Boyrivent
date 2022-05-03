/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operateur;

import ensemble.Echanges;
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

    public OperateurLocal() {
        this.positionI = -1;
        this.positionJ = -1;      
    }
    
    public OperateurLocal(Echanges echange, int positionI, int positionJ) { 
        super(echange);
        this.positionI = positionI;
        this.positionJ = positionJ;
        this.paireI = (Paire) this.echange.getCurrent(positionI);
        this.paireJ = (Paire) this.echange.getCurrent(positionJ);
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
            case INTER_DEPLACEMENT_CHAINE:
                return null; // new InterDeplacementChaine();
            case INTER_DEPLACEMENT_CYCLE: 
                return null;// new InterDeplacementCycle();
            case INTER_ECHANGE_CHAINE:
                return null;//new InterEchangeChaine();
            case INTER_ECHANGE_CYCLE:
                return null;//new InterEchangeCycle();
            case INTRA_DEPLACEMENT_CHAINE:
                return null;//new IntraDeplacementChaine();
            case INTRA_DEPLACEMENT_CYCLE:
                return null;//new IntraDeplacementCycle();
            case INTRA_ECHANGE_CHAINE:
                return null;//new IntraEchangeChaine();
            case INTRA_ECHANGE_CYCLE:
                return new IntraEchangeCycle();
            default:
                return null;
        }
    }
    
    public static OperateurIntraEchange getOperateurIntra(TypeOperateurLocal type, Echanges echange, int positionI, int positionJ) {
        switch(type) {
            case INTRA_DEPLACEMENT_CHAINE:
                return null;//new IntraDeplacementChaine(echange, positionI, positionJ);
            case INTRA_DEPLACEMENT_CYCLE:
                return null;//new IntraDeplacementCycle(echange, positionI, positionJ);
            case INTRA_ECHANGE_CHAINE:
                return null;//new IntraEchangeChaine(echange, positionI, positionJ);
            case INTRA_ECHANGE_CYCLE:
                return new IntraEchangeCycle(echange, positionI, positionJ);
            default:
                return null;
        }
    }
    
     public static OperateurInterEchange getOperateurInter(TypeOperateurLocal type, Echanges echange, Echanges autreEchange, int positionI, int positionJ) {
        switch(type) {
            case INTER_DEPLACEMENT_CHAINE:
                return null;//new InterDeplacementChaine(echange, autreEchange, positionI, positionJ);
            case INTER_DEPLACEMENT_CYCLE:
                return null;//new InterDeplacementCycle(echange, autreEchange, positionI, positionJ);
            case INTER_ECHANGE_CHAINE:
                return null;//new InterEchangeChaine(echange, autreEchange, positionI, positionJ);
            case INTER_ECHANGE_CYCLE:
                return null;//new InterEchangeCycle(echange, autreEchange, positionI, positionJ);
            default:
                return null;
        }
    }

  //  public abstract boolean isTabou(OperateurLocal operateur); // à Ajouter dans le futur
     
     

}
