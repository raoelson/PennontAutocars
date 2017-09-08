package soluces.com.pennontautocars.com.Model;

/**
 * Created by RAYA on 09/11/2016.
 */

public class Groupe {
    private Integer id;
    private String nomGroupe;
    private String img;
    private Integer admin;
    private Membre membre;
    private String tmps;
    private Integer theme;

    public Groupe() {
    }

    public Integer getTheme() {
        return theme;
    }

    public void setTheme(Integer theme) {
        this.theme = theme;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

    public String getTmps() {
        return tmps;
    }

    public void setTmps(String tmps) {
        this.tmps = tmps;
    }
}
