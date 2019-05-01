package darhadit.adresse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Adresse {
    public String port="";
    public String nomUser="";
    public String mdp="";
    int j;
    
    public Adresse(){
        try 
        {    
            File file = new File("parametre_BDD.txt");
            Scanner in = new Scanner(file);
            System.out.println("Fichier existe !!!");
                  
        }catch (IOException ex) {
            System.out.println("Fichier n'existe pas il a été creé!!!");
             FileWriter fluxw;
            try { 
                    fluxw = new FileWriter("parametre_BDD.txt");
                    fluxw.close();      
               } catch (IOException ex1) {
                       System.out.println("Fichier n'est pas trouver!!!");
               }
        }

        voirMDP();
    }

    public void voirMDP(){
        int i=0;
        j=0;
        try 
        {    
            File file = new File("parametre_BDD.txt");
            Scanner in = new Scanner(file);
            while(in.hasNextLine() && i<3){
                if(j==0){
                    port = in.nextLine();
                    j++;
                }
                else if(j==1){
                    nomUser = in.nextLine();
                    j++;
                }
                else if(j==2){
                    mdp = in.nextLine();
                    j++;
                }
                i++;
            }
                       
        }catch (IOException ex) {
            System.out.println("Fichier n'est pas trouver!!!");
        }
    }
    
    public String getPort()
    {
        return port;
    }
    
    public String getUserName()
    {
        return nomUser;
    }
    
    public String getMDP()
    {
        return mdp;
    }
}
