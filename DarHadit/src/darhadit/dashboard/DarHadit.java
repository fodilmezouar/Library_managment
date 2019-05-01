/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package darhadit.dashboard;

import darhadit.DataBase.DataBase;
import darhadit.adresse.Adresse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Fodil
 */
public class DarHadit extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Adresse a = new Adresse();
        String port = a.getPort();
        String username = a.getUserName();
        String password = a.getMDP();
        
        String conn_string="jdbc:mysql://localhost:"+port+"/dar_el_hadith?&characterEncoding=UTF-8";
        
        Connection conn = null;
        Statement stmt = null;
        
         try{
            conn=DriverManager.getConnection(conn_string,username,password);
            System.out.println("connected TO BDD");
            Parent root = FXMLLoader.load(getClass().getResource("/darhadit/login/FXMLDocument.fxml"));
            
            Scene scene = new Scene(root);
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            System.out.println("NO connected");
            Parent root = FXMLLoader.load(getClass().getResource("/darhadit/Setting/SettingFXML.fxml"));
        
            Scene scene = new Scene(root);
            stage.setTitle("Configuration");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.show();
        } 
        
        
        
        //Parent root = FXMLLoader.load(getClass().getResource("/darhadit/dashboard/dashboard.fxml"));
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

//stage.setResizable(false);
//Parent root = FXMLLoader.load(getClass().getResource("/darhadit/dashboard/dashboard.fxml"));
