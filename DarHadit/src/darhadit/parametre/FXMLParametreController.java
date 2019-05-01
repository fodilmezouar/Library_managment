package darhadit.parametre;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mysql.jdbc.PreparedStatement;
import darhadit.DataBase.DataBase;
import darhadit.alert.ClassAlert;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Pc
 */
public class FXMLParametreController implements Initializable {

    DataBase base;
    
    @FXML
    private JFXTextField nom_utilisatuer;
    @FXML
    private JFXPasswordField mot_de_passe;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXCheckBox modifier_mdp;
    @FXML
    private JFXPasswordField mot_de_passe1;
    @FXML
    private JFXPasswordField mot_de_passe2;
    @FXML
    private JFXTextField adresse_ip;
    @FXML
    private Button voir_file;
    @FXML
    private Button voir_excel;
    @FXML
    private TextField text_file;
    @FXML
    private TextField text_file1;
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        base = DataBase.getInstance();
        nom_utilisatuer.setText("مسؤول المكتبة");
        nom_utilisatuer.setEditable(false);
        modifier_mdp.setSelected(false);
        mot_de_passe1.setDisable(true);
        mot_de_passe2.setDisable(true);
        
        text_file.setDisable(true);
        voir_file.setDisable(true);
        
        try{
            InetAddress AdresseMachineServeur = InetAddress.getLocalHost();
            adresse_ip.setText(AdresseMachineServeur.getHostAddress());
            adresse_ip.setEditable(false);
            //System.out.println(AdresseMachineServeur.getHostName().toString());
            System.out.println(AdresseMachineServeur.getHostAddress());
        } catch(UnknownHostException EX){
            System.out.println("erruer");
        }
        
