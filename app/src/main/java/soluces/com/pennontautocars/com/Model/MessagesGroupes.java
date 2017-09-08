package soluces.com.pennontautocars.com.Model;

/**
 * Created by RAYA on 17/11/2016.
 */

public class MessagesGroupes {
    private Integer id;
    private String messages;
    private Groupe groupe;
    private Membre membre;
    private Integer timeAt;

    public MessagesGroupes() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public Integer getTimeAt() {
        return timeAt;
    }

    public void setTimeAt(Integer timeAt) {
        this.timeAt = timeAt;
    }
}
