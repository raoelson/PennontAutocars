package soluces.com.pennontautocars.com.Model;

import java.util.Collection;

/**
 * Created by RAYA on 24/09/2016.
 */
public class Mission {

    private Integer id;
    private String date_debut;
    private String heures_debut;
    private String date_fin;
    private String heures_fin;
    private Integer nombre_place;
    private Membre membre;
    private Vehicule vehicule;
    private Client client;
    private String depart;
    private String arrivee;

    public Mission() {
    }

    public Mission(String date_debut, String heures_debut, String date_fin, String heures_fin,
                   Integer nombre_place, Membre membre, Vehicule vehicule) {
        this.date_debut = date_debut;
        this.heures_debut = heures_debut;
        this.date_fin = date_fin;
        this.heures_fin = heures_fin;
        this.nombre_place = nombre_place;
        this.membre = membre;
        this.vehicule = vehicule;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getArrivee() {
        return arrivee;
    }

    public void setArrivee(String arrivee) {
        this.arrivee = arrivee;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public String getHeures_debut() {
        return heures_debut;
    }

    public void setHeures_debut(String heures_debut) {
        this.heures_debut = heures_debut;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public String getHeures_fin() {
        return heures_fin;
    }

    public void setHeures_fin(String heures_fin) {
        this.heures_fin = heures_fin;
    }

    public Integer getNombre_place() {
        return nombre_place;
    }

    public void setNombre_place(Integer nombre_place) {
        this.nombre_place = nombre_place;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }
}
