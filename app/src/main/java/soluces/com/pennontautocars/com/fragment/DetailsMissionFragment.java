package soluces.com.pennontautocars.com.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import soluces.com.pennontautocars.com.adapter.EncoursAdapter;
import soluces.com.pennontautocars.com.adapter.Mission_details_Adapter;
import soluces.com.pennontautocars.com.itemdecorations.SpacesItemDecoration;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;

/**
 * Created by RAYA on 29/09/2016.
 */
public class DetailsMissionFragment extends Fragment {

    private SpeedyQuickReturnRecyclerViewOnScrollListener mScrollListener;
    private HttpManager httpManager;
    private ResquestPackage resquestPackage;
    SharedPreferences preferences;
    private static final String id_search="id_search";
    private static final String id_membre="id_membre";
    @Bind(R.id.rv_mission_encours)
    RecyclerView mRecyclerView;

    public static DetailsMissionFragment newInstance(String text,String id_membre_){
        DetailsMissionFragment mFragment = new DetailsMissionFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(id_search, text);
        mBundle.putString(id_membre, id_membre_);
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
        resquestPackage = new ResquestPackage();
        httpManager = new HttpManager(getContext());
        preferences =  PreferenceManager.getDefaultSharedPreferences(getContext());

        //Log.d(this.getClass().getName(),""+getArguments().getString("id_search"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.details_fragment, container, false);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(preferences.getString("ipserver","")!=null){
            if(httpManager.isOnline()){
                String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"mission/getSearch";
                RequestData(url);
            }else{
                Toast.makeText(getContext(),"Network is't available",Toast.LENGTH_LONG).show();
            }
        }
    }
    private void RequestData(String uri) {
        resquestPackage.setMethod("GET");
        resquestPackage.setUri(uri);
        resquestPackage.setParam("id",getArguments().getString("id_search"));
        resquestPackage.setParam("idmembre",getArguments().getString("id_membre"));
        displayData(httpManager.getData(resquestPackage));
    }

    public void displayData(String param){
        if(param!=null){
            ParseJSONMembre parseJSONMembre = new ParseJSONMembre();
            mRecyclerView.setHasFixedSize(true);
            Mission_details_Adapter adapter;
            if(getArguments().getString("id_membre").equalsIgnoreCase("null")){
                //Log.d(this.getClass().getName(),"null");
               adapter= new Mission_details_Adapter(parseJSONMembre.getAllMissionRelationM(param));
            }else{
                adapter= new Mission_details_Adapter(parseJSONMembre.getAllMissionRelation(param));
            }
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
