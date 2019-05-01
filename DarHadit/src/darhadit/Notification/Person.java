package darhadit.Notification;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person {

    private IntegerProperty idadh,iddoc,idcopy,retard;
    private StringProperty emai,num,job,nom_prenom,addresse;

    public Person(Integer idadh, Integer iddoc, Integer idcopy, String emai, String num, String job, String nom_prenom,String addresse,Integer retard) {
        this.idadh = new SimpleIntegerProperty(idadh);
        this.iddoc = new SimpleIntegerProperty(iddoc);
        this.idcopy = new SimpleIntegerProperty(idcopy);
        this.emai = new SimpleStringProperty(emai);
        this.num = new SimpleStringProperty(num);
        this.job = new SimpleStringProperty(job);
        this.nom_prenom = new SimpleStringProperty(nom_prenom);
        this.addresse = new SimpleStringProperty(addresse);
        this.retard = new SimpleIntegerProperty(retard);
    }

    public Integer getIdadh() {
        return idadh.get();
    }

    public void setIdadh(IntegerProperty idadh) {
        this.idadh = idadh;
    }

    public Integer getIddoc() {
        return iddoc.get();
    }

    public void setIddoc(IntegerProperty iddoc) {
        this.iddoc = iddoc;
    }

    public Integer getIdcopy() {
        return idcopy.get();
    }

    public void setIdcopy(IntegerProperty idcopy) {
        this.idcopy = idcopy;
    }

    public String getEmai() {
        return emai.get();
    }

    public void setEmai(StringProperty emai) {
        this.emai = emai;
    }

    public String getNum() {
        return num.get();
    }

    public void setNum(StringProperty num) {
        this.num = num;
    }

    public String getJob() {
        return job.get();
    }

    public void setJob(StringProperty job) {
        this.job = job;
    }

    public String getNom_prenom() {
        return nom_prenom.get();
    }

    public void setNom_prenom(StringProperty nom_prenom) {
        this.nom_prenom = nom_prenom;
    }
    
    public String getAddresse() {
        return addresse.get();
    }

    public void setAddresse(StringProperty addresse) {
        this.addresse = addresse;
    }
    
    public Integer getRetard() {
        return retard.get();
    }

    public void setRetard(IntegerProperty retard) {
        this.retard= retard;
    }
}
