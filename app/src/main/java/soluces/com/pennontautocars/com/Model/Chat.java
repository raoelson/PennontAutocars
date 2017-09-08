package soluces.com.pennontautocars.com.Model;

import java.io.Serializable;

/**
 * Created by RAYA on 15/10/2016.
 */

public class Chat implements Serializable {
    private int id;
    private String nom;
    private String message;
    private Integer image;
    private Membre membre;
    private String CreatedAt;
    private Integer tmp;
    private String attach;


    public String getAttach() {
        return attach;
    }



    public void setAttach(String attach) {
        this.attach = attach;
    }

    private Boolean test = false;

    public Integer getTmp() {
        return tmp;
    }

    public void setTmp(Integer tmp) {
        this.tmp = tmp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public Boolean getTest() {
        return test;
    }

    public void setTest(Boolean test) {
        this.test = test;
    }

    public Chat() {

    }

    public class User{
        public Integer user_id;
        public String username;
        public String image_send;
        public Integer theme_send;

        public String getImage_send() {
            return image_send;
        }

        public void setImage_send(String image_send) {
            this.image_send = image_send;
        }

        public Integer getTheme_send() {
            return theme_send;
        }

        public void setTheme_send(Integer theme_send) {
            this.theme_send = theme_send;
        }

        public Integer getUser_id() {
            return user_id;
        }

        public void setUser_id(Integer user_id) {
            this.user_id = user_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public class Chat_{
        Integer message_id;
        Integer user_id;
        Integer id_receive;
        String message;
        String attach;
        String created_at;

        private User user = new User();
        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public Integer getMessage_id() {
            return message_id;
        }

        public void setMessage_id(Integer message_id) {
            this.message_id = message_id;
        }

        public Integer getUser_id() {
            return user_id;
        }

        public void setUser_id(Integer user_id) {
            this.user_id = user_id;
        }

        public Integer getId_receive() {
            return id_receive;
        }

        public void setId_receive(Integer id_receive) {
            this.id_receive = id_receive;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getAttach() {
            return attach;
        }

        public void setAttach(String attach) {
            this.attach = attach;
        }
    }
}
