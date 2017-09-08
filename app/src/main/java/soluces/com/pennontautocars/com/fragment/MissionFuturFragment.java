package soluces.com.pennontautocars.com.fragment;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.enums.QuickReturnViewType;
import library.listeners.SpeedyQuickReturnRecyclerViewOnScrollListener;
import library.utils.QuickReturnUtils;
import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.JSON.ParseJSONMembre;
import soluces.com.pennontautocars.com.adapter.MissionAncienneAdapter;
import soluces.com.pennontautocars.com.adapter.MissionFuturAdapter;
import soluces.com.pennontautocars.com.itemdecorations.SpacesItemDecoration;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;

/**
 * Created by RAYA on 13/10/2016.
 */
public class MissionFuturFragment  extends Fragment {

    private SpeedyQuickReturnRecyclerViewOnScrollListener mScrollListener;
    @Bind(R.id.rv_futur)
    RecyclerView mRecyclerView;
    private Context mcontext;
    private HttpManager httpManager;
    private ResquestPackage resquestPackage;
    SharedPreferences preferences;
    TextView mVide;

    public static MissionFuturFragment newInstance(){

        MissionFuturFragment mFragment = new MissionFuturFragment();
        /*Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);*/
        return mFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.futur_fragment, container, false);

        resquestPackage = new ResquestPackage();
        httpManager = new HttpManager(mcontext);
        preferences =  PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mVide = (TextView) rootView.findViewById(R.id.text_vide_futur);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(preferences.getString("ipserver","")!=null){
            if(httpManager.isOnline()){
                String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"mission/listes_missionsFutur";
                RequestDatas(url);
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

    private void RequestDatas(String uri) {
        resquestPackage.setMethod("GET");
        resquestPackage.setUri(uri);
        displayDatas(httpManager.getData(resquestPackage));
    }
    public void displayDatas(String param){
        if(param!=null){

            ParseJSONMembre parseJSONMembre = new ParseJSONMembre();
            mRecyclerView.setHasFixedSize(true);
            MissionFuturAdapter adapter = new MissionFuturAdapter(getContext(), parseJSONMembre.getAllMission(param));
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layoutManager);
            if(parseJSONMembre.getAllMission(param).isEmpty()){
                mVide.setVisibility(View.VISIBLE);
                mVide.setText("Il n\'y a pas de mission future");
            }else{
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.invalidate();
            }

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
            /*Toast.makeText(getContext(),"Veuillez verifier votre ip serveur",Toast.LENGTH_LONG).show();*/
            mVide.setVisibility(View.VISIBLE);
            mVide.setText("Veuillez réessayer svp");
            mVide.setBackgroundColor(R.color.white);
            mVide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(preferences.getString("ipserver","")!=null){
                        if(httpManager.isOnline()){
                            String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"mission/listes_missionsFutur";
                            RequestDatas(url);
                            mVide.setVisibility(View.GONE);
                        }else{
                            Toast.makeText(getContext(),"Network is't available",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }
}
