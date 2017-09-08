package soluces.com.pennontautocars.com.Model;

/**
 * Created by RAYA on 11/09/2016.
 */
public class Membre_Info {
    private Integer id;
    private Integer id_membre;
    private String adresse1;
    private String adresse2;
    private String code_postal;
    private String ville;
    private String pays;
    private String date_naissance;
    private String num_permis;
    private String validite_permis;
    private String validite_fimo;
    private String visite_medicale;

    public Membre_Info() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_membre() {
        return id_membre;
    }

    public void setId_membre(Integer id_membre) {
        this.id_membre = id_membre;
    }

    public String getAdresse1() {
        return adresse1;
    }

    public void setAdresse1(String adresse1) {
        this.adresse1 = adresse1;
    }

    public String getAdresse2() {
        return adresse2;
    }

    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }

    public String getCode_postal() {
        return code_postal;
    }

    public void setCode_postal(String code_postal) {
        this.code_postal = code_postal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(String date_naissance) {
        this.date_naissance = date_naissance;
    }

    public String getNum_permis() {
        return num_permis;
    }

    public void setNum_permis(String num_permis) {
        this.num_permis = num_permis;
    }

    public String getValidite_permis() {
        return validite_permis;
    }

    public void setValidite_permis(String validite_permis) {
        this.validite_permis = validite_permis;
    }

    public String getValidite_fimo() {
        return validite_fimo;
    }

    public void setValidite_fimo(String validite_fimo) {
        this.validite_fimo = validite_fimo;
    }

    public String getVisite_medicale() {
        return visite_medicale;
    }

    public void setVisite_medicale(String visite_medicale) {
        this.visite_medicale = visite_medicale;
    }
}
