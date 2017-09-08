package soluces.com.pennontautocars.com.activity;

import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.JSON.ParseJSONMembre;


import soluces.com.pennontautocars.com.app.MyApplication;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;


public class NewDiscussion extends AppCompatActivity {
    private HttpManager httpManager;
    private ResquestPackage resquestPackage;
    SharedPreferences preferences;
    AutoCompleteTextView autoCompleteTextView;
    EditText message;
    ImageView button_send;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resquestPackage = new ResquestPackage();
        httpManager = new HttpManager(getBaseContext());
        preferences =  PreferenceManager.getDefaultSharedPreferences(this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_new_discussion);
        autoCompleteTextView = (AutoCompleteTextView)
                findViewById(R.id.input_contact);
        toolbar = (Toolbar) findViewById(R.id.toolbar_send);
        /*message = (EditText) findViewById(R.id.message_send);
        button_send = (ImageView) findViewById(R.id.bt_send);*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Nouveau message");
        if(preferences.getString("ipserver","")!=null){
            if(httpManager.isOnline()){
                String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"membre/liste_membre";
                RequestData(url,"");
            }else{
                Toast.makeText(getBaseContext(),"Network is't available",Toast.LENGTH_LONG).show();
            }
        }

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!autoCompleteTextView.getText().toString().equalsIgnoreCase("") &&
                        !message.getText().toString().equalsIgnoreCase("")){
                    String data_auto = autoCompleteTextView.getText().toString();
                    final String[] split_ = data_auto.split("\\s+");

                    String p = (httpManager.URL_serveur()+""+preferences.getString("ipserver","")+
                            "chat/AddMessages");
                    StringRequest strReq = new StringRequest(Request.Method.POST,
                            p, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d(this.getClass().getName(), "response: " + response);

                            try {
                                JSONObject obj = new JSONObject(response);
                                if(obj.getBoolean("error") == false){
                                    JSONObject commentObj = obj.getJSONObject("message");
                                    String commentId = commentObj.getString("id");
                                    String id_receive = commentObj.getString("id_receive");
                                    String messages = commentObj.getString("messages");
                                    String created_at = commentObj.getString("created_at");

                                    JSONObject userObj = obj.getJSONObject("user");
                                    String userId = userObj.getString("id");
                                    String userName = userObj.getString("nom");

                                    message.setText("");
                                }
                            } catch (Exception e) {
                                //Log.e(TAG, "json parsing error: " + e.getMessage());
                                Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse networkResponse = error.networkResponse;
                            //Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                            Toast.makeText(getApplicationContext(),"Veuillez vérifier votre ip serveur",Toast.LENGTH_LONG).show();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("send", preferences.getString("id_user",""));
                            params.put("nomreceive", split_[0]);
                            params.put("prenomreceive", split_[1]);
                            params.put("actions", "nouveau");
                            params.put("messages", message.getText().toString().trim());
                            //Log.e(TAG, "params: " + params.toString());
                            return params;
                        }
                    };

                    //Adding request to request queue
                    MyApplication myApplication  = new MyApplication(getApplicationContext());
                    myApplication.addToRequestQueue(strReq);
                }
            }
        });


    }

    private void RequestData(String uri,String request) {
        resquestPackage.setMethod("GET");
        resquestPackage.setUri(uri);
        resquestPackage.setParam("id",request);
        displayData(httpManager.getData(resquestPackage));
    }
    public void displayData(String param){
        if(param!=null){
            ParseJSONMembre parseJSONMembre = null;
            parseJSONMembre = new ParseJSONMembre();
            String[] array = new String[parseJSONMembre.getAllStaff1(param).size()];
            array = parseJSONMembre.getAllStaff1(param).toArray(array);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,
                    array);
            autoCompleteTextView.setThreshold(1);
            autoCompleteTextView.setAdapter(adapter);
        }else{
            Toast.makeText(getApplication(),"Veuillez verifier votre ip serveur",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
