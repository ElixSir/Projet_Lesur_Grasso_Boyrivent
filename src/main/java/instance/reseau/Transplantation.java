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

    public Participant getBeneficiaire() {
        return beneficiaire;
    }

    public Participant getDonneur() {
        return donneur;
    }
    
}
