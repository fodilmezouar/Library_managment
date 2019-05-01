/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package darhadit.GestionMember.Memo;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import darhadit.DataBase.DataBase;
import darhadit.GestionMember.Adherent;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Fodil
 */
public class MemoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXTextArea memo;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXCheckBox modifier;
    private DataBase bdd;
    private int idAdherent;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        bdd = DataBase.getInstance(); 
    } 
    public void Ajouter_memo(Adherent adherent){
        idAdherent = adherent.getNumAdherent();
    }
    
    @FXML
    private void addMemo(ActionEvent e){
        String memoText = memo.getText();
        String query;
        /*if (memoText.equals("")) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert.setContentText("أدخل البيانات من فضلك");
                alert.showAndWait();
        }else{*/
            query = "UPDATE Adherent SET memo = '"+memoText+"' WHERE idAdherent = '"+idAdherent+"';";
            if (bdd.execAction(query)) {
                Stage stage3 = (Stage) cancelButton.getScene().getWindow();
                stage3.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert.setContentText("تم التعديل بنجاح");
                alert.showAndWait();
                //memo.setText("");
                
            }else{
                     Alert alert = new Alert(Alert.AlertType.ERROR);
                     alert.setHeaderText(null);
                     Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                     stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                     alert.setContentText("خـطأ في قاعدة البيانات");
                     alert.showAndWait();
                }

        //}
        
    }
    @FXML
     private void CancelButton(ActionEvent event){
         memo.setText("");
         //Stage stage = (Stage) cancelButton.getScene().getWindow();
    // do what you have to do
         //stage.close();
}
     @FXML
     private void selectModifier(ActionEvent e){
         if (modifier.isSelected()) {
             memo.setEditable(true);
             saveButton.setDisable(false);
                 System.out.println("ss");
         }else{
            memo.setEditable(false);
            saveButton.setDisable(true);
            saveButton.setText("تعديل");

            }
     }
     public void affiche_memo_Adherent(Adherent ad) throws SQLException{
         idAdherent = ad.getNumAdherent();
         modifier.setVisible(true);
         //modifier.setSelected(true);
         String memoAd;
         String query = "SELECT memo from Adherent WHERE idAdherent ='"+idAdherent+"'";
         memo.setEditable(false);
            saveButton.setDisable(true);
            //saveButton.setText("تعديل");
         ResultSet rs=bdd.execQuery(query);
         if (rs.next()) {
             memoAd = rs.getString("memo");
             memo.setText(memoAd);
           
                
         }
           
         
     }
    
}
