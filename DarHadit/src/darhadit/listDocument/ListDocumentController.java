package darhadit.listDocument;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import darhadit.DataBase.DataBase;
import darhadit.addDocument.AddDocumentController;
import darhadit.alert.ClassAlert;
import darhadit.listDocument.listExemplaire.ListExemplaireController;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ....
 */
public class ListDocumentController implements Initializable {
    
    ObservableList<Document> list = FXCollections.observableArrayList();
    
    DataBase base;
    Boolean listeComplet = false;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Document> tableView;
    @FXML
    private TableColumn<Document, Integer> idCol;
    @FXML
    private TableColumn<Document, String> titreCol;
    @FXML
    private TableColumn<Document, String> auteurCol;
    @FXML
    private TableColumn<Document, String> maison_editionCol;
    @FXML
    private TableColumn<Document, String> categorieCol;
    @FXML
    private TableColumn<Document, Integer> nbr_exempCol;
    @FXML
    private TableColumn<Document, String> typeCol;
    @FXML
    private TableColumn<Document, Integer> partieCol;
    @FXML
    private TableColumn<Document, Integer> nbr_pieceCol;
    @FXML
    private JFXTextField recherche;
    @FXML
    private JFXComboBox<String> comb1;
    @FXML
    private JFXComboBox<String> comb2;
    @FXML
    private StackPane PaneRoot;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        base = DataBase.getInstance();
        //recherche.setTooltip(new Tooltip("qsdqsdv"));
        fillComb2();
        fillComb1();
        initCol();
        loadData();
        String a = " يجرى البحث وفقا ل :"+"\n"+"رقم الوثيقة ,العنوان أو المألف";
        recherche.tooltipProperty().setValue(new Tooltip(a));
    }   
    
    private void fillComb1() {
        comb1.getItems().clear();
        ObservableList<String>  list0 = FXCollections.observableArrayList();
        String qu = "select * from categorie";
        ResultSet rs = base.execQuery(qu);
        try {
            while(rs.next()){
                list0.add(rs.getString("nomCategorie"));
                        }
        } catch (SQLException ex) {
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        list0.add("جميع التخصصات");
        comb1.setItems(list0);
        if(!list0.isEmpty()){
            comb1.setValue("جميع التخصصات");
        }
    }
    
    private void fillComb2() {
        ObservableList<String>  list3 = FXCollections.observableArrayList();
        list3.add("كتاب");
        list3.add("مذكرة");
        list3.add("مجلة");
        list3.add("كل الأنواع");
        comb2.setItems(list3);
        comb2.setValue("كل الأنواع");
    }
    
    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        auteurCol.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        categorieCol.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        nbr_exempCol.setCellValueFactory(new PropertyValueFactory<>("nbr_exem"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        maison_editionCol.setCellValueFactory(new PropertyValueFactory<>("maison_edition"));
        partieCol.setCellValueFactory(new PropertyValueFactory<>("partie"));
        nbr_pieceCol.setCellValueFactory(new PropertyValueFactory<>("nbr_piece"));
    }

    private void loadData() {
        list.clear();
        //DataBase base = new DataBase();
        String categorie = comb1.getValue();
        String type = comb2.getValue();
        boolean is_selected_cate = false;
        
        String qu = "select * from document ";
        if(!categorie.equals("جميع التخصصات")){
            qu += "where nomCategorie='" + categorie + "' ";
            is_selected_cate=true;
        }
        
        if(!type.equals("كل الأنواع")){
            if (is_selected_cate){
                qu += "AND types='" + type + "'";
            }
            else{
                qu += "WHERE types='" + type + "'";
            }
        }
        ResultSet rs = base.execQuery(qu);
        try {
            while(rs.next()){
                int id = rs.getInt("idDocument");
                String titrex = rs.getString("titre");
                String auteur = rs.getString("auteur");
                String maisonEdition = rs.getString("maisonEdition");
                String categ = rs.getString("nomCategorie");
                String types = rs.getString("types");
                int nbrExemplaire = rs.getInt("nbrExemplaire");     
                Boolean is_serie = rs.getBoolean("is_serie");                
                int partie = rs.getInt("partie");
                int nbrPieces  = rs.getInt("nbrPieces");
                String sommaire  = rs.getString("sommaire");
                String mot_cle  = rs.getString("mot_cle");
       list.add(new Document(id, titrex, auteur, categ, nbrExemplaire, types, maisonEdition, partie, nbrPieces,is_serie,sommaire,mot_cle));
                        }
        } catch (SQLException ex) {
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //recherche.setTooltip(new Tooltip("qsdqsdv"));
        tableView.setRowFactory(tv -> new TableRow<Document>() {
           
            private final Tooltip tooltip = new Tooltip();
            @Override
            public void updateItem(Document doc, boolean empty) {
                super.updateItem(doc, empty);

                if (doc == null) {
                    setTooltip(null);
                } else {
    tooltip.setText("عنوان الوثيقة  :   "+doc.getTitre()+"\n"+"مؤلف   :   "+doc.getAuteur()+"\n"+"دار النشر :  "+doc.getMaison_edition() );
                    setTooltip(tooltip);
                }
            }
        });
        tableView.setItems(list);
    }    
    
    @FXML
    private void recherhceListe_v2(KeyEvent event) {
        recherche.textProperty().addListener((observable, oldValue, newValue) -> {
            recherhceListe(new ActionEvent());
        });
    }

    @FXML
    private void recherhceListe(ActionEvent event) {
        String rech = recherche.getText();
        if(!rech.trim().isEmpty()){
            String qu = "select * from document where "
                            + "( Titre like '"+rech+"%' OR "
                            + "auteur like '"+rech+"%' OR "
                            //+ "maisonEdition like'%"+rech+"%' OR "
                            + "sommaire like '%"+rech+"%' OR "
                            + "mot_cle like '%"+rech+"%' ";
            try { 
                int b = Integer.parseInt(rech);
                qu += "OR idDocument="+rech+" )"; 
                   // + "partie= '"+rech+"' OR "
                   // + "nbrPieces = '"+rech+"' OR "
            } catch (Exception ex) {
                qu += ")";
            }
            if(!comb1.getValue().equals("جميع التخصصات")){
                qu += " AND nomCategorie= '"+ comb1.getValue() +"'";
            }
            if(!comb2.getValue().equals("كل الأنواع")){
                qu += " AND types= '"+ comb2.getValue() +"'";
            }
            ResultSet rs = base.execQuery(qu);
            list.clear();
            try {
                while(rs.next()){
                    int id = rs.getInt("idDocument");
                    String titrex = rs.getString("titre");
                    String auteur = rs.getString("auteur");
                    String maisonEdition = rs.getString("maisonEdition");
                    String categ = rs.getString("nomCategorie");
                    String types = rs.getString("types");
                    int a = rs.getInt("nbrExemplaire");
                    Boolean is_serie = rs.getBoolean("is_serie");                
                    int partie = rs.getInt("partie");
                    int nbrPieces  = rs.getInt("nbrPieces");
                    String sommaire  = rs.getString("sommaire");
                    String mot_cle  = rs.getString("mot_cle");
           list.add(new Document(id, titrex, auteur, categ, a, types, maisonEdition, partie, nbrPieces,is_serie,sommaire,mot_cle));
                            }
            } catch (SQLException ex) {
                Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            tableView.setItems(list);
            listeComplet=true;
        }
        else{
            if(listeComplet){
                //list.clear();
                loadData();
                listeComplet=false;
            }
        }
    }

    @FXML
    private void Combo_categorie(ActionEvent event) {
        String categorie = comb1.getValue();
        String type = comb2.getValue();
        String qu = "select * from document ";
        boolean is_selected_cate = false;
        if(!categorie.equals("جميع التخصصات"))
        {
            qu += "where nomCategorie='" + categorie + "'";
            is_selected_cate=true;
        }
        if(!type.equals("كل الأنواع")){
            if (is_selected_cate){
                qu += "AND types='" + type + "'";
            }
            else{
                qu += "WHERE types='" + type + "'";
                is_selected_cate=true;
            }
        }
        
        if(!recherche.getText().trim().isEmpty()){
            String rech = recherche.getText();
            String qu2 = "( Titre like '"+rech+"%' OR "
                            + "auteur like '"+rech+"%' OR "
                            //+ "maisonEdition like '%"+rech+"%' OR "
                            + "sommaire like '%"+rech+"%' OR "
                            + "mot_cle like '%"+rech+"%' ";
            if(is_selected_cate){
                qu += " AND " + qu2;
            }
            else{
                qu += "where " + qu2;
            }
            
            try { 
                int b = Integer.parseInt(rech);
                qu += "OR idDocument="+rech+"  )"; 
                // + "partie= '"+rech+"' OR "
                // + "nbrPieces = '"+rech+"' OR "
            } catch (Exception ex) {
                qu += ")";
            }
        }
        
        ResultSet rs = base.execQuery(qu);
        list.clear();
        try {
                while(rs.next()){
                    int id = rs.getInt("idDocument");
                    String titrex = rs.getString("titre");
                    String auteur = rs.getString("auteur");
                    String maisonEdition = rs.getString("maisonEdition");
                    String categ = rs.getString("nomCategorie");
                    String types = rs.getString("types");
                    int a = rs.getInt("nbrExemplaire");
                    Boolean is_serie = rs.getBoolean("is_serie");                
                    int partie = rs.getInt("partie");
                    int nbrPieces  = rs.getInt("nbrPieces");
                    String sommaire  = rs.getString("sommaire");
                    String mot_cle  = rs.getString("mot_cle");
                list.add(new Document(id, titrex, auteur, categ, a, types, maisonEdition, partie, nbrPieces,is_serie,sommaire,mot_cle));
                            }
            } catch (SQLException ex) {
                Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            tableView.setItems(list);
    }

    @FXML
    private void Combo_type(ActionEvent event) {
        String type = comb2.getValue();
        String cate = comb1.getValue();
        String qu = "select * from document ";
        boolean is_selected_type = false;
        if(!type.equals("كل الأنواع"))
        {
            qu += "where types='" + type + "'";
            is_selected_type=true;
        }
        if(!cate.equals("جميع التخصصات")){
            if (is_selected_type){
                qu += "AND nomCategorie ='" + cate + "'";
            }
            else{
                qu += "WHERE nomCategorie ='" + cate + "'";
                is_selected_type=true;
            }
        }
        
        if(!recherche.getText().trim().isEmpty()){
            String rech = recherche.getText();
            String qu2 = "( Titre Like '"+rech+"%' OR "
                            + "auteur Like '"+rech+"%' OR "
                            //+ "maisonEdition= '"+rech+"' OR "
                            //+ "nomCategorie= '"+rech+"' OR "
                            //+ "types= '"+rech+"' OR "
                            + "sommaire Like '%"+rech+"%' OR "
                            + "mot_cle Like '%"+rech+"%' ";
            if(is_selected_type){
                qu += " AND " + qu2;
            }
            else{
                qu += "where " + qu2;
            }
            
            try { 
                int b = Integer.parseInt(rech);
                qu += "OR idDocument="+rech+"  )"; 
                   // + "partie= '"+rech+"' OR "
                   // + "nbrPieces = '"+rech+"' OR "
            } catch (Exception ex) {
                qu += ")";
            }
        }
        
        ResultSet rs = base.execQuery(qu);
        list.clear();
        try {
                while(rs.next()){
                    int id = rs.getInt("idDocument");
                    System.out.println(""+id);
                    String titrex = rs.getString("titre");
                    String auteur = rs.getString("auteur");
                    String maisonEdition = rs.getString("maisonEdition");
                    String categ = rs.getString("nomCategorie");
                    String types = rs.getString("types");
                    int a = rs.getInt("nbrExemplaire");
                    //int nbrExemplaire = rs.getInt(7);
                    Boolean is_serie = rs.getBoolean("is_serie");                
                    int partie = rs.getInt("partie");
                    int nbrPieces  = rs.getInt("nbrPieces");
                    String sommaire  = rs.getString("sommaire");
                    String mot_cle  = rs.getString("mot_cle");
                list.add(new Document(id, titrex, auteur, categ, a, types, maisonEdition, partie, nbrPieces,is_serie,sommaire,mot_cle));
                            }
            } catch (SQLException ex) {
                Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            tableView.setItems(list);
    }

    @FXML
    private void modifier_document(ActionEvent event) {
        Document selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if(selectedForEdit == null){
            ClassAlert.errorAlert("لا يوجد كتاب محدد", "خطأ ");
            return;
        }
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/darhadit/addDocument/AddDocument.fxml"));
            Parent parent = loader.load();
            
            AddDocumentController controller = (AddDocumentController) loader.getController();
            controller.modifierDocument(selectedForEdit);
            
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("تعديل الوثيقة");
            stage.setScene(new Scene(parent));
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.show();
            
            //Stage stage2 = (Stage) PaneRoot.getScene().getWindow();  
            //stage2.close();
            
            stage.setOnCloseRequest((e)->{
                actualiser_liste_document(new ActionEvent());
                //stage2.show();
            });

        }catch(IOException ex){
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void information_Document(ActionEvent event) {
        Document selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if(selectedForEdit == null){
            ClassAlert.errorAlert("لا يوجد كتاب محدد", "خطأ ");
            return;
        }
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/darhadit/addDocument/AddDocument.fxml"));
            Parent parent = loader.load();
            
            AddDocumentController controller = (AddDocumentController) loader.getController();
            controller.informationDocument(selectedForEdit);
            
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("معلومات الوثيقة");
            stage.setScene(new Scene(parent));
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.show();

        }catch(IOException ex){
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void actualiser_liste_document(ActionEvent event) {
        Combo_categorie(new ActionEvent());
        //Combo_type(new ActionEvent());
        //loadData();
    }
    
    
    @FXML
    private void listExemplaire_v2(MouseEvent event) {
         tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
             @Override
             public void handle(MouseEvent event) {
                 if(event.getClickCount() == 2){
                     listExemplaire(new ActionEvent());
                 }
             } 
         });
    }

    @FXML
    private void listExemplaire(ActionEvent event) {
        Document selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if(selectedForEdit == null){
            ClassAlert.errorAlert("لا يوجد كتاب محدد", "خطأ");
            return;
        }
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/darhadit/listDocument/listExemplaire/ListExemplaire.fxml"));
            
            AnchorPane pane = loader.load();
            rootPane.setBottomAnchor(pane, 0.0);
            rootPane.setTopAnchor(pane, 0.0);
            rootPane.setLeftAnchor(pane, 0.0);
            rootPane.setRightAnchor(pane, 0.0);
            rootPane.getChildren().setAll(pane);
            ListExemplaireController controller = (ListExemplaireController) loader.getController();
            controller.afficher_liste(selectedForEdit.getId());
            
            controller.cat=comb1.getValue();
            controller.typ=comb2.getValue();
            controller.rech=recherche.getText();
            
            /*
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("قائمة النسخ");
            stage.setScene(new Scene(parent));
            stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.show();
            
            stage.getScene().getWindow().setOnCloseRequest((e)->{
                actualiser_liste_document(new ActionEvent());
            });*/
            
            
            
            pane.getScene().getWindow().setOnCloseRequest((e)->{
                actualiser_liste_document(new ActionEvent());
            });
        }catch(IOException ex){
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void version_precedente(String cat,String ty,String rec){
        comb1.setValue(cat);
        comb2.setValue(ty);
        recherche.setText(rec);
        Combo_categorie(new ActionEvent());
    }

    @FXML
    private void Ajouter_partie(ActionEvent event) {
        Document selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if(selectedForEdit == null){
            ClassAlert.errorAlert("لا يوجد كتاب محدد", "خطأ ");
            return;
        }
        else if(selectedForEdit.getIs_serie()){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/darhadit/addDocument/AddDocument.fxml"));
                Parent parent = loader.load();

                AddDocumentController controller = (AddDocumentController) loader.getController();
                controller.ajouter_partie_doc(selectedForEdit);

                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setTitle("إضافة جزء");
                stage.setScene(new Scene(parent));
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                stage.show();
                
                //Stage stage2 = (Stage) PaneRoot.getScene().getWindow();  
                //stage2.close();
                
                stage.setOnCloseRequest((e)->{
                    actualiser_liste_document(new ActionEvent());
                    //stage2.show();
            });
                
            }catch(IOException ex){
                Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void ajouter_exemplaire(ActionEvent event) {
        Document selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if(selectedForEdit == null){
            ClassAlert.errorAlert("لا يوجد كتاب محدد", "خطأ ");
            return;
        }
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/darhadit/addDocument/AddDocument.fxml"));
                Parent parent = loader.load();

                AddDocumentController controller = (AddDocumentController) loader.getController();
                controller.ajouter_exemplaire_doc(selectedForEdit);

                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setTitle("إضافة نسخة");
                stage.setScene(new Scene(parent));
                stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
                stage.show();
                
                //Stage stage2 = (Stage) PaneRoot.getScene().getWindow();  
                //stage2.close();
                
                stage.setOnCloseRequest((e)->{
                    actualiser_liste_document(new ActionEvent());
                    //stage2.show();
            });
                
            }catch(IOException ex){
                Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
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
         /* Scene scence = new Scene(parent);
          Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
          stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
            stage.setTitle("إدارة الاعضاء");
            stage.setScene(scence);
            stage.show();*/
          }catch(IOException ex){
              Logger.getLogger(ListDocumentController.class.getName()).log(Level.SEVERE, null, ex);
          }
          
      }

    public static class Document{

        private final SimpleIntegerProperty id;
        private final SimpleStringProperty titre;
        private final SimpleStringProperty auteur;
        private final SimpleStringProperty maison_edition;
        private final SimpleStringProperty categorie;
        private final SimpleStringProperty type;
        private final SimpleIntegerProperty nbr_exem;
        
        private final SimpleBooleanProperty is_serie;
        
        private final SimpleIntegerProperty partie;
        private final SimpleIntegerProperty nbr_piece;
        
        private final SimpleStringProperty sommaire;
        private final SimpleStringProperty mot_cle;
        
Document(int id,String titre,String auteur,String cat,int nbr_exemp,String type,String maison,int partie,int piece,Boolean is_serie,String sommaire,String mot_cle){
                this.id =  new SimpleIntegerProperty(id);
                this.titre = new SimpleStringProperty(titre);
                this.auteur = new SimpleStringProperty(auteur);
                this.maison_edition = new SimpleStringProperty(maison);
                this.categorie = new SimpleStringProperty(cat);
                this.type = new SimpleStringProperty(type);
                this.nbr_exem = new SimpleIntegerProperty(nbr_exemp);
                this.partie = new SimpleIntegerProperty(partie);
                this.nbr_piece = new SimpleIntegerProperty(piece);
                
                this.is_serie = new SimpleBooleanProperty(is_serie);
                this.sommaire = new SimpleStringProperty(sommaire);  
                this.mot_cle = new SimpleStringProperty(mot_cle); 
        }
        public Integer getId() {
            return id.get();
        }

        public String getTitre() {
            return titre.get();
        }

        public String getAuteur() {
            return auteur.get();
        }
        
        public String getMaison_edition() {
            return maison_edition.get();
        }

        public String getCategorie() {
            return categorie.get();
        }
        
        public String getType() {
            return type.get();
        }

        public Integer getNbr_exem() {
            return nbr_exem.get();
        }

        public Boolean getIs_serie(){
            return is_serie.get();
        }
        
        public Integer getPartie() {
            return partie.get();
        }

        public Integer getNbr_piece() {
            return nbr_piece.get();
        }
        
        public String getSommaire(){
            return sommaire.get();
        }
        
        public String getMot_cle() {
            return mot_cle.get();
        }
    }
}