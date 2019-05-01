/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package darhadit.GestionMember;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import darhadit.DataBase.DataBase;
import darhadit.GestionMember.Memo.MemoController;
import darhadit.addDocument.AddDocumentController;
import darhadit.alert.ClassAlert;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Fodil
 */

public class FXMLgestionMemberController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
  private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  private DataBase bdd;
      // ********************************* Ajoute Adherent ******************************* \\
  @FXML
  private AnchorPane rootPane;
  @FXML
  private JFXTextField num;
  @FXML
  private JFXTextField nom;
  @FXML
  private JFXTextField prenom;
  @FXML
  private JFXTextField lieuNaissance;
  @FXML
  private JFXTextField addressPersonel;
  @FXML
  private JFXTextField addressElectronique;
  @FXML
  private JFXTextField numTel;
  @FXML
  private JFXTextField profession;
  @FXML
  private JFXDatePicker dateNaissance;
  @FXML
  private JFXDatePicker dateJointeur;
  @FXML
  private JFXButton ajouter;
  @FXML
  private JFXButton modifier;
  @FXML
  private JFXButton reactive1;
  @FXML
  private JFXTextField numExemplairePossible;
  @FXML
  private JFXComboBox<String> sexe;
  
  private  ObservableList<Adherent> list ;
    // ********************************* Table d'Adherent ******************************* \\
    @FXML
    private TableView<Adherent> tableView;
    @FXML
    private TableColumn<Adherent, Integer> numAd;
    @FXML
    private TableColumn<Adherent, String> nomAd;
    @FXML
    private TableColumn<Adherent, String> prenomAd;
    @FXML
    private TableColumn<Adherent, String> lieuNa;
    @FXML
    private TableColumn<Adherent, String> dateNa;
    @FXML
    private TableColumn<Adherent, String> addressAd;
    @FXML
    private TableColumn<Adherent, String> addressEl;
    @FXML
    private TableColumn<Adherent, String> numTelf;
    @FXML
    private TableColumn<Adherent, String> dateJoin;
    @FXML
    private TableColumn<Adherent, String> dateRenouv;
    @FXML
    private TableColumn<Adherent, String> proffes;
    
    // ********************************** Search **********************************\\
    @FXML
    private JFXTextField Search;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bdd = DataBase.getInstance(); 
        // ***************** Ajouter adherent ******************** \\
        remplireID();
      fillSexe();
      //  bdd.execAction(query);
       // ajouterAdherent();
       initColumn();
       loadData();
       recupererDonneeAdherent();
       
       Search.textProperty().addListener((observable, oldValue, newValue) -> {
           try {
               ChercherUnAdherent();
           } catch (SQLException ex) {
               Logger.getLogger(FXMLgestionMemberController.class.getName()).log(Level.SEVERE, null, ex);
           }
        });

        String a = " يجرى البحث وفقا ل :"+"\n"+"رقم العضو ,الاسم ,اللقب ,المهنة";
        Search.tooltipProperty().setValue(new Tooltip(a));
    }  
        // ********************************* Ajouter Adherent ******************************* \\
    @FXML
    public void ajouterAdherent(ActionEvent event) throws SQLException{
        String nomA = nom.getText();
        String prenomeA = prenom.getText();
        String numTelf = numTel.getText();
        String profes = profession.getText();
        String lieu = lieuNaissance.getText();
        String addres = addressPersonel.getText();
        String email = addressElectronique.getText();
        int numEx = Integer.parseInt(numExemplairePossible.getText());
        LocalDate ld = dateJointeur.getValue();
        Calendar c =  Calendar.getInstance();
        String sexeAdherent = sexe.getValue();
       int sexeAdh;
       // c.setTime(dateJointeur);
       //.setValue();
        if (sexeAdherent.equals("ذكر")){
            sexeAdh = 1;
            
        }else{
            sexeAdh = 0;
        }
       String query ;
       Date date1=c.getTime();
       if(AdherentDataNullAjout()==1){
           Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("أدخل البيانات من فضلك");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert.showAndWait();
               
       } else {
         if (ld == null) {
                String l =dateFormat.format(date1); 
                dateJointeur.setValue(LOCAL_DATE(l));
               ld =LOCAL_DATE(l);
           }
           c.set(ld.getYear()+1, ld.getMonthValue()-1, ld.getDayOfMonth());
            Date date=c.getTime();
            dateFormat.format(date);
            
            System.out.println(dateJointeur.getValue());
            System.out.println(dateFormat.format(date));
           
           query = "insert into Adherent (Nom,Prenom,sexe,DateNaissance,lieuNaissance,Adresse,Telephone,email,Specialite,DateEngagement,DateRenouvelement) VALUES("+
                    "'" + nomA + "'," +    
                    "'" + prenomeA + "'," +
                   "'" + sexeAdh + "'," +
                    "'" + dateNaissance.getValue() + "'," +
                    "'" + lieu + "'," +
                    "'" + addres + "'," +
                    "'" + numTelf + "'," +
                   "'" + email + "'," +
                   "'" + profes + "'," +
                   "'" + dateJointeur.getValue() + "'," +
                   "'" + dateFormat.format(date) + "'" +
                     ")";
                     System.out.println(query);
                    
                    // bdd.execAction(query);
                   //  videFenetre();
                      if(bdd.execAction(query)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("تمت الاضافة بنجاح");
                Stage stage2 = (Stage) alert.getDialogPane().getScene().getWindow();
                stage2.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert.showAndWait();            
                videFenetre();
                remplireID();
                loadData();
                }else{
                     Alert alert = new Alert(Alert.AlertType.ERROR);
                     alert.setHeaderText(null);
                     Stage stage3 = (Stage) alert.getDialogPane().getScene().getWindow();
                     stage3.getIcons().add(new Image("/darhadit/images/dar0.png"));
                     alert.setContentText("خـطأ في قاعدة البيانات");
                     alert.showAndWait();
                }
//       
    }
    }
    private void remplireID() {
        String qu = "select count(*) from Adherent";
        numExemplairePossible.setText("2");
        ResultSet rs = bdd.execQuery(qu);
        int nbr=1;
        try {
            if(rs.next()){
                nbr=rs.getInt(1);
            }
            System.out.println("num dernier: "+nbr);
            num.setText(nbr+1+"");
            num.setDisable(true);
        } catch (SQLException ex) {
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private int AdherentDataNullAjout(){
         if(numTel.getText().equals("") || profession.getText().equals("") || addressAd.getText().equals("") || nom.getText().equals("") || prenom.getText().equals(""))
         return 1;
         else return 0;
     
     
     }
    private void videFenetre(){
     nom.setText("");
     prenom.setText("");
     numTel.setText("");
     profession.setText("");
     lieuNaissance.setText("");
     sexe.setValue(sexe.getItems().get(1));
     addressElectronique.setText("");
     addressPersonel.setText("");
     dateJointeur.setValue(null);
     dateNaissance.setValue(null);
    }
    
    // ********************************* Table d'Adherent ******************************* \\
    private void initColumn() {
   
        numAd.setCellValueFactory(new PropertyValueFactory<>("numAdherent"));
        nomAd.setCellValueFactory(new PropertyValueFactory<>("nomAdherent"));
        prenomAd.setCellValueFactory(new PropertyValueFactory<>("prenomAdherent"));
        dateNa.setCellValueFactory(new PropertyValueFactory<>("DateNaissance"));
        lieuNa.setCellValueFactory(new PropertyValueFactory<>("lieuNaissanceAdherent"));
        addressAd.setCellValueFactory(new PropertyValueFactory<>("addresAdherent"));
        addressEl.setCellValueFactory(new PropertyValueFactory<>("e_mailAdherent"));
        numTelf.setCellValueFactory(new PropertyValueFactory<>("telAdherent"));
        proffes.setCellValueFactory(new PropertyValueFactory<>("specialiteAdherent"));
        dateJoin.setCellValueFactory(new PropertyValueFactory<>("DateEngagement"));
        dateRenouv.setCellValueFactory(new PropertyValueFactory<>("DateRenevloument"));
    }
       private void loadData() {
      
        
       list=FXCollections.observableArrayList();
       list.removeAll(list);
        String qu = "SELECT * FROM Adherent";
        bdd = DataBase.getInstance();
        ResultSet rs = bdd.execQuery(qu);
        try {
            while (rs.next()) {
                int numad = rs.getInt(1);
                int numExPossible=rs.getInt("nbr_emp_possible");
                String name = rs.getString("Nom");
                String prenome = rs.getString("Prenom");
                String addr = rs.getString("Adresse");
                String telfn = rs.getString("Telephone");
                String spec = rs.getString("Specialite");
                String email = rs.getString("email");
                String lieu = rs.getString("lieuNaissance");
                String dateN = rs.getDate("DateNaissance").toString();
                String dateRe = rs.getDate("DateRenouvelement").toString();
                String dateEnga = rs.getDate("DateEngagement").toString();
            
                list.add(new Adherent(numad,numExPossible ,name, prenome, lieu, telfn, addr, email, spec, dateEnga, dateRe, dateN));
                System.out.println(numExPossible);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLgestionMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.setItems(null);
        tableView.setItems(list);
        
    }
      
       
           
       
    public static final LocalDate LOCAL_DATE (String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
}
        private void recupererDonneeAdherent(){
            
            
             tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
             @Override
             public void handle(MouseEvent event) {
                 String query;
                 int a = -1;
                 if(event.getClickCount() == 2){
                     
                    ajouter.setDisable(true);
                    modifier.setDisable(false);
                    numExemplairePossible.setDisable(false);
                    reactive1.setDisable(false);
                    Adherent ad = tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
                    query="Select sexe FROM Adherent where idAdherent = "+ad.getNumAdherent();
                    ResultSet rs=bdd.execQuery(query);
                     try {
                         if(rs.next()){
                             a = rs.getInt("sexe");
                             System.out.println(a);
                         }} catch (SQLException ex) {
                         Logger.getLogger(FXMLgestionMemberController.class.getName()).log(Level.SEVERE, null, ex);
                     }
                     
                    num.setText(ad.getNumAdherent()+"");
                    nom.setText(ad.getNomAdherent());
                    prenom.setText(ad.getPrenomAdherent());
                    sexe.setValue(sexe.getItems().get(a));
                    addressElectronique.setText(ad.getE_mailAdherent());
                    addressPersonel.setText(ad.getAddresAdherent());
                    numTel.setText(ad.getTelAdherent());
                    lieuNaissance.setText(ad.getLieuNaissanceAdherent());
                    numExemplairePossible.setText(ad.getNbr_emp_possible()+"");
                    profession.setText(ad.getSpecialiteAdherent());
                    dateJointeur.setValue(LOCAL_DATE(ad.getDateEngagement()));
                    dateNaissance.setValue(LOCAL_DATE(ad.getDateNaissance())) ; 
                  }
                  } 
         });
 
     }
     @FXML
     private void CancelButton(ActionEvent event){
         videFenetre();
         remplireID();
         ajouter.setDisable(false);
         reactive1.setDisable(true);
         numExemplairePossible.setDisable(true);
         if (!modifier.isDisable()) {
            modifier.setDisable(true); 
         }
     } 
     
     @FXML
     private void modfierAdherent(ActionEvent event){
        LocalDate ld = dateJointeur.getValue();
        Calendar c =  Calendar.getInstance();
        c.set(ld.getYear()+1, ld.getMonthValue()-1, ld.getDayOfMonth());
        Date date=c.getTime();
        int code = Integer.parseInt(num.getText());
        int sexeAdh = sexAd(sexe.getValue());
         String query = "UPDATE Adherent SET Nom='"+nom.getText()+"'"
                 + ",Prenom='"+prenom.getText()+"', DateNaissance ='"
                 +dateNaissance.getValue()+"', sexe ='"+sexeAdh
                 +"', lieuNaissance ='"+lieuNaissance.getText()+"',"
                 + " Adresse ='"+addressPersonel.getText()+"', Telephone='"+numTel.getText()+"', nbr_emp_possible='"+numExemplairePossible.getText()+"', email='"+addressElectronique.getText()+"',"
                 + " Specialite='"+profession.getText()+"',"
                 + " nbr_emp_possible='"+ numExemplairePossible.getText()+"',"
                 + " DateEngagement ='"+dateJointeur.getValue()+"' ,DateRenouvelement = '"+dateFormat.format(date)+"' WHERE idAdherent="+code+";";
           if(AdherentDataNullAjout()==1){
           Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert.setContentText("أدخل البيانات من فضلك");
                alert.showAndWait();
               
       } else {
              if(bdd.execAction(query)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert.setContentText("تمت التعديل بنجاح");
                alert.showAndWait(); 
                loadData();
                videFenetre();
                  CancelButton(event);
                
                }else{
                     Alert alert = new Alert(Alert.AlertType.ERROR);
                     alert.setHeaderText(null);
                     Stage stage2 = (Stage) alert.getDialogPane().getScene().getWindow();
                     stage2.getIcons().add(new Image("/darhadit/images/dar0.png"));
                     alert.setContentText("خـطأ في قاعدة البيانات");
                     alert.showAndWait();
                }
           }
     }
     private void ChercherUnAdherent() throws SQLException{
         String recherche = Search.getText();
         String query="SELECT * FROM Adherent WHERE Nom LIKE '"+recherche+"%' OR Prenom LIKE '"+recherche+"%' OR Specialite LIKE '"+recherche+"%' ";
         
         try { 
                query += " OR idAdherent ="+ Integer.parseInt(recherche);
            } catch (Exception ex) {
            }
         
         try {
            list=FXCollections.observableArrayList();
            ResultSet rs=bdd.execQuery(query);
             while (rs.next()) {
                 int numad = rs.getInt(1);
                 int numExPossible=rs.getInt("nbr_emp_possible");
                String name = rs.getString("Nom");
                String prenome = rs.getString("Prenom");
                String addr = rs.getString("Adresse");
                String telfn = rs.getString("Telephone");
                String spec = rs.getString("Specialite");
                String email = rs.getString("email");
                String lieu = rs.getString("lieuNaissance");
                String dateN = rs.getDate("DateNaissance").toString();
                String dateRe = rs.getDate("DateRenouvelement").toString();
                String dateEnga = rs.getDate("DateEngagement").toString();
            
                list.add(new Adherent(numad,numExPossible ,name, prenome, lieu, telfn, addr, email, spec, dateEnga, dateRe, dateN));
                 
             }
         } catch (Exception e) {
             System.err.println("خـطأ في قاعدة البيانات"+e);
         }
         initColumn();
         tableView.setItems(list);
     }
     @FXML
     private void reactive(ActionEvent event){
         Date d = new Date();
         
         LocalDate ld = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
         System.out.println(ld);
        Calendar c =  Calendar.getInstance();
        c.set(ld.getYear()+1, ld.getMonthValue()-1, ld.getDayOfMonth());
        Date date=c.getTime();
        System.out.println(dateFormat.format(date));
        int code = Integer.parseInt(num.getText());
        String query = "UPDATE Adherent SET DateEngagement ='"+ld+"',DateRenouvelement ='"+dateFormat.format(date)+"' WHERE idAdherent="+code+";";
        System.out.println(query);
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("تأكيد الحوار");
        Stage stage = (Stage) alertConfirm.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
        alertConfirm.setHeaderText("تأكيد التجديد ");
        alertConfirm.setContentText("هل أنت موافق مع هذا؟");
        Optional<ButtonType> result = alertConfirm.showAndWait();
        if (result.get() == ButtonType.OK){
            if(bdd.execAction(query)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                Stage stage3 = (Stage) alert.getDialogPane().getScene().getWindow();
                stage3.getIcons().add(new Image("/darhadit/images/dar0.png"));
                alert.setContentText("تمت التجديد بنجاح");
                alert.showAndWait(); 
                loadData();
                
                reactive1.setDisable(true);
                
                }else{
                     Alert alert = new Alert(Alert.AlertType.ERROR);
                     alert.setHeaderText(null);
                     Stage stage4 = (Stage) alert.getDialogPane().getScene().getWindow();
                     stage4.getIcons().add(new Image("/darhadit/images/dar0.png"));
                     alert.setContentText("خـطأ في قاعدة البيانات");
                     alert.showAndWait();
                }
        }
           
     }
      private void fillSexe() {
        ObservableList<String>  listeSexe = FXCollections.observableArrayList();
        listeSexe.add("أنثى");
        listeSexe.add("ذكر");
        sexe.setItems(listeSexe);
        sexe.setValue(sexe.getItems().get(1));
    }
      private int sexAd(String a){
          if (a.equals("ذكر")) 
              return 1;
          else return 0;
      }
    
 
      @FXML
      private void AddMemoAdherent(ActionEvent e){
          Adherent ad = tableView.getSelectionModel().getSelectedItem();
           if(ad == null){
               ClassAlert.errorAlert("لا يوجد عضو محدد", "خطأ");
            return;
        }
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/darhadit/GestionMember/Memo/Memo.fxml"));
            Parent parent = loader.load();
            
            MemoController controller =(MemoController)  loader.getController();
           // controller.
           controller.Ajouter_memo(ad);
            
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("تعليقات");
            stage.setScene(new Scene(parent));
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.show();
            
        }catch(IOException ex){
            Logger.getLogger(MemoController.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      @FXML
      private void ListMemoAdherent(ActionEvent e) throws SQLException{
          Adherent ad = tableView.getSelectionModel().getSelectedItem();
           if(ad == null){
               ClassAlert.errorAlert("لا يوجد عضو محدد", "خطأ");
            return;
        }
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/darhadit/GestionMember/Memo/Memo.fxml"));
            Parent parent = loader.load();
            
            MemoController controller =(MemoController)  loader.getController();
           // controller.
           controller.affiche_memo_Adherent(ad);
            
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("تعليقات");
            stage.setScene(new Scene(parent));
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.show();
            
        }catch(IOException ex){
            Logger.getLogger(MemoController.class.getName()).log(Level.SEVERE, null, ex);
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
            /*
          Scene scence = new Scene(parent);
          Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
          stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.setTitle("إدارة الاعضاء");
            stage.setScene(scence);
            stage.show();*/
          }catch(IOException ex){
              Logger.getLogger(FXMLgestionMemberController.class.getName()).log(Level.SEVERE, null, ex);
          }
          
      }
      
}
