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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import soluces.com.library.liveo.Model.HelpLiveo;
import soluces.com.library.liveo.interfaces.OnItemClickListener;
import soluces.com.library.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import soluces.com.library.liveo.navigationliveo.NavigationLiveo;
import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.app.MyApplication;
import soluces.com.pennontautocars.com.fragment.AproposFragment;
import soluces.com.pennontautocars.com.fragment.ClientFragment;
import soluces.com.pennontautocars.com.fragment.DiscussionsFragment;
import soluces.com.pennontautocars.com.fragment.GridMenuFragment;
import soluces.com.pennontautocars.com.fragment.MainFragment;
import soluces.com.pennontautocars.com.fragment.MembreFragment;
import soluces.com.pennontautocars.com.fragment.ViewPagerFragment;
import soluces.com.pennontautocars.com.ws.HttpManager;


public class MainActivity extends NavigationLiveo implements OnItemClickListener {

    private HelpLiveo mHelpLiveo;

    @Override
    public void onInt(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.addSubHeader(preferences.getString("nom_user",""),getString(R.string.icon_user_check));
        mHelpLiveo.add(getString(R.string.important),getString(R.string.icon_dashbord));
        mHelpLiveo.addSeparator();
        mHelpLiveo.addSubHeader(getString(R.string.categories), getString(R.string.icon_user)); //Item subHeader
       /* mHelpLiveo.addSeparator();*/
        mHelpLiveo.add(getString(R.string.employee), getString(R.string.icon_directions));
       /* mHelpLiveo.add(getString(R.string.client), getString(R.string.icon_directions));*/
        mHelpLiveo.addSeparator(); //Item separator
        mHelpLiveo.add(getString(R.string.missions), getString(R.string.icon_missions));
        mHelpLiveo.addSeparator();
        mHelpLiveo.add(getString(R.string.messages),getString(R.string.icon_messages));
       /* mHelpLiveo.add(getString(R.string.discussion), getString(R.string.icon_directions));
        mHelpLiveo.add(getString(R.string.groupe), getString(R.string.icon_directions));*/
        mHelpLiveo.addSeparator();
        mHelpLiveo.add(getString(R.string.action_propos),getString(R.string.icon_propos));
        with(this).startingPosition(1)
                .addAllHelpItem(mHelpLiveo.getHelp())
                .colorItemSelected(R.color.nliveo_blue_colorPrimary)

                //.removeFooter()
                .footerItem(R.string.logout, getString(R.string.icon_users))

                .setOnPrepareOptionsMenu(onPrepare)
                .setOnClickFooter(onClickFooter)
                .build();
        int position = this.getCurrentPosition();
        this.setElevationToolBar(position != 1 ? 15 : 0);
    }

    @Override
    public void onItemClick(int position) {
        Fragment mFragment;
        FragmentManager mFragmentManager = getSupportFragmentManager();
        switch (position){

            case 1:
                mFragment = new GridMenuFragment();
                break;
            case 4:
                mFragment = new MembreFragment();
                break;
            case 5:

                mFragment = new ClientFragment();
                break;
            case 6:
                mFragment = new ViewPagerFragment().newInstance("");
                break;
            case 8:
                mFragment = new  DiscussionsFragment();
                break;
            case 10:
                mFragment = new AproposFragment();

                break;
            default:
                mFragment = MainFragment.newInstance(mHelpLiveo.get(position).getName());
                //mFragment = new DashboardFragment();
                break;
        }

        if (mFragment != null){
            mFragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
        }

        setElevationToolBar(position != 1 ? 15 : 0);
    }

    private OnPrepareOptionsMenuLiveo onPrepare = new OnPrepareOptionsMenuLiveo() {
        @Override
        public void onPrepareOptionsMenu(Menu menu, int position, boolean visible) {
        }
    };

    private View.OnClickListener onClickPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "onClickPhoto :D", Toast.LENGTH_SHORT).show();
            closeDrawer();
        }
    };
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private View.OnClickListener onClickFooter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            HttpManager httpManager;
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor = preferences.edit();
            String id_user = preferences.getString("id_user","");
            httpManager = new HttpManager(getBaseContext());

            if(preferences.getString("ipserver","")!=null){
                if(httpManager.isOnline()){
                    doInBackground(httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"membre/logout",id_user);
                }else{
                    Toast.makeText(getApplicationContext(),"Network is't available",Toast.LENGTH_LONG).show();
                }
            }
            closeDrawer();
        }
    };

    protected void doInBackground(String p,final String id) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                p, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        editor.remove("login");
                        editor.remove("password");
                        editor.apply();
                        editor.commit();
                        startActivity(new Intent(getApplicationContext(), soluces.com.pennontautocars.MainActivity.class));
                        finish();
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
                Toast.makeText(getApplicationContext(),"Veuillez vérifier votre ip serveur",Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        //Adding request to request queue
        MyApplication myApplication  = new MyApplication(getApplicationContext());
        myApplication.addToRequestQueue(strReq);
    }

}

