package soluces.com.pennontautocars.com.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.JSON.ParseJSONMembre;
import soluces.com.pennontautocars.com.Model.Membre;
import soluces.com.pennontautocars.com.Model.Product;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;

/**
 * Created by RAYA on 05/10/2016.
 */
public class MembreConducteurAdapteur extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    List<Membre> objects;
    String element_select;
    private HttpManager httpManager;
    private ResquestPackage resquestPackage;
    SharedPreferences preferences;

    MembreConducteurAdapteur(Context context, List<Membre> membres,String element) {
        ctx = context;
        objects = membres;
        element_select =element;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_membre_conduteurs, parent, false);
        }

        resquestPackage = new ResquestPackage();
        httpManager = new HttpManager(ctx);
        preferences =  PreferenceManager.getDefaultSharedPreferences(ctx);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Membre p = getMembre(position);

        ((TextView) view.findViewById(R.id.txt_conducteur)).setText(p.getNom()+" "+p.getPrenom());

        Boolean checked_ = false;
        if(!element_select.equalsIgnoreCase("null")){
            if(preferences.getString("ipserver","")!=null) {
                if (httpManager.isOnline()) {
                    String url = httpManager.URL_serveur() + "" + preferences.getString("ipserver", "") + "mission/getSearchAttr";
                    resquestPackage.setMethod("GET");
                    resquestPackage.setUri(url);
                    resquestPackage.setParam("id",element_select);
                    ParseJSONMembre parseJSONMembre = new ParseJSONMembre();
                    //parseJSONMembre.testMission(httpManager.getData(resquestPackage));
                    if(String.valueOf(getMembre(position).getId()).equals(
                            parseJSONMembre.getMissionSingle(httpManager.getData(resquestPackage))
                    )){
                        checked_ = true;
                    }

                } else {
                    Toast.makeText(ctx, "Network is't available", Toast.LENGTH_LONG).show();
                }
            }
        }
        CheckBox cbBuy = (CheckBox) view.findViewById(R.id.image_check_);
        cbBuy.setOnCheckedChangeListener(myCheckChangList);
        cbBuy.setTag(position);
        cbBuy.setChecked(p.isSelected());
        cbBuy.setChecked(checked_);
        return view;
    }

    Membre getMembre(int position) {
        return ((Membre) getItem(position));
    }

    ArrayList<Membre> getBox() {
        ArrayList<Membre> box = new ArrayList<>();
        for (Membre p : objects) {
            if (p.isSelected())
                box.add(p);
        }
        return box;
    }

    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            getMembre((Integer) buttonView.getTag()).setSelected(isChecked);

        }
    };
}