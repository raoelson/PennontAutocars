package soluces.com.pennontautocars.com.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.app.Config;
import soluces.com.pennontautocars.com.app.MyApplication;
import soluces.com.pennontautocars.com.ws.HttpManager;


public class GcmIntentService extends IntentService {

    private static final String TAG = GcmIntentService.class.getSimpleName();

    public GcmIntentService() {
        super(TAG);
    }

    public static final String KEY = "key";
    public static final String TOPIC = "topic";
    public static final String SUBSCRIBE = "subscribe";
    public static final String UNSUBSCRIBE = "unsubscribe";

    private HttpManager httpManager;
    private String ipserveur = null;
    SharedPreferences preferences;
    static MyApplication myApplication ;


    @Override
    protected void onHandleIntent(Intent intent) {
        httpManager = new HttpManager(this);
        myApplication  = new MyApplication(getApplicationContext());
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String key = intent.getStringExtra(KEY);
        switch (key) {
            case SUBSCRIBE:
                // subscribe to a topic
                String topic = intent.getStringExtra(TOPIC);
                subscribeToTopic(topic);
                break;
            case UNSUBSCRIBE:
                break;
            default:
                // if key is specified, register with GCM
                registerGCM();
        }

    }

    /**
     * Registering with GCM and obtaining the gcm registration id
     */
    private void registerGCM() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = null;
        try {

            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.e(TAG, "GCM Registration Token: " + token);

            // sending the registration id to our server
            sendRegistrationToServer(token);

            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, true).apply();
        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);

            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {

        // checking for valid login session
        if (preferences.getString("id_user","") == null) {
            // TODO
            // user not found, redirecting him to login screen
            return;
        }


        if(preferences.getString("ipserver","")!=null){
            if(httpManager.isOnline()){
                String endPoint = httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"membre/updateGCMToken";
                Log.e(TAG, "endpoint: " + endPoint);
                StringRequest strReq = new StringRequest(Request.Method.POST,
                        endPoint, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "response: " + response);

                        try {
                            JSONObject obj = new JSONObject(response);

                            // check for error
                            if (obj.getBoolean("error") == false) {
                                // broadcasting token sent to server
                                Intent registrationComplete = new Intent(Config.SENT_TOKEN_TO_SERVER);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(registrationComplete);
                            } else {
                                Toast.makeText(getApplicationContext(), "Unable to send gcm registration id to our sever. " + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Log.e(TAG, "json parsing error: " + e.getMessage());
                            Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                        Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("gcm_registration_id", token);
                        params.put("id", preferences.getString("id_user",""));

                        Log.e(TAG, "params: " + params.toString());
                        return params;
                    }
                };

                //Adding request to request queue

                myApplication.addToRequestQueue(strReq);
            }else{
                Toast.makeText(getApplicationContext(),"Network is't available",Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Subscribe to a topic
     */
    public  void subscribeToTopic(String topic) {

        String token = null;
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            if (token != null) {
                pubSub.subscribe(token, "/topics/" + topic, null);
                Log.e(TAG, "Subscribed to topic: " + topic);
            } else {
                Log.e(TAG, "error: gcm registration id is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



       /*
        InstanceID instanceID = InstanceID.getInstance(this);
        String token = null;
        try {
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            if (token != null) {
                pubSub.subscribe(token, "/topics/" + topic, null);
                Log.e(TAG, "Subscribed to topic: " + topic);
            } else {
                Log.e(TAG, "error: gcm registration id is null");
            }
        } catch (IOException e) {
            Log.e(TAG, "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage());
            Toast.makeText(myApplication, "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }*/
    }

    public void unsubscribeFromTopic(String topic) {
        GcmPubSub pubSub = GcmPubSub.getInstance(getApplicationContext());
        InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
        String token = null;
        try {
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            if (token != null) {
                pubSub.unsubscribe(token, "");
                Log.e(TAG, "Unsubscribed from topic: " + topic);
            } else {
                Log.e(TAG, "error: gcm registration id is null");
            }
        } catch (IOException e) {
            Log.e(TAG, "Topic unsubscribe error. Topic: " + topic + ", error: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
