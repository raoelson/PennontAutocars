package soluces.com.pennontautocars.com.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.Model.Lieu_arrivee;
import soluces.com.pennontautocars.com.Model.Lieu_depart;
import soluces.com.pennontautocars.com.Model.Mission;

/**
 * Created by RAYA on 15/09/2016.
 */
public class Lieu_details_Adapter extends RecyclerView.Adapter<Lieu_details_Adapter.MyViewHolder> {

    private List<Lieu_depart> lieu_detailDeparts;
    private List<Lieu_arrivee> lieu_arrivees;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView adresse_depart,code_depart,ville_depart,
                pays_depart,adresse_arrivee,code_arrivee,ville_arrivee,
                pays_arrivee;

        public MyViewHolder(View view) {
            super(view);
            adresse_depart = (TextView) view.findViewById(R.id.textView_adresse_depart);
            code_depart = (TextView) view.findViewById(R.id.textView_codepostal_depart);
            ville_depart = (TextView) view.findViewById(R.id.textView_ville_depart);
            pays_depart = (TextView) view.findViewById(R.id.textView_pays_depart);
            adresse_arrivee = (TextView) view.findViewById(R.id.textView_adresse_arrivee);
            code_arrivee = (TextView) view.findViewById(R.id.textView_codepostal_arrivee);
            ville_arrivee = (TextView) view.findViewById(R.id.textView_ville_arrivee);
            pays_arrivee = (TextView) view.findViewById(R.id.textView_pays_arrivee);
        }
    }


    public Lieu_details_Adapter(List<Lieu_depart> lieuDeparts1,List<Lieu_arrivee> lieu_arrivees1) {
        this.lieu_detailDeparts = lieuDeparts1;
        this.lieu_arrivees = lieu_arrivees1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_details_lieu, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Lieu_depart departs = lieu_detailDeparts.get(position);
        Lieu_arrivee arrivees = lieu_arrivees.get(position);

        holder.adresse_depart.setText(departs.getAdresse());
        holder.code_depart.setText(departs.getCodePostal());
        holder.ville_depart.setText(departs.getVille());
        holder.pays_depart.setText(departs.getPays());

        holder.adresse_arrivee.setText(arrivees.getAdresse());
        holder.code_arrivee.setText(arrivees.getCodePostal());
        holder.ville_arrivee.setText(arrivees.getVille());
        holder.pays_arrivee.setText(arrivees.getPays());


    }

    @Override
    public int getItemCount() {
        return lieu_detailDeparts.size();
    }
}
