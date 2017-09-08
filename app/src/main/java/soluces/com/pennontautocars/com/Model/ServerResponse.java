package soluces.com.pennontautocars.com.Model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RAYA on 09/02/2017.
 */

public class ServerResponse {
    public boolean error;
    public List<Chat.Chat_> messages = new ArrayList<>();
    public Room chat_room = new Room();

    public Room getChat_room() {
        return chat_room;
    }

    public void setChat_room(Room chat_room) {
        this.chat_room = chat_room;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Chat.Chat_> getMessages() {
        return messages;
    }

    public void setMessages(List<Chat.Chat_> messages) {
        this.messages = messages;
    }

   /* public JSONObject getChat_room() {
        return chat_room;
    }

    public void setChat_room(JSONObject chat_room) {
        this.chat_room = chat_room;
    }*/

    public class Room{
        private Integer chat_room_id;
        private String name;

        public Integer getChat_room_id() {
            return chat_room_id;
        }

        public void setChat_room_id(Integer chat_room_id) {
            this.chat_room_id = chat_room_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
