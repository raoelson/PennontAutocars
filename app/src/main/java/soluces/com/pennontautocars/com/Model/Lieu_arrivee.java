package soluces.com.pennontautocars.com.Model;

/**
 * Created by RAYA on 12/10/2016.
 */
public class Lieu_arrivee {
    String adresse;
    String codePostal;
    String ville;
    String pays;

    public Lieu_arrivee() {

    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }
}
