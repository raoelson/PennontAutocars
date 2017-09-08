/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package soluces.com.pennontautocars.com.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.Model.Chat;
import soluces.com.pennontautocars.com.Model.Membre;
import soluces.com.pennontautocars.com.activity.ChatDiscussionActivity;
import soluces.com.pennontautocars.com.activity.MainActivity;
import soluces.com.pennontautocars.com.app.Config;

import static java.security.AccessController.getContext;


public class MyGcmPushReceiver extends GcmListenerService {

    SharedPreferences preferences;

    private static final String TAG = MyGcmPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;

    /**
     * Called when message is received.
     *
     * @param from   SenderID of the sender.
     * @param bundle Data bundle containing message data as key/value pairs.
     *               For Set of keys use data.keySet().
     */

    @Override
    public void onMessageReceived(String from, Bundle bundle) {

        preferences =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.d(TAG, "data: " + bundle);
        String title = bundle.getString("title");
        Boolean isBackground = Boolean.valueOf(bundle.getString("is_background"));
        String flag = bundle.getString("flag");
        String data = bundle.getString("data");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "title: " + title);
        Log.d(TAG, "isBackground: " + isBackground);
        Log.d(TAG, "flag: " + flag);
        Log.d(TAG, "data: " + data);
        if (flag == null)
            return;

        if(preferences.getString("id_user","") == null){
            // user is not logged in, skipping push notification
            Log.e(TAG, "user is not logged in, skipping push notification");
            return;
        }

        if (from.startsWith("/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        switch (Integer.parseInt(flag)) {
            case Config.PUSH_TYPE_CHATROOM:
                // push notification belongs to a chat room
                processChatRoomPush(title, isBackground, data);
                break;
            case Config.PUSH_TYPE_USER:
                // push notification is specific to user
                //processUserMessage(title, isBackground, data);
                break;
        }


        /*if(!NotificationUtils.isAppIsInBackground(getApplicationContext())){
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message",data);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            NotificationUtils notificationUtils = new NotificationUtils();
            notificationUtils.playNotificationSound();
        }else{
            Intent resultIntent = new Intent(getApplicationContext(),MainActivity.class);
            resultIntent.putExtra("message",data);
            showNotificationMessage(getApplicationContext(),title,data,"",resultIntent);
        }*/
    }

  /*  private void add(final Bundle data) {
        Intent intent = new Intent(this, ChatDiscussionActivity.class);
        intent.putExtra("data","message notif");
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setTicker(data.getString("body"))
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(data.getString("title"))
                .setContentText(data.getString("body"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
        pushNotification.putExtra("type", Config.PUSH_TYPE_CHATROOM);
        pushNotification.putExtra("message", data);
        //pushNotification.putExtra("chat_room_id", chatRoomId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
    }*/
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);

        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    private void processChatRoomPush(String title, boolean isBackground, String data) {
        if (!isBackground) {

            try {
                JSONObject datObj = new JSONObject(data);

                String chatRoomId = datObj.getString("chat_room_id");
                String chatSendId = datObj.getString("chat_send_id");
                /*String chatSendNom = datObj.getString("nom_send");
                String chatSendImg = datObj.getString("img_send");*/
                 Chat chat = new Chat();
                //JSONObject mObj = datObj.getJSONObject("messages");
               JSONArray commentsObj = datObj.getJSONArray("messages");
                for (int i = 0; i < commentsObj.length(); i++) {
                    JSONObject commentObj = (JSONObject) commentsObj.get(i);

                    String commentId = commentObj.getString("message_id");
                    String commentText = commentObj.getString("message");
                    String attach = commentObj.getString("attach");
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
                    chat.setAttach(attach);
                    chat.setCreatedAt(createdAt);
                    chat.setMembre(membre);

                }



               /* Chat message = new Chat();
                message.setMessage(mObj.getString("messages"));
                message.setId(Integer.parseInt(mObj.getString("id")));
                message.setCreatedAt(mObj.getString("created_at"));*/

                //JSONObject uObj = datObj.getJSONObject("user");

                // skip the message if the message belongs to same user as
                // the user would be having the same message when he was sending
                // but it might differs in your scenario
                /*if (!uObj.getString("id").equals(preferences.getString("id_user",""))) {
                    Log.e(TAG, "Skipping the push message as it belongs to same user");
                    return;
                }*/

                /*Membre user = new Membre();
                user.setId(Integer.parseInt(uObj.getString("id")));
                user.setEmail(uObj.getString("mail_pro"));
                user.setNom(uObj.getString("nom"));
                message.setMembre(user);*/

                // verifying whether the app is in background or foreground
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

                    // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("type", Config.PUSH_TYPE_CHATROOM);
                    pushNotification.putExtra("message", chat);
                    pushNotification.putExtra("chat_room_id", chatRoomId);
                    pushNotification.putExtra("chat_send_id", chatSendId);
                   /* pushNotification.putExtra("chat_send_nom", chatSendNom);
                    pushNotification.putExtra("chat_send_img", chatSendImg);*/
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                    // play notification sound
                    NotificationUtils notificationUtils = new NotificationUtils();
                    notificationUtils.playNotificationSound();
                } else {

                    // app is in background. show the message in notification try
                    Intent resultIntent = new Intent(getApplicationContext(), ChatDiscussionActivity.class);
                    //resultIntent.putExtra("chat_room_id", chatSendId);
                    resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    resultIntent.putExtra("id_receive",chatSendId);
                    resultIntent.putExtra("id_send",chatRoomId);
                    resultIntent.putExtra("nom_receive",chat.getMembre().getNom());
                    resultIntent.putExtra("online_receive",chat.getMembre().getOnline());
                    resultIntent.putExtra("time_receive",chat.getMembre().getTimeStart());
                    resultIntent.putExtra("actions", false);
                    resultIntent.putExtra("notification", true);
                    showNotificationMessage(getApplicationContext(), title, chat.getMembre().getNom() + " : " + chat.getMessage(), chat.getCreatedAt(), resultIntent);
                }

            } catch (JSONException e) {
                Log.e(TAG, "json parsing error: " + e.getMessage());
                Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
        }
    }

}
