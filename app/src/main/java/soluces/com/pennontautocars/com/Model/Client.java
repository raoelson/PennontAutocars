package soluces.com.pennontautocars.com.Model;

/**
 * Created by RAYA on 21/09/2016.
 */
public class Client {
    private Integer id;
    private String raison_sociale;
    private String siret;
    private String adresse1;
    private String adresse2;
    private String code_postal;
    private String ville;
    private String pays;
    private String telephone;
    private String faxe;
    private String email;
    private String compte_client;

    public Client() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRaison_sociale() {
        return raison_sociale;
    }

    public void setRaison_sociale(String raison_sociale) {
        this.raison_sociale = raison_sociale;
    }

    public String getSiret() {
        return siret;
    }

    public void setSiret(String siret) {
        this.siret = siret;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFaxe() {
        return faxe;
    }

    public void setFaxe(String faxe) {
        this.faxe = faxe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompte_client() {
        return compte_client;
    }

    public void setCompte_client(String compte_client) {
        this.compte_client = compte_client;
    }
}
