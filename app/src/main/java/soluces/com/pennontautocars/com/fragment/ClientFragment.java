package soluces.com.pennontautocars.com.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import library.enums.QuickReturnViewType;
import library.listeners.SpeedyQuickReturnRecyclerViewOnScrollListener;
import library.utils.QuickReturnUtils;
import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.JSON.ParseJSONMembre;
import soluces.com.pennontautocars.com.adapter.ClientAdapter;
import soluces.com.pennontautocars.com.adapter.MembrePlusAdapter;
import soluces.com.pennontautocars.com.itemdecorations.SpacesItemDecoration;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;

/**
 * Created by RAYA on 21/09/2016.
 */
public class ClientFragment extends Fragment {

    private SpeedyQuickReturnRecyclerViewOnScrollListener mScrollListener;
    @Bind(R.id.rv_clients)
    RecyclerView mRecyclerView;
    @Bind(R.id.networkerror)
    TextView networkerror;
    private static Context mcontext;
    private HttpManager httpManager;
    private ResquestPackage resquestPackage;
    SharedPreferences preferences;
    private boolean mSearchCheck;
    private SmoothProgressBar smoothProgressBar;
    private List<asynchrone_data> tasks;

    public static ClientFragment newInstance() {
        ClientFragment fragment = new ClientFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_clients, container, false);

        smoothProgressBar = (SmoothProgressBar) rootView.findViewById(R.id.smoothProgressBar);
        tasks = new ArrayList<>();
        resquestPackage = new ResquestPackage();
        httpManager = new HttpManager(mcontext);
        preferences =  PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(preferences.getString("ipserver","")!=null){
            if(httpManager.isOnline()){
                String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"client/listes_clients";
                RequestData(url,"");
            }else{
                Toast.makeText(getContext(),"Network is't available",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext = context;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

        //Select search item
        final MenuItem menuItem = menu.findItem(R.id.menu_search);
        menuItem.setVisible(true);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(this.getString(R.string.search));
        ((EditText) searchView.findViewById(R.id.search_src_text))
                .setHintTextColor(getResources().getColor(R.color.nliveo_white));
        searchView.setOnQueryTextListener(onQuerySearchView);

        /*menu.findItem(R.id.menu_add).setVisible(true);*/

        mSearchCheck = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {
            case R.id.menu_search:
                mSearchCheck = true;
        }
        return true;
    }


    private void removeListeners() {
        mRecyclerView.removeOnScrollListener(mScrollListener);
    }
    private SearchView.OnQueryTextListener onQuerySearchView = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            if (mSearchCheck){
                if(preferences.getString("ipserver","")!=null){
                    String url = null;
                    if(httpManager.isOnline()){
                        url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"client/listes_clients";
                        if(query.equals("")){
                            // RequestData(url,query);
                        }else{
                            RequestData(url,query);
                        }
                    }
                }else{
                    Toast.makeText(getContext(),"Network is't available",Toast.LENGTH_LONG).show();
                }
            }
            return false;
        }
    };
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeListeners();

        ButterKnife.unbind(this);
    }

    private void RequestData(String uri,String query) {
        resquestPackage.setMethod("GET");
        resquestPackage.setUri(uri);
        resquestPackage.setParam("id",query);
        asynchrone_data asynchroneData = new asynchrone_data();
        asynchroneData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,resquestPackage);
    }
    private class asynchrone_data extends  AsyncTask<ResquestPackage,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(tasks.size() == 0){
                smoothProgressBar.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
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
            tasks.remove(this);
            if(tasks.size() == 0){
                smoothProgressBar.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {

            displayData(values[0]);
        }
    }

    Boolean test = true;
    public void displayData(String param){
        if(param!=null){
            networkerror.setVisibility(View.GONE);
            ParseJSONMembre parseJSONMembre = null;
            mRecyclerView.setHasFixedSize(true);
            ClientAdapter adapter = new ClientAdapter(getContext(), parseJSONMembre.getAllClient(param));
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(adapter);
            if(test){
                mRecyclerView.addItemDecoration(new SpacesItemDecoration(QuickReturnUtils.dp2px(getActivity(), 8)));
                test = false;
            }
            ArrayList<View> headerViews = new ArrayList<>();
            headerViews.add(getActionBarView());

            ArrayList<View> footerViews = new ArrayList<>();

            mScrollListener = new SpeedyQuickReturnRecyclerViewOnScrollListener.Builder(getActivity(), QuickReturnViewType.GOOGLE_PLUS)
                    .footerViews(footerViews)
                    .slideHeaderUpAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_header_up))
                    .slideHeaderDownAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_header_down))
                    .slideFooterUpAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_footer_up))
                    .slideFooterDownAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_footer_down))
                    .build();

            mRecyclerView.addOnScrollListener(mScrollListener);
        }else{
            networkerror.setVisibility(View.VISIBLE);
            networkerror.setText("Veuillez réessayer svp");
            networkerror.setBackgroundColor(R.color.white);
            networkerror.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(preferences.getString("ipserver","")!=null){
                        if(httpManager.isOnline()){
                            String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"client/listes_clients";
                            RequestData(url,"");
                            networkerror.setVisibility(View.GONE);
                        }else{
                            Toast.makeText(getContext(),"Network is't available",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }
    private View getActionBarView() {
        Window window = getActivity().getWindow();
        View v = window.getDecorView();
        int resId = getResources().getIdentifier("action_bar_container", "id", "android");
        return v.findViewById(resId);
    }
}
