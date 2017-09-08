package soluces.com.pennontautocars.com.Model;

/**
 * Created by RAYA on 24/09/2016.
 */
public class Vehicule {

    private Integer id;
    private String logo;
    private String immatricule;
    private String marque;
    private String modele;

    public Vehicule() {
    }

    public Integer getId() {
        return id;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getImmatricule() {
        return immatricule;
    }

    public void setImmatricule(String immatricule) {
        this.immatricule = immatricule;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }
}
