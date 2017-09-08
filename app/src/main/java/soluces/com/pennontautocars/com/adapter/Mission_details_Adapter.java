package soluces.com.pennontautocars.com.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.Model.Contrat;
import soluces.com.pennontautocars.com.Model.Membre_Info;
import soluces.com.pennontautocars.com.Model.Mission;

/**
 * Created by RAYA on 15/09/2016.
 */
public class Mission_details_Adapter extends RecyclerView.Adapter<Mission_details_Adapter.MyViewHolder> {

    private List<Mission> missions;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nb_place,horaire_depart,horaire_fin,
                chauffeur,vehicule,
                nomclient,adrsclient,
                villeclient,emailclient;

        public MyViewHolder(View view) {
            super(view);
            nb_place = (TextView) view.findViewById(R.id.textView_nbr_perso);
            horaire_depart = (TextView) view.findViewById(R.id.textView_horaire_depart);
            horaire_fin = (TextView) view.findViewById(R.id.textView_horaire_fin);
            chauffeur = (TextView) view.findViewById(R.id.textView_chauffeur);
            vehicule = (TextView) view.findViewById(R.id.textView_vehicules);
            nomclient = (TextView) view.findViewById(R.id.textView_nomclient);
            adrsclient = (TextView) view.findViewById(R.id.textView_adresseClient);
            villeclient = (TextView) view.findViewById(R.id.textView_ville);
            emailclient = (TextView) view.findViewById(R.id.textView_emailClient);
        }
    }


    public Mission_details_Adapter(List<Mission> missions1) {
        this.missions = missions1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_details_mission, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Mission mission = missions.get(position);

        holder.nb_place.setText(String .valueOf(mission.getNombre_place()));
        holder.horaire_depart.setText(mission.getHeures_debut());
        holder.horaire_fin.setText(mission.getHeures_fin());
        holder.chauffeur.setText(mission.getMembre().getNom()+" "+mission.getMembre().getPrenom());
        holder.vehicule.setText(mission.getVehicule().getMarque());
        holder.nomclient.setText(mission.getClient().getRaison_sociale());
        holder.villeclient.setText(mission.getClient().getCode_postal()+","+mission.getClient().getVille());
        holder.adrsclient.setText(mission.getClient().getAdresse1());
        holder.emailclient.setText(mission.getClient().getEmail());

    }

    @Override
    public int getItemCount() {
        return missions.size();
    }
}
