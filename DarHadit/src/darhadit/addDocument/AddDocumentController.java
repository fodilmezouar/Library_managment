package darhadit.addDocument;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import darhadit.DataBase.DataBase;
import darhadit.alert.ClassAlert;
import darhadit.listDocument.ListDocumentController;
import darhadit.listDocument.listExemplaire.ListExemplaireController;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author ....
 */
public class AddDocumentController implements Initializable {

    DataBase base;
    private Boolean isModifierMode = false;
    private Boolean isModifierMode_exemplaire = false;
    @FXML
    private JFXButton back;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXTextField num;
    @FXML
    private JFXTextField titre;
    @FXML
    private JFXTextField auteur;
    @FXML
    private JFXTextField maison_ed;
    @FXML
    private JFXComboBox<String> comb1;
    @FXML
    private JFXRadioButton radio2;
    @FXML
    private JFXRadioButton radio1;
    @FXML
    private JFXTextField partie;
    @FXML
    private JFXTextArea sommaire;
    @FXML
    private JFXComboBox<String> comb2;
    
    private String mot_cle_interfae="";
    private String sommaire_intface="";
    //-----------------------------------Exemplaire---------------------------
    @FXML
    private JFXTextField num_doc;
    @FXML
    private JFXTextField num_exem;
    @FXML
    private JFXTextField emplacement;
    @FXML
    private JFXTextField source;
    @FXML
    private JFXTextField prix;
    @FXML
    private JFXComboBox<String> comb3;
    @FXML
    private JFXRadioButton radio3;
    @FXML
    private JFXRadioButton radio4;
    @FXML
    private Label titre_doc;
    @FXML
    private Label auteur_doc;
    @FXML
    private Label specialite_doc;
    @FXML
    private JFXTabPane PanGlobale;
    @FXML
    private Tab pan1;
    @FXML
    private Tab pan2;
    @FXML
    private JFXButton saveButton1;
    @FXML
    private JFXButton cancelButton1;
    @FXML
    private JFXRadioButton radio5;
    @FXML
    private JFXRadioButton radio6;
    @FXML
    private Label label_sommaire_mot_cle;
    @FXML
    private JFXTextField emplacement2;
    @FXML
    private Label image_aide;
    @FXML
    private AnchorPane rootPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        base = DataBase.getInstance(); 
        remplireID();
        //checkDate();
        fillComb1();
        fillCombo2();
        //PanGlobale.getSelectionModel().select(1);
        radio1.setSelected(true);
        partie.setDisable(true);
        radio2.setSelected(false);
        //----------exemplaire----------
        fillComb3();
        radio3.setSelected(true);
        radio4.setSelected(false);
        emplacement2.setEditable(false);
        //comb2.tooltipProperty().setValue(new Tooltip("أنقر علي اليمين لإضافة ,تغيير أو حذف تخصص"));
        Image image = new Image("/darhadit/images/aide1.png", 20, 20, true, true);
        image_aide.setGraphic(new ImageView(image));
        image_aide.tooltipProperty().setValue(new Tooltip("أنقر علي اليمين في تخصص لإضافة ,تغيير أو حذف تخصص"));
    }    

    @FXML
    private void addDocument(ActionEvent event) {
        String titre_Doc = titre.getText();
        String auteur_Doc  = auteur.getText();
        String maison_ed_Doc  = maison_ed.getText();
        String cate = comb2.getValue();
        String type = comb1.getValue();
        
        String qu=null;
        if(radio5.isSelected())
            sommaire_intface = sommaire.getText();
        else if(radio6.isSelected())
            mot_cle_interfae = sommaire.getText();
        qu = "insert into Document (Titre,auteur,maisonEdition,nomCategorie,types,sommaire,mot_cle,is_serie,partie) values("+
                    "'" + titre_Doc + "'," +    
                    "'" + auteur_Doc + "'," +
                    "'" + maison_ed_Doc + "'," +
                    "'" + cate + "'," +
                    "'" + type + "'," +
                    "'" + sommaire_intface + "'," +
                    "'" + mot_cle_interfae + "',";

        //cas de ajout non serie
        if(radio1.isSelected()){
            if(titre_Doc.trim().isEmpty()||auteur_Doc.trim().isEmpty()||cate.trim().isEmpty()||type.trim().isEmpty())
            {
                ClassAlert.errorAlert("أدخل جميع البيانات اللازمة", "خطأ");
                return;
            }
            
            if (isModifierMode){
    if(si_information_sont_modifier_non_serie(titre_Doc,auteur_Doc,maison_ed_Doc,cate,type,sommaire_intface,mot_cle_interfae,false)==0)            
    {
        
        return;
    }
                
                modifier_operation();
                return;
            }
            
            //si c1==0 alors le document n'existe pas sinon il existe deja 
            int c1=verifier_existe_Document_serie(titre_Doc,auteur_Doc,cate,type,null);
            if(c1!=0){
                ClassAlert.errorAlert(" هذا المستند موجود من قبل ذات   رقم     "+"      "+c1, "خطأ");
                return;
            }

            qu += "false," +
                    "null)";
            System.out.println(qu);
        }
        //cas ajoute d'un document serie
        else if(radio2.isSelected()){
            String partie_Doc  = partie.getText();
            
            if(titre_Doc.trim().isEmpty()||auteur_Doc.trim().isEmpty()||cate.trim().isEmpty()||type.trim().isEmpty()||partie_Doc.trim().isEmpty())
            {
                ClassAlert.errorAlert("أدخل جميع البيانات اللازمة", "خطأ");
                return;
            }
            
            //en cas de modifier les info
            if (isModifierMode){
    if(si_information_sont_modifier_serie(titre_Doc,auteur_Doc,maison_ed_Doc,cate,type,sommaire_intface,mot_cle_interfae,partie_Doc)==0)            
    {
        
        return;
    }
                
                
                modifier_operation();
                //non serie -> serie
                incerement_nbr_piece(titre_Doc,auteur_Doc,cate,type);
                return;
            }
            
            //si c2==0 alor le document existe déja sinon il n'existe pas
            int c2=verifier_existe_Document_serie(titre_Doc,auteur_Doc,cate,type,partie_Doc);
            if(c2!=0){
                ClassAlert.errorAlert("هذا المستند موجود من قبل ذات   رقم     "+c2, "خطأ");
                return;
            }

            qu += "true," +
                    Integer.parseInt(partie_Doc) + ")";
            
            System.out.println(qu);
        }

       if(base.execAction(qu)){
            ClassAlert.infoAlert("المستند قد تم اظافتة", "نجاح");
            remplireID();
            if(radio2.isSelected()){
                incerement_nbr_piece(titre_Doc,auteur_Doc,cate,type);
            }
            //vider_fenetre();
       }else{
            ClassAlert.errorAlert("خطأ في اظافتة المستند ", "خطأ");
       }
       vider_fenetre();
    }
    
    private int si_information_sont_modifier_non_serie(String titre,String auteur,String Med,String cat,String type,String som,String mot,Boolean serie){
        String qu = "select * from document where idDocument="+num.getText();
                
                ResultSet rs = base.execQuery(qu);
               // System.out.println("10");
                try {
                    if(rs.next()){
                        //System.out.println("20");
if(rs.getString("titre").equals(titre) && rs.getString("auteur").equals(auteur) &&
 rs.getString("maisonEdition").equals(Med) && rs.getString("nomCategorie").equals(cat) && rs.getString("types").equals(type) &&
 rs.getString("sommaire").equals(som) && rs.getString("mot_cle").equals(mot) && rs.getBoolean("is_serie") == serie
        ){
    //System.out.println("30");
    //rien n'a changer
    ClassAlert.warningAlert("المعلومات لم تتغير", "تحذير");
    return 0;
}   
                                }
                } catch (SQLException ex) {
                    Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
        String qu2 = "select distinct * from document where"
                + " titre='" + titre + "' AND "
                + " auteur='" + auteur + "' AND "
                //+ " maisonEdition='" + Med + "' AND "
                + " nomCategorie='" + cat + "' AND "
                + " types='" + type + "' AND"
                + " is_serie=false AND"
                //+ " sommaire='" + som + "' AND "
                //+ " mot_cle='" + mot + "' AND "
                + " idDocument !="+num.getText();
        ResultSet rs2 = base.execQuery(qu2);
        //System.out.println("333");
        try {
            while(rs2.next()){
                //System.out.println("222");
                ClassAlert.errorAlert("هذا المستند موجود من قبل ذات   رقم     "+rs2.getInt("idDocument"), "خطأ");
                return 0;
                        }
        } catch (SQLException ex) {
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        //il ya une modification sur les champs
        return 1;
    }
    
    private int si_information_sont_modifier_serie(String titre,String auteur,String Med,String cat,String type,String som,String mot,String parti){
        String qu = "select * from document where idDocument="+num.getText();
                
                ResultSet rs = base.execQuery(qu);
                //System.out.println("10");
                try {
                    if(rs.next()){
                        System.out.println("20");
if(rs.getString("titre").equals(titre) && rs.getString("auteur").equals(auteur) &&
 rs.getString("maisonEdition").equals(Med) && rs.getString("nomCategorie").equals(cat) && rs.getString("types").equals(type) &&
 rs.getString("sommaire").equals(som) && rs.getString("mot_cle").equals(mot) && rs.getInt("partie")== Integer.valueOf(parti)
        ){
    //System.out.println("30");
    ClassAlert.warningAlert("المعلومات لم تتغير", "تحذير");
    return 0;
}       
                                }
                } catch (SQLException ex) {
                    Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
        String qu2 = "select distinct * from document where"
                + " titre='" + titre + "' AND "
                + " auteur='" + auteur + "' AND "
                //+ " maisonEdition='" + Med + "' AND "
                + " nomCategorie='" + cat + "' AND "
                + " types='" + type + "' AND"
                + " partie=" + Integer.valueOf(parti) + " AND"
                //+ " sommaire='" + som + "' AND "
                //+ " mot_cle='" + mot + "' AND "
                + " idDocument !="+num.getText();
        ResultSet rs2 = base.execQuery(qu2);
        //System.out.println("333");
        try {
            while(rs2.next()){
                //System.out.println("222");
                ClassAlert.errorAlert("هذا المستند موجود من قبل ذات   رقم     "+rs2.getInt("idDocument"), "خطأ");
                return 0;
                        }
        } catch (SQLException ex) {
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }        
                
                
                
        //sont modifier
        return 1;
    }
    
    //verifier si un document existe deja ( serie ou non serie ) 
    private int verifier_existe_Document_serie(String titr,String aute,String cat,String ty,String part) {
        String qu = "select distinct * from document where"
                + " titre='" + titr + "' AND "
                + " auteur='" + aute + "' AND "
                + " nomCategorie='" + cat + "' AND "
                + " types='" + ty + "' AND";
        //non serie
        if(part == null){
            qu += " is_serie=false";
        }
        else{// cas serie
            qu += " is_serie=true AND"
                +" partie=" + Integer.parseInt(part);
        }
        ResultSet rs = base.execQuery(qu);
        try {
            while(rs.next()){ 
                return rs.getInt("idDocument");   
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    /*
    //lorsque on ajouter un document on incremente le nbr de piece de ce documnent
    private void incerement_nbr_piece(String titr,String aute,String cat,String ty) {
        String qu = "select distinct * from document where"
                + " titre='" + titr + "' AND "
                + " auteur='" + aute + "' AND "
                + " nomCategorie='" + cat + "' AND "
                + " types='" + ty + "'";
        
        ResultSet rs = base.execQuery(qu);
        int nbr = 0;
        try {
            if(rs.next()){
                nbr = rs.getInt("nbrPieces");
                        }
        } catch (SQLException ex) {
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        int nbr2 = nbr+1;
        String qu2 = "update document set nbrPieces = " + nbr2
                + " where titre='" + titr
                + "' AND auteur='" + aute
                + "' AND nomCategorie='" + cat
                + "' AND types='" + ty 
                + "' AND is_serie=true";
        base.execAction(qu2);
    }*/
    
    //lorsque on ajouter un document on incremente le nbr de piece de ce documnent
    private void incerement_nbr_piece(String titr,String aute,String cat,String ty) {
        String qu = "select count(idDocument) from document where"
                + " titre='" + titr + "' AND "
                + " auteur='" + aute + "' AND "
                + " nomCategorie='" + cat + "' AND "
                + " types='" + ty + "' AND "
                + " is_serie=true ";
                        
        ResultSet rs = base.execQuery(qu);
        int nbr=0;
        try {
            if(rs.next()){
                nbr = rs.getInt(1);
                        }
        } catch (SQLException ex) {
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //int nbr2 = nbr+1;
        String qu2 = "update document set nbrPieces = " + nbr
                + " where titre='" + titr
                + "' AND auteur='" + aute
                + "' AND nomCategorie='" + cat
                + "' AND types='" + ty 
                + "' AND is_serie=true";
        base.execAction(qu2);
    }

    @FXML
    private void cancelDocument(ActionEvent event) {
        //Stage stage = (Stage) rootPane.getScene().getWindow();  stage.close();
        vider_fenetre();
    }
    
    private void fillCombo2() {
        comb2.getItems().clear();
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
        comb2.setItems(list0);
        if(list0.isEmpty()){
            //comb2.promptTextProperty().setValue("azert");
        }
        else{
            comb2.setValue(comb2.getItems().get(0));
        }
    }
    
    private void fillComb1() {
        ObservableList<String>  list = FXCollections.observableArrayList();
        list.add("كتاب");
        list.add("مذكرة");
        list.add("مجلة");
        comb1.setItems(list);
        comb1.setValue(comb1.getItems().get(0));
    }

    @FXML
    private void radio_button2(ActionEvent event) {
        if(radio2.isSelected()){
            radio1.setSelected(false);
            partie.setDisable(false);            
        }
        else{
            radio1.setSelected(true);
            partie.setDisable(true);  
            partie.setText("");
        }
    }

    @FXML
    private void radio_button1(ActionEvent event) {
        if(radio1.isSelected()){
            radio2.setSelected(false);
            partie.setDisable(true); 
            partie.setText("");
        }
        else{
            radio2.setSelected(true);
            partie.setDisable(false);  
        }  
    }
    
    private void checkDate() {
        String qu = "select titre from document";
        ResultSet rs = base.execQuery(qu);
        try {
            while(rs.next()){
                String titrex = rs.getString("titre");
                System.out.println(titrex);
                        }
        } catch (SQLException ex) {
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void remplireID() {
        String qu = "select count(*) from document";
        ResultSet rs = base.execQuery(qu);
        int nbr=1;
        try {
            if(rs.next()){
                nbr=rs.getInt(1);
            }
            if(nbr<10-1){
                num.setText("00000"+(nbr+1));}
            else if(nbr<100-1){
                num.setText("0000"+(nbr+1));}
            else if(nbr<1000-1){
                num.setText("000"+(nbr+1));}
            else if(nbr<10000-1){
                num.setText("00"+(nbr+1));}
            else if(nbr<100000-1){
                num.setText("0"+(nbr+1));}
            else{
                num.setText(nbr+1+"");}
            //num.setDisable(true);
        } catch (SQLException ex) {
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void vider_fenetre() {
        titre.setText("");
        auteur.setText("");
        partie.setText("");
        sommaire.setText("");
        maison_ed.setText("");
        radio1.setSelected(true);
        radio2.setSelected(false);
        partie.setDisable(true);
        comb2.setValue(comb2.getItems().get(0));
        comb1.setValue(comb1.getItems().get(0));
        mot_cle_interfae = "";
        sommaire_intface = "";
        radio5.setSelected(true);
        radio6.setSelected(false);
    }

    @FXML
    private void plusCategorie(ActionEvent event) {
        String valeurRetour =
        ClassAlert.inputDialog("", "أضف تخصص", "تخصص","أدخل تخصصك الجديد:                           ");
        if(valeurRetour!=null && !(valeurRetour.trim().equals(""))){
            String qu = "insert into Categorie values("
                    + "'" + valeurRetour + "'"
                    +")";
            if(base.execAction(qu)){
                ClassAlert.infoAlert("التخصص قد تم اظافتة", "نجاح");        
                fillCombo2();
           }else{
                ClassAlert.errorAlert("خطأ في اظافتة التخصص ", "خطأ");
           }
        }
    }

    @FXML
    private void modiferCateButton(ActionEvent event) {
        String nom = comb2.getValue();
        if(nom.isEmpty()){
            ClassAlert.errorAlert("اختر التخصص ", "خطأ");
        }
        else{
        String valeurRetour=ClassAlert.inputDialog("", " تغير تخصص", nom,"تعديل تخصص:                           ");
            if(valeurRetour!=null && !(valeurRetour.trim().equals(""))){
                String qu = "update categorie set nomcategorie='"+valeurRetour+"' where nomcategorie='"+nom+"'";
                if(base.execAction(qu)){
                    ClassAlert.infoAlert("التخصص قد تم تغيره ", "نجاح");         
                    fillCombo2();
                }else{
                    ClassAlert.errorAlert("خطأ في تغيره التخصص", "خطأ");
                }
            }
        }
    }

    @FXML
    private void moincategorie(ActionEvent event) {
        String nom = comb2.getValue();
        if(nom == null){
            ClassAlert.errorAlert("اختر التخصص ", "خطأ");
        }
        else{
            String qu = "delete from categorie where nomCategorie='" +nom+ "'"; 
            if(base.execAction(qu)){
                ClassAlert.infoAlert("التخصص قد تم حذفه", "نجاح");          
                fillCombo2();
            }else{
                ClassAlert.errorAlert("  فشل لا يمكن حذف التخصص", "فشل");
            }
        }
    } 
    //--------------------------------Exempalire--------------------------
    @FXML
    private void remlire_info(ActionEvent event) {
        String numD = num_doc.getText();
        if(!numD.isEmpty()){
            int d = Integer.parseInt(numD);
            String qu = "select titre,auteur,nomcategorie,types,is_serie from document where iddocument=" + d;
            ResultSet rs = base.execQuery(qu);
            try {
                //si le document existe
                if(rs.next()){     
                    titre_doc.setText(rs.getString("titre"));
                    auteur_doc.setText(rs.getString("auteur"));
                    specialite_doc.setText(rs.getString("nomcategorie"));
                    if(d<10-1){
                        num_exem.setText(remplireIDExemplaire()+" - 00000"+d);}
                    else if(d<100-1){
                        num_exem.setText(remplireIDExemplaire()+" - 0000"+d);}
                    else if(d<1000-1){
                        num_exem.setText(remplireIDExemplaire()+" - 000"+d);}
                    else if(d<10000-1){
                        num_exem.setText(remplireIDExemplaire()+" - 00"+d);}
                    else if(d<100000-1){
                        num_exem.setText(remplireIDExemplaire()+" - 0"+d);}
                    else{
                        num_exem.setText(remplireIDExemplaire()+" - "+d);}
                    //remlire le champ de num_categorie pour un exemplaire
                    emplacement2.setText(""+remplireNumCategorieExemplaire(d,rs.getString("nomcategorie"),rs.getString("types"),rs.getBoolean("is_serie"),rs.getString("titre"),rs.getString("auteur")));
                    //num_exem.setDisable(true);
                }
                else{
                    titre_doc.setText("عنوان الوثيقة");
                    auteur_doc.setText("مؤلف");
                    specialite_doc.setText("تخصص");     
                    num_exem.setText("");
                    //num_exem.setDisable(true);
                    emplacement2.setText("");
                }
            } catch (SQLException ex) {
                Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }  
        }
        else
            vider_fenetre_exemplaire();
    }
    
    private int remplireIDExemplaire() {
        String numD = num_doc.getText();
        int d = Integer.parseInt(numD);       
        String qu = "select idexemplaire from exemplaire where iddocument="+d;
        ResultSet rs = base.execQuery(qu);
        int nbr=1;
        try {
            while(rs.next()){
                if(rs.getInt("idexemplaire")>nbr){
                    return nbr;
                }
                nbr++;
            }
            return nbr;
        } catch (SQLException ex) {
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nbr;
    }
    
    private int remplireNumCategorieExemplaire(int numD,String cat,String ty,boolean serie,String titre,String auteur) {
        String qu = "select nbrExemplaire from document where idDocument="+numD;
        ResultSet rs = base.execQuery(qu);
        try {
            while(rs.next()){
                if(rs.getInt("nbrExemplaire")==0){
                    //une serie a le meme numcategorie 
                    if(serie){   
    String qu1="select distinct max(nbrExemplaire),idDocument from document where titre='"+titre+"' AND auteur='"+auteur+"' AND nomCategorie='"+cat+"' AND types='"+ty+"'"; 
                        ResultSet rs1 = base.execQuery(qu1);
                        try {
                            while(rs1.next()){
                                if(rs1.getInt(1)!=0){
                                    String qu11= "select num_categorie from exemplaire where idDocument="+rs1.getInt("idDocument");
                                    ResultSet rs11 = base.execQuery(qu11);
                                    try {
                                        while(rs11.next()){
                                            return rs11.getInt(1);
                                        }
                                    }catch (SQLException ex11) {
                                        Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex11);
                                    } 
                                }
                            }
                        }catch (SQLException ex1) {
                        Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                    }
                    
                    String qu2 = "select distinct max(num_categorie) from Exemplaire E,Document D where"
                           + " D.idDocument=E.idDocument AND nomCategorie= '" + cat + "' "
                           + " AND types= '" + ty + "'";
                    ResultSet rs2 = base.execQuery(qu2);
                    try {
                        if(rs2.next()){
                            return rs2.getInt(1)+1;
                        }
                        return 1;
                    }catch (SQLException ex2) {
                        Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex2);
                    }
                }
                else{
                    String qu3 = "select distinct num_categorie from exemplaire where idDocument="+numD;
                    ResultSet rs3 = base.execQuery(qu3);
                    try {
                        if(rs3.next()){
                            return rs3.getInt(1);
                        }
                    }catch (SQLException ex3) {
                        Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex3);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @FXML
    private void radio_button3(ActionEvent event) {
        if(radio3.isSelected())
            radio4.setSelected(false);       
        else
            radio4.setSelected(true);
    }

    @FXML
    private void radio_button4(ActionEvent event) {
        if(radio4.isSelected())
            radio3.setSelected(false);         
        else
            radio3.setSelected(true);      
    }

    @FXML
    private void add_exemplaire(ActionEvent event) {
        String numD = num_doc.getText(); 
        String emp = emplacement.getText();
        String sourceEx = source.getText();
        String prixEx = prix.getText();
        String origine = comb3.getValue();
        String emp2 = emplacement2.getText();
        if(numD.isEmpty()||emp.trim().isEmpty())
        {
            ClassAlert.errorAlert("أدخل جميع البيانات اللازمة", "خطأ");
            return;
        }
        int d = Integer.parseInt(numD);
        int e = remplireIDExemplaire();
        int emp3 = Integer.parseInt(emp2);
        if(isModifierMode_exemplaire==true){
            if(radio3.isSelected())
                modifier_operation_exemplaire(d,emp,emp3,sourceEx,Double.parseDouble(prixEx),origine,true);
            else if(radio4.isSelected())
                modifier_operation_exemplaire(d,emp,emp3,sourceEx,Double.parseDouble(prixEx),origine,false);
            return;
        }
        String qu = "insert into exemplaire (idExemplaire,idDocument,emplacement,num_categorie,sourceExemplaire,dateEnregistrement,version,empruntable,prix) values("+
                    e + "," +    
                    d + "," +
                    "'" + emp + "'," +
                    emp3 + "," +
                    "'" + sourceEx + "'," +
                    "NOW()," +
                    "'" + origine + "'";
        System.out.println(qu);
        if(radio3.isSelected()){
            if(!prixEx.trim().equals("")){
                    //qu += ",true," + Integer.parseInt(prixEx) + ")";
                    qu += ",true," + Float.parseFloat(prixEx) + ")";
            }else{
                qu += ",true," + "null)";
            }
        }
        else if(radio4.isSelected()){
          if(!prixEx.trim().equals("")){
                qu += ",false," + Float.parseFloat(prixEx) + ")";           
          }else{
                qu += ",false," + "null)";             
          }
        }
        System.out.println(qu);
        if(base.execAction(qu)){
           ClassAlert.infoAlert("النسخة قد تم إضافتها", "نجاح");
            vider_fenetre_exemplaire(); 
            incerement_nbr_exemplaire(d);
       }else{
            ClassAlert.errorAlert("خطأ في إضافت النسخة ", "خطأ");
       }    
    }

    @FXML
    private void cancelExemplaire(ActionEvent event) {
        //Stage stage = (Stage) rootPane.getScene().getWindow(); stage.close();
        vider_fenetre_exemplaire();   
    }
    
    private void fillComb3() {
        ObservableList<String>  list = FXCollections.observableArrayList();
        list.add("أصلية");
        list.add("طبق الاصل");
        list.add("CD");
        comb3.setItems(list);
        comb3.setValue(comb3.getItems().get(0));
    }
    
    private void vider_fenetre_exemplaire() {
        num_doc.setText("");
        num_exem.setText("");
        emplacement.setText("");
        emplacement2.setText("");
        source.setText("");
        prix.setText("");
        titre_doc.setText("عنوان الوثيقة");
        auteur_doc.setText("مؤلف");
        specialite_doc.setText("تخصص");
        radio3.setSelected(true);
        radio4.setSelected(false);
        comb3.setValue(comb3.getItems().get(0));
    }
    
    //lorsque on ajouter un exemplaire on incremente le nbr d'exemplaire du documnent
    private void incerement_nbr_exemplaire(int numDocument)
    {
        String qu = "select nbrExemplaire from document WHERE idDocument=" + numDocument;
        ResultSet rs = base.execQuery(qu);
        int nbr = 0;
        try {
            if(rs.next()){
                nbr = rs.getInt("nbrExemplaire");
                        }
        } catch (SQLException ex) {
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        int nbr2 = nbr+1;
        String qu2 = "update document set nbrExemplaire = "
                + nbr2
                + " where idDocument ="
                + "" + numDocument + "";
        base.execAction(qu2);
    }
    
    public void modifierDocument(ListDocumentController.Document Document){
        int identifiant_doc = Document.getId();
        String a = String.valueOf(identifiant_doc);    
        num.setText(a);
        titre.setText(Document.getTitre());
        auteur.setText(Document.getAuteur());
        maison_ed.setText(Document.getMaison_edition());
        comb2.setValue(Document.getCategorie());
        comb1.setValue(Document.getType());
        back.setVisible(false);
        mot_cle_interfae = Document.getMot_cle();
        sommaire_intface = Document.getSommaire();
        
        sommaire.setText(sommaire_intface);
        if(Document.getIs_serie()){
            radio2.setSelected(true);
            radio1.setSelected(false);
            String b = String.valueOf(Document.getPartie());
            partie.setText(b);
            partie.setDisable(false);            
        }
        else{
            radio2.setSelected(false);
            radio1.setSelected(true);
        }
        isModifierMode=true;
        
        pan2.setDisable(true);
        saveButton1.setText("تعديل");
        pan1.setText("تعديل الوثيقة");
        pan2.setText("");
    }
    
    private void modifier_operation(){
            int inum = Integer.parseInt(num.getText());
            String titrex = titre.getText();
            String auteurx = auteur.getText();
            String maison_edx = maison_ed.getText();
            String categx = comb2.getValue();
            String typex = comb1.getValue();
            String qu2 = null;
            if(radio1.isSelected()){
                qu2 = "is_serie=false,partie=null,nbrPieces=null,"; 
            }
            else if(radio2.isSelected()){
                qu2 = "is_serie=true,"; 
            }
            String qu = "update document set "
                    + "Titre = '" + titrex + "', "
                    + "auteur = '" + auteurx + "', "
                    + "maisonEdition = '" + maison_edx + "', "
                    + "nomCategorie = '" + categx + "', "
                    + "types = '" + typex + "', "
                    + qu2
                    + "sommaire = '" + sommaire_intface + "', "
                    + "mot_cle = '" + mot_cle_interfae + "' ";
             try{
                 int partiex = Integer.parseInt(partie.getText());
                 qu += ",partie = " + partiex + " ";
             }catch(Exception ex){

             }
            qu += "where idDocument="+inum;
            //on cas de modifier serie -> non serie  --- decrement le nbr de piece
            String qu3="select titre,auteur,nomCategorie,types,nbrpieces from document where idDocument="+inum;
            ResultSet rs = base.execQuery(qu3);
            if(base.execAction(qu))
            {
                ClassAlert.infoAlert("المعلومات المستند تم تغيرها", "نجاح");
                //on cas de modifier serie -> non serie  --- decrement le nbr de piece
                try {
                    if(rs.next()){
                        if(rs.getInt("nbrpieces")!=0){
                            int nbr2 = rs.getInt("nbrpieces") - 1;
                            String qu4 = "update document set nbrPieces = " + nbr2
                                    + " where titre='" + rs.getString("titre")
                                    + "' AND auteur='" + rs.getString("auteur")
                                    + "' AND nomCategorie='" + rs.getString("nomCategorie")
                                    + "' AND types='" + rs.getString("types") 
                                    + "' AND is_serie=true";
                            System.out.println("qu4"+qu4);
                            base.execAction(qu4);
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                ClassAlert.errorAlert("خطأ في تغير المعلومات", "خطأ");
            }
    }
    
    public void informationDocument(ListDocumentController.Document Document){
        modifierDocument(Document);
        //System.out.println("adffe");
        num.setEditable(false);
        titre.setEditable(false);
        auteur.setEditable(false);
        maison_ed.setEditable(false);
        comb2.setDisable(true);
        comb1.setDisable(true);
        radio1.setDisable(true);
        radio2.setDisable(true);
        back.setVisible(false);
        partie.setEditable(false);
        sommaire.setEditable(false);
        saveButton1.setVisible(false);
        cancelButton1.setVisible(false);
        pan1.setText("معلومات الوثيقة");
        pan2.setText("");
    }
    
    public void ajouter_partie_doc(ListDocumentController.Document Document){
        titre.setText(Document.getTitre());
        auteur.setText(Document.getAuteur());
        //maison_ed.setText(Document.getMaison_edition());
        comb2.setValue(Document.getCategorie());
        comb1.setValue(Document.getType());
        back.setVisible(false);
        radio1.setSelected(false);
        radio2.setSelected(true);
        partie.setDisable(false);  
    }
    
    public void ajouter_exemplaire_doc(ListDocumentController.Document Document){
        PanGlobale.getSelectionModel().select(1);
        num_doc.setText(""+Document.getId());
        remlire_info(new ActionEvent());
        pan1.setDisable(true);
        back.setVisible(false);
    }

    //le champ numDoc de exemplire n'accpete que des nombres entier
    @FXML
    private void remlire_info_v2(KeyEvent event) {
        num_doc.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                char c=num_doc.getText().charAt(num_doc.getText().length()-1);
                if (c!='0' && c!='1' && c!='2' && c!='3' && c!='4' && c!='5' && c!='6' && c!='7' && c!='8' && c!='9')
                num_doc.setText(num_doc.getText().substring(0,num_doc.getText().length()-1)); 
            }catch(Exception ex){
                
            }
            remlire_info(new ActionEvent());
        });
    }

    @FXML
    private void radio_button5(ActionEvent event) {
        if(radio5.isSelected()){
            mot_cle_interfae = sommaire.getText();
            radio6.setSelected(false);
            sommaire.setText(sommaire_intface);
            label_sommaire_mot_cle.setText("الفهرسة");
        }
        else{
            sommaire_intface = sommaire.getText();
            radio6.setSelected(true);
            sommaire.setText(mot_cle_interfae);
            label_sommaire_mot_cle.setText("الكلمات مفتاحية");
        }
    }

    @FXML
    private void radio_button6(ActionEvent event) {
        if(radio6.isSelected()){
            sommaire_intface = sommaire.getText();
            radio5.setSelected(false);
            sommaire.setText(mot_cle_interfae);
            label_sommaire_mot_cle.setText("الكلمات مفتاحية");
        }
        else{
            mot_cle_interfae = sommaire.getText();
            radio5.setSelected(true);
            sommaire.setText(sommaire_intface);
            label_sommaire_mot_cle.setText("الفهرسة");
        }  
    }
    
    public void modifierExemplaire(ListExemplaireController.Exemplaire Exemplaire){
        num_doc.setText(Exemplaire.getId_doc()+"");
        num_doc.setEditable(false);
        back.setVisible(false);
        int d = Exemplaire.getId_doc();
        if(d<10-1){
            num_exem.setText(Exemplaire.getId_exemp()+" - 00000"+d);}
        else if(d<100-1){
            num_exem.setText(Exemplaire.getId_exemp()+" - 0000"+d);}
        else if(d<1000-1){
            num_exem.setText(Exemplaire.getId_exemp()+" - 000"+d);}
        else if(d<10000-1){
            num_exem.setText(Exemplaire.getId_exemp()+" - 00"+d);}
        else if(d<100000-1){
            num_exem.setText(Exemplaire.getId_exemp()+" - 0"+d);}
        else{
            num_exem.setText(Exemplaire.getId_exemp()+" - "+d);}
        
        emplacement.setText(Exemplaire.getEmplacement());
        emplacement2.setText(Exemplaire.getNum_cat()+"");
        source.setText(Exemplaire.getSource());
        prix.setText(Exemplaire.getPrix()+"");
        comb3.setValue(Exemplaire.getVersion());
        
        if(Exemplaire.getEmpruntable().equals("نعم")){
            radio3.setSelected(true);
            radio4.setSelected(false);    
        }
        else{
            radio3.setSelected(false);
            radio4.setSelected(true);
        }
        String qu = "select titre,auteur,nomcategorie from document where iddocument=" + d;
        ResultSet rs = base.execQuery(qu);
        try {
            if(rs.next()){     
                titre_doc.setText(rs.getString("titre"));
                auteur_doc.setText(rs.getString("auteur"));
                specialite_doc.setText(rs.getString("nomcategorie"));
            }
            } catch (SQLException ex) {
            Logger.getLogger(AddDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }  
        isModifierMode_exemplaire=true;
        
        pan1.setDisable(true);
        PanGlobale.getSelectionModel().select(1);
        saveButton.setText("تعديل");
        pan2.setText("تعديل نسخة");
    }
    
    private void modifier_operation_exemplaire(int d,String emp,int emp2,String source,Double prix,String origine,Boolean empruntable){
        String id_exe = num_exem.getText();
        boolean b = true;
        String e = "";
        for(int i = 0; i < id_exe.length(); i++){
            if(id_exe.charAt(i) == ' ' && id_exe.charAt(i+1) == '-'){
                b = false;
                i++;
            }
            if(b){
                e += id_exe.charAt(i);
            }
        }
        int id_e = Integer.parseInt(e);
        String qu = "update exemplaire set "
                    + "emplacement = '" + emp + "', "
                    + "num_categorie = " + emp2 + ", "
                    + "empruntable = " + empruntable + ", "
                    + "sourceExemplaire = '" + source + "', "
                    + "prix = '" + prix + "', "
                    + "version = '" + origine + "' "
                    + "where idDocument=" +d+ " AND idExemplaire="+id_e;
            System.out.println(qu);
            ResultSet rs = base.execQuery(qu);
            if(base.execAction(qu))
            {
                //Stage stage2 = (Stage) rootPane.getScene().getWindow();  
                //stage2.close();
                ClassAlert.infoAlert("المعلومات المستند تم تغيرها", "نجاح");
            }
            else{
                ClassAlert.errorAlert("خطأ في تغير المعلومات", "خطأ");
            }
    }
    
    public void ajouter_Exemplaire_exemplaire(ListExemplaireController.Exemplaire Exemplaire){
        PanGlobale.getSelectionModel().select(1);
        back.setVisible(false);
        num_doc.setText(""+Exemplaire.getId_doc());
        remlire_info(new ActionEvent());
        pan1.setDisable(true);
    }

    @FXML
    private void just_number(KeyEvent event) {
        partie.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                char c=partie.getText().charAt(partie.getText().length()-1);
                if (c!='0' && c!='1' && c!='2' && c!='3' && c!='4' && c!='5' && c!='6' && c!='7' && c!='8' && c!='9')
                partie.setText(partie.getText().substring(0,partie.getText().length()-1)); 
            }catch(Exception ex){
                
            }
           
        });
        
        partie.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length()>3){
                    partie.setText(newValue.substring(0, 3));
                }
            }
        });
    }

    @FXML
    private void just_number_prix(KeyEvent event) {
        prix.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                char c=prix.getText().charAt(prix.getText().length()-1);
                if (c!='0' && c!='1' && c!='2' && c!='3' && c!='4' && c!='5' && c!='6' && c!='7' && c!='8' && c!='9' && c!='.')
                prix.setText(prix.getText().substring(0,prix.getText().length()-1)); 
            }catch(Exception ex){
                
            }
           
        });
        
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
}