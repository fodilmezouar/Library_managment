/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package darhadit.Setting;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import darhadit.adresse.Adresse;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Pc
 */
public class SettingFXMLController implements Initializable {

    @FXML
    private JFXTextField username;
    @FXML
    private JFXButton ButtonAnnuler;
    @FXML
    private JFXButton ButtonOk;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXPasswordField mdp;
    @FXML
    private JFXTextField port;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        recupere_Donnees();
    }    

    @FXML
    private void operationAnnuler(ActionEvent event) {
        port.setText("");
        username.setText("");
        mdp.setText("");
    }

    @FXML
    private void operationOk(ActionEvent event) throws SQLException {
        FileWriter fluxw;
        BufferedWriter output;
        PrintWriter writer;
        try {
            writer = new PrintWriter("parametre_BDD.txt");
            writer.println(port.getText());
            writer.println(username.getText());
            writer.println(mdp.getText());
            writer.close();
            
            
            Stage stage1 = (Stage) rootPane.getScene().getWindow();
            stage1.close();
            
            String conn_string_MYSQL="jdbc:mysql://localhost:"+port.getText();
            
            Connection conn = null;
            Statement stmt = null;
            try{
                conn=DriverManager.getConnection(conn_string_MYSQL,username.getText(),mdp.getText());
                System.out.println("connected TO MY SQL");
                
                String Schemat_Name="dar_el_hadith";
                stmt=conn.createStatement();
                stmt.execute("CREATE SCHEMA IF NOT EXISTS `"+Schemat_Name+"` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci");
                conn.close();
                
                String conn_string_BDD="jdbc:mysql://localhost:"+port.getText()+"/"+Schemat_Name+"?&characterEncoding=UTF-8";
                conn=DriverManager.getConnection(conn_string_BDD,username.getText(),mdp.getText());
                System.out.println("connected TO BDD darhadith");
                
                Parent parent = FXMLLoader.load(getClass().getResource("/darhadit/login/FXMLDocument.fxml"));
                System.out.println("kjhgvhvb");
                Stage stage = new Stage(StageStyle.DECORATED);
                
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                stage.setResizable(false);
                stage.setScene(new Scene(parent));
                stage.show();
                return ;
            }catch(Exception e){

            }
            
            
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("/darhadit/Setting/SettingFXML.fxml"));
                Stage stage = new Stage(StageStyle.DECORATED);
                
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                stage.setTitle("Configuration");
                stage.setResizable(false);
                stage.setScene(new Scene(parent));
                stage.show();
            } catch (IOException ex) {
                //Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(SettingFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void recupere_Donnees(){
        Adresse a = new Adresse();
        port.setText(a.getPort());
        username.setText(a.getUserName());
        mdp.setText(a.getMDP()); 
    }
    
}
