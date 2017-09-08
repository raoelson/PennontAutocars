package soluces.com.pennontautocars.com.JSON;



import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import soluces.com.pennontautocars.com.Model.Chat;
import soluces.com.pennontautocars.com.Model.Client;
import soluces.com.pennontautocars.com.Model.Contrat;
import soluces.com.pennontautocars.com.Model.Groupe;
import soluces.com.pennontautocars.com.Model.Lieu_arrivee;
import soluces.com.pennontautocars.com.Model.Lieu_depart;
import soluces.com.pennontautocars.com.Model.Membre;
import soluces.com.pennontautocars.com.Model.Membre_Info;
import soluces.com.pennontautocars.com.Model.MessagesGroupes;
import soluces.com.pennontautocars.com.Model.Mission;
import soluces.com.pennontautocars.com.Model.Vehicule;

import static java.security.AccessController.getContext;


/**
 * Created by RAYA on 31/08/2016.
 */
public class ParseJSONMembre {

        public ParseJSONMembre() {
        }

        public static List<Membre> getAllStaff(String content){
            try {
                List<Membre> list=null;
                JSONArray jsonArray = new JSONArray(content);
                list = new ArrayList<>();
                for (int i =0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    Membre membre = new Membre();
                    membre.setId(object.getInt("id"));
                    membre.setNom(object.getString("nom"));
                    membre.setPrenom(object.getString("prenom"));
                    membre.setStaff_type(object.getString("staff_type"));
                    membre.setTelephone_pro(object.getString("telephone_pro"));
                    membre.setTelephone_perso(object.getString("telephone_perso"));
                    membre.setEmail(object.getString("mail_pro"));
                    membre.setId_staff(object.getString("id_staff"));
                    membre.setStatut(object.getString("statut"));
                    membre.setDate_creation(object.getString("created_at"));
                    membre.setImage(object.getString("image_profil"));
                    membre.setTheme(object.getInt("theme"));

                    list.add(membre);
                }
                return list;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static Membre_Info getAllStaff_info(String content){
            try {
                    Membre_Info membre_info = new Membre_Info();
                    JSONObject jsonObject = new JSONObject(content);
                    membre_info.setId(jsonObject.getInt("id"));
                    membre_info.setAdresse1(jsonObject.getString("adresse1"));
                    membre_info.setAdresse2(jsonObject.getString("adresse2"));
                    membre_info.setCode_postal(jsonObject.getString("code_postal"));
                    membre_info.setVille(jsonObject.getString("ville"));
                    membre_info.setPays(jsonObject.getString("pays"));
                    membre_info.setDate_naissance(jsonObject.getString("date_naissance"));

                return membre_info;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static List<Contrat> getAllStaff_contrat(String content){
            try {
                List<Contrat> list=null;
                JSONArray jsonArray = new JSONArray(content);
                list = new ArrayList<>();
                for (int i =0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Contrat contrat = new Contrat();
                    contrat.setId(jsonObject.getInt("id"));
                    contrat.setDate_debut(jsonObject.getString("date_debut"));
                    contrat.setDate_fin(jsonObject.getString("date_fin"));
                    contrat.setDate_rupture(jsonObject.getString("date_rupture"));
                    contrat.setMotif_rupture(jsonObject.getString("motif_rupture"));
                    contrat.setNbr_heure(jsonObject.getString("nb_heures_hebdo"));
                    list.add(contrat);
                }
                return list;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static Membre getID_membre(String content){
            try {
                Membre membre = new Membre();
                JSONObject jsonObject = new JSONObject(content);
                membre.setId(jsonObject.getInt("id"));
                membre.setNom(jsonObject.getString("nom"));
                membre.setPrenom(jsonObject.getString("prenom"));
                membre.setStaff_type(jsonObject.getString("staff_type"));
                membre.setTelephone_pro(jsonObject.getString("telephone_pro"));
                membre.setTelephone_perso(jsonObject.getString("telephone_perso"));
                membre.setEmail(jsonObject.getString("mail_pro"));
                membre.setTheme(jsonObject.getInt("theme"));
                return membre;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        public static List<Membre_Info> getAllInfo(String content){
            try {
                List<Membre_Info> list=null;
                JSONArray jsonArray = new JSONArray(content);
                list = new ArrayList<>();
                for (int i =0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Membre_Info membre_info = new Membre_Info();
                    membre_info.setId(jsonObject.getInt("id"));
                    membre_info.setAdresse1(jsonObject.getString("adresse1"));
                    membre_info.setAdresse2(jsonObject.getString("adresse2"));
                    membre_info.setCode_postal(jsonObject.getString("code_postal"));
                    membre_info.setVille(jsonObject.getString("ville"));
                    membre_info.setPays(jsonObject.getString("pays"));
                    membre_info.setDate_naissance(jsonObject.getString("date_naissance"));

                    list.add(membre_info);
                }
                return list;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static List<Client> getAllClient(String content){
            try {
                List<Client> list=null;
                JSONArray jsonArray = new JSONArray(content);
                list = new ArrayList<>();
                for (int i =0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    Client client = new Client();
                    client.setId(object.getInt("id"));
                    client.setRaison_sociale(object.getString("raison_sociale"));
                    client.setSiret(object.getString("siret"));
                    client.setAdresse1(object.getString("adresse1"));
                    client.setAdresse1(object.getString("adresse2"));
                    client.setCode_postal(object.getString("code_postal"));
                    client.setVille(object.getString("ville"));
                    client.setPays(object.getString("pays"));
                    client.setTelephone(object.getString("telephone"));
                    client.setFaxe(object.getString("fax"));
                    client.setEmail(object.getString("mail"));
                    client.setCompte_client(object.getString("compte_client"));
                    list.add(client);
                }
                return list;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public List<Mission> getAllMission(String content) {
            try {
                List<Mission> list=null;
                JSONArray jsonArray = new JSONArray(content);
                list = new ArrayList<>();
                for (int i =0;i<jsonArray.length();i++){
                    Boolean test = false;
                    JSONObject object = jsonArray.getJSONObject(i);

                    Mission mission = new Mission();
                    Vehicule vehicule = new Vehicule();
                    vehicule.setMarque(object.getString("marque"));
                    vehicule.setLogo(object.getString("logo"));
                    vehicule.setImmatricule(object.getString("immatriculation"));
                    vehicule.setModele(object.getString("conducteur_id"));

                    Client client = new Client();
                    client.setRaison_sociale(object.getString("raison_sociale"));


                    mission.setId(object.getInt("id"));
                    mission.setNombre_place(object.getInt("nombre_place"));
                    mission.setDate_debut(object.getString("date_debut"));
                    mission.setDate_fin(object.getString("date_fin"));
                    mission.setHeures_debut(object.getString("heure_debut"));
                    mission.setHeures_fin(object.getString("heure_fin"));
                    mission.setVehicule(vehicule);
                    mission.setClient(client);
                    mission.setDepart(object.getString("lieu_d_id"));
                    mission.setArrivee(object.getString("lieu_a_id"));


                    list.add(mission);

                }
                 return list;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    public List<Mission> getAllMissionRelation(String content) {
        try {
            List<Mission> list=null;
            JSONArray jsonArray = new JSONArray(content);
            list = new ArrayList<>();
            for (int i =0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                Mission mission = new Mission();
                Vehicule vehicule = new Vehicule();
                vehicule.setMarque(object.getString("marque"));
                vehicule.setLogo(object.getString("logo"));
                vehicule.setId(object.getInt("vehicule_id"));
                vehicule.setImmatricule(object.getString("immatriculation"));

                Membre membre = new Membre();
                membre.setNom(object.getString("nom"));
                membre.setPrenom(object.getString("prenom"));

                Client client = new Client();
                client.setRaison_sociale(object.getString("raison_sociale"));
                client.setEmail(object.getString("mail"));
                client.setSiret(object.getString("siret"));
                client.setAdresse1(object.getString("adresse1"));
                client.setCode_postal(object.getString("code_postal"));
                client.setVille(object.getString("ville"));


                mission.setId(object.getInt("id"));
                mission.setNombre_place(object.getInt("nombre_place"));
                mission.setDate_debut(object.getString("date_debut"));
                mission.setDate_fin(object.getString("date_fin"));
                mission.setHeures_debut(object.getString("heure_debut"));
                mission.setHeures_fin(object.getString("heure_fin"));
                mission.setVehicule(vehicule);
                mission.setMembre(membre);
                mission.setClient(client);

                list.add(mission);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<Mission> getAllMissionRelationM(String content) {
        try {
            List<Mission> list=null;
            JSONArray jsonArray = new JSONArray(content);
            list = new ArrayList<>();
            for (int i =0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                Mission mission = new Mission();
                Vehicule vehicule = new Vehicule();
                vehicule.setMarque(object.getString("marque"));
                vehicule.setLogo(object.getString("logo"));
                vehicule.setId(object.getInt("vehicule_id"));
                vehicule.setImmatricule(object.getString("immatriculation"));

                Membre membre = new Membre();
                membre.setNom("");
                membre.setPrenom("");

                Client client = new Client();
                client.setRaison_sociale(object.getString("raison_sociale"));
                client.setEmail(object.getString("mail"));
                client.setSiret(object.getString("siret"));
                client.setAdresse1(object.getString("adresse1"));
                client.setCode_postal(object.getString("code_postal"));
                client.setVille(object.getString("ville"));


                mission.setId(object.getInt("id"));
                mission.setNombre_place(object.getInt("nombre_place"));
                mission.setDate_debut(object.getString("date_debut"));
                mission.setDate_fin(object.getString("date_fin"));
                mission.setHeures_debut(object.getString("heure_debut"));
                mission.setHeures_fin(object.getString("heure_fin"));
                mission.setVehicule(vehicule);
                mission.setMembre(membre);
                mission.setClient(client);

                list.add(mission);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<Membre> testMission(String content) {
        try {
            List<Membre> list=null;
            JSONArray jsonArray = new JSONArray(content);
            list = new ArrayList<>();
            for (int i =0;i<jsonArray.length();i++){
                Membre membre = new Membre();
                JSONObject object = jsonArray.getJSONObject(i);
                if(!(object.getString("conducteur_id").equals("null"))){
                    membre.setId(Integer.parseInt(object.getString("conducteur_id")));
                }
                list.add(membre);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getMissionSingle(String content) {
        try {
            String data =null;
            JSONArray jsonArray = new JSONArray(content);
            for (int i =0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                data = object.getString("conducteur_id");

            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Lieu_depart> departJson(String content){
        try {
            List<Lieu_depart> list=null;
            JSONArray jsonArray = new JSONArray(content);
            list = new ArrayList<>();
            for (int i =0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                Lieu_depart depart = new Lieu_depart();
                depart.setAdresse(object.getString("adresse1"));
                depart.setCodePostal(object.getString("code_postal"));
                depart.setVille(object.getString("ville"));
                depart.setPays(object.getString("pays"));
                list.add(depart);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Lieu_arrivee> arriveesJson(String content){
        try {
            List<Lieu_arrivee> list=null;
            JSONArray jsonArray = new JSONArray(content);
            list = new ArrayList<>();
            for (int i =0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                Lieu_arrivee arrivee = new Lieu_arrivee();
                arrivee.setAdresse(object.getString("adresse1"));
                arrivee.setCodePostal(object.getString("code_postal"));
                arrivee.setVille(object.getString("ville"));
                arrivee.setPays(object.getString("pays"));
                list.add(arrivee);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Chat> getListChat(String content){
        List<Chat> mChat;
        try {
            mChat = new ArrayList<>();
            JSONObject obj = new JSONObject(content);

            // check for error
            if (obj.getBoolean("error") == false) {
                JSONArray commentsObj = obj.getJSONArray("messages");

                for (int i = 0; i < commentsObj.length(); i++) {
                    JSONObject commentObj = (JSONObject) commentsObj.get(i);

                    String commentId = commentObj.getString("message_id");
                    String commentText = commentObj.getString("message");
                    String createdAt = commentObj.getString("created_at");

                    JSONObject userObj = commentObj.getJSONObject("user");
                    String userId = userObj.getString("user_id");
                    String userName = userObj.getString("username");
                    Chat chat = new Chat();
                    Membre membre = new Membre();
                    membre.setId(Integer.parseInt(userId));
                    membre.setNom(userName);
                    membre.setImage(userObj.getString("image_send"));
                    membre.setTheme(userObj.getInt("theme_send"));
                    //membre.setImage(commentObj.getString("image_profil"));

                    chat.setId(Integer.parseInt(commentId));
                    chat.setMessage(commentText);
                    chat.setAttach(commentObj.getString("attach"));
                    chat.setCreatedAt(createdAt);
                    chat.setMembre(membre);
                    mChat.add(chat);

                }
            }
            return mChat;

        } catch (JSONException e) {
            //Log.e(TAG, "json parsing error: " + e.getMessage());
            return  null;
        }
    }

    public Chat getDataADDchat(String param){
        Chat chat = new Chat();
        try {
            JSONObject obj = new JSONObject(param);

            // check for error
            if (obj.getBoolean("error") == false) {
                JSONArray commentsObj = obj.getJSONArray("messages");

                for (int i = 0; i < commentsObj.length(); i++) {
                    JSONObject commentObj = (JSONObject) commentsObj.get(i);

                    String commentId = commentObj.getString("message_id");
                    String commentText = commentObj.getString("message");
                    String createdAt = commentObj.getString("created_at");

                    JSONObject userObj = commentObj.getJSONObject("user");
                    String userId = userObj.getString("user_id");
                    String userName = userObj.getString("username");

                    Membre membre = new Membre();
                    membre.setId(Integer.parseInt(userId));
                    membre.setNom(userName);
                    membre.setImage(userObj.getString("image_send"));
                    membre.setTheme(userObj.getInt("theme_send"));
                    //membre.setImage(commentObj.getString("image_profil"));

                    chat.setId(Integer.parseInt(commentId));
                    chat.setMessage(commentText);
                    chat.setCreatedAt(createdAt);
                    chat.setMembre(membre);

                }

            }
            return chat;

        } catch (JSONException e) {
            //Log.e(TAG, "json parsing error: " + e.getMessage());
                return null;
        }
    }

    public List<Chat> getListChatD(String content){
        try {
            List<Chat> list=new ArrayList<>();
            JSONObject obj = new JSONObject(content);
            if (obj.getBoolean("error") == false) {
                JSONArray commentsObj = obj.getJSONArray("messages");
                for (int i = 0; i < commentsObj.length(); i++) {
                    JSONObject userObj = (JSONObject) commentsObj.get(i);
                    int Id = userObj.getInt("id_message");
                    String messages = userObj.getString("messages");
                    String id_send = userObj.getString("id_send");
                    Integer id_receive = userObj.getInt("id_receive");

                    Integer online =userObj.getInt("online_receive");
                    String nom =userObj.getString("nom__receive");
                    String prenom =userObj.getString("prenom__receive");
                    Integer start = userObj.getInt("reg_time__receive");
                    Membre membre = new Membre();
                   /* if(id.equalsIgnoreCase(id_send) && id.equalsIgnoreCase(userObj.getString("id_receive"))){
                        membre.setId(id_receive);
                    }else{
                        membre.setId(Integer.parseInt(id_send));
                    }*/

                    membre.setId(id_receive);
                    membre.setNom(nom);
                    membre.setPrenom(prenom);
                    membre.setOnline(online);
                    membre.setTimeStart(start);
                    membre.setTheme(userObj.getInt("theme__receive"));

                    Chat message = new Chat();
                    message.setTmp(Integer.parseInt(id_send));
                    message.setId(Id);
                    message.setMessage(messages);
                    message.setMembre(membre);
                    list.add(message);
                }

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getAllStaff1(String content){
        try {
            List<String> list=null;
            JSONArray jsonArray = new JSONArray(content);
            list = new ArrayList<>();
            for (int i =0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                //Membre membre = new Membre();
                /*membre.setId(object.getInt("id"));*/
                //membre.setNom(object.getString("nom")+" "+object.getString("prenom"));
                /*membre.setPrenom(object.getString("prenom"));*/
                list.add(object.getString("nom")+" "+object.getString("prenom"));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Groupe> getGroupes(String content){
        List<Groupe> groupeList =new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(content);
            if (obj.getBoolean("error") == false) {
                JSONArray commentsObj = obj.getJSONArray("messages");
                for (int i =0;i<commentsObj.length();i++){
                    JSONObject object = commentsObj.getJSONObject(i);
                    Groupe groupe = new Groupe();
                    groupe.setAdmin(object.getInt("id_admin"));
                    groupe.setId(object.getInt("id"));
                    groupe.setNomGroupe(object.getString("nom"));
                    groupe.setImg(object.getString("image"));
                    groupe.setTmps(object.getString("membre"));
                    groupe.setTheme(object.getInt("theme"));
                    groupeList.add(groupe);
                }

            }
            return groupeList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<MessagesGroupes> getMessagesGroupes(String content){
        List<MessagesGroupes> messagesGroupesList =new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(content);

                JSONArray commentsObj = obj.getJSONArray("messages");
                for (int i =0;i<commentsObj.length();i++){
                    JSONObject object = commentsObj.getJSONObject(i);
                    MessagesGroupes messagesGroupes = new MessagesGroupes();

                    Membre membre = new Membre();
                    membre.setNom(object.getString("nom"));
                    membre.setPrenom(object.getString("prenom"));
                    membre.setOnline(object.getInt("online"));
                    membre.setTheme(object.getInt("theme"));
                    membre.setImage(object.getString("image_profil"));

                    Groupe groupe = new Groupe();
                    groupe.setId(object.getInt("idgroupes"));



                    messagesGroupes.setId(object.getInt("id"));
                    messagesGroupes.setMessages(object.getString("messages"));
                    messagesGroupes.setTimeAt(object.getInt("createAt"));
                    messagesGroupes.setMembre(membre);
                    messagesGroupes.setGroupe(groupe);

                    messagesGroupesList.add(messagesGroupes);
                }


            return messagesGroupesList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MessagesGroupes getMessageAdd(String param){
        try {
            JSONObject obj = new JSONObject(param);
            MessagesGroupes messagesGroupes = new MessagesGroupes();
            JSONObject jsonObject = new JSONObject(obj.getString("messages"));
            if(jsonObject.getBoolean("error") == false){
                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("message"));
                Membre membre = new Membre();
                membre.setNom(jsonObject1.getString("nom"));
                membre.setPrenom(jsonObject1.getString("prenom"));
                membre.setOnline(jsonObject1.getInt("online"));
                membre.setTheme(jsonObject1.getInt("theme"));
                membre.setImage(jsonObject1.getString("image_profil"));

                Groupe groupe = new Groupe();
                groupe.setId(jsonObject1.getInt("idgroupes"));
                messagesGroupes.setId(jsonObject1.getInt("id"));
                messagesGroupes.setMessages(jsonObject1.getString("messages"));
                messagesGroupes.setTimeAt(jsonObject1.getInt("createAt"));
                messagesGroupes.setMembre(membre);
                messagesGroupes.setGroupe(groupe);
            }

            return messagesGroupes;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getCount(String content){
        JSONObject obj = null;
        try {
            obj = new JSONObject(content);
            JSONObject jsonObject = new JSONObject(obj.getString("user"));
            if(jsonObject.getBoolean("error") == false){
                //Toast.makeText(Context,""+jsonObject.getString("nom"),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
