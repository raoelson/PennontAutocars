package soluces.com.pennontautocars.com.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.Model.Membre;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;

/**
 * Created by RAYA on 15/11/2016.
 */

public class GroupesListesAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    List<Membre> objects;
    String element_select;
    Integer admin;

    GroupesListesAdapter(Context context, List<Membre> membres, String element,Integer admin) {
        ctx = context;
        objects = membres;
        element_select = element;
        this.admin = admin;
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

        Membre p = getMembre(position);


        ((TextView) view.findViewById(R.id.txt_conducteur)).setText(p.getNom() + " " + p.getPrenom());

        Boolean checked_ = false;
        try {
            JSONObject obj = new JSONObject(element_select);
            JSONArray commentsObj = obj.getJSONArray("id");
            for(int i=0;i<commentsObj.length();i++){
                if(p.getId() == commentsObj.get(i)){
                    checked_ = true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        CheckBox cbBuy = (CheckBox) view.findViewById(R.id.image_check_);
        cbBuy.setOnCheckedChangeListener(myCheckChangList);
        cbBuy.setTag(position);
        cbBuy.setChecked(p.isSelected());
        cbBuy.setChecked(checked_);
        if(p.getId() == admin){
            cbBuy.setEnabled(false);
        }
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