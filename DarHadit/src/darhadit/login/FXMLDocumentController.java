package darhadit.login;

import com.jfoenix.controls.JFXPasswordField;
import darhadit.DataBase.DataBase;
import darhadit.alert.ClassAlert;
import darhadit.dashboard.DashboardController;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Properties;
import javafx.scene.control.Hyperlink;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class FXMLDocumentController implements Initializable {
    
    DataBase base;
    @FXML
    private JFXPasswordField password;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        base = DataBase.getInstance(); 
        verifier_mdp_existe();
    }
    
    @FXML
    private void handleLoginAction(ActionEvent event) {
        String pass=password.getText();
        String qu = "select mot_de_passe from user";
        ResultSet rs = base.execQuery(qu);
        try {
            if(rs.next()){
                if (pass.equals(rs.getString("mot_de_passe"))) {
                    closeWindow();
                    loadMain();
                }
                else{
                    password.getStyleClass().add("false");
                }
            }
        } catch (SQLException ex) {
            
        } 
    }

    private void verifier_mdp_existe() {
        String qu = "select * from user";
        ResultSet rs = base.execQuery(qu);
        try {
            if(rs.next()){
            }
            else{
                String qu2 = "insert into user values ('مسؤول المكتبة','','mokh.bena2@gmail.com')";
                base.execAction(qu2);
            }
        } catch (SQLException ex) {
            
        }
    }
    
    private void closeWindow() {
        ((Stage) password.getScene().getWindow()).close();
    }
     
    void loadMain() {
        try {
                Parent parent = FXMLLoader.load(getClass().getResource("/darhadit/dashboard/dashboard.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("مساعد المكتبة");
            stage.setScene(new Scene(parent));
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.show();
            //LibraryAssistantUtil.setStageIcon(stage);
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void KeyLoad(KeyEvent key){
        if (key.getCode().equals(KeyCode.ENTER))
        {
            String qu = "select mot_de_passe from user";
            ResultSet rs = base.execQuery(qu);
            try {
                if(rs.next()){
                    if (password.getText().equals(rs.getString("mot_de_passe"))) {
                        closeWindow();
                        loadMain();
                    }
                    else{
                        password.getStyleClass().add("false");
                    }
                }
            } catch (SQLException ex) {

            }
        }
    }

    @FXML
    private void envoyer_mail(MouseEvent event) {
    String expediteur = ClassAlert.inputDialog("", "نسيت كلمة المرور", "", "يرجى إدخال البريد الإلكتروني الخاص بك لإكمال الإجراء");
    if(expediteur != null){   
       //final String username = "darhadith2017@gmail.com";
       final String username = "darHadithTlm@gmail.com";
       final String password = "darhadith@m2017";
       
       Properties prop = new Properties();
       prop.put("mail.smtp.auth","true");    
       prop.put("mail.smtp.starttls.enable","true");
       prop.put("mail.smtp.host","smtp.gmail.com");
       prop.put("mail.smtp.port","587");
        
       Session session = Session.getInstance(prop, new javax.mail.Authenticator(){
           protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(username, password);
           } 
       });
       
       
       
       String qu = "select mot_de_passe from user where email='"+expediteur+"'";
        ResultSet rs = base.execQuery(qu);
        try {
            if(rs.next()){
                try {
                    Message message =new MimeMessage(session);
                    message.setFrom(new InternetAddress("darhadith2017@gmail.com"));
                    message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(expediteur));//to
                    message.setSubject("Mot de passe oublié : ");
                    message.setText("Votre mot de passe de la bibliothèque de Dar El Hadith est : "+rs.getString("mot_de_passe"));

                    Transport.send(message);
                    System.out.println("Done");
                } catch(MessagingException e){
                    ClassAlert.errorAlert("Veuillez verifier votre connexion internet ou votre compte", "Echec");                    //throw new RuntimeException(e); 
                    //System.out.println("non");
                }
            }
            else{
                ClassAlert.errorAlert("veuillez verifier votre compte", "Echec");                    //throw new RuntimeException(e);   
            }
        } catch (SQLException ex) { 
        }
       
    }
    
    }
      
}

