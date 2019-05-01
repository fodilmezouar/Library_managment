package darhadit.DataBase;

import com.mysql.jdbc.PreparedStatement;
import darhadit.adresse.Adresse;
import darhadit.alert.ClassAlert;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public final class DataBase {
    
    private static DataBase base=null;
    
    Adresse a = new Adresse();
    //private static final String username="root";
    //private static final String password="";
    
    private final String username = a.getUserName();
    private final String password = a.getMDP();
    private final String port = a.getPort();
   
    //private static final String conn_string="jdbc:mysql://localhost:3306/dar_el_hadith?&characterEncoding=UTF-8";
       
    private static Connection conn = null;
    private static Statement stmt = null;
    private static PreparedStatement stmPrep = null;
    
    private DataBase(){
        
        createConnection();
        createTableCategorie();
        createTableDocument();
        createTableExemplaire();
        createTableAdherent();
        createTableEmprunt();
        createTableRendre();
        createTableUser();
    }  
    
    public static DataBase getInstance(){
        if(base==null){
            base = new DataBase();
        }
        return base;
    }
    
    void createConnection(){
        /*
        String conn_string_MYSQL="jdbc:mysql://localhost:"+port;
        try{
            conn=DriverManager.getConnection(conn_string_MYSQL,username,password);
            System.out.println("connected To MYSQL");
        }catch(Exception e){
            ClassAlert.errorAlert("Verifier username et mot de passe de la Base de données", "erreur");
            e.printStackTrace();
        } 
        
        String Schemat_Name="dar_el_hadith";
        try{
            stmt=conn.createStatement();
            stmt.execute("CREATE SCHEMA IF NOT EXISTS `"+Schemat_Name+"` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci");
        }catch(SQLException e){
            ClassAlert.errorAlert("Verifier username et mot de passe de la Base de données", "erreur");
            System.err.println(e.getMessage()+ " ... Setup DataBase");
        }finally{
            
        }
        */
        String Schemat_Name="dar_el_hadith";
        String conn_string_BDD="jdbc:mysql://localhost:"+port+"/"+Schemat_Name+"?&characterEncoding=UTF-8";
        try{
            //conn.close();
            conn=DriverManager.getConnection(conn_string_BDD,username,password);
            System.out.println("connected TO BDD");
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
        /*try{
            conn=DriverManager.getConnection(conn_string,username,password);
            System.out.println("connected");
        }catch(Exception e){
            e.printStackTrace();
        } */
    }
    
    void createTableCategorie(){
        String Table_Name="Categorie";
        try{
            stmt=conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(password, username, Table_Name.toUpperCase(), null);
            
            if(tables.next()){
                System.out.println("Table "+ Table_Name+ "existe deja !!! ");
            } else{
                stmt.execute("CREATE TABLE IF NOT EXISTS "+Table_Name+"("
                            + "nomCategorie VARCHAR(40) NOT NULL,\n"
                            + "PRIMARY KEY (nomCategorie)"
                            + ")");
            }
        }catch(SQLException e){
            System.err.println(e.getMessage()+ " ... Setup DataBase");
        }finally{
            
        }
    }
    
    void createTableDocument(){
        String Table_Name="document";
        try{
            stmt=conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(password, username, Table_Name.toUpperCase(), null);
            
            if(tables.next()){
                System.out.println("Table "+ Table_Name+ "existe deja !!! ");
            } else{
                stmt.execute("CREATE TABLE IF NOT EXISTS "+Table_Name+"("
                            + "idDocument INT(6) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,\n"
                            + "Titre VARCHAR(50) NOT NULL,\n"
                            + "auteur VARCHAR(50) NOT NULL,\n"
                            + "maisonEdition VARCHAR(50) NULL,\n"             
                            + "nomCategorie VARCHAR(40) NOT NULL,\n"
                            + "types VARCHAR(20) NOT NULL,\n"
                            + "nbrExemplaire INT(3) NOT NULL DEFAULT 0,\n"
                            + "is_serie BOOLEAN NOT NULL,\n"
                            + "partie INT(2) NULL,\n"
                            + "nbrPieces INT(2) NULL,\n"                           
                            + "sommaire TEXT NULL,\n" 
                        
                            + "mot_cle TEXT NULL,\n" 
                        
                            + "PRIMARY KEY (idDocument),\n" 
                            + "INDEX FK_doc_categorie_idx (nomCategorie ASC),\n" 
+ "CONSTRAINT FK_doc_categorie FOREIGN KEY (nomCategorie) REFERENCES Categorie (nomCategorie) ON DELETE NO ACTION ON UPDATE CASCADE\n"
                            + " )");
            }
        }catch(SQLException e){
            System.err.println(e.getMessage()+ " ... SetupDataBase");
        }finally{
            
        }
    }
    
    private void createTableExemplaire() {
                String Table_Name="Exemplaire";
        try{
            stmt=conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(password, username, Table_Name.toUpperCase(), null);
            
            if(tables.next()){
                System.out.println("Table "+ Table_Name+ "existe deja !!! ");
            } else{
                stmt.execute("CREATE TABLE IF NOT EXISTS "+Table_Name+"("
                            + "idExemplaire INT(2) UNSIGNED ZEROFILL NOT NULL,\n"
                            + "idDocument INT(6) UNSIGNED ZEROFILL NOT NULL,\n"
                            + "emplacement VARCHAR(20) NOT NULL,\n"
                        
                            + "num_categorie INT(2) UNSIGNED NOT NULL,\n"
                        
                            + "disponible BOOLEAN NOT NULL DEFAULT true,\n"   
                            + "empruntable BOOLEAN NOT NULL,\n" 
                            + "sourceExemplaire VARCHAR(40) NULL,\n" 
                            + "dateEnregistrement DATE NOT NULL,\n"
                            + "prix decimal(10,2) NULL,\n"
                            + "version VARCHAR(40) NULL,\n"
                            + "PRIMARY KEY (idExemplaire,idDocument),\n"  
                            + "INDEX FK_exm_document_idx (idDocument ASC),\n"  
 + "CONSTRAINT FK_exm_document FOREIGN KEY (idDocument) REFERENCES Document (idDocument) ON DELETE NO ACTION ON UPDATE NO ACTION\n"
                        + " )");
            }
        }catch(SQLException e){
            System.err.println(e.getMessage()+ " ... SetupDataBase");
        }finally{
            
        }
    }
    
    private void createTableAdherent() {
        String Table_Name="Adherent";
        try{
            stmt=conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(password, username, Table_Name.toUpperCase(), null);
            
            if(tables.next()){
                System.out.println("Table "+ Table_Name+ "existe deja !!! ");
            } else{
                stmt.execute("CREATE TABLE IF NOT EXISTS "+Table_Name+"("
                            + "idAdherent INT(6) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,\n"
                            + "Nom VARCHAR(20) NOT NULL,\n"
                            + "Prenom VARCHAR(45) NOT NULL,\n"
                            + "sexe BOOLEAN NOT NULL,\n"
                            + "DateNaissance DATE NOT NULL,\n"
                            + "lieuNaissance VARCHAR(20) NULL,\n"
                            + "Adresse VARCHAR(60) NOT NULL,\n"
                            + "Telephone VARCHAR(15) NOT NULL,\n"
                            + "email VARCHAR(50) NULL,\n"
                            + "memo VARCHAR(200) NULL,\n"
                        
                            + "Specialite VARCHAR(30) NOT NULL,\n"
                            + "nbr_emp_possible INT(2) UNSIGNED NOT NULL DEFAULT 2,\n"
                        
                            + "DateEngagement DATE NOT NULL,\n"
                            + "DateRenouvelement DATE NOT NULL,\n"
                            + "PRIMARY KEY (idAdherent)\n" 
                            + ")");
            }
        }catch(SQLException e){
            System.err.println(e.getMessage()+ " ... SetupDataBase");
        }finally{
            
        }        
    }
    
    void createTableEmprunt(){
        String Table_Name="Emprunt";
        try{
            stmt=conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(password, username, Table_Name.toUpperCase(), null);
            
            if(tables.next()){
                System.out.println("Table "+ Table_Name+ "existe deja !!! ");
            } else{
                stmt.execute("CREATE TABLE IF NOT EXISTS "+Table_Name+"("
                            + "idAdherent INT(6) UNSIGNED ZEROFILL NOT NULL,\n"
                            + "idExemplaire INT(2) UNSIGNED ZEROFILL NOT NULL,\n"
                            + "idDocument INT(6) UNSIGNED ZEROFILL NOT NULL,\n"
                            + "dateEmprunt DATE NOT NULL,\n"
                            + "dateRetour DATE NOT NULL,\n"
                            + "PRIMARY KEY (idAdherent,idExemplaire,idDocument),\n"
                            + "INDEX idExemplaire_idx (idExemplaire ASC),\n"
                            + "INDEX FK_emp_document_idx (idDocument ASC),\n"
+ "CONSTRAINT FK_emp_adherent FOREIGN KEY (idAdherent) REFERENCES Adherent (idAdherent) ON DELETE NO ACTION ON UPDATE NO ACTION,\n"
+ "CONSTRAINT FK_emp_exemplaire FOREIGN KEY (idExemplaire,idDocument) REFERENCES Exemplaire(idExemplaire,idDocument) ON DELETE NO ACTION ON UPDATE NO ACTION,\n"
+ "CONSTRAINT FK_emp_document FOREIGN KEY (idDocument) REFERENCES Document (idDocument) ON DELETE NO ACTION ON UPDATE NO ACTION\n"
                            + ")");
            }
        }catch(SQLException e){
            System.err.println(e.getMessage()+ " ... SetupDataBase");
        }finally{
            
        }
    }    
    
    void createTableRendre(){
        String Table_Name="Rendre";
        try{
            stmt=conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(password, username, Table_Name.toUpperCase(), null);
            
            if(tables.next()){
                System.out.println("Table "+ Table_Name+ "existe deja !!! ");
            } else{
                stmt.execute("CREATE TABLE IF NOT EXISTS "+Table_Name+"("
                            + "idRendre INT UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,\n"
                            + "idAdherent INT(6) UNSIGNED ZEROFILL NOT NULL,\n"
                            + "idExemplaire INT(2) UNSIGNED ZEROFILL ,\n"
                            + "idDocument INT(6) UNSIGNED ZEROFILL ,\n"
                            + "dateEmprunt DATE NOT NULL,\n"
                            + "dateRetour DATE NOT NULL,\n"
                            + "PRIMARY KEY (idRendre),\n"
                            + "INDEX FK_rend_adherent_idx (idAdherent ASC),\n"
                            + "INDEX FK_rend_exemplaire_idx (idExemplaire ASC),\n"
                            + "INDEX FK_rend_document_idx (idDocument ASC),\n" 
+ "CONSTRAINT FK_rend_adherent FOREIGN KEY (idAdherent) REFERENCES Adherent (idAdherent) ON DELETE NO ACTION ON UPDATE NO ACTION,\n"
+ "CONSTRAINT FK_rend_exemplaire FOREIGN KEY (idExemplaire,idDocument) REFERENCES Exemplaire(idExemplaire,idDocument) ON DELETE SET NULL ON UPDATE NO ACTION,\n"
+ "CONSTRAINT FK_rend_document FOREIGN KEY (idDocument)  REFERENCES Document(idDocument) ON DELETE NO ACTION ON UPDATE NO ACTION\n"
                            + ")");
            }
        }catch(SQLException e){
            System.err.println(e.getMessage()+ " ... SetupDataBase");
        }finally{
            
        }
    }
    
    void createTableUser(){
        String Table_Name="user";
        try{
            stmt=conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(password, username, Table_Name.toUpperCase(), null);
            
            if(tables.next()){
                System.out.println("Table "+ Table_Name+ "existe deja !!! ");
            } else{
                stmt.execute("CREATE TABLE IF NOT EXISTS "+Table_Name+"("
                            + "username VARCHAR(40) NOT NULL,\n"
                            + "mot_de_passe VARCHAR(20) ,\n"
                            + "email VARCHAR(50),\n"
                            + "PRIMARY KEY (username,mot_de_passe,email)"
                            + ")");
            }
        }catch(SQLException e){
            System.err.println(e.getMessage()+ " ... Setup DataBase");
        }finally{
            
        }
    }
    
    public ResultSet execQuery(String query){
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exeption at execQuery "+ex.getLocalizedMessage());
               return null;
        } finally { 
        }
        return result;
    } 
    
    public boolean execAction(String qu){
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Erreur" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exeption at execAction"+ex.getLocalizedMessage());
               return false;
        } finally { 
        }
    }
    public PreparedStatement prepareStat(String query){
       
        try {
            stmPrep = (PreparedStatement) conn.prepareStatement(query);
        } catch (SQLException ex) {
            System.out.println("Exeption at execQuery "+ex.getLocalizedMessage());
               return null;
        } finally { 
        }
        return stmPrep;
        
        
    }
    
}
