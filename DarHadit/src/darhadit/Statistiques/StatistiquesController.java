package darhadit.Statistiques;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import darhadit.DataBase.DataBase;
import darhadit.GestionMember.FXMLgestionMemberController;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StatistiquesController implements Initializable {

    DataBase base;
    
    int emprunts[] = new int[12];
    int retours[] = new int[12];
    int nbrEmprunts = 0;
    int nbrRetours = 0;
    int nbrRetards = 0;
    
    int adherents[] = new int[12];
    int nbrAdherent = 0;
    int nbrHomme = 0;
    int nbrFemme = 0;
    int nbrAdherentExpirer = 0;
    double avgAge = 0;
    
    int emprunt00_14 = 0;
    int emprunt15_17 = 0;
    int emprunt18_24 = 0;
    int emprunt24_99 = 0;
    
    int rendre00_14 = 0;
    int rendre15_17 = 0;
    int rendre18_24 = 0;
    int rendre24_99 = 0;
    
    int nombre00_14 = 0;
    int nombre15_17 = 0;
    int nombre18_24 = 0;
    int nombre24_99 = 0;
    
    int empruntable = 0;
    int nonEmpruntable = 0;
    
    //EMPRUNT/RENDRE
    @FXML
    private AnchorPane rootPane;
    @FXML
    private BarChart<?, ?> barChartEmpruntRetour;
    @FXML
    private NumberAxis numberAxis;
    @FXML
    private CategoryAxis categoryAxisMois;
    @FXML
    private RadioButton radioEmpruntTous;
    @FXML
    private RadioButton radioEmpruntAnnee;
    @FXML
    private RadioButton radioEmpruntMois;
    @FXML
    private RadioButton radioEmpruntJour;
    @FXML
    private Label nombreEmprunts;
    @FXML
    private Label nombreRetours;
    @FXML
    private Label nombreRetards;
    @FXML
    private JFXDatePicker empruntDatePicker;
    //ADHERENT
    @FXML
    private JFXDatePicker adherentDatePicker;
    @FXML
    private RadioButton radioAdherentTous;
    @FXML
    private RadioButton radioAdherentAnnee;
    @FXML
    private RadioButton radioAdherentMois;
    @FXML
    private RadioButton radioAdherentJour;
    @FXML
    private BarChart<?, ?> barChartAdherent;
    @FXML
    private NumberAxis adherentNumberAxis;
    @FXML
    private CategoryAxis adherentCategoryAxisMois;
    @FXML
    private Label nombreHomme;
    @FXML
    private Label nombreFemme;
    @FXML
    private Label nombreAdherent;
    @FXML
    private Label nombreAdherentExpirer;
    @FXML
    private Label moyenneAge;
    @FXML
    private PieChart pieChartAdherentEmprunt;
    @FXML
    private PieChart pieChartAdherentRendre;
    @FXML
    private PieChart pieChartAdherentNombreAge;
    //Document
    @FXML
    private JFXComboBox<String> categorieCombo;
    @FXML
    private Label nombreDocument;
    @FXML
    private Label nombreLivre;
    @FXML
    private Label nombreMagazine;
    @FXML
    private Label nombreMemoire;
    @FXML
    private Label nombreExemplaire;
    @FXML
    private Label nombreLivreExemplaire;
    @FXML
    private Label nombreMagazineExemplaire;
    @FXML
    private Label nombreMemoireExemplaire;
    @FXML
    private PieChart pieChartDocumentEmpruntable;
    @FXML
    private PieChart pieChartTypeDucument;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        base = DataBase.getInstance();
        radioEmpruntTous.setSelected(true);
        radioAdherentTous.setSelected(true);
        empruntDatePicker.setValue(LocalDate.now());
        adherentDatePicker.setValue(LocalDate.now());
        remplirEmpruntsRetours();
        remplirAdherent();
        fillCategorieCombo();
        remplirDocument();
    }
    //*****EMPRUNT/RENDRE*****
    private void remplirEmpruntsRetours(){
        if(radioEmpruntTous.isSelected()){//Tous
            for(int i = 0; i < 12 ;i++){
                String qu1 = "SELECT COUNT(*) AS total "
                        + "FROM Emprunt "
                        + "WHERE MONTH(dateEmprunt) = " + (i+1);
                
                String qu2 = "SELECT COUNT(*) AS total "
                        + "FROM Rendre "
                        + "WHERE MONTH(dateEmprunt) = " + (i+1);
                
                String qu3 = "SELECT COUNT(*) AS total "
                            + "FROM Rendre "
                            + "WHERE MONTH(dateRetour) = " + (i+1);
                
                ResultSet rs1 = base.execQuery(qu1);
                ResultSet rs2 = base.execQuery(qu2);
                ResultSet rs3 = base.execQuery(qu3);
                
                try {
                    if(rs1.next() && rs2.next() && rs3.next()){
                        emprunts[i] = rs1.getInt("total") + rs2.getInt("total");
                        retours[i] = rs3.getInt("total");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String qu1 = "SELECT COUNT(*) AS total FROM Emprunt";
            String qu2 = "SELECT COUNT(*) AS total FROM Rendre";
            String qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Rendre "
                    + "WHERE DATEDIFF(dateRetour, dateEmprunt) > 15 ";
            ResultSet rs1 = base.execQuery(qu1);
            ResultSet rs2 = base.execQuery(qu2);
            ResultSet rs3 = base.execQuery(qu3);
            try {
                if(rs1.next() && rs2.next() && rs3.next()){
                    nbrRetours = rs2.getInt("total");
                    nbrEmprunts = rs1.getInt("total") + nbrRetours;
                    nbrRetards = rs3.getInt("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(radioEmpruntAnnee.isSelected()){//Annee
            LocalDate date = empruntDatePicker.getValue();
            barChartEmpruntRetour.setTitle("عدد اللإعارة و التقديم لكل شهر في سنة  " + date.getYear());
            for(int i = 0; i < 12 ;i++){
                String qu1 = "SELECT COUNT(*) AS total "
                        + "FROM Emprunt "
                        + "WHERE MONTH(dateEmprunt) = " + (i+1) + " AND "
                        + "YEAR(dateEmprunt) = YEAR('" + date + "')";
                
                String qu2 = "SELECT COUNT(*) AS total "
                        + "FROM Rendre "
                        + "WHERE MONTH(dateEmprunt) = " + (i+1) + " AND "
                        + "YEAR(dateEmprunt) = YEAR('" + date + "')";
                
                String qu3 = "SELECT COUNT(*) AS total "
                            + "FROM Rendre "
                            + "WHERE MONTH(dateRetour) = " + (i+1) + " AND "
                        + "YEAR(dateRetour) = YEAR('" + date + "')";
                
                ResultSet rs1 = base.execQuery(qu1);
                ResultSet rs2 = base.execQuery(qu2);
                ResultSet rs3 = base.execQuery(qu3);
                
                try {
                    if(rs1.next() && rs2.next() && rs3.next()){
                        emprunts[i] = rs1.getInt("total") + rs2.getInt("total");
                        retours[i] = rs3.getInt("total");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Emprunt "
                    + "WHERE YEAR(dateEmprunt) = YEAR('" + date + "')";
            String qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Rendre "
                    + "WHERE YEAR(dateEmprunt) = YEAR('" + date + "')";
            String qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Rendre "
                    + "WHERE DATEDIFF(dateRetour, dateEmprunt) > 15 AND "
                    + "YEAR(dateRetour) = YEAR('" + date + "')";
            System.out.println(date);
            ResultSet rs1 = base.execQuery(qu1);
            ResultSet rs2 = base.execQuery(qu2);
            ResultSet rs3 = base.execQuery(qu3);
            try {
                if(rs1.next() && rs2.next() && rs3.next()){
                    nbrRetours = rs2.getInt("total");
                    nbrEmprunts = rs1.getInt("total") + nbrRetours;
                    nbrRetards = rs3.getInt("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(radioEmpruntMois.isSelected()){//Mois
            LocalDate date = empruntDatePicker.getValue();
            String qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Emprunt "
                    + "WHERE YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "')";
            String qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Rendre "
                    + "WHERE YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "')";
            String qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Rendre "
                    + "WHERE DATEDIFF(dateRetour, dateEmprunt) > 15 AND "
                    + "YEAR(dateRetour) = YEAR('" + date + "') AND "
                    + "MONTH(dateRetour) = MONTH('" +date + "')";
            ResultSet rs1 = base.execQuery(qu1);
            ResultSet rs2 = base.execQuery(qu2);
            ResultSet rs3 = base.execQuery(qu3);
            try {
                if(rs1.next() && rs2.next() && rs3.next()){
                    nbrRetours = rs2.getInt("total");
                    nbrEmprunts = rs1.getInt("total") + nbrRetours;
                    nbrRetards = rs3.getInt("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(radioEmpruntJour.isSelected()){//Jour
            LocalDate date = empruntDatePicker.getValue();
            String qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Emprunt "
                    + "WHERE YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "') AND "
                    + "DAY(dateEmprunt) = DAY('" + date + "')";
            String qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Rendre "
                    + "WHERE YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "') AND "
                    + "DAY(dateEmprunt) = DAY('" + date + "')";
            String qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Rendre "
                    + "WHERE DATEDIFF(dateRetour, dateEmprunt) > 15 AND "
                    + "YEAR(dateRetour) = YEAR('" + date + "') AND "
                    + "MONTH(dateRetour) = MONTH('" +date + "') AND "
                    + "DAY(dateRetour) = DAY('" + date + "')";
            System.out.println(date);
            ResultSet rs1 = base.execQuery(qu1);
            ResultSet rs2 = base.execQuery(qu2);
            ResultSet rs3 = base.execQuery(qu3);
            try {
                if(rs1.next() && rs2.next() && rs3.next()){
                    nbrRetours = rs2.getInt("total");
                    nbrEmprunts = rs1.getInt("total") + nbrRetours;
                    nbrRetards = rs3.getInt("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Diagramme nombre d'emprunts et retours par mois
        if(radioEmpruntTous.isSelected() || radioEmpruntAnnee.isSelected()){
            categoryAxisMois.setLabel("اشهر السنة");
            numberAxis.setLabel("العدد");

            ObservableList<XYChart.Data> emprunts = 
                    FXCollections.observableArrayList(
                            new XYChart.Data("يناير", this.emprunts[0]),
                            new XYChart.Data("فبراير", this.emprunts[1]),
                            new XYChart.Data("مارس", this.emprunts[2]),
                            new XYChart.Data("أبريل", this.emprunts[3]),
                            new XYChart.Data("مايو", this.emprunts[4]),
                            new XYChart.Data("يونيو", this.emprunts[5]),
                            new XYChart.Data("يوليو", this.emprunts[6]),
                            new XYChart.Data("أغسطس", this.emprunts[7]),
                            new XYChart.Data("سبتمبر", this.emprunts[8]),
                            new XYChart.Data("أكتوبر", this.emprunts[9]),
                            new XYChart.Data("نوفمبر", this.emprunts[10]),
                            new XYChart.Data("ديسمبر", this.emprunts[11])
                    );
            XYChart.Series seriesEmprunts = new XYChart.Series("عدد القروض",emprunts);

            ObservableList<XYChart.Data> retours = 
                    FXCollections.observableArrayList(
                            new XYChart.Data("يناير", this.retours[0]),
                            new XYChart.Data("فبراير", this.retours[1]),
                            new XYChart.Data("مارس", this.retours[2]),
                            new XYChart.Data("أبريل", this.retours[3]),
                            new XYChart.Data("مايو", this.retours[4]),
                            new XYChart.Data("يونيو", this.retours[5]),
                            new XYChart.Data("يوليو", this.retours[6]),
                            new XYChart.Data("أغسطس", this.retours[7]),
                            new XYChart.Data("سبتمبر", this.retours[8]),
                            new XYChart.Data("أكتوبر", this.retours[9]),
                            new XYChart.Data("نوفمبر", this.retours[10]),
                            new XYChart.Data("ديسمبر", this.retours[11])
                    );
            XYChart.Series seriesRetours = new XYChart.Series("عدد التقديمات",retours);
            barChartEmpruntRetour.getData().clear();
            barChartEmpruntRetour.getData().addAll(seriesEmprunts,seriesRetours);
        }
        //Nombre des emprunts, retours et retards en fonction de (tous, Annee, Mois, Jour)
        nombreEmprunts.setText(nbrEmprunts +"");
        nombreRetours.setText(nbrRetours +"");
        nombreRetards.setText(nbrRetards +"");
    }

    @FXML
    private void actionRadioEmpruntTous(ActionEvent event) {
        if(radioEmpruntTous.isSelected()){
            radioEmpruntAnnee.setSelected(false);
            radioEmpruntMois.setSelected(false);
            radioEmpruntJour.setSelected(false);
        }else{
            radioEmpruntTous.setSelected(true);
        }
        barChartEmpruntRetour.setTitle("عدد اللإعارة و التقديم لكل شهر في السنة");
        remplirEmpruntsRetours();
    }

    @FXML
    private void actionRadioEmpruntAnnee(ActionEvent event) {
        if(radioEmpruntAnnee.isSelected()){
            radioEmpruntTous.setSelected(false);
            radioEmpruntMois.setSelected(false);
            radioEmpruntJour.setSelected(false);
        }else{
            radioEmpruntAnnee.setSelected(true);
        }
        remplirEmpruntsRetours();
    }

    @FXML
    private void actionRadioEmpruntMois(ActionEvent event) {
        if(radioEmpruntMois.isSelected()){
            radioEmpruntTous.setSelected(false);
            radioEmpruntAnnee.setSelected(false);
            radioEmpruntJour.setSelected(false);
        }else{
            radioEmpruntMois.setSelected(true);
        }
        remplirEmpruntsRetours();
    }

    @FXML
    private void actionRadioEmpruntJour(ActionEvent event) {
        if(radioEmpruntJour.isSelected()){
            radioEmpruntTous.setSelected(false);
            radioEmpruntAnnee.setSelected(false);
            radioEmpruntMois.setSelected(false);
        }else{
            radioEmpruntJour.setSelected(true);
        }
        remplirEmpruntsRetours();
    }

    @FXML
    private void actionDate(ActionEvent event) {
        remplirEmpruntsRetours();
    }
    //*****ADHERENT*****

    private void remplirAdherent(){
        if(radioAdherentTous.isSelected()){//Tous
            for(int i=0;i < 12;i++){
                String qu1 = "SELECT COUNT(*) AS total "
                        + "FROM Adherent "
                        + "WHERE MONTH(DateEngagement) = " + (i+1);
                ResultSet rs1 = base.execQuery(qu1);
                try {
                    if(rs1.next()){
                        adherents[i] = rs1.getInt("total");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String qu1 = "SELECT COUNT(*) AS total FROM Adherent";
            String qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE sexe = 1";
            String qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE sexe = 0";
            String qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE DATEDIFF(DateRenouvelement, CURRENT_DATE()) < 0";
            String qu5 = "SELECT AVG(YEAR(CURRENT_DATE())-YEAR(DateNaissance)) AS total "
                    + "FROM Adherent "
                    + "WHERE DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0";
            ResultSet rs1 = base.execQuery(qu1);
            ResultSet rs2 = base.execQuery(qu2);
            ResultSet rs3 = base.execQuery(qu3);
            ResultSet rs4 = base.execQuery(qu4);
            ResultSet rs5 = base.execQuery(qu5);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next() && rs5.next()){
                    nbrAdherent = rs1.getInt("total");
                    nbrHomme = rs2.getInt("total");
                    nbrFemme = rs3.getInt("total");
                    nbrAdherentExpirer = rs4.getInt("total");
                    avgAge = rs5.getDouble("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //PieChart nombre adherent en fonction des intervales d'age
            qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE (TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 0 AND 14) AND "
                    + "DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE (TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 15 AND 17) AND "
                    + "DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0";
            qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE (TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 18 AND 24) AND "
                    + "DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0";
            qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE (TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 25 AND 200) AND "
                    + "DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0";
            rs1 = base.execQuery(qu1);
            rs2 = base.execQuery(qu2);
            rs3 = base.execQuery(qu3);
            rs4 = base.execQuery(qu4);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next()){
                    nombre00_14 = rs1.getInt("total");
                    nombre15_17 = rs2.getInt("total");
                    nombre18_24 = rs3.getInt("total");
                    nombre24_99 = rs4.getInt("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //PieChart Rendre
            qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 0 AND 14)";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 15 AND 17)";
            qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 18 AND 24)";
            qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 25 AND 200)";
            rs1 = base.execQuery(qu1);
            rs2 = base.execQuery(qu2);
            rs3 = base.execQuery(qu3);
            rs4 = base.execQuery(qu4);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next()){
                    rendre00_14 = rs1.getInt("total");
                    rendre15_17 = rs2.getInt("total");
                    rendre18_24 = rs3.getInt("total");
                    rendre24_99 = rs4.getInt("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //PieChart Emprunt
            qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Emprunt E "
                    + "WHERE A.idAdherent = E.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 0 AND 14)";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Emprunt E "
                    + "WHERE A.idAdherent = E.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 15 AND 17)";
            qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Emprunt E "
                    + "WHERE A.idAdherent = E.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 18 AND 24)";
            qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Emprunt E,Rendre R "
                    + "WHERE A.idAdherent = E.idAdherent AND A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 25 AND 200)";
            rs1 = base.execQuery(qu1);
            rs2 = base.execQuery(qu2);
            rs3 = base.execQuery(qu3);
            rs4 = base.execQuery(qu4);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next()){
                    emprunt00_14 = rs1.getInt("total") + rendre00_14;
                    emprunt15_17 = rs2.getInt("total") + rendre15_17;
                    emprunt18_24 = rs3.getInt("total") + rendre18_24;
                    emprunt24_99 = rs4.getInt("total") + rendre24_99;
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(radioAdherentAnnee.isSelected()){//Annee
            LocalDate date = adherentDatePicker.getValue();
            barChartAdherent.setTitle("عدد المشتركين الجدد لكل شهر في سنة   " + date.getYear());
            for(int i=0;i < 12;i++){
                String qu1 = "SELECT COUNT(*) AS total "
                        + "FROM Adherent "
                        + "WHERE YEAR(DateEngagement) = YEAR('" + date + "') AND "
                        + "MONTH(DateEngagement) = " + (i+1);
                ResultSet rs1 = base.execQuery(qu1);
                try {
                    if(rs1.next()){
                        adherents[i] = rs1.getInt("total");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE YEAR(DateEngagement) = YEAR('" + date + "')";
            String qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE sexe = 1 AND YEAR(DateEngagement) = YEAR('" + date + "')";
            String qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE sexe = 0 AND YEAR(DateEngagement) = YEAR('" + date + "')";
            String qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE DATEDIFF(DateRenouvelement, CURRENT_DATE()) < 0 AND "
                    + "YEAR(DateRenouvelement) = YEAR('" + date +"')";
            String qu5 = "SELECT AVG(YEAR(CURRENT_DATE())-YEAR(DateNaissance)) AS total "
                    + "FROM Adherent "
                    + "WHERE DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0 AND "
                    + "YEAR(DateEngagement) = YEAR('" + date + "')";
            ResultSet rs1 = base.execQuery(qu1);
            ResultSet rs2 = base.execQuery(qu2);
            ResultSet rs3 = base.execQuery(qu3);
            ResultSet rs4 = base.execQuery(qu4);
            ResultSet rs5 = base.execQuery(qu5);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next() && rs5.next()){
                    nbrAdherent = rs1.getInt("total");
                    nbrHomme = rs2.getInt("total");
                    nbrFemme = rs3.getInt("total");
                    nbrAdherentExpirer = rs4.getInt("total");
                    avgAge = rs5.getDouble("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //PieChart nombre adherent en fonction des intervales d'age
            qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE (TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 0 AND 14) AND "
                    + "DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0 AND "
                    + "YEAR(DateEngagement) = YEAR('" + date + "')";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE (TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 15 AND 17) AND "
                    + "DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0 AND "
                    + "YEAR(DateEngagement) = YEAR('" + date + "')";
            qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE (TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 18 AND 24) AND "
                    + "DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0 AND "
                    + "YEAR(DateEngagement) = YEAR('" + date + "')";
            qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE (TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 25 AND 200) AND "
                    + "DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0 AND "
                    + "YEAR(DateEngagement) = YEAR('" + date + "')";
            rs1 = base.execQuery(qu1);
            rs2 = base.execQuery(qu2);
            rs3 = base.execQuery(qu3);
            rs4 = base.execQuery(qu4);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next()){
                    nombre00_14 = rs1.getInt("total");
                    nombre15_17 = rs2.getInt("total");
                    nombre18_24 = rs3.getInt("total");
                    nombre24_99 = rs4.getInt("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //PieChart Emprunt (Rendre)
            qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 0 AND 14) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "')";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 15 AND 17) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "')";
            qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 18 AND 24) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "')";
            qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 25 AND 200) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "')";
            rs1 = base.execQuery(qu1);
            rs2 = base.execQuery(qu2);
            rs3 = base.execQuery(qu3);
            rs4 = base.execQuery(qu4);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next()){
                    rendre00_14 = rs1.getInt("total");
                    rendre15_17 = rs2.getInt("total");
                    rendre18_24 = rs3.getInt("total");
                    rendre24_99 = rs4.getInt("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //PieChart Emprunt (Emprunt)
            qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Emprunt E "
                    + "WHERE A.idAdherent = E.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 0 AND 14) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "')";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Emprunt E "
                    + "WHERE A.idAdherent = E.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 15 AND 17) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "')";
            qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Emprunt E "
                    + "WHERE A.idAdherent = E.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 18 AND 24) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "')";
            qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Emprunt E "
                    + "WHERE A.idAdherent = E.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 25 AND 200) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "')";
            rs1 = base.execQuery(qu1);
            rs2 = base.execQuery(qu2);
            rs3 = base.execQuery(qu3);
            rs4 = base.execQuery(qu4);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next()){
                    emprunt00_14 = rs1.getInt("total") + rendre00_14;
                    emprunt15_17 = rs2.getInt("total") + rendre15_17;
                    emprunt18_24 = rs3.getInt("total") + rendre18_24;
                    emprunt24_99 = rs4.getInt("total") + rendre24_99;
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            rendre00_14 = 0;
            rendre15_17 = 0;
            rendre18_24 = 0;
            rendre24_99 = 0;
            //PieChart Rendre
            qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 0 AND 14) AND "
                    + "YEAR(dateRetour) = YEAR('" + date + "')";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 15 AND 17) AND "
                    + "YEAR(dateRetour) = YEAR('" + date + "')";
            qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 18 AND 24) AND "
                    + "YEAR(dateRetour) = YEAR('" + date + "')";
            qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 25 AND 200) AND "
                    + "YEAR(dateRetour) = YEAR('" + date + "')";
            rs1 = base.execQuery(qu1);
            rs2 = base.execQuery(qu2);
            rs3 = base.execQuery(qu3);
            rs4 = base.execQuery(qu4);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next()){
                    rendre00_14 = rs1.getInt("total");
                    rendre15_17 = rs2.getInt("total");
                    rendre18_24 = rs3.getInt("total");
                    rendre24_99 = rs4.getInt("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(radioAdherentMois.isSelected()){//Mois
            LocalDate date = adherentDatePicker.getValue();
            String qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE YEAR(DateEngagement) = YEAR('" + date + "') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "')";
            String qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE sexe = 1 AND YEAR(DateEngagement) = YEAR('" + date + "') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "')";
            String qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE sexe = 0 AND YEAR(DateEngagement) = YEAR('" + date + "') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "')";
            String qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE DATEDIFF(DateRenouvelement, CURRENT_DATE()) < 0 AND "
                    + "YEAR(DateRenouvelement) = YEAR('" + date +"') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "')";
            String qu5 = "SELECT AVG(YEAR(CURRENT_DATE())-YEAR(DateNaissance)) AS total "
                    + "FROM Adherent "
                    + "WHERE DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0 AND "
                    + "YEAR(DateEngagement) = YEAR('" + date + "') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "')";
            ResultSet rs1 = base.execQuery(qu1);
            ResultSet rs2 = base.execQuery(qu2);
            ResultSet rs3 = base.execQuery(qu3);
            ResultSet rs4 = base.execQuery(qu4);
            ResultSet rs5 = base.execQuery(qu5);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next() && rs5.next()){
                    nbrAdherent = rs1.getInt("total");
                    nbrHomme = rs2.getInt("total");
                    nbrFemme = rs3.getInt("total");
                    nbrAdherentExpirer = rs4.getInt("total");
                    avgAge = rs5.getDouble("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //PieChart nombre adherent en fonction des intervales d'age
            qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE (TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 0 AND 14) AND "
                    + "DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0 AND "
                    + "YEAR(DateEngagement) = YEAR('" + date + "') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "')";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE (TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 15 AND 17) AND "
                    + "DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0 AND "
                    + "YEAR(DateEngagement) = YEAR('" + date + "') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "')";
            qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE (TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 18 AND 24) AND "
                    + "DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0 AND "
                    + "YEAR(DateEngagement) = YEAR('" + date + "') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "')";
            qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE (TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 25 AND 200) AND "
                    + "DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0 AND "
                    + "YEAR(DateEngagement) = YEAR('" + date + "') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "')";
            rs1 = base.execQuery(qu1);
            rs2 = base.execQuery(qu2);
            rs3 = base.execQuery(qu3);
            rs4 = base.execQuery(qu4);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next()){
                    nombre00_14 = rs1.getInt("total");
                    nombre15_17 = rs2.getInt("total");
                    nombre18_24 = rs3.getInt("total");
                    nombre24_99 = rs4.getInt("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //PieChart Emprunt (Rendre)
            qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 0 AND 14) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "')";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 15 AND 17) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "')";
            qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 18 AND 24) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "')";
            qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 25 AND 200) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "')";
            rs1 = base.execQuery(qu1);
            rs2 = base.execQuery(qu2);
            rs3 = base.execQuery(qu3);
            rs4 = base.execQuery(qu4);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next()){
                    rendre00_14 = rs1.getInt("total");
                    rendre15_17 = rs2.getInt("total");
                    rendre18_24 = rs3.getInt("total");
                    rendre24_99 = rs4.getInt("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //PieChart Emprunt (Emprunt)
            qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Emprunt E "
                    + "WHERE A.idAdherent = E.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 0 AND 14) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "')";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Emprunt E "
                    + "WHERE A.idAdherent = E.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 15 AND 17) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "')";
            qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Emprunt E "
                    + "WHERE A.idAdherent = E.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 18 AND 24) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "')";
            qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Emprunt E "
                    + "WHERE A.idAdherent = E.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 25 AND 200) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "')";
            rs1 = base.execQuery(qu1);
            rs2 = base.execQuery(qu2);
            rs3 = base.execQuery(qu3);
            rs4 = base.execQuery(qu4);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next()){
                    emprunt00_14 = rs1.getInt("total") + rendre00_14;
                    emprunt15_17 = rs2.getInt("total") + rendre15_17;
                    emprunt18_24 = rs3.getInt("total") + rendre18_24;
                    emprunt24_99 = rs4.getInt("total") + rendre24_99;
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            rendre00_14 = 0;
            rendre15_17 = 0;
            rendre18_24 = 0;
            rendre24_99 = 0;
            //PieChart Rendre
            qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 0 AND 14) AND "
                    + "YEAR(dateRetour) = YEAR('" + date + "') AND "
                    + "MONTH(dateRetour) = MONTH('" + date + "')";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 15 AND 17) AND "
                    + "YEAR(dateRetour) = YEAR('" + date + "') AND "
                    + "MONTH(dateRetour) = MONTH('" + date + "')";
            qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 18 AND 24) AND "
                    + "YEAR(dateRetour) = YEAR('" + date + "') AND "
                    + "MONTH(dateRetour) = MONTH('" + date + "')";
            qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 25 AND 200) AND "
                    + "YEAR(dateRetour) = YEAR('" + date + "') AND "
                    + "MONTH(dateRetour) = MONTH('" + date + "')";
            rs1 = base.execQuery(qu1);
            rs2 = base.execQuery(qu2);
            rs3 = base.execQuery(qu3);
            rs4 = base.execQuery(qu4);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next()){
                    rendre00_14 = rs1.getInt("total");
                    rendre15_17 = rs2.getInt("total");
                    rendre18_24 = rs3.getInt("total");
                    rendre24_99 = rs4.getInt("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(radioAdherentJour.isSelected()){//Jour
            LocalDate date = adherentDatePicker.getValue();
            String qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE YEAR(DateEngagement) = YEAR('" + date + "') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "') AND "
                    + "DAY(DateEngagement) = DAY('" + date + "')";
            String qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE sexe = 1 AND YEAR(DateEngagement) = YEAR('" + date + "') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "') AND "
                    + "DAY(DateEngagement) = DAY('" + date + "')";
            String qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE sexe = 0 AND YEAR(DateEngagement) = YEAR('" + date + "') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "') AND "
                    + "DAY(DateEngagement) = DAY('" + date + "')";
            String qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE DATEDIFF(DateRenouvelement, CURRENT_DATE()) < 0 AND "
                    + "YEAR(DateRenouvelement) = YEAR('" + date +"') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "') AND "
                    + "DAY(DateEngagement) = DAY('" + date + "')";
            String qu5 = "SELECT AVG(YEAR(CURRENT_DATE())-YEAR(DateNaissance)) AS total "
                    + "FROM Adherent "
                    + "WHERE DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0 AND "
                    + "YEAR(DateEngagement) = YEAR('" + date + "') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "') AND "
                    + "DAY(DateEngagement) = DAY('" + date + "')";
            ResultSet rs1 = base.execQuery(qu1);
            ResultSet rs2 = base.execQuery(qu2);
            ResultSet rs3 = base.execQuery(qu3);
            ResultSet rs4 = base.execQuery(qu4);
            ResultSet rs5 = base.execQuery(qu5);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next() && rs5.next()){
                    nbrAdherent = rs1.getInt("total");
                    nbrHomme = rs2.getInt("total");
                    nbrFemme = rs3.getInt("total");
                    nbrAdherentExpirer = rs4.getInt("total");
                    avgAge = rs5.getDouble("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //PieChart nombre adherent en fonction des intervales d'age
            qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE (TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 0 AND 14) AND "
                    + "DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0 AND "
                    + "YEAR(DateEngagement) = YEAR('" + date + "') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "') AND "
                    + "DAY(DateEngagement) = DAY('" + date + "')";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE (TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 15 AND 17) AND "
                    + "DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0 AND "
                    + "YEAR(DateEngagement) = YEAR('" + date + "') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "') AND "
                    + "DAY(DateEngagement) = DAY('" + date + "')";
            qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE (TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 18 AND 24) AND "
                    + "DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0 AND "
                    + "YEAR(DateEngagement) = YEAR('" + date + "') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "') AND "
                    + "DAY(DateEngagement) = DAY('" + date + "')";
            qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent "
                    + "WHERE (TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 25 AND 200) AND "
                    + "DATEDIFF(DateRenouvelement, CURRENT_DATE()) > 0 AND "
                    + "YEAR(DateEngagement) = YEAR('" + date + "') AND "
                    + "MONTH(DateEngagement) = MONTH('" + date + "') AND "
                    + "DAY(DateEngagement) = DAY('" + date + "')";
            rs1 = base.execQuery(qu1);
            rs2 = base.execQuery(qu2);
            rs3 = base.execQuery(qu3);
            rs4 = base.execQuery(qu4);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next()){
                    nombre00_14 = rs1.getInt("total");
                    nombre15_17 = rs2.getInt("total");
                    nombre18_24 = rs3.getInt("total");
                    nombre24_99 = rs4.getInt("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //PieChart Emprunt (Rendre)
            qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 0 AND 14) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "') AND "
                    + "DAY(dateEmprunt) = DAY('" + date + "')";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 15 AND 17) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "') AND "
                    + "DAY(dateEmprunt) = DAY('" + date + "')";
            qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 18 AND 24) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "') AND "
                    + "DAY(dateEmprunt) = DAY('" + date + "')";
            qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 25 AND 200) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "') AND "
                    + "DAY(dateEmprunt) = DAY('" + date + "')";
            rs1 = base.execQuery(qu1);
            rs2 = base.execQuery(qu2);
            rs3 = base.execQuery(qu3);
            rs4 = base.execQuery(qu4);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next()){
                    rendre00_14 = rs1.getInt("total");
                    rendre15_17 = rs2.getInt("total");
                    rendre18_24 = rs3.getInt("total");
                    rendre24_99 = rs4.getInt("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //PieChart Emprunt (Emprunt)
            qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Emprunt E "
                    + "WHERE A.idAdherent = E.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 0 AND 14) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "') AND "
                    + "DAY(dateEmprunt) = DAY('" + date + "')";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Emprunt E "
                    + "WHERE A.idAdherent = E.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 15 AND 17) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "') AND "
                    + "DAY(dateEmprunt) = DAY('" + date + "')";
            qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Emprunt E "
                    + "WHERE A.idAdherent = E.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 18 AND 24) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "') AND "
                    + "DAY(dateEmprunt) = DAY('" + date + "')";
            qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Emprunt E "
                    + "WHERE A.idAdherent = E.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 25 AND 200) AND "
                    + "YEAR(dateEmprunt) = YEAR('" + date + "') AND "
                    + "MONTH(dateEmprunt) = MONTH('" + date + "') AND "
                    + "DAY(dateEmprunt) = DAY('" + date + "')";
            rs1 = base.execQuery(qu1);
            rs2 = base.execQuery(qu2);
            rs3 = base.execQuery(qu3);
            rs4 = base.execQuery(qu4);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next()){
                    emprunt00_14 = rs1.getInt("total") + rendre00_14;
                    emprunt15_17 = rs2.getInt("total") + rendre15_17;
                    emprunt18_24 = rs3.getInt("total") + rendre18_24;
                    emprunt24_99 = rs4.getInt("total") + rendre24_99;
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            rendre00_14 = 0;
            rendre15_17 = 0;
            rendre18_24 = 0;
            rendre24_99 = 0;
            //PieChart Rendre
            qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 0 AND 14) AND "
                    + "YEAR(dateRetour) = YEAR('" + date + "') AND "
                    + "MONTH(dateRetour) = MONTH('" + date + "') AND "
                    + "DAY(dateRetour) = DAY('" + date + "')";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 15 AND 17) AND "
                    + "YEAR(dateRetour) = YEAR('" + date + "') AND "
                    + "MONTH(dateRetour) = MONTH('" + date + "') AND "
                    + "DAY(dateRetour) = DAY('" + date + "')";
            qu3 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 18 AND 24) AND "
                    + "YEAR(dateRetour) = YEAR('" + date + "') AND "
                    + "MONTH(dateRetour) = MONTH('" + date + "') AND "
                    + "DAY(dateRetour) = DAY('" + date + "')";
            qu4 = "SELECT COUNT(*) AS total "
                    + "FROM Adherent A,Rendre R "
                    + "WHERE A.idAdherent = R.idAdherent AND "
                    + "(TIMESTAMPDIFF(YEAR,DateNaissance,CURRENT_DATE()) BETWEEN 25 AND 200) AND "
                    + "YEAR(dateRetour) = YEAR('" + date + "') AND "
                    + "MONTH(dateRetour) = MONTH('" + date + "') AND "
                    + "DAY(dateRetour) = DAY('" + date + "')";
            rs1 = base.execQuery(qu1);
            rs2 = base.execQuery(qu2);
            rs3 = base.execQuery(qu3);
            rs4 = base.execQuery(qu4);
            try {
                if(rs1.next() && rs2.next() && rs3.next() && rs4.next()){
                    rendre00_14 = rs1.getInt("total");
                    rendre15_17 = rs2.getInt("total");
                    rendre18_24 = rs3.getInt("total");
                    rendre24_99 = rs4.getInt("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Diagramme des nouveaux Adherent de chaque mois
        if(radioAdherentTous.isSelected() || radioAdherentAnnee.isSelected()){
            adherentCategoryAxisMois.setLabel("اشهر السنة");
            adherentNumberAxis.setLabel("العدد");
            
            ObservableList<XYChart.Data> nombreAdherentMois = 
                    FXCollections.observableArrayList(
                            new XYChart.Data("يناير", this.adherents[0]),
                            new XYChart.Data("فبراير", this.adherents[1]),
                            new XYChart.Data("مارس", this.adherents[2]),
                            new XYChart.Data("أبريل", this.adherents[3]),
                            new XYChart.Data("مايو", this.adherents[4]),
                            new XYChart.Data("يونيو", this.adherents[5]),
                            new XYChart.Data("يوليو", this.adherents[6]),
                            new XYChart.Data("أغسطس", this.adherents[7]),
                            new XYChart.Data("سبتمبر", this.adherents[8]),
                            new XYChart.Data("أكتوبر", this.adherents[9]),
                            new XYChart.Data("نوفمبر", this.adherents[10]),
                            new XYChart.Data("ديسمبر", this.adherents[11])
                    );
            XYChart.Series seriesNombreAdherentMois = new XYChart.Series("عدد القروض",nombreAdherentMois);
            barChartAdherent.getData().clear();
            barChartAdherent.getData().addAll(seriesNombreAdherentMois);
        }
        //Nombre D'adherent, homme, femme, les adherent qui n'ont pas renouvler, moyenne des ages
        nombreAdherent.setText(nbrAdherent + "");
        nombreHomme.setText(nbrHomme + "");
        nombreFemme.setText(nbrFemme + "");
        nombreAdherentExpirer.setText(nbrAdherentExpirer + "");
        moyenneAge.setText((int)Math.rint(avgAge) + "");
        //System.out.println(Math.rint(13.4329));
        //moyenneAge.setText(String.format("%.1f%%",avgAge + ""));
        //PieChart Emprunt Rendre nombre en fonction des intervales d'age
        int somme = nombre00_14 + nombre15_17 + nombre18_24 + nombre24_99;
        if(somme == 0){
            somme = 1;
        }
        ObservableList<PieChart.Data> pieChartNombreAgeData = 
                FXCollections.observableArrayList(
                        new PieChart.Data("0-14" + "(" + (int)Math.rint((nombre00_14*100)/somme) + "%)", nombre00_14),
                        new PieChart.Data("15-17" + "(" + (int)Math.rint((nombre15_17*100)/somme) + "%)", nombre15_17),
                        new PieChart.Data("18-24" + "(" + (int)Math.rint((nombre18_24*100)/somme) + "%)", nombre18_24),
                        new PieChart.Data("25-+" + "(" + (int)Math.rint((nombre24_99*100)/somme) + "%)", nombre24_99)
                );
        pieChartAdherentNombreAge.setTitle("نسبة الأعضاء حسب الأعمار");
        pieChartAdherentNombreAge.setData(pieChartNombreAgeData);
        
        somme = emprunt00_14 + emprunt15_17 + emprunt18_24 + emprunt24_99;
        if(somme == 0){
            somme = 1;
        }
        ObservableList<PieChart.Data> pieChartEmpruntData = 
                FXCollections.observableArrayList(
                        new PieChart.Data("0-14" + "(" + (int)Math.rint((emprunt00_14*100)/somme) + "%)", emprunt00_14),
                        new PieChart.Data("15-17" + "(" + (int)Math.rint((emprunt15_17*100)/somme) + "%)", emprunt15_17),
                        new PieChart.Data("18-24" + "(" + (int)Math.rint((emprunt18_24*100)/somme) + "%)", emprunt18_24),
                        new PieChart.Data("25-+" + "(" + (int)Math.rint((emprunt24_99*100)/somme) + "%)", emprunt24_99)
                );
        pieChartAdherentEmprunt.setTitle("نسبة القروض حسب الأعمار");
        pieChartAdherentEmprunt.setData(pieChartEmpruntData);
        
        somme = rendre00_14 + rendre15_17 + rendre18_24 + rendre24_99;
        if(somme == 0){
            somme = 1;
        }
        ObservableList<PieChart.Data> pieChartRendreData = 
                FXCollections.observableArrayList(
                        new PieChart.Data("0-14" + "(" + (int)Math.rint((rendre00_14*100)/somme) + "%)", rendre00_14),
                        new PieChart.Data("15-17" + "(" + (int)Math.rint((rendre15_17*100)/somme) + "%)", rendre15_17),
                        new PieChart.Data("18-24" + "(" + (int)Math.rint((rendre18_24*100)/somme) + "%)", rendre18_24),
                        new PieChart.Data("25-+" + "(" + (int)Math.rint((rendre24_99*100)/somme) + "%)", rendre24_99)
                );
        pieChartAdherentRendre.setTitle("نسبة الإرجاع حسب الأعمار");
        pieChartAdherentRendre.setData(pieChartRendreData);
    }
    @FXML
    private void actionAdherentDate(ActionEvent event) {
        remplirAdherent();
    }

    @FXML
    private void actionRadioAdherentTous(ActionEvent event) {
        if(radioAdherentTous.isSelected()){
            radioAdherentAnnee.setSelected(false);
            radioAdherentMois.setSelected(false);
            radioAdherentJour.setSelected(false);
        }else{
            radioAdherentTous.setSelected(true);
        }
        barChartAdherent.setTitle("عدد المشتركين الجدد لكل شهر في السنة");
        remplirAdherent();
    }

    @FXML
    private void actionRadioAdherentAnnee(ActionEvent event) {
        if(radioAdherentAnnee.isSelected()){
            radioAdherentTous.setSelected(false);
            radioAdherentMois.setSelected(false);
            radioAdherentJour.setSelected(false);
        }else{
            radioAdherentAnnee.setSelected(true);
        }
        remplirAdherent();
    }

    @FXML
    private void actionRadioAdherentMois(ActionEvent event) {
        if(radioAdherentMois.isSelected()){
            radioAdherentTous.setSelected(false);
            radioAdherentAnnee.setSelected(false);
            radioAdherentJour.setSelected(false);
        }else{
            radioAdherentMois.setSelected(true);
        }
        remplirAdherent();
    }

    @FXML
    private void actionRadioAdherentJour(ActionEvent event) {
        if(radioAdherentJour.isSelected()){
            radioAdherentTous.setSelected(false);
            radioAdherentAnnee.setSelected(false);
            radioAdherentMois.setSelected(false);
        }else{
            radioAdherentJour.setSelected(true);
        }
        remplirAdherent();
    }
    //*****DOCUMENT*****
    private void remplirDocument(){
        //System.out.println(Math.rint(13.4329));
        //Document Livre Magazine Memoire et leur Exemplaire
        String categorie = categorieCombo.getValue();
        String qu1 = "SELECT COUNT(*) AS total "
                + "FROM document ";
        String qu2 = "SELECT COUNT(*) AS total "
                + "FROM Exemplaire ";
        String qu3 = "SELECT COUNT(*) AS total "
                + "FROM document "
                + "WHERE types = 'كتاب' ";
        String qu4 = "SELECT COUNT(*) AS total "
                + "FROM document D,Exemplaire E "
                + "WHERE types = 'كتاب' AND D.idDocument = E.idDocument ";
        String qu5 = "SELECT COUNT(*) AS total "
                + "FROM document "
                + "WHERE types = 'مجلة' ";
        String qu6 = "SELECT COUNT(*) AS total "
                + "FROM document D,Exemplaire E "
                + "WHERE types = 'مجلة' AND D.idDocument = E.idDocument ";
        String qu7 = "SELECT COUNT(*) AS total "
                + "FROM document "
                + "WHERE types = 'مذكرة' ";
        String qu8 = "SELECT COUNT(*) AS total "
                + "FROM document D,Exemplaire E "
                + "WHERE types = 'مذكرة' AND D.idDocument = E.idDocument ";
        if(!categorie.equals("جميع التخصصات")){
            qu1 += "WHERE nomCategorie = '" + categorie + "'";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Exemplaire E,document D "
                    + "WHERE nomCategorie = '" + categorie + "' AND D.idDocument = E.idDocument";
            qu3 += "AND nomCategorie = '" + categorie + "'";
            qu4 += "AND nomCategorie = '" + categorie + "'";
            qu5 += "AND nomCategorie = '" + categorie + "'";
            qu6 += "AND nomCategorie = '" + categorie + "'";
            qu7 += "AND nomCategorie = '" + categorie + "'";
            qu8 += "AND nomCategorie = '" + categorie + "'";
        }
        ResultSet rs1 = base.execQuery(qu1);
        ResultSet rs2 = base.execQuery(qu2);
        ResultSet rs3 = base.execQuery(qu3);
        ResultSet rs4 = base.execQuery(qu4);
        ResultSet rs5 = base.execQuery(qu5);
        ResultSet rs6 = base.execQuery(qu6);
        ResultSet rs7 = base.execQuery(qu7);
        ResultSet rs8 = base.execQuery(qu8);

        try {
            if(rs1.next() && rs2.next() && rs3.next() && rs4.next() && rs5.next() && rs6.next() && rs7.next() && rs8.next()){
                nombreDocument.setText(rs1.getInt("total") + "");
                nombreExemplaire.setText(rs2.getInt("total") + "");
                nombreLivre.setText(rs3.getInt("total") + "");
                nombreLivreExemplaire.setText(rs4.getInt("total") + "");
                nombreMagazine.setText(rs5.getInt("total") + "");
                nombreMagazineExemplaire.setText(rs6.getInt("total") + "");
                nombreMemoire.setText(rs7.getInt("total") + "");
                nombreMemoireExemplaire.setText(rs8.getInt("total") + "");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //PieChart Empruntable
        if(!categorie.equals("جميع التخصصات")){
            qu1 = "SELECT COUNT(*) AS total "
                    + "FROM document,Exemplaire "
                    + "WHERE empruntable = true AND nomCategorie = '" + categorie +"'";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM document,Exemplaire "
                    + "WHERE empruntable = false AND nomCategorie = '" + categorie +"'";
        }else {
            qu1 = "SELECT COUNT(*) AS total "
                    + "FROM Exemplaire "
                    + "WHERE empruntable = true ";
            qu2 = "SELECT COUNT(*) AS total "
                    + "FROM Exemplaire "
                    + "WHERE empruntable = false ";
        }
        rs1 = base.execQuery(qu1);
        rs2 = base.execQuery(qu2);
        try {
            if(rs1.next() && rs2.next()){
                empruntable = rs1.getInt("total");
                nonEmpruntable = rs2.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int somme = empruntable + nonEmpruntable;
        if(somme == 0){
            somme = 1;
        }
        ObservableList<PieChart.Data> pieChartEmpruntable = 
                FXCollections.observableArrayList(
                        new PieChart.Data("للإقراض" + "(%" + (int)Math.rint((empruntable*100)/somme) + ")", empruntable),
                        new PieChart.Data("ليسة للإقراض" + "(%" + (int)Math.rint((nonEmpruntable*100)/somme) + ")", nonEmpruntable)
                );
        pieChartDocumentEmpruntable.setTitle("نسبة الوثائق للإقراض");
        pieChartDocumentEmpruntable.setData(pieChartEmpruntable);
        
        //PieChart Type Document
        somme = Integer.parseInt(nombreLivreExemplaire.getText()) + Integer.parseInt(nombreMagazineExemplaire.getText()) + Integer.parseInt(nombreMemoireExemplaire.getText());
        if(somme == 0){
            somme = 1;
        }
        ObservableList<PieChart.Data> pieChartType = 
                FXCollections.observableArrayList(
                        new PieChart.Data("الكتب" + "(%" + (int)Math.rint((Integer.parseInt(nombreLivreExemplaire.getText())*100)/somme) + ")", Integer.parseInt(nombreLivreExemplaire.getText())),
                        new PieChart.Data("المجلات" + "(%" + (int)Math.rint((Integer.parseInt(nombreMagazineExemplaire.getText())*100)/somme) + ")", Integer.parseInt(nombreMagazineExemplaire.getText())),
                        new PieChart.Data("المذكرات" + "(%" + (int)Math.rint((Integer.parseInt(nombreMemoireExemplaire.getText())*100)/somme) + ")", Integer.parseInt(nombreMemoireExemplaire.getText()))
                );
        pieChartTypeDucument.setTitle("نسبة كل نوع");
        pieChartTypeDucument.setData(pieChartType);
    }
    private void fillCategorieCombo() {
        categorieCombo.getItems().clear();
        ObservableList  list = FXCollections.observableArrayList();
        list.add("جميع التخصصات");
        String qu = "select * from categorie";
        ResultSet rs = base.execQuery(qu);
        try {
            while(rs.next()){
                list.add(rs.getString("nomCategorie"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StatistiquesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        categorieCombo.setItems(list);
        if(!list.isEmpty()){
            categorieCombo.setValue("جميع التخصصات");
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

    @FXML
    private void actionCategorieCombo(ActionEvent event) {
        remplirDocument();
    }
}
