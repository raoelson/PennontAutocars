package soluces.com.pennontautocars.com.Model;

/**
 * Created by RAYA on 11/09/2016.
 */
public class Contrat {
    private Integer id;
    private String date_debut;
    private String date_fin;
    private String date_rupture;
    private String motif_rupture;
    private Integer id_membre;
    private String nbr_heure;

    public Contrat() {

    }

    public String getNbr_heure() {
        return nbr_heure;
    }

    public void setNbr_heure(String nbr_heure) {
        this.nbr_heure = nbr_heure;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public String getDate_rupture() {
        return date_rupture;
    }

    public void setDate_rupture(String date_rupture) {
        this.date_rupture = date_rupture;
    }

    public String getMotif_rupture() {
        return motif_rupture;
    }

    public void setMotif_rupture(String motif_rupture) {
        this.motif_rupture = motif_rupture;
    }

    public Integer getId_membre() {
        return id_membre;
    }

    public void setId_membre(Integer id_membre) {
        this.id_membre = id_membre;
    }
}
