package soluces.com.pennontautocars.com.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.JSON.ParseJSONMembre;
import soluces.com.pennontautocars.com.Model.MessagesGroupes;
import soluces.com.pennontautocars.com.adapter.GroupsCommentAdapter;
import soluces.com.pennontautocars.com.view.SendCommentButton;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;


/**
 * Created by RAYA on 15/11/2016.
 */

public class ChatGroupesActivity extends AppCompatActivity  implements SendCommentButton.OnSendClickListener {


    RecyclerView rvComments;
    Toolbar toolbar;
    GroupsCommentAdapter groupsCommentAdapter;
    List<MessagesGroupes> messagesGroupesList;
    Integer id_group;
    private HttpManager httpManager;
    private ResquestPackage resquestPackage;
    SharedPreferences preferences;
    SendCommentButton btnSendComment;
    EditText etComment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatgroups_activity);
        rvComments = (RecyclerView) findViewById(R.id.rvComments__);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnSendComment = (SendCommentButton) findViewById(R.id.btnSendComment);
        etComment = (EditText) findViewById(R.id.etComment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle( getIntent().getExtras().getString("nomgrps"));

        id_group = getIntent().getExtras().getInt("idgrps");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        resquestPackage = new ResquestPackage();
        httpManager = new HttpManager(this);
        preferences =  PreferenceManager.getDefaultSharedPreferences(this);
        prepareData();
        btnSendComment.setOnSendClickListener(this);
    }


    private void prepareData() {
        if(preferences.getString("ipserver","")!=null){
            if(httpManager.isOnline()){
                String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"groupes_chat/ChatGroupes";
                RequestData(url);
            }else{
                Toast.makeText(getApplication(),"Network is't available",Toast.LENGTH_LONG).show();
            }
        }
    }
    private void RequestData(String uri) {
        resquestPackage.setMethod("GET");
        resquestPackage.setUri(uri);
        resquestPackage.setParam("idgrp",String.valueOf(id_group));
        asynchrone_data asynchroneData = new asynchrone_data();
        asynchroneData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,resquestPackage);
    }
    private class asynchrone_data extends  AsyncTask<ResquestPackage,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
            displayData(s);

        }

        @Override
        protected void onProgressUpdate(String... values) {

            displayData(values[0]);
        }
    }
    public void displayData(String param){
        if(param!=null){
            ParseJSONMembre parseJSONMembre = new ParseJSONMembre();
            messagesGroupesList = new ArrayList<>();
            messagesGroupesList = parseJSONMembre.getMessagesGroupes(param);
            groupsCommentAdapter = new GroupsCommentAdapter(getBaseContext(),messagesGroupesList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            rvComments.setLayoutManager(mLayoutManager);
            rvComments.setItemAnimator(new DefaultItemAnimator());
            rvComments.setAdapter(groupsCommentAdapter);
            if (groupsCommentAdapter.getItemCount() > 1) {
                rvComments.getLayoutManager().smoothScrollToPosition(rvComments, null, groupsCommentAdapter.getItemCount() - 1);
            }
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

     @Override
        public void onSendClickListener(View v) {
        if (validateComment()) {

            if(preferences.getString("ipserver","")!=null){
                if(httpManager.isOnline()){
                    String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"groupes_chat/addMessage";
                    getMessage(url);
                }else{
                    Toast.makeText(getApplicationContext(),"Network is't available",Toast.LENGTH_LONG).show();
                }
            }
            btnSendComment.setCurrentState(SendCommentButton.STATE_DONE);
        }
    }
    private boolean validateComment() {
        if (TextUtils.isEmpty(etComment.getText())) {
            btnSendComment.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_error));
            return false;
        }

        return true;
    }

    public void getMessage(String url){
        resquestPackage.setMethod("GET");
        resquestPackage.setUri(url);
        resquestPackage.setParam("idsend",preferences.getString("id_user",""));
        resquestPackage.setParam("idgrp",String.valueOf(id_group));
        resquestPackage.setParam("messages",etComment.getText().toString());
        asynchroneMessage asynchroneData = new asynchroneMessage();
        asynchroneData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,resquestPackage);
    }
    private class asynchroneMessage extends  AsyncTask<ResquestPackage,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
            displayMessages(s);

        }

        @Override
        protected void onProgressUpdate(String... values) {

            displayMessages(values[0]);
        }
    }

    public void displayMessages(String param){
        ParseJSONMembre parseJSONMembre = new ParseJSONMembre();
        messagesGroupesList.add(parseJSONMembre.getMessageAdd(param));
        groupsCommentAdapter.notifyDataSetChanged();
        rvComments.setAdapter(groupsCommentAdapter);
        if (groupsCommentAdapter.getItemCount() > 1) {
            rvComments.getLayoutManager().smoothScrollToPosition(rvComments, null, groupsCommentAdapter.getItemCount() - 1);
        }
        etComment.setText("");

        groupsCommentAdapter.setAnimationsLocked(false);
        groupsCommentAdapter.setDelayEnterAnimation(false);

    }
}
