package soluces.com.pennontautocars.com.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
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
import soluces.com.pennontautocars.com.activity.NewGroupActivity;
import soluces.com.pennontautocars.com.adapter.ClientAdapter;
import soluces.com.pennontautocars.com.adapter.GroupsAdapter;
import soluces.com.pennontautocars.com.adapter.MembrePlusAdapter;
import soluces.com.pennontautocars.com.itemdecorations.SpacesItemDecoration;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;

/**
 * Created by RAYA on 21/09/2016.
 */
public class GroupFragment extends Fragment {

    @Bind(R.id.rvFeed_group)
    RecyclerView mRecyclerView;
    private static Context mcontext;
    private HttpManager httpManager;
    private ResquestPackage resquestPackage;
    SharedPreferences preferences;
    public static GroupFragment newInstance() {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.groupfragment, container, false);


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),NewGroupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mcontext.startActivity(intent);
            }
        });
        DataRequest();
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        DataRequest();
    }

    public void DataRequest(){
        resquestPackage = new ResquestPackage();
        httpManager = new HttpManager(mcontext);
        preferences =  PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if(preferences.getString("ipserver","")!=null){
            if(httpManager.isOnline()){
                String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"groupes/ListesGroupes";
                RequestData(url);
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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

    Boolean test = true;
    public void displayData(String param){
        if(param!=null){
            ParseJSONMembre parseJSONMembre = null;
            mRecyclerView.setHasFixedSize(true);
            parseJSONMembre =new ParseJSONMembre();
            GroupsAdapter groupsAdapter = new GroupsAdapter(mcontext,parseJSONMembre.getGroupes(param));
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mcontext, 2);
            mRecyclerView.setLayoutManager(mLayoutManager);
            if(test){
                mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                test = false;
            }
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(groupsAdapter);

        }else{
            //Toast.makeText(getContext(),"Veuillez verifier votre ip serveur",Toast.LENGTH_LONG).show();
        }
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
