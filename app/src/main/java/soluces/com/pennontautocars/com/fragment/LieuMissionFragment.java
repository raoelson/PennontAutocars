package soluces.com.pennontautocars.com.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.enums.QuickReturnViewType;
import library.listeners.SpeedyQuickReturnRecyclerViewOnScrollListener;
import library.utils.QuickReturnUtils;
import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.JSON.ParseJSONMembre;
import soluces.com.pennontautocars.com.adapter.Lieu_details_Adapter;
import soluces.com.pennontautocars.com.adapter.Mission_details_Adapter;
import soluces.com.pennontautocars.com.itemdecorations.SpacesItemDecoration;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;

/**
 * Created by RAYA on 29/09/2016.
 */
public class LieuMissionFragment extends Fragment {
    private static final String id_depart="id_depart";
    private static final String id_arrive="id_arrive";
    private SpeedyQuickReturnRecyclerViewOnScrollListener mScrollListener;
    private HttpManager httpManager;
    private ResquestPackage resquestPackage,resquestPackage1;
    SharedPreferences preferences;
    @Bind(R.id.rv_mission_lieu)
    RecyclerView mRecyclerView;

    public static LieuMissionFragment newInstance(String depart,String arrivee){
        LieuMissionFragment mFragment = new LieuMissionFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(id_depart, depart);
        mBundle.putString(id_arrive, arrivee);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        httpManager = new HttpManager(getContext());
        preferences =  PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragments
        View rootView = inflater.inflate(R.layout.details_lieu_fragment, container, false);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(preferences.getString("ipserver","")!=null){
            if(httpManager.isOnline()){
                String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"lieu_depart/search_depart";
                String url_ =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"lieu_arrive/search_arrive";
                RequestData(url,url_);
            }else{
                Toast.makeText(getContext(),"Network is't available",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void RequestData(String uri,String url) {
        resquestPackage = new ResquestPackage();
        resquestPackage1 = new ResquestPackage();
        if(!uri.equals("")){
            resquestPackage.setMethod("GET");
            resquestPackage.setUri(uri);
            resquestPackage.setParam("id",getArguments().getString("id_depart"));
        }
        if(!url.equals("")){
            resquestPackage1.setMethod("GET");
            resquestPackage1.setUri(url);
            resquestPackage1.setParam("id",getArguments().getString("id_arrive"));
        }

        displayData(httpManager.getData(resquestPackage)
                ,httpManager.getData(resquestPackage1));
    }
    public void displayData(String param,String p1){
        /*Log.d(this.getClass().getName(),"reponses: "+param+" -p1--"+
                p1);*/
        if(param!=null && p1!=null){

            ParseJSONMembre parseJSONMembre = new ParseJSONMembre();
            mRecyclerView.setHasFixedSize(true);
            Lieu_details_Adapter adapter;
            adapter = new Lieu_details_Adapter(parseJSONMembre.departJson(param),
                    parseJSONMembre.arriveesJson(p1));
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.addItemDecoration(new SpacesItemDecoration(QuickReturnUtils.dp2px(getActivity(), 8)));
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
            Toast.makeText(getContext(),"Veuillez verifier votre ip serveur",Toast.LENGTH_LONG).show();
        }
    }

    private void removeListeners() {
        mRecyclerView.removeOnScrollListener(mScrollListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeListeners();

        ButterKnife.unbind(this);
    }
    private View getActionBarView() {
        Window window = getActivity().getWindow();
        View v = window.getDecorView();
        int resId = getResources().getIdentifier("action_bar_container", "id", "android");
        return v.findViewById(resId);
    }
}
