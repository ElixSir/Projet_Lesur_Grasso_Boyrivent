/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io;

import instance.reseau.Paire;
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

/**
 *
 * @author Clem
 */
public class InstanceWriter {
    
    private final String PATH_FILE = "instance.json";
    
    private JSONObject solutions;

    public InstanceWriter() {
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
}
