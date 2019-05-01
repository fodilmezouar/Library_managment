/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package darhadit.Notification;

import com.jfoenix.controls.JFXTextField;
import darhadit.DataBase.DataBase;
import darhadit.GestionMember.FXMLgestionMemberController;
import darhadit.alert.ClassAlert;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class NotificationController implements Initializable {

    @FXML
    private TableView<Person> List;
    @FXML
    private TableColumn<Person, Integer> idadh;
    @FXML
    private TableColumn<Person, String> nom_prenom;
    @FXML
    private TableColumn<Person, String> num;
    @FXML
    private TableColumn<Person, String> email;
    @FXML
    private TableColumn<Person, String> job;
    @FXML
    private TableColumn<Person, Integer> iddoc;
    @FXML
    private TableColumn<Person, Integer> idcopy;
    @FXML
    private TableColumn<Person, String> addresse;
    @FXML
    private TableColumn<Person, Integer> retards;
    @FXML
    private JFXTextField text;
    @FXML
    private AnchorPane rootPane;

    DataBase base;
    private ObservableList<Person> Data;

    private ClassAlert alert;
    private Date dateemp;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        base = DataBase.getInstance();
        Data = FXCollections.observableArrayList();
        alert = new ClassAlert();
        Reset();

        idadh.setCellValueFactory(new PropertyValueFactory<>("idadh"));
        iddoc.setCellValueFactory(new PropertyValueFactory<>("iddoc"));
        idcopy.setCellValueFactory(new PropertyValueFactory<>("idcopy"));
        nom_prenom.setCellValueFactory(new PropertyValueFactory<>("nom_prenom"));
        num.setCellValueFactory(new PropertyValueFactory<>("num"));
        email.setCellValueFactory(new PropertyValueFactory<>("emai"));
        job.setCellValueFactory(new PropertyValueFactory<>("job"));
        addresse.setCellValueFactory(new PropertyValueFactory<>("addresse"));
        retards.setCellValueFactory(new PropertyValueFactory<>("retard"));
        String a = " يجرى البحث وفقا ل :"+"\n"+"رقم الوثيقة او النسخة"+"\n"+"رقم , إسم , لقب العضو او رقم الهاتف";
        text.tooltipProperty().setValue(new Tooltip(a));

    }

    public int nbrNotif() {
        int i = 0;
        base = DataBase.getInstance();
        Date cejour = new Date();
        String quiry = "select dateRetour , idAdherent ,idDocument, idExemplaire from Emprunt order by dateRetour";
        try {
            ResultSet rs = base.execQuery(quiry);

            while (rs.next()) {
                if (cejour.after(rs.getDate("dateRetour"))) {

                    int retard = (int) difr(cejour, rs.getDate("dateRetour"));
                    String quiry2 = "select idAdherent,Nom,Prenom,Telephone,email,Specialite,Adresse from Adherent where idAdherent = "
                            + rs.getInt("idAdherent");
                    int iddocc = rs.getInt("idDocument");
                    int idex = rs.getInt("idExemplaire");
                    ResultSet rs2 = base.execQuery(quiry2);
                    while (rs2.next()) {
                        i++;
                    }
                }
            }

        } catch (Exception e) {
            System.err.printf("Erruer" + e);
        }

        return i;
    }

    private void Reset() {
        Date cejour = new Date();
        String quiry = "select dateRetour , idAdherent ,idDocument, idExemplaire from Emprunt order by dateRetour";
        try {
            ResultSet rs = base.execQuery(quiry);

            while (rs.next()) {
                if (cejour.after(rs.getDate("dateRetour"))) {

                    int retard = (int) difr(cejour, rs.getDate("dateRetour"));
                    String quiry2 = "select idAdherent,Nom,Prenom,Telephone,email,Specialite,Adresse from Adherent where idAdherent = "
                            + rs.getInt("idAdherent");
                    int iddocc = rs.getInt("idDocument");
                    int idex = rs.getInt("idExemplaire");
                    ResultSet rs2 = base.execQuery(quiry2);
                    while (rs2.next()) {

                        Data.add(new Person(rs2.getInt("idAdherent"), iddocc, idex, rs2.getString("email"), rs2.getString("Telephone"), rs2.getString("Special"
                                + "ite"), rs2.getString("Nom") + " " + rs2.getString("Prenom"), rs2.getString("Adresse"), retard));
                    }
                }
            }

        } catch (Exception e) {
            System.err.printf("Erruer" + e);
        }

        List.setItems(null);
        List.setItems(Data);
    }

    @FXML
    public void Recheche(ActionEvent event) throws ParseException {
        String ch1 = text.getText();

        if (!ch1.trim().isEmpty()) {

            Date cejour = new Date();

            String quiry = "select dateRetour,E.idAdherent,Nom,Prenom,Telephone,email,Specialite,Adresse,idDocument,idExemplaire \n"
                    + "from Emprunt E, Adherent A \n"
                    + "where E.idAdherent = A.idAdherent AND( ";
            try {
                quiry += "idDocument = " + Integer.parseInt(ch1) + " OR idExemplaire = " + Integer.parseInt(ch1) + " OR E.idAdherent = " + Integer.parseInt(ch1) + " OR ";

            } catch (NumberFormatException ex) {

            }
            quiry += " Prenom like '" + ch1 + "%' OR Nom like '" + ch1 + "%' OR Telephone like '" + ch1 + "%' )";

            ResultSet rs = base.execQuery(quiry);

            try {
                Data.clear();
                while (rs.next()) {

                    if (cejour.after(rs.getDate("dateRetour"))) {

                        Data.add(new Person(rs.getInt("idAdherent"), rs.getInt("idDocument"), rs.getInt("idExemplaire"), rs.getString("email"), rs.getString("Telephone"), rs.getString("Special"
                                + "ite"), rs.getString("Nom") + " " + rs.getString("Prenom"), rs.getString("Adresse"), (int) difr(cejour, rs.getDate("dateRetour"))));

                    }

                }

            } catch (SQLException e) {
                System.err.printf("Erruer" + e);
            }

            List.setItems(Data);
        } else {
            Data.clear();
            Reset();
        }

    }

    public long difr(Date i, Date j)
            throws ParseException {

        Date firstDate = i;
        Date secondDate = j;

        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;

    }

    @FXML
    private void Recheche_v2(KeyEvent event) {
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Recheche(new ActionEvent());
            } catch (ParseException ex) {
                Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    @FXML
    public void Supprimer(ActionEvent event) {
        Person selectedToRendre = List.getSelectionModel().getSelectedItem();
        if (selectedToRendre == null) {
            ClassAlert.errorAlert("حدد المعلومات اللتي تريد تغيرها", "خطأ ");
            return;
        }
        int idoc = selectedToRendre.getIddoc(), idcopy = selectedToRendre.getIdcopy();
        String qu1 = "delete from Emprunt where idDocument = " + idoc + " and idExemplaire = " + idcopy;
        String qu2 = "delete from Exemplaire where idDocument = " + idoc + " and idExemplaire = " + idcopy;
        String qu3 = "UPDATE document SET nbrExemplaire = nbrExemplaire - 1 WHERE idDocument =" + idoc;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("أنت متأكد من هذه العملية");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            if (base.execAction(qu1) && base.execAction(qu2) && base.execAction(qu3)) {
                Data.clear();
                Reset();
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setHeaderText(null);
                Stage stage2 = (Stage) alert.getDialogPane().getScene().getWindow();
                stage2.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert2.setContentText("تمة عمليةالحدف ");
                alert2.showAndWait();

            } else {
                //Erreur
            }
        } else {
            //CANCEL
        }

    }

    @FXML
    public void rendre(ActionEvent event) throws SQLException {
        Person selectedToRendre = List.getSelectionModel().getSelectedItem();
        if (selectedToRendre == null) {
            ClassAlert.errorAlert("حدد المعلومات اللتي تريد تغيرها", "خطأ ");
            return;
        }
        String qu = "select dateEmprunt from Emprunt where idDocument= " + selectedToRendre.getIddoc();
        ResultSet rs = base.execQuery(qu);
        Date date = null;

        while (rs.next()) {
            date = rs.getDate("dateEmprunt");
        }
        String output;
        SimpleDateFormat formatter;

        formatter = new SimpleDateFormat("yyyy/MM/dd");
        output = formatter.format(date);
        System.out.println(output);

        String qu1 = "UPDATE Exemplaire "
                + "SET disponible = true "
                + "WHERE idDocument = " + selectedToRendre.getIddoc() + " AND idExemplaire = " + selectedToRendre.getIdcopy();
        String qu2 = "delete from Emprunt where idDocument = " + selectedToRendre.getIddoc() + " and idExemplaire = " + selectedToRendre.getIdcopy();
        String qu3 = "insert into Rendre (idAdherent,idExemplaire,idDocument,dateEmprunt,dateRetour) values("
                + selectedToRendre.getIdadh() + "," + selectedToRendre.getIdcopy() + "," + selectedToRendre.getIddoc() + ",'" + output + "',CURRENT_DATE())";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
        alert.setContentText("أنت متأكد من هذه العملية");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            if (base.execAction(qu1) && base.execAction(qu2) && base.execAction(qu3)) {
                Data.clear();
                Reset();
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setHeaderText(null);
                Stage stage2 = (Stage) alert.getDialogPane().getScene().getWindow();
                stage2.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert2.setContentText("تمة عمليةالتقديم ");
                alert2.showAndWait();

            } else {
                //Erreur
            }
        } else {
            //CANCEL
        }

    }
    @FXML
      private void backToDash(ActionEvent event){
          try {
            AnchorPane pane =FXMLLoader.load(getClass().getResource("/darhadit/dashboard/dashboard.fxml"));
          
            rootPane.setBottomAnchor(pane, 0.0);
            rootPane.setTopAnchor(pane, 0.0);
            rootPane.setLeftAnchor(pane, 0.0);
            rootPane.setRightAnchor(pane, 0.0);
            rootPane.getChildren().setAll(pane);
         /* stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.setTitle("إدارة الاعضاء");
            stage.setScene(scence);
            stage.show();*/
          }catch(IOException ex){
              Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
          }
          
      }

}
