package darhadit.listDocument.listExemplaire;

import darhadit.DataBase.DataBase;
import darhadit.addDocument.AddDocumentController;
import darhadit.alert.ClassAlert;
import darhadit.listDocument.ListDocumentController;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleFloatProperty;
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
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author ....
 */
public class ListExemplaireController implements Initializable {
    
    ObservableList<Exemplaire> list = FXCollections.observableArrayList();
    @FXML
    private TableView<Exemplaire> tableView;
    @FXML
    private TableColumn<Exemplaire, Integer> docCol;
    @FXML
    private TableColumn<Exemplaire, Integer> exemCol;
    @FXML
    private TableColumn<Exemplaire, String> emplacementCol;
    @FXML
    private TableColumn<Exemplaire, String> empruntableCol;
    @FXML
    private TableColumn<Exemplaire, String> disponibleCol;
    @FXML
    private TableColumn<Exemplaire, String> enregCol;
    @FXML
    private TableColumn<Exemplaire, String> versionCol;
    @FXML
    private TableColumn<Exemplaire, Float> prixCol;
    @FXML
    private TableColumn<Exemplaire, String> sourceCol;
    @FXML
    private TableColumn<?, ?> emplacementCol1;
    @FXML
    private AnchorPane rootPane;
    
    public String rech="";
    public String typ="";
    public String cat="";
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
    }   
    
    private void initCol() {
        docCol.setCellValueFactory(new PropertyValueFactory<>("id_doc"));
        exemCol.setCellValueFactory(new PropertyValueFactory<>("id_exemp"));
        emplacementCol.setCellValueFactory(new PropertyValueFactory<>("emplacement"));
        emplacementCol1.setCellValueFactory(new PropertyValueFactory<>("num_cat"));
        empruntableCol.setCellValueFactory(new PropertyValueFactory<>("empruntable"));
        disponibleCol.setCellValueFactory(new PropertyValueFactory<>("disponible"));
        enregCol.setCellValueFactory(new PropertyValueFactory<>("date_enreg"));
        versionCol.setCellValueFactory(new PropertyValueFactory<>("version"));
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
        sourceCol.setCellValueFactory(new PropertyValueFactory<>("source"));
    }

    public void afficher_liste(int num) {
        list.clear();
        DataBase base = DataBase.getInstance();        
        String qu = "select * from Exemplaire where idDocument = " + num;
        ResultSet rs = base.execQuery(qu);
        try {
            while(rs.next()){
                int id_doc = rs.getInt("idDocument");
                int id_exemp = rs.getInt("idExemplaire");
                String empla = rs.getString("emplacement");
                int num_cat = rs.getInt("num_categorie");
                String disp = oui_non(rs.getBoolean("disponible"));
                String empr = oui_non(rs.getBoolean("empruntable"));
                String source = rs.getString("sourceExemplaire");
                String date_enreg = rs.getString("dateEnregistrement");               
                Float prix = rs.getFloat("prix");
                String version  = rs.getString("version");   
        list.add(new Exemplaire(id_doc, id_exemp, empla, num_cat, empr, disp, date_enreg , version, prix, source));
                        }
        } catch (SQLException ex) {
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.setItems(list);
    }
    
    private String oui_non(boolean nbr){
        if(nbr == true) return "نعم";
        else return "لا";
    }

    @FXML
    private void modifier_exemplaire(ActionEvent event) {
        Exemplaire selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if(selectedForEdit == null){
            ClassAlert.errorAlert("لا يوجد كتاب محدد", "خطأ ");
            return;
        }
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/darhadit/addDocument/AddDocument.fxml"));
            Parent parent = loader.load();
            
            AddDocumentController controller = (AddDocumentController) loader.getController();
            controller.modifierExemplaire(selectedForEdit);
            
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("تعديل الوثيقة");
            stage.setScene(new Scene(parent));
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.show();
            
            //Stage stage2 = (Stage) rootPane.getScene().getWindow();  
            //stage2.close();
            
            stage.setOnCloseRequest((e)->{
                actualiser_liste_exemplaire(selectedForEdit.getId_doc());
                //stage2.show();
            });
        }catch(IOException ex){
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void actualiser_liste_exemplaire(int num) {
        afficher_liste(num);
    }

    @FXML
    private void actualiser_liste_exemplaire_v2(ActionEvent event) {
        Exemplaire selectedForEdit = tableView.getItems().get(0);
        //if(selectedForEdit != null){
            afficher_liste(selectedForEdit.getId_doc());
        //}   
    }

    @FXML
    private void ajouter_exemplaire(ActionEvent event) {
        Exemplaire selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if(selectedForEdit == null){
            ClassAlert.errorAlert("لا يوجد كتاب محدد", "خطأ ");
            return;
        }
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/darhadit/addDocument/AddDocument.fxml"));
            Parent parent = loader.load();
            
            AddDocumentController controller = (AddDocumentController) loader.getController();
            controller.ajouter_Exemplaire_exemplaire(selectedForEdit);
            
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("تعديل الوثيقة");
            stage.setScene(new Scene(parent));
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.show();
            
            stage.setOnCloseRequest((e)->{
                actualiser_liste_exemplaire(selectedForEdit.getId_doc());
            });
        }catch(IOException ex){
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void supp_exemplaire(ActionEvent event) {
        Exemplaire selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if(selectedForEdit == null){
            ClassAlert.errorAlert("لا يوجد كتاب محدد", "خطأ ");
            return;
        }
        if(ClassAlert.confirmDialog("هل  تريد حذف النسخة", "حذف النسخة ")){
            String qu = "delete from exemplaire where idExemplaire="+selectedForEdit.getId_exemp()+" AND idDocument="+selectedForEdit.getId_doc();
            String qu2 = "update document set nbrExemplaire=nbrExemplaire-1 where idDocument="+selectedForEdit.getId_doc();
            DataBase base = DataBase.getInstance(); 
            if(base.execAction(qu) && base.execAction(qu2)){
                list.remove(selectedForEdit);
                ClassAlert.infoAlert("تم حذف النسخة بنجاح", "نجاح");
                
            }
            else
                ClassAlert.infoAlert("فشل لا يمكن حذفها لأنها معارة", "فشل");
        }
        else{
            ClassAlert.infoAlert("عملية الحذف الملغاة", "الحذف ملغى");
        }
    }
    @FXML
      private void backToDoc(ActionEvent event){
          try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/darhadit/listDocument/ListDocument.fxml"));  
        AnchorPane pane = loader.load();      
              
          //AnchorPane pane =FXMLLoader.load(getClass().getResource("/darhadit/listDocument/ListDocument.fxml"));
            rootPane.setBottomAnchor(pane, 0.0);
            rootPane.setTopAnchor(pane, 0.0);
            rootPane.setLeftAnchor(pane, 0.0);
            rootPane.setRightAnchor(pane, 0.0);
            rootPane.getChildren().setAll(pane);
            
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/darhadit/listDocument/ListDocument.fxml"));
            ListDocumentController controller = (ListDocumentController) loader.getController();
            controller.version_precedente(cat,typ,rech);
            
            
         /* Scene scence = new Scene(parent);
          Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
          stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.setTitle("إدارة الاعضاء");
            stage.setScene(scence);
            stage.show();*/
          }catch(IOException ex){
              Logger.getLogger(ListExemplaireController.class.getName()).log(Level.SEVERE, null, ex);
          }
          
      }
    
    public static class Exemplaire{
        private final SimpleIntegerProperty id_doc;
        private final SimpleIntegerProperty id_exemp;
        private final SimpleStringProperty emplacement;
        private final SimpleIntegerProperty num_cat;
        private final SimpleStringProperty empruntable;
        private final SimpleStringProperty disponible;
        private final SimpleStringProperty date_enreg;
        private final SimpleStringProperty version;
        private final SimpleFloatProperty prix;
        private final SimpleStringProperty source; 
        
Exemplaire(int id_doc,int id_exemp,String emplacement,int num_cat,String empruntable,String disponible,String date_enreg,String version,Float prix,String source){
                this.id_doc =  new SimpleIntegerProperty(id_doc);
                this.id_exemp = new SimpleIntegerProperty(id_exemp);
                this.emplacement = new SimpleStringProperty(emplacement); 
                this.num_cat = new SimpleIntegerProperty(num_cat);              
                this.empruntable = new SimpleStringProperty(empruntable);
                this.disponible = new SimpleStringProperty(disponible);
                this.date_enreg = new SimpleStringProperty(date_enreg);
                this.version = new SimpleStringProperty(version);
                this.prix = new SimpleFloatProperty(prix);
                this.source = new SimpleStringProperty(source);      
        }

        public Integer getId_doc() {
            return id_doc.get();
        }

        public Integer getId_exemp() {
            return id_exemp.get();
        }

        public String getEmplacement() {
            return emplacement.get();
        }
        
        public Integer getNum_cat() {
            return num_cat.get();
        }

        public String getEmpruntable() {
            return empruntable.get();
        }

        public String getDisponible() {
            return disponible.get();
        }

        public String getDate_enreg() {
            return date_enreg.get();
        }

        public String getVersion() {
            return version.get();
        }

        public Float getPrix() {
            return prix.get();
        }

        public String getSource() {
            return source.get();
        }
    }
}
