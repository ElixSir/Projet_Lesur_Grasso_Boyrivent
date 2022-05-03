package test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import instance.Instance;
import operateur.IntraEchangeCycle;
import operateur.OperateurIntraEchange;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;

/**
 *
 * @author yanni
 */
public class TestIntraEchangeCycle {
    
    public static void main(String[] args) {
       /* int id = 1;
        Instance inst = new Instance("test", 100, d);
        Client c1 = new Client(10, id++, 10, 0);
        Client c2 = new Client(10, id++, 20, 0);
        Client c3 = new Client(10, id++, 30, 0);
        Client c4 = new Client(10, id++, 40, 0);
        Client c5 = new Client(10, id++, 50, 0);
        
        inst.ajouterClient(c1);
        inst.ajouterClient(c2);
        inst.ajouterClient(c3);
        inst.ajouterClient(c4);
        inst.ajouterClient(c5);
        
        Tournee t = new Tournee(inst);
        
        t.ajouterClient(c2);
        t.ajouterClient(c1);
        t.ajouterClient(c3);
        t.ajouterClient(c4);
        t.ajouterClient(c5);
        
        System.out.println(t.deltaCoutEchange(0,1)); //-20
        System.out.println(t.deltaCoutEchange(3,4)); //0
        System.out.println(t.deltaCoutEchange(1,4)); //0
        System.out.println(t.deltaCoutEchange(1,5)); //Infinity
        System.out.println(t.deltaCoutEchange(2,3)); //20
        
        IntraEchange op = new IntraEchange(t,0,1); //-20
        IntraEchange op1 = new IntraEchange(t,3,4); //0
        OperateurLocal op2 = OperateurIntraTournee.getOperateurIntra(TypeOperateurLocal.INTRA_ECHANGE,t,1,4);
        IntraEchange op3 = new IntraEchange(t,1,5); //Infinity
        IntraEchange op4 = new IntraEchange(t,2,3); //20
        IntraEchange op5 = new IntraEchange(t,1,0); //20
        
        System.out.println(op); //-20
        System.out.println(op1); //0
        System.out.println(op2); //0
        System.out.println(op3); //Infinity
        System.out.println(op4); //20
        
        System.out.println(op.isMouvementRealisable()); //true
        System.out.println(op.isMouvementAmeliorant()); //true
        
        System.out.println(op1.isMouvementRealisable()); //true
        System.out.println(op1.isMouvementAmeliorant()); //false car deltaCout=0
        
        System.out.println(op3.isMouvementRealisable()); //false
        System.out.println(op3.isMouvementAmeliorant()); //false car deltaCout infini;
        
        System.out.println(op4.isMouvementRealisable()); //true
        System.out.println(op4.isMouvementAmeliorant()); //false car deltaCout=20
        */


        System.out.println("TestIntraEchangeCycle");
     
    }
            
    
}
