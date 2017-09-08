package soluces.com.pennontautocars.com.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


import java.util.ArrayList;

import java.util.List;

import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.JSON.ParseJSONMembre;
import soluces.com.pennontautocars.com.Model.Chat;
import soluces.com.pennontautocars.com.activity.ChatDiscussionActivity;
import soluces.com.pennontautocars.com.adapter.DiscussionAdapter;
import soluces.com.pennontautocars.com.app.Config;
import soluces.com.pennontautocars.com.gcm.GcmIntentService;
import soluces.com.pennontautocars.com.gcm.NotificationUtils;
import soluces.com.pennontautocars.com.interfaces.RecyclerTouchListener;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;

/**
 * Created by RAYA on 08/10/2016.
 */
public class DiscussionsFragment extends Fragment{


    BroadcastReceiver broadcastReceiver = null;
    private RecyclerView mList;
    private DiscussionAdapter discussionAdapter;
    private List<Chat> chatList;
    private HttpManager httpManager;
    private ResquestPackage resquestPackage;
    SharedPreferences preferences;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView networkerror;

    public static DiscussionsFragment newInstance(){
        DiscussionsFragment mFragment = new DiscussionsFragment();
        /*Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);*/
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        httpManager = new HttpManager(getContext());
        preferences =  PreferenceManager.getDefaultSharedPreferences(getActivity());
        resquestPackage = new ResquestPackage();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.discussionfragment, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swifeRefresh);
        networkerror  = (TextView) rootView.findViewById(R.id.networkerror);
        //mList = (ListView) rootView.findViewById(R.id.homeChatsList);
        mList = (RecyclerView) rootView.findViewById(R.id.homeChatsList);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChatDiscussionActivity.class);
                intent.putExtra("actions", true);
                getContext().startActivity(intent);
            }
        });

        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestDATA();
            }
        });
    }

    public void RequestDATA(){
        if(preferences.getString("ipserver","")!=null){
            if(httpManager.isOnline()){
                String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"chat/ListesChatRoom";
                RequestData(url);
            }else{
                Toast.makeText(getContext(),"Network is't available",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Config.REGISTRATION_COMPLETE)){
                   // subscribeToGlobalTopic();
                    //Toast.makeText(getContext(),""+token,Toast.LENGTH_LONG).show();
                }else if(intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)){
                    Toast.makeText(getContext(),"GCM registration",Toast.LENGTH_LONG).show();
                }else if(intent.getAction().equals(Config.PUSH_NOTIFICATION)){
                    HandlePushnotification(intent);
                }
            }
        };

    }


    @Override
    public void onResume() {
        RequestDATA();
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils notificationUtils = new NotificationUtils(getContext());
        notificationUtils.clearNotifications();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    private void RequestData(String uri) {
        resquestPackage.setMethod("GET");
        resquestPackage.setUri(uri);
        resquestPackage.setParam("id",preferences.getString("id_user",""));
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
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return(httpManager.getData(strings[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            //mList.onRefreshComplete();
            displayData(s);
        }
    }

    public void displayData(final String param){
        if(param!=null){
            networkerror.setVisibility(View.GONE);
            chatList = new ArrayList<>();
            ParseJSONMembre parseJSONMembre = new ParseJSONMembre();
            chatList = parseJSONMembre.getListChatD(param);

            discussionAdapter = new DiscussionAdapter(getContext(),chatList,
                    Integer.parseInt(preferences.getString("id_user","")));
            mList.setAdapter(discussionAdapter);
            mList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mSwipeRefreshLayout.setRefreshing(false);
            mList.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mList, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {

                    Chat chat =chatList.get(position);
                    int send = 0;
                    int receive = 0;
                    if(preferences.getString("id_user","").equalsIgnoreCase(String.
                            valueOf(chat.getMembre().getId()))){
                        send = chat.getMembre().getId();
                        receive = chat.getTmp();
                    }else{
                        send = chat.getTmp();
                        receive = chat.getMembre().getId();
                    }
                    Intent intent = new Intent(getContext(), ChatDiscussionActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("id_receive",receive);
                    intent.putExtra("id_send",send);
                    intent.putExtra("nom_receive",chat.getMembre().getNom());
                    intent.putExtra("online_receive",chat.getMembre().getOnline());
                    intent.putExtra("time_receive",chat.getMembre().getTimeStart());
                    intent.putExtra("actions", false);
                    getContext().startActivity(intent);
                }


            }));
            subscribeToAllTopics();

        }else{
            networkerror.setVisibility(View.VISIBLE);
            networkerror.setText("Veuillez réessayer svp");
            networkerror.setBackgroundColor(R.color.white);
            networkerror.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(preferences.getString("ipserver","")!=null){
                        if(httpManager.isOnline()){
                            String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"chat/ListesChatRoom";
                            RequestData(url);
                            networkerror.setVisibility(View.GONE);
                        }else{
                            Toast.makeText(getContext(),"Network is't available",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }



    private void HandlePushnotification(Intent intent){
        int type = intent.getIntExtra("type",-1);
        if(type == Config.PUSH_TYPE_CHATROOM){
            Chat message = (Chat) intent.getSerializableExtra("message");
            String chatRoomId = intent.getStringExtra("chat_room_id");
            if (message != null && chatRoomId != null) {
                updateRow(chatRoomId, message);
            }
        }
    }

    private void updateRow(String chatRoomId, Chat message) {
        for (Chat cr : chatList) {
            if (cr.getId() == Integer.parseInt(chatRoomId)) {
                int index = chatList.indexOf(cr);
                cr.setMessage(message.getMessage());
                //cr.setUnreadCount(cr.getUnreadCount() + 1);
                chatList.remove(index);
                chatList.add(index, cr);
                break;
            }
        }
        discussionAdapter.notifyDataSetChanged();
    }

    private void subscribeToGlobalTopic() {
        Intent intent = new Intent(getContext(), GcmIntentService.class);
        intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
        intent.putExtra(GcmIntentService.TOPIC, Config.TOPIC_GLOBAL);
        getContext().startService(intent);
    }

    // Subscribing to all chat room topics
    // each topic name starts with `topic_` followed by the ID of the chat room
    // Ex: topic_1, topic_2
    private void subscribeToAllTopics() {
        for (Chat cr : chatList) {

            Intent intent = new Intent(getContext(), GcmIntentService.class);
            intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
            intent.putExtra(GcmIntentService.TOPIC, "topic_" + cr.getMembre().getId());
            getContext().startService(intent);
        }
    }
}
