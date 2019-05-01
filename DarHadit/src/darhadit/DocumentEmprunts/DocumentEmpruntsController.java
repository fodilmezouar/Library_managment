package darhadit.DocumentEmprunts;

import com.jfoenix.controls.JFXTextField;
import darhadit.DataBase.DataBase;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DocumentEmpruntsController implements Initializable {

    DataBase base;
    
    ObservableList<Emprunts> list = FXCollections.observableArrayList();
    
    
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Emprunts> tableView;
    @FXML
    private TableColumn<Emprunts, Integer> numDocumentCol;
    @FXML
    private TableColumn<Emprunts, Integer> numExemplaireCol;
    @FXML
    private TableColumn<Emprunts, String> titreDocumentCol;
    @FXML
    private TableColumn<Emprunts, Integer> numAdherentCol;
    @FXML
    private TableColumn<Emprunts, String> prenomAdherentCol;
    @FXML
    private TableColumn<Emprunts, String> nomAdherentCol;
    @FXML
    private TableColumn<Emprunts, String> telephoneAdherentCol;
    @FXML
    private TableColumn<Emprunts, String> emailAdherentCol;
    @FXML
    private TableColumn<Emprunts, Integer> joursCol;
    @FXML
    private JFXTextField recherche;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initCol();
        base = DataBase.getInstance();
        loadData();
        String a = " يجرى البحث وفقا ل :"+"\n"+"رقم الوثيقة ,رقم النسخة أو عنوان "+"\n"+"رقم العضو ,الاسم أو اللقب";
        recherche.tooltipProperty().setValue(new Tooltip(a));
    }

    private void initCol() {
        numDocumentCol.setCellValueFactory(new PropertyValueFactory<>("numDocument"));
        numExemplaireCol.setCellValueFactory(new PropertyValueFactory<>("numExemplaire"));
        titreDocumentCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        numAdherentCol.setCellValueFactory(new PropertyValueFactory<>("numAdherent"));
        prenomAdherentCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        nomAdherentCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        telephoneAdherentCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        emailAdherentCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        joursCol.setCellValueFactory(new PropertyValueFactory<>("jours"));
    }

    private void loadData() {
        String qu = "SELECT E.idDocument, E.idExemplaire, Titre, E.idAdherent, Prenom, Nom, Telephone, email, DATEDIFF(dateRetour, CURRENT_DATE()) AS nbrJours "
                + "FROM Emprunt E, Adherent A, document D "
                + "WHERE E.idAdherent = A.idAdherent AND E.idDocument = D.idDocument ";
        ResultSet rs = base.execQuery(qu);
        try {
            while(rs.next()){
                int idDoc = rs.getInt("idDocument");
                int idExemp = rs.getInt("idExemplaire");
                String titre = rs.getString("Titre");
                int idAdher = rs.getInt("idAdherent");
                String prenom = rs.getString("Prenom");
                String nom = rs.getString("Nom");
                String tel = rs.getString("Telephone");
                String email = rs.getString("email");
                int nbrJours = rs.getInt("nbrJours");
                
                list.add(new Emprunts(idDoc, idExemp, titre, idAdher, prenom, nom, tel, email, nbrJours));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DocumentEmpruntsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tableView.getItems().setAll(list);
    }

    @FXML
    private void rechercheEmprunts(KeyEvent event) {
        String chaine = recherche.getText();
        int intChaine;
        if(!chaine.trim().isEmpty()){
            String qu = "SELECT E.idDocument, E.idExemplaire, Titre, E.idAdherent, Prenom, Nom, Telephone, email, DATEDIFF(dateRetour, CURRENT_DATE()) AS nbrJours \n"
                    + "FROM Emprunt E, Adherent A, document D \n"
                    + "WHERE (E.idAdherent = A.idAdherent AND E.idDocument = D.idDocument) AND (\n";
            try {
                qu += "E.idDocument = " + Integer.parseInt(chaine) + " OR \n";
                qu += "E.idExemplaire = "+ Integer.parseInt(chaine) + " OR \n";
                qu += "E.idAdherent = " + Integer.parseInt(chaine) + " OR \n";
            } catch (NumberFormatException ex){
                
            }   
                    qu += "Titre LIKE '%" + chaine + "%' OR \n";     
                    qu += "Prenom LIKE '%" + chaine + "%' OR \n";
                    qu += "Nom LIKE '%" + chaine + "%')";
            System.out.println(qu);
            ResultSet rs = base.execQuery(qu);
            try {
                list.clear();
                while(rs.next()){
                    int idDoc = rs.getInt("idDocument");
                    int idExemp = rs.getInt("idExemplaire");
                    String titre = rs.getString("Titre");
                    int idAdher = rs.getInt("idAdherent");
                    String prenom = rs.getString("Prenom");
                    String nom = rs.getString("Nom");
                    String tel = rs.getString("Telephone");
                    String email = rs.getString("email");
                    int nbrJours = rs.getInt("nbrJours");

                    list.add(new Emprunts(idDoc, idExemp, titre, idAdher, prenom, nom, tel, email, nbrJours));
                }
            } catch (SQLException ex) {
                Logger.getLogger(DocumentEmpruntsController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            tableView.getItems().setAll(list);
        }else{
            list.clear();
            loadData();
        }
    }
    @FXML
      private void backToDash(ActionEvent event) throws IOException{
            AnchorPane pane =FXMLLoader.load(getClass().getResource("/darhadit/dashboard/dashboard.fxml"));
            rootPane.setBottomAnchor(pane, 0.0);
            rootPane.setTopAnchor(pane, 0.0);
            rootPane.setLeftAnchor(pane, 0.0);
            rootPane.setRightAnchor(pane, 0.0);
            rootPane.getChildren().setAll(pane);
          /* Scene scence = new Scene(parent);
          Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
          stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
          stage.setTitle("إدارة الاعضاء");
          stage.setScene(scence);
          stage.show();
          */
      }
    
    public static class Emprunts {
        private final SimpleIntegerProperty numDocument;
        private final SimpleIntegerProperty numExemplaire;
        private final SimpleStringProperty titre;
        private final SimpleIntegerProperty numAdherent;
        private final SimpleStringProperty prenom;
        private final SimpleStringProperty nom;
        private final SimpleStringProperty telephone;
        private final SimpleStringProperty email;
        private final SimpleIntegerProperty jours;
        
        public Emprunts(int numDocument, int numExemplaire, String titre, int numAdherent, String prenom, String nom, String telephone, String email, int jours){
            this.numDocument = new SimpleIntegerProperty(numDocument);
            this.numExemplaire = new SimpleIntegerProperty(numExemplaire);
            this.titre = new SimpleStringProperty(titre);
            this.numAdherent = new SimpleIntegerProperty(numAdherent);
            this.prenom = new SimpleStringProperty(prenom);
            this.nom = new SimpleStringProperty(nom);
            this.telephone = new SimpleStringProperty(telephone);
            this.email = new SimpleStringProperty(email);
            this.jours = new SimpleIntegerProperty(jours);
        }

        public int getNumDocument() {
            return numDocument.get();
        }

        public int getNumExemplaire() {
            return numExemplaire.get();
        }

        public String getTitre() {
            return titre.get();
        }

        public int getNumAdherent() {
            return numAdherent.get();
        }

        public String getPrenom() {
            return prenom.get();
        }

        public String getNom() {
            return nom.get();
        }

        public String getTelephone() {
            return telephone.get();
        }

        public String getEmail() {
            return email.get();
        }

        public int getJours() {
            return jours.get();
        }
    }
}
