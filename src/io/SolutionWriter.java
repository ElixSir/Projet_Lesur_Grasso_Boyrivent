/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io;

import instance.Instance;
import instance.reseau.Paire;
import io.exception.ReaderException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import solution.Solution;
import solution.ensemble.Chaine;
import solution.ensemble.Cycle;
import solveur.PLNE;
import solveur.LargeCycles;

/**
 *
 * @author Clem
 */
public class SolutionWriter { 
    
    private final String PATH_FILE = "solution.json";
    
    private JSONObject solutions;

    public SolutionWriter() {
        this.solutions = new JSONObject();
        
        this.solutions.put("solutions", new JSONArray());
    }
    
    public void ajouterSolution(Solution s, String nomSolution) {
        JSONObject sol = new JSONObject();
        
        sol.put("nom", nomSolution);
        sol.put("benefice", s.getCoutTotal());
        
        JSONArray cycles = new JSONArray();
        
        for (Cycle c : s.getCycles()) {
            JSONArray cycle = new JSONArray();
            
            for (Paire paire : c.getPaires()) {
                cycle.put(paire.getId());
            }
            
            cycles.put(cycle);
        }
        
        sol.put("cycles", cycles);
        
        JSONArray chaines = new JSONArray();
        
        for (Chaine c : s.getChaines()) {
            JSONArray chaine = new JSONArray();
            
            chaine.put(c.getAltruiste().getId());
            
            for (Paire paire : c.getPaires()) {
                chaine.put(paire.getId());
            }
            
            chaines.put(chaine);
        }
        
        sol.put("chaines", chaines);
        sol.put("nom_instance", s.getInstance().getNom());
        
        this.solutions.getJSONArray("solutions").put(sol);
        
    }
    
    public void write() {
        this.write("");
    }
    
    public boolean write(String path) {
        if( null == path ) return false;
        
        if( path.equals("") ) {
            path = this.PATH_FILE;
        }
        
        File solutionFile = new File(path);
        
        
        
        try{
            if(! solutionFile.exists()) {
                solutionFile.createNewFile();
            }
            
            FileWriter f = new FileWriter(solutionFile.getAbsolutePath());
            BufferedWriter br = new BufferedWriter(f);
            
            br.write(this.solutions.toString()); //this.solutions.toString()
            
            br.close();
            f.close();
            
        } catch (FileNotFoundException ex) {
            System.out.println("File not Found");
            return false;
        } catch (IOException ex) {
            System.out.println("IO");
            return false;
        }
        
        return true;
    }
    
    public static void main(String[] args) {
        // KEP_p9_n1_k3_l3
        // KEP_p100_n11_k3_l7
        try {
            InstanceReader read = new InstanceReader("instancesInitiales/KEP_p100_n11_k3_l7.txt");
            Instance i = read.readInstance();

            LargeCycles algo1 = new LargeCycles();
            PLNE algo2 = new PLNE();

            Solution sol1 = algo1.solve(i);
            Solution sol2 = algo2.solve(i);
            
            SolutionWriter sw = new SolutionWriter();
            
            sw.ajouterSolution(sol1, "Soltution Larges Cycles");
            sw.ajouterSolution(sol2, "Soltution Encore Un Arbre");
            
            //if(!sw.write("C:\\Users\\felix\\Interface web\\src\\data\\db\\solutions.json")) {
            if(!sw.write("Interface web\\\\src\\\\data\\\\db\\\\solutions.json")) {
                System.out.println("erreur");
            }
            

        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}