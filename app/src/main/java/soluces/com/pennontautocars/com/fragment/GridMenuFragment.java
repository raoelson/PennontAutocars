package soluces.com.pennontautocars.com.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;

/**
 * Created by RAYA on 08/12/2016.
 */

public class GridMenuFragment extends Fragment{

    private BottomNavigationView bottomNavigationView;

    private Context context;

    private  Fragment fragment;

    private PieChart mChart,missionChat;
    private HttpManager httpManager;
    private ResquestPackage resquestPackage;
    SharedPreferences preferences;
   // private float[] yValues = {21, 2, 2};
    private float[] yValues ;
    //private String[] xValues = {"En cours", "Anciennes", "Futures"};
    private String[] xValues ;

    // colors for different sections in pieChart
    public static  final int[] MY_COLORS = {
            Color.rgb(64, 89, 128), Color.rgb(84,124,101), Color.rgb(193, 37, 82),
            Color.rgb(38,40,53), Color.rgb(215,60,55)
           // /
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.essai_fragment, container, false);


        bottomNavigationView = (BottomNavigationView) rootView.findViewById(R.id.bottomNavigation);
        mChart = (PieChart) rootView.findViewById(R.id.chart1);

        resquestPackage = new ResquestPackage();
        httpManager = new HttpManager(getContext());
        preferences =  PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        int[] image = {R.drawable.dashboard,R.drawable.user_color, R.drawable.car_front,
                R.drawable.speechbubbleblack, R.drawable.info};
        int[] color = {ContextCompat.getColor(context, R.color.blue),ContextCompat.getColor(context, R.color.blue), ContextCompat.getColor(context, R.color.blue),
                ContextCompat.getColor(context, R.color.blue), ContextCompat.getColor(context, R.color.blue)};

        if (bottomNavigationView != null) {
            bottomNavigationView.isWithText(true);
            // bottomNavigationView.activateTabletMode();
            bottomNavigationView.isColoredBackground(true);
            bottomNavigationView.setTextActiveSize(getResources().getDimension(R.dimen.activity_horizontal_margin));
            bottomNavigationView.setTextInactiveSize(getResources().getDimension(R.dimen.activity_horizontal_margin));
            bottomNavigationView.setItemActiveColorWithoutColoredBackground(ContextCompat.getColor(context, R.color.blue));
            //bottomNavigationView.setFont(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Noh_normal.ttf"));
        }
        BottomNavigationItem bottomNavigationItem0 = new BottomNavigationItem
                ("Dashboard", color[0], image[0]);
        BottomNavigationItem bottomNavigationItem = new BottomNavigationItem
                ("Employés", color[1], image[1]);
        BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem
                ("Missions", color[2], image[2]);
        BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem
                ("Messages", color[3], image[3]);
        BottomNavigationItem bottomNavigationItem3 = new BottomNavigationItem
                ("A propos", color[4], image[4]);


        bottomNavigationView.addTab(bottomNavigationItem0);
        bottomNavigationView.addTab(bottomNavigationItem);
        bottomNavigationView.addTab(bottomNavigationItem1);
        bottomNavigationView.addTab(bottomNavigationItem2);
        bottomNavigationView.addTab(bottomNavigationItem3);



        bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            final  FragmentTransaction fragmentTransaction=  fragmentManager.beginTransaction();
            @Override
            public void onNavigationItemClick(int index) {
                switch (index) {
                    case 0:
                        break;
                    case 1:
                        fragment = new MembreFragment();
                        fragmentTransaction.replace(R.id.container, fragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 2:
                        fragment =  new ViewPagerFragment().newInstance("");
                        fragmentTransaction.replace(R.id.container, fragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 3:
                        fragment = new  DiscussionsFragment();
                        fragmentTransaction.replace(R.id.container, fragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 4:
                        fragment =  new AproposFragment();
                        fragmentTransaction.replace(R.id.container, fragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                }
                //fragmentManager.popBackStack();
            }


        });

       //fonction(mChart);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(preferences.getString("ipserver","")!=null){
            if(httpManager.isOnline()){
                String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"membre/countMembreClientMission";
                RequestData(url);
            }else{
                Toast.makeText(getContext(),"Network is't available",Toast.LENGTH_LONG).show();
            }
        }
    }

    void fonction(PieChart mChart){
        mChart.setDescription("");
        mChart.setCenterText(generateCenterSpannableText());

        mChart.setRotationEnabled(true);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                Toast.makeText(context,
                        xValues[e.getXIndex()] + " is " + e.getVal() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // setting sample Data for Pie Chart
        setDataForPieChart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void setDataForPieChart() {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < yValues.length; i++)
            yVals1.add(new Entry(yValues[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xValues.length; i++)
            xVals.add(xValues[i]);

        // create pieDataSet
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // adding colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // Added My Own colors
        for (int c : MY_COLORS)
            colors.add(c);





        dataSet.setColors(colors);

        //  create pie data object and set xValues and yValues and set it to the pieChart
        PieData data = new PieData(xVals, dataSet);
        //   data.setValueFormatter(new DefaultValueFormatter());
        //   data.setValueFormatter(new PercentFormatter());

        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // refresh/update pie chart
        mChart.invalidate();

        // animate piechart
        mChart.animateXY(1400, 1400);


        // Legends to show on bottom of the graph
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }


    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + ""; // e.g. append a dollar-sign
        }
    }


    private void RequestData(String uri) {
        resquestPackage.setMethod("GET");
        resquestPackage.setUri(uri);
        asynchrone_data asynchroneData = new asynchrone_data();
        asynchroneData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,resquestPackage);
    }
    private class asynchrone_data extends AsyncTask<ResquestPackage,String,String> {

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
            JSONObject obj = null;
            try {
                obj = new JSONObject(param);
                JSONArray jsonObject = obj.getJSONArray("mission");
                String xDonnee="",yDonnee = "";
                if(obj.getBoolean("reponse") == false){
                    for (int i =0;i<jsonObject.length();i++){
                        JSONObject object = jsonObject.getJSONObject(i);
                        xDonnee+=object.getString("id");
                        yDonnee+=object.getString("nombre");

                        xDonnee+=",";
                        yDonnee+=",";
                    }

                    String[] s_plit = yDonnee.split(",");
                    yValues = new float[]{Integer.parseInt(s_plit[0]),
                            Integer.parseInt(s_plit[1]),
                            Integer.parseInt(s_plit[2])};

                    String[] x_plit = xDonnee.split(",");
                    xValues = new String[]{x_plit[0],
                            x_plit[1],x_plit[2]};
                    fonction(mChart);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        /*else{
            *//*networkerror.setVisibility(View.VISIBLE);
            networkerror.setText("Veuillez réessayer svp");
            networkerror.setBackgroundColor(R.color.white);
            networkerror.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(preferences.getString("ipserver","")!=null){
                        if(httpManager.isOnline()){
                            String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"membre/liste_membre";
                            RequestData(url,"");
                            networkerror.setVisibility(View.GONE);
                        }else{
                            Toast.makeText(getContext(),"Network is't available",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });*//*
        }*/
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("MISSIONS");
        return s;
    }
}