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
import instance.Instance;
import instance.reseau.Altruiste;
import instance.reseau.Transplantation;
import io.exception.ReaderException;

/**
 *
 * @author Clem
 */
public class InstanceWriter {
    
    private final String PATH_FILE = "instances.json";
    
    private JSONObject instances;

    public InstanceWriter() {
        this.instances = new JSONObject();
        
        this.instances.put("instances", new JSONArray());
    }
    
    public void ajouterInstance(Instance i) {
        JSONObject sol = new JSONObject();
        
        sol.put("nom", i.getNom());
        sol.put("maxChaines", i.getMaxChaines());
        sol.put("maxCycles", i.getMaxCycles());
        sol.put("nbAltruistes", i.getNbAltruistes());
        sol.put("nbPaires", i.getNbPaires());
        
        JSONArray altruistes = new JSONArray();
        
        for (Altruiste a : i.getAltruistes()) {
            JSONObject altruiste = new JSONObject();
            JSONArray transplantations = new JSONArray();
            
            altruiste.put("id", a.getId());
            
            for (Transplantation t : a.getTransplantations()) {
                JSONObject transplantation = new JSONObject();
                
                transplantation.put("beneficiaire", t.getBeneficiaire().getId());
                transplantation.put("benefice", t.getBenefice());

                transplantations.put(transplantation);
            }
            
            altruiste.put("transplantations", transplantations);
            
            altruistes.put(altruiste);
        }
        
        sol.put("altruistes", altruistes);
        
        JSONArray paires = new JSONArray();
        
        for (Paire p : i.getPaires()) {
            JSONObject paire = new JSONObject();
            JSONArray transplantations = new JSONArray();
            
            paire.put("id", p.getId());
            
            for (Transplantation t : p.getTransplantations()) {
                JSONObject transplantation = new JSONObject();
                
                transplantation.put("beneficiaire", t.getBeneficiaire().getId());
                transplantation.put("benefice", t.getBenefice());

                transplantations.put(transplantation);
            }
            
            paire.put("transplantations", transplantations);
            
            paires.put(paire);
        }
        
        sol.put("paires", paires);
        
        this.instances.getJSONArray("instances").put(sol);
        
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
            
            br.write(this.instances.toString()); //this.solutions.toString()
            
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
        String path = "C:\\Users\\Clem\\Documents\\Travail\\Le4\\POO\\Projet\\Interface web\\src\\data\\db\\instances.json";
        String[] instancesDirectories = {"instancesInitiales","instancesFinales"};
        InstanceWriter iw = new InstanceWriter();

        for (String instancesDirectory : instancesDirectories) {
            File[] listOfFiles;
            if ( (listOfFiles = new File(instancesDirectory).listFiles()) != null) {
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        try {

                            InstanceReader reader = new InstanceReader(file.getAbsolutePath());

                            Instance i = reader.readInstance();

                            iw.ajouterInstance(i);

                        } catch (ReaderException ex) {
                            System.out.println("L'instance " + file.getAbsolutePath()
                                    + " n'a pas pu etre lue correctement");
                        }
                    }
                }
            }
        }

        iw.write(path);
    }
}
