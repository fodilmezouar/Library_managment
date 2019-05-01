package darhadit.dashboard;

import com.jfoenix.controls.JFXTextField;
import darhadit.DataBase.DataBase;
import darhadit.addDocument.AddDocumentController;
import darhadit.DocumentEmprunts.DocumentEmpruntsController;
import darhadit.Notification.NotificationController;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DashboardController implements Initializable {
    
    DataBase base;
    
    int idDoc = 0;
    int idExemp = 0;
    int idAdher = 0;
    
    @FXML
    private Label titre;
    @FXML
    private JFXTextField idDocument;
    @FXML
    private JFXTextField idExemplaire;
    @FXML
    private Label nomAdherent;
    @FXML
    private Label telephoneAdherent;
    @FXML
    private Label specialiteAdherent;
    @FXML
    private JFXTextField idAdherent;
    @FXML
    private Label emplacementExemplaire;
    @FXML
    private Label disponibleExemplaire;
    @FXML
    private Label empruntableExemplaire;
    @FXML
    private Label nomCategorie;
    @FXML
    private Label notification;
    @FXML
    private Label nombreExemplaire;
    @FXML
    private JFXTextField idDocumentExemplaire;
    @FXML
    private Label rendreTitre;
    @FXML
    private Label rendreEmplacement;
    @FXML
    private Label rendreNumCategorie;
    @FXML
    private Label rendreNomPrenom;
    @FXML
    private Label rendreTelephone;
    @FXML
    private Label rendreSpecialite;
    @FXML
    private Label rendreNomCategorie;
    @FXML
    private AnchorPane rootPane;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        base = DataBase.getInstance();
        noti();
    }    

    private void noti(){
        NotificationController notControle = new NotificationController();
        int nbrNotification = notControle.nbrNotif();
        System.out.println(nbrNotification);
        if (nbrNotification >0) {
            notification.setVisible(true);
            notification.setText(nbrNotification+"");
        }else{
            notification.setVisible(false);
        }
    }
    @FXML
    private void tremplirFormulaireDocument(ActionEvent event) {
        if(!idDocument.getText().trim().isEmpty()){
            idDoc = Integer.parseInt(idDocument.getText());
            String qu = "select titre,nomCategorie from document where iddocument = " + idDoc;
            ResultSet rs = base.execQuery(qu);
            try {
                //si le document existe
                if(rs.next()){     
                    titre.setText(rs.getString("titre"));
                    nomCategorie.setText(rs.getString("nomCategorie"));
                    qu = "select nbrExemplaire from document where iddocument = " + idDoc;
                    //qu = "select count(*) as total from exemplaire where iddocument = " + idDoc;
                    rs = base.execQuery(qu);
                    int nbrExemplaire = 0;
                    if(rs.next()){
                        //nbrExemplaire = rs.getInt("total");
                        nbrExemplaire = rs.getInt("nbrExemplaire");
                    }
                    qu = "select count(*) as total from exemplaire where iddocument = " + idDoc + " and disponible = true";
                    rs = base.execQuery(qu);
                    int nbrExemplaireDisponible = 0;
                    if(rs.next()){
                        nbrExemplaireDisponible = (int)rs.getInt("total");
                    }
                    nombreExemplaire.setText(nbrExemplaire + "\\" + nbrExemplaireDisponible);
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                    alert.setContentText("رقم الكتاب خطأ");
                    alert.showAndWait();
                    return;
                }
            } catch (SQLException ex) {
                Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            titre.setText("اسم الوثيقة");
            nomCategorie.setText("تخصص");
            nombreExemplaire.setText("النسخ المتاحة\\جميع النسخ");
        }
        
    }

    @FXML
    private void remplirFormulaireExemplaire(ActionEvent event) {
        if(!idExemplaire.getText().trim().isEmpty()){
            idExemp = Integer.parseInt(idExemplaire.getText());
            String qu = "select emplacement,num_categorie,disponible,empruntable,version from exemplaire "
                    + "where idDocument = '" + idDoc + "' and idExemplaire = '" + idExemp + "'";
            ResultSet rs = base.execQuery(qu);
            try {
                //si le document existe
                if(rs.next()){  
                    emplacementExemplaire.setText(rs.getString("num_categorie") + " | " + rs.getString("emplacement"));
                    if(rs.getBoolean("disponible")==true){
                        disponibleExemplaire.setText("متوفرة");
                    }else{
                        disponibleExemplaire.setText("غير متوفرة");
                    }
                    if(rs.getBoolean("empruntable")==true){
                        empruntableExemplaire.setText("للإقراض");
                    }else{
                        empruntableExemplaire.setText("لا تقترض");
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                    alert.setContentText("رقم النسخة خطأ");
                    alert.showAndWait();
                    return;
                }
            } catch (SQLException ex) {
                Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            emplacementExemplaire.setText("مكان النسخه | الرقم في الفئة");
            disponibleExemplaire.setText("إتاحة");
            empruntableExemplaire.setText("للإقراض");
        }
        
    }

    @FXML
    private void remplirFormulaireAdherent(ActionEvent event) {
        if(!idAdherent.getText().trim().isEmpty()){
            idAdher = Integer.parseInt(idAdherent.getText());
            String qu = "select nom,prenom,telephone,Specialite from Adherent where idadherent = "+idAdher;
            ResultSet rs = base.execQuery(qu);
            try {
                //si le document existe
                if(rs.next()){
                    nomAdherent.setText(rs.getString("prenom") + "\t" + rs.getString("nom"));
                    telephoneAdherent.setText(rs.getString("telephone"));
                    specialiteAdherent.setText(rs.getString("Specialite"));
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                    alert.setContentText("رقم العضو خطأ");
                    alert.showAndWait();
                    return;
                }
            } catch (SQLException ex) {
                Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            nomAdherent.setText("اسم العضو");
            telephoneAdherent.setText("رقم الهاتف");
            specialiteAdherent.setText("المهنة");
        }
    }
    
    private boolean verifierDroitEmprunt(boolean renouvellement){
        int nbr_emp_possible = 0;
        String qu1 = "SELECT nbr_emp_possible "
                + "FROM Adherent "
                + "WHERE idAdherent = " + idAdher;
        
        int nbr_emp = 0;
        String qu2 = "SELECT COUNT(*) AS total "
                + "FROM Emprunt "
                + "WHERE idAdherent = " + idAdher;
        
        boolean disponible = false;
        boolean empruntable = false;
        String qu3 = "SELECT disponible,empruntable "
                + "FROM Exemplaire "
                + "WHERE idDocument = " + idDoc + " AND idExemplaire = " + idExemp;
        
        Date dateRenouvelement = null;
        String qu4 = "SELECT DateRenouvelement "
                + "FROM Adherent "
                + "WHERE idAdherent = " + idAdher;
        
        Date sysDate = null;
        String qu5 = "SELECT CURRENT_DATE() AS sysDate";
        
        int totalRetards = 0;
        String qu6 = "SELECT COUNT(*) AS totalRetards "
                + "FROM Emprunt "
                + "WHERE idAdherent = " + idAdher + " AND DATEDIFF(dateRetour, CURRENT_DATE()) < 0";
        
        String qu7 = qu6 + " AND idDocument != " + idDoc + " AND idExemplaire != " + idExemp;
        
        ResultSet rs = base.execQuery(qu1);
        try{
            if(rs.next()){
                nbr_emp_possible = rs.getInt("nbr_emp_possible");
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert.setContentText("رقم العضو خطأ");
                alert.showAndWait();
            }
        }catch(SQLException ex){
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        rs = base.execQuery(qu2);
        try{
            if(rs.next()){
                nbr_emp = rs.getInt("total");
            }
        }catch(SQLException ex){
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        rs = base.execQuery(qu3);
        try{
            if(rs.next()){
                disponible = rs.getBoolean("disponible");
                empruntable = rs.getBoolean("empruntable");
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert.setContentText("رقم الوثيقة او رقم النسخه خطأ");
                alert.showAndWait();
            }
        }catch(SQLException ex){
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        rs = base.execQuery(qu4);
        ResultSet rs2 = base.execQuery(qu5);
        try{
            if(rs.next() && rs2.next()){
                dateRenouvelement = rs.getDate("DateRenouvelement");
                sysDate = rs2.getDate("sysDate");
            }else{
                //Erreur
            }
        }catch(SQLException ex){
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(renouvellement == false){
            rs = base.execQuery(qu6);
        }else{
            rs = base.execQuery(qu7);
        }
        try {
            if(rs.next()){
                totalRetards = rs.getInt("totalRetards");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if( ( (nbr_emp < nbr_emp_possible &&  renouvellement == false) || (nbr_emp <= nbr_emp_possible &&  renouvellement == true) ) && 
                (disponible == true || renouvellement == true) && 
                empruntable == true && 
                sysDate.compareTo(dateRenouvelement) <= 0 && 
                totalRetards <= 0){
            return true;
        }else {
            if(disponible == false && renouvellement == false){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert.setContentText("الوثيقة ليسة متوفرة");
                alert.showAndWait();
            }
            if(empruntable == false){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert.setContentText("الوثيقه ليسة للإقراض ");
                alert.showAndWait();
            }
            if((nbr_emp >= nbr_emp_possible && renouvellement == false) || (nbr_emp > nbr_emp_possible && renouvellement == true)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert.setContentText("العضو تجاوز عدد القروض المتاحة له");
                alert.showAndWait();
            }
            if(sysDate.compareTo(dateRenouvelement) > 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert.setContentText("اكتمال اشتراك العضو يجب عليه التجديد");
                alert.showAndWait();
            }
            if(totalRetards > 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert.setContentText("للعضو وثائق تجاوز يوم إرجاعها عددها " + totalRetards);
                alert.showAndWait();
            }
            return false;
        }
    }

    @FXML
    private void emprunter(ActionEvent event) throws SQLException {
        if(idDocument.getText().isEmpty() == true || 
                idExemplaire.getText().isEmpty() == true || 
                idAdherent.getText().isEmpty() == true || 
                nombreExemplaire.getText().equals("النسخ المتاحة\\جميع النسخ") || 
                disponibleExemplaire.getText().equals("إتاحة") || 
                telephoneAdherent.getText().equals("رقم الهاتف")){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            alert.setContentText("يرجى إدخال البيانات والضغط على ENTRER في كل خانة");
            alert.showAndWait();

            return;
        }
        String qu1 = "insert into Emprunt (idAdherent,idExemplaire,idDocument,dateEmprunt,dateRetour) "
                + "values (" + idAdher +"," + idExemp +"," + idDoc + ","
                + "CURRENT_DATE(),ADDDATE(CURRENT_DATE(),15))";
        //System.out.println(qu1);
        String qu2 = "UPDATE Exemplaire "
                + "SET disponible = false "
                + "WHERE idDocument = " + idDoc + " AND idExemplaire = " + idExemp;
        
        String qu3 = "SELECT ADDDATE(CURRENT_DATE(),15) AS dateRetour";
        Date dateRetour = null;
        ResultSet rs = base.execQuery(qu3);
        try{
            if(rs.next()){
                dateRetour = rs.getDate("DateRetour");
            }else{
                //Erreur
            }
        }catch(SQLException ex){
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
        alert.setContentText("أنت متأكد من هذه العملية");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK){
            if(verifierDroitEmprunt(false)){
                if(base.execAction(qu1) && base.execAction(qu2)){
                    titre.setText("اسم الوثيقة");
                    nomCategorie.setText("تخصص");
                    nombreExemplaire.setText("النسخ المتاحة\\جميع النسخ");
                    idDocument.setText("");
                    
                    emplacementExemplaire.setText("مكان النسخه | الرقم في الفئة");
                    disponibleExemplaire.setText("إتاحة");
                    empruntableExemplaire.setText("للإقراض");
                    idExemplaire.setText("");
                    
                    nomAdherent.setText("اسم العضو");
                    telephoneAdherent.setText("رقم الهاتف");
                    specialiteAdherent.setText("المهنة");
                    idAdherent.setText("");
                    
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setHeaderText(null);
                    Stage stage2 = (Stage) alert2.getDialogPane().getScene().getWindow();
                    stage2.getIcons().add(new Image("/darhadit/images/dar0.png"));
                    alert2.setContentText(" تمة عملية القرض يرجى الإعادة يوم   " + dateRetour);
                    alert2.showAndWait();
                }else{
                    //Erreur
                }
            }else{
                //Absence de droit
                titre.setText("اسم الوثيقة");
                nomCategorie.setText("تخصص");
                nombreExemplaire.setText("النسخ المتاحة\\جميع النسخ");
                idDocument.setText("");

                emplacementExemplaire.setText("مكان النسخه | الرقم في الفئة");
                disponibleExemplaire.setText("إتاحة");
                empruntableExemplaire.setText("للإقراض");
                idExemplaire.setText("");

                nomAdherent.setText("اسم العضو");
                telephoneAdherent.setText("رقم الهاتف");
                specialiteAdherent.setText("المهنة");
                idAdherent.setText("");
            }
        } else {
            //CANCEL
        }
        
        //qu = "select * from Emprunt";
        //ResultSet rs = base.execQuery(qu);
        //System.out.println(rs.getString(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4)+"\t"+rs.getString(5));        
    }
    
    @FXML
    private void remplirFormulaireRendre(ActionEvent event) {
        String idDocExemp = idDocumentExemplaire.getText();
        idDoc = 0;
        idExemp = 0;
        idAdher = 0;
        String idDoc1 = "";
        String idExemp1 = "";
        boolean b = true;
        for(int i = 0; i < idDocExemp.length(); i++){
            if(idDocExemp.charAt(i) == '-'){
                b = false;
                i++;
            }
            if(b){
                idDoc1 += idDocExemp.charAt(i);
            }else{
                idExemp1 += idDocExemp.charAt(i);
            }   
        }
        try{
            idDoc = Integer.parseInt(idDoc1);
            idExemp = Integer.parseInt(idExemp1);
        }catch(NumberFormatException ex){
            
        }
        if(!idDocumentExemplaire.getText().trim().isEmpty()){
            String qu1 = "SELECT titre,nomCategorie,emplacement,num_categorie "
                    + "FROM document D,Exemplaire E "
                    + "WHERE D.idDocument = " + idDoc + " AND E.idDocument = " + idDoc + " AND E.idExemplaire = " + idExemp;
            
            String qu2 = "SELECT Nom,Prenom,Telephone,Specialite "
                    + "FROM Adherent A,Emprunt E "
                    + "WHERE E.idDocument = " + idDoc + " AND E.idExemplaire = " + idExemp + " AND E.idAdherent = A.idAdherent";
            ResultSet rs = base.execQuery(qu1);
            ResultSet rs2 = base.execQuery(qu2);
            try {
                //si le document existe
                if(rs.next() && rs2.next()){     
                    rendreTitre.setText(rs.getString("titre"));
                    rendreNomCategorie.setText(rs.getString("nomCategorie"));
                    rendreEmplacement.setText(rs.getString("emplacement"));
                    rendreNumCategorie.setText(rs.getString("num_categorie"));
                    
                    rendreNomPrenom.setText(rs2.getString("Nom") + " " + rs2.getString("Prenom"));
                    rendreTelephone.setText(rs2.getString("Telephone"));
                    rendreSpecialite.setText(rs2.getString("Specialite"));
                } else{
                    rendreTitre.setText("....................");
                    rendreNomCategorie.setText("....................");
                    rendreEmplacement.setText("....................");
                    rendreNumCategorie.setText("....................");
                    rendreNomPrenom.setText("....................");
                    rendreTelephone.setText("....................");
                    rendreSpecialite.setText("....................");
                    
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                    alert.setContentText("رقم الوثيقة خطأ او الوثيقة ليسة مقروضة");
                    alert.showAndWait();
                    return;
                }
            } catch (SQLException ex) {
                Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            rendreTitre.setText("....................");
            rendreNomCategorie.setText("....................");
            rendreEmplacement.setText("....................");
            rendreNumCategorie.setText("....................");
            rendreNomPrenom.setText("....................");
            rendreTelephone.setText("....................");
            rendreSpecialite.setText("....................");
        }
        
    }

    @FXML
    private void rendre(ActionEvent event) {
        if(idDocumentExemplaire.getText().isEmpty() == true || rendreTitre.getText().equals("....................")){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            alert.setContentText("يرجى إدخال (رقم النسخة-رقم الوثيقة) والضغط على ENTRER");
            alert.showAndWait();

            return;
        }
        String idDocExemp = idDocumentExemplaire.getText();
        idDoc = 0;
        idExemp = 0;
        idAdher = 0;
        String idDoc1 = "";
        String idExemp1 = "";
        Date dateEmprunt = null;
        boolean b = true;
        for(int i = 0; i < idDocExemp.length(); i++){
            if(idDocExemp.charAt(i) == '-'){
                b = false;
                i++;
            }
            if(b){
                idDoc1 += idDocExemp.charAt(i);
            }else{
                idExemp1 += idDocExemp.charAt(i);
            }   
        }
        try{
            idDoc = Integer.parseInt(idDoc1);
            idExemp = Integer.parseInt(idExemp1);
        }catch(NumberFormatException ex){
            
        }
        
        //System.out.println(idDoc1);
        //System.out.println(idExemp1);
        String qu = "select idAdherent from Emprunt where idDocument = " + idDoc +" and idExemplaire = " + idExemp;
        String quDateEmprunt = "SELECT dateEmprunt FROM Emprunt "
                + "WHERE idDocument = " + idDoc + " AND idExemplaire = " + idExemp;
        ResultSet rs1 = base.execQuery(qu);
        ResultSet rs2 = base.execQuery(quDateEmprunt);
        try {
            //si l'id existe
            if(rs1.next() && rs2.next()){  
                idAdher = rs1.getInt("idAdherent");
                dateEmprunt = rs2.getDate("dateEmprunt");
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert.setContentText("النسخة غير موجودة في قائمة القروض");
                alert.showAndWait();
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String qu1 = "UPDATE Exemplaire "
                + "SET disponible = true "
                + "WHERE idDocument = " + idDoc + " AND idExemplaire = " + idExemp;
        String qu2 = "delete from Emprunt where idDocument = " + idDoc + " and idExemplaire = " + idExemp;
        String qu3 = "insert into Rendre (idAdherent,idExemplaire,idDocument,dateEmprunt,dateRetour) values("
                + idAdher + "," + idExemp + "," + idDoc + "," + "'" + dateEmprunt + "'" + ",CURRENT_DATE())";
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
        alert.setContentText("أنت متأكد من هذه العملية");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK){
            if(base.execAction(qu1) && base.execAction(qu2) && base.execAction(qu3)){
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setHeaderText(null);
                    Stage stage2 = (Stage) alert2.getDialogPane().getScene().getWindow();
                    stage2.getIcons().add(new Image("/darhadit/images/dar0.png"));
                    alert2.setContentText("تمة عمليةالتقديم ");
                    alert2.showAndWait();
                    
                    noti();
                    
                    idDocumentExemplaire.setText("");
                    
                    rendreTitre.setText("....................");
                    rendreNomCategorie.setText("....................");
                    rendreEmplacement.setText("....................");
                    rendreNumCategorie.setText("....................");
                    rendreNomPrenom.setText("....................");
                    rendreTelephone.setText("....................");
                    rendreSpecialite.setText("....................");
                }else{
                    //Erreur
                }
        } else {
            //CANCEL
        }
    }
    
    @FXML
    private void renouveler(ActionEvent event) {
        if(idDocumentExemplaire.getText().isEmpty() == true || rendreTitre.getText().equals("....................")){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            alert.setContentText("يرجى إدخال (رقم النسخة-رقم الوثيقة) والضغط على ENTRER");
            alert.showAndWait();

            return;
        }
        String idDocExemp = idDocumentExemplaire.getText();
        idDoc = 0;
        idExemp = 0;
        idAdher = 0;
        String idDoc1 = "";
        String idExemp1 = "";
        Date dateEmprunt = null;
        Date dateRetour = null;
        boolean b = true;
        for(int i = 0; i < idDocExemp.length(); i++){
            if(idDocExemp.charAt(i) == '-'){
                b = false;
                i++;
            }
            if(b){
                idDoc1 += idDocExemp.charAt(i);
            }else{
                idExemp1 += idDocExemp.charAt(i);
            }   
        }
        try{
            idDoc = Integer.parseInt(idDoc1);
            idExemp = Integer.parseInt(idExemp1); 
        }catch(NumberFormatException ex){
            
        }
        
        String qu1 = "select idAdherent from Emprunt where idDocument = " + idDoc +" and idExemplaire = " + idExemp;
        String quDateEmprunt = "SELECT dateEmprunt FROM Emprunt "
                            + "WHERE idDocument = " + idDoc + " AND idExemplaire = " + idExemp;
        ResultSet rs1 = base.execQuery(qu1);
        ResultSet rs2 = base.execQuery(quDateEmprunt);
        try {
            //si l'id existe
            if(rs1.next() && rs2.next()){  
                idAdher = rs1.getInt("idAdherent");
                dateEmprunt = rs2.getDate("dateEmprunt");
            }
            else{
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setHeaderText(null);
                Stage stage = (Stage) alert1.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert1.setContentText("النسخة غير موجودة في قائمة القروض");
                alert1.showAndWait();
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
        alert.setContentText("أنت متأكد من عملية التجديد");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK){
            if(verifierDroitEmprunt(true)){
                //Faire le retoure
                String qu2 = "UPDATE Exemplaire "
                        + "SET disponible = true "
                        + "WHERE idDocument = " + idDoc + " AND idExemplaire = " + idExemp;
                String qu3 = "delete from Emprunt where idDocument = " + idDoc + " and idExemplaire = " + idExemp;
                String qu4 = "insert into Rendre (idAdherent,idExemplaire,idDocument,dateEmprunt,dateRetour) values("
                        + idAdher + "," + idExemp + "," + idDoc + "," + "'" + dateEmprunt + "'" + ",CURRENT_DATE())";
                
                if(base.execAction(qu2) && base.execAction(qu3) && base.execAction(qu4)){   
                }else{
                    //Erreur
                }
                
                //Faire l'emprunt
                qu1 = "insert into Emprunt (idAdherent,idExemplaire,idDocument,dateEmprunt,dateRetour) "
                    + "values (" + idAdher +"," + idExemp +"," + idDoc + ","
                    + "CURRENT_DATE(),ADDDATE(CURRENT_DATE(),15))";
                
                qu2 = "UPDATE Exemplaire "
                    + "SET disponible = false "
                    + "WHERE idDocument = " + idDoc + " AND idExemplaire = " + idExemp;

                qu3 = "SELECT ADDDATE(CURRENT_DATE(),15) AS dateRetour";
                ResultSet rs = base.execQuery(qu3);
                try{
                    if(rs.next()){
                        dateRetour = rs.getDate("DateRetour");
                    }else{
                        //Erreur
                    }
                }catch(SQLException ex){
                    Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(base.execAction(qu1) && base.execAction(qu2)){
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setHeaderText(null);
                    Stage stage2 = (Stage) alert2.getDialogPane().getScene().getWindow();
                    stage2.getIcons().add(new Image("/darhadit/images/dar0.png"));
                    alert2.setContentText(" تمة عملية التجديد يرجى الإعادة يوم   " + dateRetour);
                    alert2.showAndWait();
                    
                    noti();
                    
                    idDocumentExemplaire.setText("");
                    
                    rendreTitre.setText("....................");
                    rendreNomCategorie.setText("....................");
                    rendreEmplacement.setText("....................");
                    rendreNumCategorie.setText("....................");
                    rendreNomPrenom.setText("....................");
                    rendreTelephone.setText("....................");
                    rendreSpecialite.setText("....................");
                }else {
                    //Erreur
                }
            }else{
                //Absence de droit
                idDocumentExemplaire.setText("");
                    
                rendreTitre.setText("....................");
                rendreNomCategorie.setText("....................");
                rendreEmplacement.setText("....................");
                rendreNumCategorie.setText("....................");
                rendreNomPrenom.setText("....................");
                rendreTelephone.setText("....................");
                rendreSpecialite.setText("....................");
            }
        }else {
            //CANCEL
        }
    }

    @FXML
    private void viderFormulaireDocument(KeyEvent event) {
        if(idDocument.getText().trim().isEmpty()){
            titre.setText("اسم الوثيقة");
            nomCategorie.setText("تخصص");
            nombreExemplaire.setText("النسخ المتاحة\\جميع النسخ");
        }
    }

    @FXML
    private void viderFormulaireExemplaire(KeyEvent event) {
        if(idExemplaire.getText().trim().isEmpty()){
            emplacementExemplaire.setText("مكان النسخه | الرقم في الفئة");
            disponibleExemplaire.setText("إتاحة");
            empruntableExemplaire.setText("للإقراض");
        }
    }

    @FXML
    private void viderFormulaireAdherent(KeyEvent event) {
        if(idAdherent.getText().trim().isEmpty()){
            nomAdherent.setText("اسم العضو");
            telephoneAdherent.setText("رقم الهاتف");
            specialiteAdherent.setText("المهنة");
        }
    }

    @FXML
    private void viderFormulaireRendre(KeyEvent event) {
        if(idDocumentExemplaire.getText().trim().isEmpty()){
            rendreTitre.setText("....................");
            rendreNomCategorie.setText("....................");
            rendreEmplacement.setText("....................");
            rendreNumCategorie.setText("....................");
            rendreNomPrenom.setText("....................");
            rendreTelephone.setText("....................");
            rendreSpecialite.setText("....................");
        }
    }
    
    @FXML
    private void openNotification(MouseEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/darhadit/Notification/Notification.fxml"));
            rootPane.setBottomAnchor(pane, 0.0);
            rootPane.setTopAnchor(pane, 0.0);
            rootPane.setLeftAnchor(pane, 0.0);
            rootPane.setRightAnchor(pane, 0.0);
            rootPane.getChildren().setAll(pane);
            /*Scene scence = new Scene(parent);
          Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.setTitle("إشعارات");
            stage.setScene(scence);
            stage.show();*/
            
            pane.getScene().getWindow().setOnCloseRequest((e)->{
                noti();
            });
            //LibraryAssistantUtil.setStageIcon(stage);
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void openGestionMember(MouseEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/darhadit/GestionMember/gestionMember.fxml"));
            rootPane.setBottomAnchor(pane, 0.0);
            rootPane.setTopAnchor(pane, 0.0);
            rootPane.setLeftAnchor(pane, 0.0);
            rootPane.setRightAnchor(pane, 0.0);
            rootPane.getChildren().setAll(pane);
            /*Scene scene = new Scene(parent);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.setTitle("إدارة الاعضاء");
            stage.setScene(scene);
            stage.show();
            //LibraryAssistantUtil.setStageIcon(stage);*/
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openAddDocument(MouseEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/darhadit/addDocument/AddDocument.fxml"));
            rootPane.setBottomAnchor(pane, 0.0);
            rootPane.setTopAnchor(pane, 0.0);
            rootPane.setLeftAnchor(pane, 0.0);
            rootPane.setRightAnchor(pane, 0.0);
            rootPane.getChildren().setAll(pane);
            /* Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.setTitle("إضافة وثيقة");
            stage.setScene(scene);
            stage.show();*/
            //LibraryAssistantUtil.setStageIcon(stage);
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openListDocument(MouseEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/darhadit/listDocument/ListDocument.fxml"));
            rootPane.setBottomAnchor(pane, 0.0);
            rootPane.setTopAnchor(pane, 0.0);
            rootPane.setLeftAnchor(pane, 0.0);
            rootPane.setRightAnchor(pane, 0.0);
            rootPane.getChildren().setAll(pane);
            /*Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.setTitle("قائمة الوثائق");
            stage.setScene(scene);
            stage.show();*/
            //LibraryAssistantUtil.setStageIcon(stage);
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openDocumentEmprunts(MouseEvent event) {
        try {
            AnchorPane pane  = FXMLLoader.load(getClass().getResource("/darhadit/DocumentEmprunts/DocumentEmprunts.fxml"));
            rootPane.setBottomAnchor(pane, 0.0);
            rootPane.setTopAnchor(pane, 0.0);
            rootPane.setLeftAnchor(pane, 0.0);
            rootPane.setRightAnchor(pane, 0.0);
            rootPane.getChildren().setAll(pane);
//DocumentEmpruntController controller = parent.getController();
//            controller.setMainApp(this);
           /* Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.setTitle(" الوثائق المستعارة");
            stage.setScene(scene);
            stage.show();*/
            //LibraryAssistantUtil.setStageIcon(stage);
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openParametre(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/darhadit/parametre/FXMLParametre.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.setTitle("إعدادات");
            stage.setResizable(false);
            stage.setScene(new Scene(parent));
            stage.setFullScreen(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openStatistiques(MouseEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/darhadit/Statistiques/Statistiques.fxml"));
            rootPane.setBottomAnchor(pane, 0.0);
            rootPane.setTopAnchor(pane, 0.0);
            rootPane.setLeftAnchor(pane, 0.0);
            rootPane.setRightAnchor(pane, 0.0);
            rootPane.getChildren().setAll(pane);
            /* Scene scene = new Scene(parent);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.setTitle("إحصائيات");
            //stage.setResizable(false);
            stage.setScene(scene);
            stage.show();*/
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void just_number(KeyEvent event) {
        idDocument.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                char c=idDocument.getText().charAt(idDocument.getText().length()-1);
                if (c!='0' && c!='1' && c!='2' && c!='3' && c!='4' && c!='5' && c!='6' && c!='7' && c!='8' && c!='9')
                idDocument.setText(idDocument.getText().substring(0,idDocument.getText().length()-1)); 
            }catch(Exception ex){
                
            }
           
        });
        
        idExemplaire.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                char c=idExemplaire.getText().charAt(idExemplaire.getText().length()-1);
                if (c!='0' && c!='1' && c!='2' && c!='3' && c!='4' && c!='5' && c!='6' && c!='7' && c!='8' && c!='9')
                idExemplaire.setText(idExemplaire.getText().substring(0,idExemplaire.getText().length()-1)); 
            }catch(Exception ex){
                
            }
           
        });
        
        idAdherent.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                char c=idAdherent.getText().charAt(idAdherent.getText().length()-1);
                if (c!='0' && c!='1' && c!='2' && c!='3' && c!='4' && c!='5' && c!='6' && c!='7' && c!='8' && c!='9')
                idAdherent.setText(idAdherent.getText().substring(0,idAdherent.getText().length()-1)); 
            }catch(Exception ex){
                
            }
           
        });
        
        idDocumentExemplaire.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                char c=idDocumentExemplaire.getText().charAt(idDocumentExemplaire.getText().length()-1);
                if (c!='0' && c!='1' && c!='2' && c!='3' && c!='4' && c!='5' && c!='6' && c!='7' && c!='8' && c!='9' && c!='-')
                idDocumentExemplaire.setText(idDocumentExemplaire.getText().substring(0,idDocumentExemplaire.getText().length()-1)); 
            }catch(Exception ex){
                
            }
           
        });
        
    }
}