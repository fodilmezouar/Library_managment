/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package darhadit.GestionMember;

import java.time.MonthDay;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Fodil
 */
public class Adherent {
    private final IntegerProperty numAdherent;
    private final StringProperty nomAdherent;
    private final IntegerProperty nbr_emp_possible;
    private final StringProperty prenomAdherent;
    private final StringProperty lieuNaissanceAdherent;
    private final StringProperty telAdherent;
    private final StringProperty addresAdherent;
    private final StringProperty e_mailAdherent;
    private final StringProperty specialiteAdherent;
    private final StringProperty DateEngagement;
    private final StringProperty DateRenevloument;
    private final StringProperty DateNaissance;
   

    public Adherent(int numAdherent,int nbr_emp_possible ,String nomAdherent, String prenomAdherent, String lieuNaissanceAdherent, String telAdherent, String addresAdherent, String e_mailAdherent, String specialiteAdherent, String DateEngagement, String DateRenevloument, String DateNaissance) {
        this.numAdherent = new SimpleIntegerProperty(numAdherent);
        this.nbr_emp_possible = new SimpleIntegerProperty(nbr_emp_possible);
        this.nomAdherent = new SimpleStringProperty(nomAdherent);
        this.prenomAdherent = new SimpleStringProperty(prenomAdherent);
        this.lieuNaissanceAdherent = new SimpleStringProperty(lieuNaissanceAdherent);
        this.telAdherent = new SimpleStringProperty(telAdherent);
        this.addresAdherent = new SimpleStringProperty(addresAdherent);
        this.e_mailAdherent = new SimpleStringProperty(e_mailAdherent);
        this.specialiteAdherent = new SimpleStringProperty(specialiteAdherent);
        this.DateEngagement = new SimpleStringProperty(DateEngagement);
        this.DateRenevloument = new SimpleStringProperty(DateRenevloument);
        this.DateNaissance = new SimpleStringProperty(DateNaissance);
    }

    public final int getNumAdherent() {
        return numAdherent.get();
    }

    public final void setNumAdherent(int value) {
        numAdherent.set(value);
    }

    public IntegerProperty numAdherentProperty() {
        return numAdherent;
    }

    public final String getNomAdherent() {
        return nomAdherent.get();
    }

    public final void setNomAdherent(String value) {
        nomAdherent.set(value);
    }

    public StringProperty nomAdherentProperty() {
        return nomAdherent;
    }

    public final int getNbr_emp_possible() {
        return nbr_emp_possible.get();
    }

    public final void setNbr_emp_possible(int value) {
        nbr_emp_possible.set(value);
    }

    public IntegerProperty nbr_emp_possibleProperty() {
        return nbr_emp_possible;
    }

    public final String getPrenomAdherent() {
        return prenomAdherent.get();
    }

    public final void setPrenomAdherent(String value) {
        prenomAdherent.set(value);
    }

    public StringProperty prenomAdherentProperty() {
        return prenomAdherent;
    }

    public final String getLieuNaissanceAdherent() {
        return lieuNaissanceAdherent.get();
    }

    public final void setLieuNaissanceAdherent(String value) {
        lieuNaissanceAdherent.set(value);
    }

    public StringProperty lieuNaissanceAdherentProperty() {
        return lieuNaissanceAdherent;
    }

    public final String getTelAdherent() {
        return telAdherent.get();
    }

    public final void setTelAdherent(String value) {
        telAdherent.set(value);
    }

    public StringProperty telAdherentProperty() {
        return telAdherent;
    }

    public final String getAddresAdherent() {
        return addresAdherent.get();
    }

    public final void setAddresAdherent(String value) {
        addresAdherent.set(value);
    }

    public StringProperty addresAdherentProperty() {
        return addresAdherent;
    }

    public final String getE_mailAdherent() {
        return e_mailAdherent.get();
    }

    public final void setE_mailAdherent(String value) {
        e_mailAdherent.set(value);
    }

    public StringProperty e_mailAdherentProperty() {
        return e_mailAdherent;
    }

    public final String getSpecialiteAdherent() {
        return specialiteAdherent.get();
    }

    public final void setSpecialiteAdherent(String value) {
        specialiteAdherent.set(value);
    }

    public StringProperty specialiteAdherentProperty() {
        return specialiteAdherent;
    }

    public final String getDateEngagement() {
        return DateEngagement.get();
    }

    public final void setDateEngagement(String value) {
        DateEngagement.set(value);
    }

    public StringProperty DateEngagementProperty() {
        return DateEngagement;
    }

    public final String getDateRenevloument() {
        return DateRenevloument.get();
    }

    public final void setDateRenevloument(String value) {
        DateRenevloument.set(value);
    }

    public StringProperty DateRenevloumentProperty() {
        return DateRenevloument;
    }

    public final String getDateNaissance() {
        return DateNaissance.get();
    }

    public final void setDateNaissance(String value) {
        DateNaissance.set(value);
    }

    public StringProperty DateNaissanceProperty() {
        return DateNaissance;
    }

  
    
   
}
