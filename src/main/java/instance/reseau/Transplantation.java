/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instance.reseau;

/**
 *
 * @author Bart
 */
public class Transplantation {
    private int benefice;
    private Participant beneficiaire;
    private Participant donneur;
    
    
    public Transplantation() {
        
       benefice = 0;
    }

    public int getBenefice() {
        return benefice;
    }
    
    public void setBenefice(int b){
        this.benefice = b;
    }
    
    public void addBenefice(int b){
        this.benefice += b;
    }

    public Participant getBeneficiaire() {
        return beneficiaire;
    }

    public Participant getDonneur() {
        return donneur;
    }
    
    public boolean ajouterDonneur(Participant p){
        if(p ==null) return false;
        if(this.donneur != null) return false; // si il est deja la on remplace pas
        
        this.donneur = p;
        
        return true;
    }
    
    public boolean ajouterBeneficiaire(Participant p){
        if(p == null) return false;
        if(this.beneficiaire != null) return false; // si il est deja la on remplace pas
        
        this.beneficiaire = p;
        
        return true;
    }
    
    
    
}
