package darhadit.listDocument.listExemplaire.Memo;

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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ....
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
        bdd = DataBase.getInstance(); 
    } 
    
    public void Ajouter_memo(Adherent adherent){
        idAdherent = adherent.getNumAdherent();
    }

    @FXML
    private void addMemo(ActionEvent e){
        String memoText = memo.getText();
        String query;
        if (memoText.equals("")) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("أدخل البيانات من فضلك");
                alert.showAndWait();
        }else{
            query = "UPDATE Adherent SET memo = '"+memoText+"' WHERE idAdherent = '"+idAdherent+"';";
            if (bdd.execAction(query)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("تمت الاضافة بنجاح");
                alert.showAndWait();
                memo.setText("");
                
            }else{
                     Alert alert = new Alert(Alert.AlertType.ERROR);
                     alert.setHeaderText(null);
                     alert.setContentText("خـطأ في قاعدة البيانات");
                     alert.showAndWait();
                }

        }
        
    }
    @FXML
     private void CancelButton(ActionEvent event){
         memo.setText("");
         Stage stage = (Stage) cancelButton.getScene().getWindow();
         // do what you have to do
         stage.close();
}
     @FXML
     private void selectModifier(ActionEvent e){
        if (modifier.isSelected()) {
            memo.setEditable(true);
            saveButton.setDisable(false);
        }else{
            memo.setEditable(false);
            saveButton.setDisable(true);
            //saveButton.setText("تعديل");
        }
     }

}