        String qu = "select mot_de_passe,email from user";
        ResultSet rs = base.execQuery(qu);
        try {
            if(rs.next()){
                if (rs.getString("mot_de_passe").equals("")) {
                    email.setText(rs.getString("email"));
                    text_file.setDisable(false);
                    text_file.setEditable(false);
                    voir_file.setDisable(false);
                }
            }
        } catch (SQLException ex) { 
        }
    }    

    @FXML
    private void verifier_mdp(ActionEvent event) {
        String qu = "select mot_de_passe,email from user";
        ResultSet rs = base.execQuery(qu);
        try {
            if(rs.next()){
                if (mot_de_passe.getText().equals(rs.getString("mot_de_passe"))) {
                    email.setText(rs.getString("email"));
                    //text_file.setEditable(false);
                    text_file.setDisable(false);
                    text_file.setEditable(false);
                    voir_file.setDisable(false);
                    //mot_de_passe.getStyleClass().add("true");
                }
                else{
                    vider_venetre(new ActionEvent());
                    //mot_de_passe.getStyleClass().add("false");
                }
            }
        } catch (SQLException ex) {
            
        } 
    }
    

    @FXML
    private void operation_modifier_mdp(ActionEvent event) {
        String qu = "select * from user";
        ResultSet rs = base.execQuery(qu);
        try {
            if(rs.next()){
                if (mot_de_passe.getText().equals(rs.getString("mot_de_passe")) ) {
                    if(modifier_mdp.isSelected())
                    {
                        mot_de_passe1.setDisable(false);
                        mot_de_passe2.setDisable(false);
                    }
                    else{
                        mot_de_passe1.setDisable(true);
                        mot_de_passe2.setDisable(true);
                        
                        mot_de_passe1.setText("");
                        mot_de_passe2.setText("");
                    }
                }
                else{
                    ClassAlert.errorAlert("يرجي إدخال كلمة السر", "خطأ");
                    if(modifier_mdp.isSelected()){
                        //mot_de_passe.getStyleClass().add("false");
                        modifier_mdp.setSelected(false);
                    }
                }
            }
        } catch (SQLException ex) {
            
        }    
    }

    @FXML
    private void vider_venetre(ActionEvent event) {
        mot_de_passe.setText("");
        email.setText("");
        modifier_mdp.setSelected(false);
        mot_de_passe1.setText("");
        mot_de_passe2.setText("");
        mot_de_passe1.setDisable(true);
        mot_de_passe2.setDisable(true);
        modifier_mdp.setSelected(false);
        mot_de_passe.getStyleClass().add("vide");
    }

    @FXML
    private void verifier_mdp_nouv(ActionEvent event) {
        if(mot_de_passe1.getText().equals(mot_de_passe2.getText())){
            //mot_de_passe2.getStyleClass().add("true");
            //mot_de_passe1.getStyleClass().add("true");
        }
        else{
            ClassAlert.errorAlert("يرجي تأكد من كلمة المرور الجديدة", "خطأ");
            //mot_de_passe2.getStyleClass().add("false");
            //mot_de_passe1.getStyleClass().add("false");
        }
    }

    @FXML
    private void enregistrer(ActionEvent event) throws IOException {
        String qu1 = "select * from user";
        ResultSet rs = base.execQuery(qu1);
        try {
            if(rs.next()){
                if (mot_de_passe.getText().equals(rs.getString("mot_de_passe"))) {
                    if(!mot_de_passe1.getText().equals(mot_de_passe2.getText())){
                        ClassAlert.errorAlert("يرجي تأكد من كلمة المرور الجديدة", "خطأ");
                        return;
                    }
                    if(ClassAlert.confirmDialog("يرجى تأكيد لتغيير المعلومات", "تأكيد")){
                    String qu = "update user set email='"+email.getText()+"'";
                    if(!text_file.getText().trim().equals("")){
                        enregistrer_BDD();
                    }
                    if(!text_file1.getText().trim().equals("")){
                       // ExportExcel();
                    }
                    
                        if(modifier_mdp.isSelected()){
                            if(mot_de_passe1.getText().equals(mot_de_passe2.getText())){
                                qu += " , mot_de_passe='" + mot_de_passe2.getText()+"'"; 
                            }
                            else{
                                ClassAlert.errorAlert("يرجي تأكد من كلمة المرور الجديدة", "خطأ");
                                return;
                            }
                        }
                        base.execAction(qu);
                        ((Stage) mot_de_passe.getScene().getWindow()).close();
                        ClassAlert.infoAlert("تم تغيير المعلومات", "نجاح");
                    }
                    else {
                        ClassAlert.infoAlert("عمليةالتغيير ملغاة", "إلغاء");
                        ((Stage) mot_de_passe.getScene().getWindow()).close();
                    }
                }
                else{
                    ClassAlert.errorAlert("يرجي إدخال كلمة السر", "خطأ");
                }
            }
        } catch (SQLException ex) {
            
        } 
        
    }
    
    private void enregistrer_BDD()
    {
        try 
        {    
            File file = new File(text_file.getText()+"\\Copie De La Base De Donnée" + LocalDate.now() + ".sql");
            Scanner in = new Scanner(file);
            System.out.println("Fichier existe !!!");
                  
        }catch (IOException ex) {
            System.out.println("Fichier n'existe pas il a été creé!!!");
             FileWriter fluxw;
            try { 
                    fluxw = new FileWriter(text_file.getText()+"\\Copie De La Base De Donnée" + LocalDate.now() + ".sql");
                    fluxw.close();      
               } catch (IOException ex1) {
                       System.out.println("Fichier n'est pas trouver!!!");
               }
        }
        FileWriter fluxw;
        BufferedWriter output;
        PrintWriter writer;
        String nomBDD="dar_el_hadith";
        try {
            writer = new PrintWriter(text_file.getText()+"\\Copie De La Base De Donnée.sql");
            writer.println("CREATE DATABASE  IF NOT EXISTS `"+nomBDD+"` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;\n" +
                        "USE `"+nomBDD+"`;\n" +
                        
                        "/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;\n" +
                        "/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;\n" +
                        "/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;\n" +
                        "/*!40101 SET NAMES utf8 */;\n" +
                        "/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;\n" +
                        "/*!40103 SET TIME_ZONE='+00:00' */;\n" +
                        "/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;\n" +
                        "/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;\n" +
                        "/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;\n" +
                        "/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;\n" +
                        "\n" +
                        "--\n" +
                        "-- Table structure for table `adherent`\n" +
                        "--");
            writer.println("");
            //adherent
            writer.println("DROP TABLE IF EXISTS `adherent`;\n" +
                        "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
                        "/*!40101 SET character_set_client = utf8 */;");
            writer.println("");
            writer.println("CREATE TABLE `adherent` (\n" +
                        "  `idAdherent` int(6) unsigned zerofill NOT NULL AUTO_INCREMENT,\n" +
                        "  `Nom` varchar(20) COLLATE utf8_unicode_ci NOT NULL,\n" +
                        "  `Prenom` varchar(45) COLLATE utf8_unicode_ci NOT NULL,\n" +
                        "  `sexe` tinyint(1) NOT NULL,\n" +
                        "  `DateNaissance` date NOT NULL,\n" +
                        "  `lieuNaissance` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,\n" +
                        "  `Adresse` varchar(60) COLLATE utf8_unicode_ci NOT NULL,\n" +
                        "  `Telephone` varchar(15) COLLATE utf8_unicode_ci NOT NULL,\n" +
                        "  `email` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,\n" +
                        "  `memo` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,\n" +
                        "  `Specialite` varchar(30) COLLATE utf8_unicode_ci NOT NULL,\n" +
                        "  `nbr_emp_possible` int(2) unsigned NOT NULL DEFAULT '2',\n" +
                        "  `DateEngagement` date NOT NULL,\n" +
                        "  `DateRenouvelement` date NOT NULL,\n" +
                        "  PRIMARY KEY (`idAdherent`)\n" +
                        ") ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;\n" +
                        "/*!40101 SET character_set_client = @saved_cs_client */;");
            writer.println("");
            
            String qu1 = "select * from adherent";
            ResultSet rs = base.execQuery(qu1);
            String msg="";
            Boolean a = true;
            Boolean b = false;
            try {
                while(rs.next()){
                    if(a==true){
                        msg += "INSERT INTO adherent VALUES ";
                        a=false;
                    }
                    if(b==true){
                        msg += ",";
                    }
                    msg += "("+rs.getInt("idAdherent")+",'"+rs.getString("Nom")+"','"+rs.getString("Prenom")+"',"+rs.getBoolean("sexe")+",'"+rs.getString("DateNaissance")+"','"+rs.getString("lieuNaissance")+"','"+rs.getString("Adresse")+"','"+rs.getString("Telephone")+"','"+rs.getString("email")+"','"+rs.getString("memo")+"','"+rs.getString("Specialite")+"',"+rs.getInt("nbr_emp_possible")+",'"+rs.getString("DateEngagement")+"','"+rs.getString("DateRenouvelement")+"')";
                    b=true;        
                }
                writer.println(msg+";");  
            } catch (SQLException ex) {
                //Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            writer.println("/*!40000 ALTER TABLE `adherent` ENABLE KEYS */;\n" +"UNLOCK TABLES;");  
            writer.println("");  
                //categorie
            writer.println("DROP TABLE IF EXISTS `categorie`;\n" +
                            "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
                            "/*!40101 SET character_set_client = utf8 */;");
            writer.println("");
            writer.println("CREATE TABLE `categorie` (\n" +
                        "  `nomCategorie` varchar(40) COLLATE utf8_unicode_ci NOT NULL,\n" +
                        "  PRIMARY KEY (`nomCategorie`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;\n" +
                        "/*!40101 SET character_set_client = @saved_cs_client */;");
            writer.println("");
            
            qu1 = "select * from categorie";
            rs = base.execQuery(qu1);
            msg="";
            a = true;
            b = false;
            try {
                while(rs.next()){
                    if(a==true){
                        msg += "INSERT INTO categorie VALUES ";
                        a=false;
                    }
                    if(b==true){
                        msg += ",";
                    }
                    msg += "('"+rs.getString("nomCategorie")+"')";
                    b=true;        
                }
                writer.println(msg+";");
            } catch (SQLException ex) {
                //Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            
            writer.println("/*!40000 ALTER TABLE `categorie` ENABLE KEYS */;\n" +"UNLOCK TABLES;");  
            writer.println("");  
                //document
            writer.println("DROP TABLE IF EXISTS `document`;\n" +
                        "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
                        "/*!40101 SET character_set_client = utf8 */;");
            writer.println("");
            writer.println("CREATE TABLE `document` (\n" +
                    "  `idDocument` int(6) unsigned zerofill NOT NULL AUTO_INCREMENT,\n" +
                    "  `Titre` varchar(50) COLLATE utf8_unicode_ci NOT NULL,\n" +
                    "  `auteur` varchar(50) COLLATE utf8_unicode_ci NOT NULL,\n" +
                    "  `maisonEdition` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,\n" +
                    "  `nomCategorie` varchar(40) COLLATE utf8_unicode_ci NOT NULL,\n" +
                    "  `types` varchar(20) COLLATE utf8_unicode_ci NOT NULL,\n" +
                    "  `nbrExemplaire` int(3) NOT NULL DEFAULT '0',\n" +
                    "  `is_serie` tinyint(1) NOT NULL,\n" +
                    "  `partie` int(2) DEFAULT NULL,\n" +
                    "  `nbrPieces` int(2) DEFAULT NULL,\n" +
                    "  `sommaire` text COLLATE utf8_unicode_ci,\n" +
                    "  `mot_cle` text COLLATE utf8_unicode_ci,\n" +
                    "  PRIMARY KEY (`idDocument`),\n" +
                    "  KEY `FK_doc_categorie_idx` (`nomCategorie`),\n" +
                    "  CONSTRAINT `FK_doc_categorie` FOREIGN KEY (`nomCategorie`) REFERENCES `categorie` (`nomCategorie`) ON DELETE NO ACTION ON UPDATE CASCADE\n" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;\n" +
                    "/*!40101 SET character_set_client = @saved_cs_client */;");
            writer.println("");
            
            qu1 = "select * from document";
            rs = base.execQuery(qu1);
            msg="";
            a = true;
            b = false;
            try {
                while(rs.next()){
                    if(a==true){
                        msg += "INSERT INTO document VALUES ";
                        a=false;
                    }
                    if(b==true){
                        msg += ",";
                    }
                    msg += "("+rs.getInt("idDocument")+",'"+rs.getString("Titre")+"','"+rs.getString("auteur")+"','"+rs.getString("maisonEdition")+"','"+rs.getString("nomCategorie")+"','"+rs.getString("types")+"',"+rs.getInt("nbrExemplaire")+","+rs.getBoolean("is_serie")+","+rs.getInt("partie")+","+rs.getInt("nbrPieces")+",'"+rs.getString("sommaire")+"','"+rs.getString("mot_cle")+"')";
                    
                    b=true;        
                }
                writer.println(msg+";");
            } catch (SQLException ex) {
                //Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            writer.println("/*!40000 ALTER TABLE `document` ENABLE KEYS */;\n" +"UNLOCK TABLES;");  
            writer.println("");
            
            //exemplaire
            writer.println("DROP TABLE IF EXISTS `exemplaire`;\n" +
                    "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
                    "/*!40101 SET character_set_client = utf8 */;");
            writer.println("");
            writer.println("CREATE TABLE `exemplaire` (\n" +
                    "  `idExemplaire` int(2) unsigned zerofill NOT NULL,\n" +
                    "  `idDocument` int(6) unsigned zerofill NOT NULL,\n" +
                    "  `emplacement` varchar(20) COLLATE utf8_unicode_ci NOT NULL,\n" +
                    "  `num_categorie` int(2) unsigned NOT NULL,\n" +
                    "  `disponible` tinyint(1) NOT NULL DEFAULT '1',\n" +
                    "  `empruntable` tinyint(1) NOT NULL,\n" +
                    "  `sourceExemplaire` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,\n" +
                    "  `dateEnregistrement` date NOT NULL,\n" +
                    "  `prix` decimal(10,2) DEFAULT NULL,\n" +
                    "  `version` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,\n" +
                    "  PRIMARY KEY (`idExemplaire`,`idDocument`),\n" +
                    "  KEY `FK_exm_document_idx` (`idDocument`),\n" +
                    "  CONSTRAINT `FK_exm_document` FOREIGN KEY (`idDocument`) REFERENCES `document` (`idDocument`) ON DELETE NO ACTION ON UPDATE NO ACTION\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;\n" +
                    "/*!40101 SET character_set_client = @saved_cs_client */;");
            writer.println("");
            
            qu1 = "select * from exemplaire";
            rs = base.execQuery(qu1);
            msg="";
            a = true;
            b = false;
            try {
                while(rs.next()){
                    if(a==true){
                        msg += "INSERT INTO exemplaire VALUES ";
                        a=false;
                    }
                    if(b==true){
                        msg += ",";
                    }
                    msg += "("+rs.getInt("idExemplaire")+","+rs.getInt("idDocument")+",'"+rs.getString("emplacement")+"',"+rs.getInt("num_categorie")+","+rs.getBoolean("disponible")+","+rs.getBoolean("empruntable")+",'"+rs.getString("sourceExemplaire")+"','"+rs.getString("dateEnregistrement")+"',"+rs.getFloat("prix")+",'"+rs.getString("version")+"')";
                    
                    b=true;        
                }
                writer.println(msg+";");
            } catch (SQLException ex) {
                //Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            writer.println("/*!40000 ALTER TABLE `exemplaire` ENABLE KEYS */;\n" +"UNLOCK TABLES;");  
            writer.println("");
            
            //emprun
            writer.println("DROP TABLE IF EXISTS `emprunt`;\n" +
                        "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
                    "/*!40101 SET character_set_client = utf8 */;");
            writer.println("");
            writer.println("CREATE TABLE `emprunt` (\n" +
                    "  `idAdherent` int(6) unsigned zerofill NOT NULL,\n" +
                    "  `idExemplaire` int(2) unsigned zerofill NOT NULL,\n" +
                    "  `idDocument` int(6) unsigned zerofill NOT NULL,\n" +
                    "  `dateEmprunt` date NOT NULL,\n" +
                    "  `dateRetour` date NOT NULL,\n" +
                    "  PRIMARY KEY (`idAdherent`,`idExemplaire`,`idDocument`),\n" +
                    "  KEY `idExemplaire_idx` (`idExemplaire`),\n" +
                    "  KEY `FK_emp_document_idx` (`idDocument`),\n" +
                    "  KEY `FK_emp_exemplaire` (`idExemplaire`,`idDocument`),\n" +
                    "  CONSTRAINT `FK_emp_adherent` FOREIGN KEY (`idAdherent`) REFERENCES `adherent` (`idAdherent`) ON DELETE NO ACTION ON UPDATE NO ACTION,\n" +
                    "  CONSTRAINT `FK_emp_document` FOREIGN KEY (`idDocument`) REFERENCES `document` (`idDocument`) ON DELETE NO ACTION ON UPDATE NO ACTION,\n" +
                    "  CONSTRAINT `FK_emp_exemplaire` FOREIGN KEY (`idExemplaire`, `idDocument`) REFERENCES `exemplaire` (`idExemplaire`, `idDocument`) ON DELETE NO ACTION ON UPDATE NO ACTION\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;\n" +
                    "/*!40101 SET character_set_client = @saved_cs_client */;");
            writer.println("");
            
            qu1 = "select * from emprunt";
            rs = base.execQuery(qu1);
            msg="";
            a = true;
            b = false;
            try {
                while(rs.next()){
                    if(a==true){
                        msg += "INSERT INTO emprunt VALUES ";
                        a=false;
                    }
                    if(b==true){
                        msg += ",";
                    }
                    msg += "("+rs.getInt("idAdherent")+","+rs.getInt("idExemplaire")+","+rs.getInt("idDocument")+",'"+rs.getString("dateEmprunt")+"','"+rs.getString("dateRetour")+"')";
                    
                    b=true;        
                }
                writer.println(msg+";");
            } catch (SQLException ex) {
                //Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            writer.println("/*!40000 ALTER TABLE `emprunt` ENABLE KEYS */;\n" +"UNLOCK TABLES;");  
            writer.println("");
            
            
            //retour
            writer.println("DROP TABLE IF EXISTS `rendre`;\n" +
                    "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
                    "/*!40101 SET character_set_client = utf8 */;");
            writer.println("");
            writer.println("CREATE TABLE `rendre` (\n" +
                        "  `idRendre` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,\n" +
                        "  `idAdherent` int(6) unsigned zerofill NOT NULL,\n" +
                        "  `idExemplaire` int(2) unsigned zerofill DEFAULT NULL,\n" +
                        "  `idDocument` int(6) unsigned zerofill DEFAULT NULL,\n" +
                        "  `dateEmprunt` date NOT NULL,\n" +
                        "  `dateRetour` date NOT NULL,\n" +
                        "  PRIMARY KEY (`idRendre`),\n" +
                        "  KEY `FK_rend_adherent_idx` (`idAdherent`),\n" +
                        "  KEY `FK_rend_exemplaire_idx` (`idExemplaire`),\n" +
                        "  KEY `FK_rend_document_idx` (`idDocument`),\n" +
                        "  KEY `FK_rend_exemplaire` (`idExemplaire`,`idDocument`),\n" +
                        "  CONSTRAINT `FK_rend_adherent` FOREIGN KEY (`idAdherent`) REFERENCES `adherent` (`idAdherent`) ON DELETE NO ACTION ON UPDATE NO ACTION,\n" +
                        "  CONSTRAINT `FK_rend_document` FOREIGN KEY (`idDocument`) REFERENCES `document` (`idDocument`) ON DELETE NO ACTION ON UPDATE NO ACTION,\n" +
                        "  CONSTRAINT `FK_rend_exemplaire` FOREIGN KEY (`idExemplaire`, `idDocument`) REFERENCES `exemplaire` (`idExemplaire`, `idDocument`) ON DELETE SET NULL ON UPDATE NO ACTION\n" +
                        ") ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;\n" +
                        "/*!40101 SET character_set_client = @saved_cs_client */;");
            writer.println("");
            
            qu1 = "select * from rendre";
            rs = base.execQuery(qu1);
            msg="";
            a = true;
            b = false;
            try {
                while(rs.next()){
                    if(a==true){
                        msg += "INSERT INTO rendre VALUES ";
                        a=false;
                    }
                    if(b==true){
                        msg += ",";
                    }
                    msg += "("+rs.getInt("idRendre")+","+rs.getInt("idAdherent")+","+rs.getInt("idExemplaire")+","+rs.getInt("idDocument")+",'"+rs.getString("dateEmprunt")+"','"+rs.getString("dateRetour")+"')";
                    
                    b=true;        
                }
                writer.println(msg+";");
            } catch (SQLException ex) {
                //Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            writer.println("/*!40000 ALTER TABLE `rendre` ENABLE KEYS */;\n" +"UNLOCK TABLES;");  
            writer.println("");
            
            
             //user
            writer.println("DROP TABLE IF EXISTS `user`;\n" +
                            "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
                            "/*!40101 SET character_set_client = utf8 */;");
            writer.println("");
            writer.println("CREATE TABLE `user` (\n" +
                            "  `username` varchar(40) COLLATE utf8_unicode_ci NOT NULL,\n" +
                            "  `mot_de_passe` varchar(20) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',\n" +
                            "  `email` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',\n" +
                            "  PRIMARY KEY (`username`,`mot_de_passe`,`email`)\n" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;\n" +
                            "/*!40101 SET character_set_client = @saved_cs_client */;");
            writer.println("");
            
            qu1 = "select * from user";
            rs = base.execQuery(qu1);
            msg="";
            a = true;
            b = false;
            try {
                while(rs.next()){
                    if(a==true){
                        msg += "INSERT INTO user VALUES ";
                        a=false;
                    }
                    if(b==true){
                        msg += ",";
                    }
                    msg += "('"+rs.getString("username")+"','"+rs.getString("mot_de_passe")+"','"+rs.getString("email")+"')";
                    b=true;        
                }
                writer.println(msg+";");
            } catch (SQLException ex) {
                //Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            
            writer.println("/*!40000 ALTER TABLE `user` ENABLE KEYS */;\n" +"UNLOCK TABLES;");  
            writer.println("");
                
            
            writer.close();
            } catch (IOException ex) {
            //Logger.getLogger(SettingFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void enregistrer_file(ActionEvent event) {
        
        String qu1 = "select * from user";
        ResultSet rs = base.execQuery(qu1);
        try {
            if(rs.next()){
                if (mot_de_passe.getText().equals(rs.getString("mot_de_passe"))) {
                    voir_file.setOnAction(actionEvent -> { 
                    final DirectoryChooser dialog = new DirectoryChooser();  
                    final File directory = dialog.showDialog(voir_file.getScene().getWindow()); 
                    if (directory != null) { 
                        
                        text_file.setText(directory.toString());
                        // Effectuer la sauvegarde.  
                        //enregistrer_BDD();
                    } 
                });
                }
                else{
                    ClassAlert.errorAlert("يرجي إدخال كلمة السر", "خطأ");
                }
            }
        } catch (SQLException ex) {
            
        }

    }
    @FXML
    private void enregistrer_Excel(ActionEvent event) {
        
        String qu1 = "select * from user";
        ResultSet rs = base.execQuery(qu1);
        try {
            if(rs.next()){
                if (mot_de_passe.getText().equals(rs.getString("mot_de_passe"))) {
                    voir_excel.setOnAction(actionEvent -> { 
                    final DirectoryChooser dialog = new DirectoryChooser();  
                    final File directory = dialog.showDialog(voir_excel.getScene().getWindow()); 
                    if (directory != null) { 
                        
                        text_file1.setText(directory.toString());
                        
                    } 
                });
                }
                else{
                    ClassAlert.errorAlert("يرجي إدخال كلمة السر", "خطأ");
                }
            }
        } catch (SQLException ex) {
            
        }

    }
    
   /* private void ExportExcel() throws IOException{
        String query = "Select * from document";
        ResultSet rs=base.execQuery(query);
        
            XSSFWorkbook wb = new XSSFWorkbook();
            
            XSSFSheet sheet = wb.createSheet("documents");
            XSSFRow header = sheet.createRow(1);
            header.createCell(0).setCellValue("رقم الوثيقة");
            header.createCell(1).setCellValue("عنوان الوثيقة");
            header.createCell(2).setCellValue("مؤلف");
            header.createCell(3).setCellValue("تخصص");
            header.createCell(4).setCellValue("نوع المستند");
            header.createCell(5).setCellValue(" عدد النسخ");
            header.createCell(6).setCellValue("جزء");
            int i=2;
         try {
            while (rs.next()) {
               
                XSSFRow row = sheet.createRow(i);
                row.createCell(0).setCellValue(rs.getInt(1));
                row.createCell(1).setCellValue(rs.getString(2));
                row.createCell(2).setCellValue(rs.getString(3));
                row.createCell(3).setCellValue(rs.getString(5));
                row.createCell(4).setCellValue(rs.getString(6));
                row.createCell(5).setCellValue(rs.getInt(7));
                row.createCell(6).setCellValue(rs.getInt(8));
                
                i++;
            }
             System.out.println(header.getPhysicalNumberOfCells());
            for(int j=1; j<= header.getPhysicalNumberOfCells();j++){
                sheet.autoSizeColumn(j);
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(FXMLParametreController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            FileOutputStream fileout = new FileOutputStream(text_file1.getText()+"\\documets.xlsx");
            wb.write(fileout);
            fileout.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLParametreController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     @FXML
    private void importExcel(ActionEvent e) throws IOException, SQLException{
        FileChooser chooser = new FileChooser();
    chooser.setTitle("Open File");
    
    FileChooser.ExtensionFilter extnsion = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
    chooser.getExtensionFilters().add(extnsion);
    File file = chooser.showOpenDialog(voir_file.getScene().getWindow());
        String query = "insert into document(Titre,auteur,nomCategorie,types,nbrExemplaire,is_serie) values(?,?,?,?,?,?)";
        if(file != null){
            if(ClassAlert.confirmDialog("يرجى تأكيد عملية الادراج", "تأكيد")){
        try (PreparedStatement stm = base.prepareStat(query); 
                FileInputStream filein = new FileInputStream(new File(file.toString())); 
                XSSFWorkbook wb = new XSSFWorkbook(filein)) {
            
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                stm.setString(1, row.getCell(1).getStringCellValue());
                stm.setString(2, row.getCell(2).getStringCellValue());
                stm.setString(3, row.getCell(3).getStringCellValue());
                stm.setString(4, row.getCell(4).getStringCellValue());
                stm.setInt(5, 0);
                stm.setInt(6,(int) row.getCell(6).getNumericCellValue());
                stm.executeUpdate();
                
            }
            wb.close();
            filein.close();
        }
            }
          
    }
    }*/
    
    
    
}
