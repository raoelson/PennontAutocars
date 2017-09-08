/*
 * Copyright 2015 Rudson Lima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soluces.com.pennontautocars.com.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import library.utils.QuickReturnUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.JSON.ParseJSONMembre;
import soluces.com.pennontautocars.com.Model.Chat;
import soluces.com.pennontautocars.com.Model.ItemAttach;
import soluces.com.pennontautocars.com.Model.Membre;
import soluces.com.pennontautocars.com.Model.ServerResponse;
import soluces.com.pennontautocars.com.adapter.AttachAdapter;
import soluces.com.pennontautocars.com.adapter.Chats_details_Adapter;
import soluces.com.pennontautocars.com.app.Config;
import soluces.com.pennontautocars.com.app.MyApplication;
import soluces.com.pennontautocars.com.gcm.NotificationUtils;
import soluces.com.pennontautocars.com.gcm.SinchService;
import soluces.com.pennontautocars.com.itemdecorations.SpacesItemDecoration;
import soluces.com.pennontautocars.com.video.BaseActivity;
import soluces.com.pennontautocars.com.video.CallScreenActivity;
import soluces.com.pennontautocars.com.ws.Connexion;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;


public class ChatDiscussionActivity extends BaseActivity {

    RecyclerView recyclerView;
    private HttpManager httpManager;
    private ResquestPackage resquestPackage;
    SharedPreferences preferences;
    List<Chat> messageArrayList;
    Chats_details_Adapter chatAdapter;
    Toolbar toolbar;
    Integer reponse_receive = 0,reponse_send = 0;
    ImageView chatMessageSend,btnPieceJointe;
    EditText chatMessageInput;
    AutoCompleteTextView autoCompleteTextView;
    TextInputLayout textInputLayout;
    private BroadcastReceiver broadcastReceiver;
    Boolean test = true;
    TextView networkerror;
    public String[] names;
    private TypedArray imgs;
    private List<ItemAttach> itemAttaches;
    File mediaStorageDir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_discussion);
        networkerror = (TextView) findViewById(R.id.networkerror);
        resquestPackage = new ResquestPackage();
        httpManager = new HttpManager(this);
        preferences =  PreferenceManager.getDefaultSharedPreferences(this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

         mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "autocarspennont");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(this.getClass().getName(), "Oops! Failed create  directory");
            }
        }

        chatMessageSend = (ImageView) findViewById(R.id.chatMessageSends_);
        btnPieceJointe = (ImageView) findViewById(R.id.btnPieceJointes);
        chatMessageInput  = (EditText) findViewById(R.id.chatMessageInputs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        autoCompleteTextView = (AutoCompleteTextView)
                toolbar.findViewById(R.id.input_contact_);
        textInputLayout = (TextInputLayout) toolbar.findViewById(R.id.TextInputLayout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        recyclerView = (RecyclerView) findViewById(R.id.rv_chatD_);
        messageArrayList = new ArrayList<>();
        //Log.d(this.getClass().getName(),"reponses "+getIntent().getExtras().getString("data"));

        if(getIntent().getExtras().getBoolean("actions") == false){

            reponse_receive = getIntent().getExtras().getInt("id_receive");
            reponse_send = getIntent().getExtras().getInt("id_send");


            actionBar.setTitle(getIntent().getExtras().getString("nom_receive"));
            //actionBar.setIcon(R.drawable.logo);
            String rep_;
            if(getIntent().getExtras().getInt("time_receive") == 0){
                rep_ = "Déconnecté";
            }else{
                 rep_ = "Déconnecté "+ Config.getTimeAgo(getIntent().getExtras().getInt("time_receive"));
            }

            if((getIntent().getExtras().getInt("online_receive"))==1){
                rep_ = "Connecté";
            }
            actionBar.setSubtitle(rep_);
            if(httpManager.isOnline()){
                String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"" +
                        "chat/Recuperation";
                RequestOuverture(url);

            }
        }else{
            textInputLayout.setVisibility(View.VISIBLE);
            actionBar.setTitle("");
            actionBar.setSubtitle("");

            if(preferences.getString("ipserver","")!=null){
                if(httpManager.isOnline()){
                    String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"membre/liste_membre";
                    RequestData(url,"");
                }else{
                    Toast.makeText(getBaseContext(),"Network is't available",Toast.LENGTH_LONG).show();
                }
            }

        }


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push message is received
                    handlePushNotification(intent);
                }
            }
        };

        chatMessageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateComment()) {

                    chatAdapter.setAnimationsLocked(false);
                    chatAdapter.setDelayEnterAnimation(false);
                    final String message = chatMessageInput.getText().toString();
                    if (preferences.getString("ipserver", "") != null) {
                        if (httpManager.isOnline()) {
                            String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"chat/AddMessages";
                            RequestDataMessage(url, message);
                        } else {
                            Toast.makeText(getApplicationContext(), "Network is't available", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        btnPieceJointe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachList();
                final ArrayAdapter<ItemAttach> adapter = new AttachAdapter(ChatDiscussionActivity.this, itemAttaches);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChatDiscussionActivity.this);
                dialogBuilder.setTitle("Ajouter de pièce jointe");
                dialogBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(adapter.getItem(item).getName().equalsIgnoreCase("Images")){
                            captureImage();
                        }
                    }
                });
                AlertDialog alertDialogObject = dialogBuilder.create();
                //Show the dialog
                alertDialogObject.show();

            }
        });
    }


    private boolean validateComment() {
        if (TextUtils.isEmpty(chatMessageInput.getText().toString())) {
            chatMessageSend.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_error));
            return false;
        }

        return true;
    }
    private void handlePushNotification(Intent intent) {

        Chat message = (Chat) intent.getSerializableExtra("message");
        String chatRoomId = intent.getStringExtra("chat_room_id");
        /*String chatSendId = intent.getStringExtra("chat_send_id");
        String chatSendNom = intent.getStringExtra("chat_send_nom");
        String chatSendImg = intent.getStringExtra("chat_send_img");*/
        if (message != null && chatRoomId != null) {

            messageArrayList.add(message);

            chatAdapter.notifyDataSetChanged();
            if (chatAdapter.getItemCount() > 1) {
                // scrolling to bottom of the recycler view
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, chatAdapter.getItemCount() - 1);
            }
        }
    }


    public void RequestOuverture(String url){
        resquestPackage.setMethod("GET");
        resquestPackage.setUri(url);
        if((getIntent().getExtras().getBoolean("notification") )){
            Log.d(this.getClass().getName()," reponses notification");
            resquestPackage.setParam("idreceive",
                    getIntent().getExtras().getString("id_receive"));
            resquestPackage.setParam("idsend",getIntent().getExtras().getString("id_send"));

        }else{
            resquestPackage.setParam("idreceive",
                    String.valueOf(reponse_receive));
            resquestPackage.setParam("idsend",String.valueOf(reponse_send));
        }
        asynchrone_dataOuverture asynchroneData = new asynchrone_dataOuverture();
        asynchroneData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,resquestPackage);

    }


    public void DisplayMessage(String param){
        networkerror.setVisibility(View.GONE);
        if(param!=null){
            ParseJSONMembre parseJSONMembre = new ParseJSONMembre();
            messageArrayList = parseJSONMembre.getListChat(param);
            chatAdapter = new Chats_details_Adapter(getBaseContext(),messageArrayList
            ,httpManager.URL_serveur()+""+preferences.getString("ipserver",""));
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getParent());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(chatAdapter);
            if(test){
                recyclerView.addItemDecoration(new SpacesItemDecoration(QuickReturnUtils.dp2px(this, 8)));
                test = false;
            }
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            if (chatAdapter.getItemCount() > 1) {
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, chatAdapter.getItemCount()-1);
            }
        }
        else{
            /*Toast.makeText(getApplicationContext(),"Veuillez vérifier votre ip serveur",Toast.LENGTH_LONG).show();*/
            networkerror.setVisibility(View.VISIBLE);
            networkerror.setText("Veuillez réessayer svp");
            networkerror.setBackgroundColor(R.color.white);
            networkerror.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(preferences.getString("ipserver","")!=null){
                        if(httpManager.isOnline()){
                            String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"" +
                                    "chat/Recuperation";
                            RequestOuverture(url);
                            networkerror.setVisibility(View.GONE);
                        }else{
                            Toast.makeText(getApplicationContext(),"Network is't available",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils notificationUtils = new NotificationUtils(this);
        notificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //handler.removeCallbacks(null);
                onBackPressed();
                this.finish();
                return true;
            case R.id.menu_appel:
                callDemarrage();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
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
    private void RequestDataMessage(String uri,String query) {
        int longeur = query.length();
        String message = "";
        for(int i=0;i<longeur ; i+=22){
            int z = 22;
            if(query.length()<z){
                z=query.length();
            }
            message=message+query.substring(0,z)+"\n";
            query=query.substring(z);
        }
        resquestPackage.setMethod("GET");
        resquestPackage.setUri(uri);
        resquestPackage.setParam("id",query);
        if(getIntent().getExtras().getBoolean("actions") == true){

            resquestPackage.setParam("send",preferences.getString("id_user",""));
            String data_auto = autoCompleteTextView.getText().toString();
            final String[] split_ = data_auto.split("\\s+");
            resquestPackage.setParam("nomreceive",split_[0]);
            resquestPackage.setParam("prenomreceive",split_[1]);
            resquestPackage.setParam("actions","nouveau");
        }else{

            resquestPackage.setParam("send",String.valueOf(reponse_send));
            resquestPackage.setParam("receive",String.valueOf(reponse_receive));
            resquestPackage.setParam("actions","discussion");
        }
        resquestPackage.setParam("messages",message);
        resquestPackage.setParam("attach","");
        //Log.d("reponses"," id "+query +" send "+preferences.getString("id_user","")+" receive"+reponse_receive);
        asynchrone_data asynchroneData = new asynchrone_data();
        asynchroneData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,resquestPackage);
    }



    private class asynchrone_data extends AsyncTask<ResquestPackage,String,String> {
        final ProgressDialog progressDialog = new ProgressDialog(ChatDiscussionActivity.this,
                R.style.AppTheme_Dark_Dialog);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Envoi d'un message...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(ResquestPackage... strings) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return(httpManager.getData(strings[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            displayDataMessage(s);
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {

            displayDataMessage(values[0]);
        }
    }
    public void displayDataMessage(String param){
        if(param!=null){
            ParseJSONMembre parseJSONMembre = null;
            parseJSONMembre = new ParseJSONMembre();
            messageArrayList.add(parseJSONMembre.getDataADDchat(param));

            chatAdapter.notifyDataSetChanged();
            if (chatAdapter.getItemCount() > 1) {
                // scrolling to bottom of the recycler view
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, chatAdapter.getItemCount() - 1);
            }
            chatMessageInput.setText("");
        }else{
            //Toast.makeText(getBaseContext(),"Veuillez verifier votre ip serveur",Toast.LENGTH_LONG).show();
        }
    }


    private class asynchrone_dataOuverture extends AsyncTask<ResquestPackage,String,String> {
        final ProgressDialog progressDialog = new ProgressDialog(ChatDiscussionActivity.this,
                R.style.AppTheme_Dark_Dialog);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Ouverture d'une conversation...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(ResquestPackage... strings) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return(httpManager.getData(strings[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            DisplayMessage(s);
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {

            DisplayMessage(values[0]);
        }
    }

    private void attachList() {
        itemAttaches = new ArrayList<ItemAttach>();
        names = getResources().getStringArray(R.array.actions_names);
        imgs = getResources().obtainTypedArray(R.array.pic_flags);
        for(int i = 0; i < names.length; i++){
            itemAttaches.add(new ItemAttach(names[i],imgs.getDrawable(i)));
        }
    }


    private void captureImage() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();



            try {
                File file = new File(getPath(selectedImageUri));

                if (mediaStorageDir.canWrite()) {

                    String sourceImagePath= getPath(selectedImageUri);
                    String destinationImagePath= mediaStorageDir+"/"+file.getName();
                    File source= new File(sourceImagePath);
                    File destination= new File(destinationImagePath);
                    if (source.exists()) {
                        FileChannel src = new FileInputStream(source).getChannel();
                        FileChannel dst = new FileOutputStream(destination).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                    }
                }
                Connexion connexion ;
                if(preferences.getString("ipserver","")!=null){
                    if(httpManager.isOnline()){
                        connexion = new Connexion(httpManager.URL_serveur()+""+preferences.getString("ipserver",""));
                        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                        RequestBody sends = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(reponse_send));
                        RequestBody receive = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(reponse_receive));
                        RequestBody messages = RequestBody.create(MediaType.parse("text/plain"),"Objet");
                        Call<ServerResponse> mService = connexion.getInterfaceService().getSendFile(fileToUpload,filename,
                                sends,receive,messages);
                        mService.enqueue(new Callback<ServerResponse>() {
                            @Override
                            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                                ServerResponse serverResponse = response.body();
                                if(serverResponse.isError() == false){
                                    Chat chat1 = new Chat();
                                    //Integer userId =serverResponse.getChat_room().getChat_room_id();
                                    String userName = serverResponse.getChat_room().getName();
                                    Membre membre = new Membre();

                                    for(Chat.Chat_ chat__ : serverResponse.getMessages()){
                                        membre.setId(chat__.getUser_id());
                                        membre.setNom(chat__.getUser().getUsername());
                                        membre.setTheme(chat__.getUser().getTheme_send());
                                        membre.setImage("null");
                                        chat1.setId(chat__.getMessage_id());
                                        chat1.setMessage(chat__.getMessage());
                                        chat1.setCreatedAt(chat__.getCreated_at());
                                        chat1.setAttach(chat__.getAttach());
                                        chat1.setMembre(membre);

                                    }
                                    messageArrayList.add(chat1);
                                    chatAdapter.notifyDataSetChanged();
                                    if (chatAdapter.getItemCount() > 1) {
                                        // scrolling to bottom of the recycler view
                                        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, chatAdapter.getItemCount() - 1);
                                    }


                                }else{
                                    Log.d(this.getClass().getName(),"reponses true");
                                }
                           /* for(Chat chat : serverResponse.getMessages()){
                                Log.d(this.getClass().getName(),"reponses"+chat.getUser().getUsername());
                            }*/
                            }

                            @Override
                            public void onFailure(Call<ServerResponse> call, Throwable t) {
                                Log.d(this.getClass().getName(),"errrroooorrr");
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(),"Network is't available",Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {}

            //Log.d(this.getClass().getName(),"reponses"+);
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuchat, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onServiceConnected() {
        Log.d(this.getClass().getName(),"depart----------------------"+getIntent().getExtras().getString("nom_receive")+"____"
                + getSinchServiceInterface().getUserName());
    }

    public void callDemarrage(){
        com.sinch.android.rtc.calling.Call call = getSinchServiceInterface().
                callUserVideo(getIntent().getExtras().getString("nom_receive"));
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
    }


}
