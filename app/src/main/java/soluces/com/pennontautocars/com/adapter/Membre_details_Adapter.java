package soluces.com.pennontautocars.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.Model.Contrat;
import soluces.com.pennontautocars.com.Model.Membre;
import soluces.com.pennontautocars.com.Model.Membre_Info;

/**
 * Created by RAYA on 15/09/2016.
 */
public class Membre_details_Adapter extends RecyclerView.Adapter<Membre_details_Adapter.MyViewHolder> {

    private List<Membre_Info> membre_infos;

    private String phone;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView _phonepersoMembre,_adresse1Membre;

        public MyViewHolder(View view) {
            super(view);
            _phonepersoMembre = (TextView) view.findViewById(R.id.textView_phone);
            _adresse1Membre = (TextView) view.findViewById(R.id.textView_adresse1);
        }
    }


    public Membre_details_Adapter(List<Membre_Info> Listmembre,String phone) {
        this.membre_infos = Listmembre;
        this.phone = phone;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_details_membre, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Membre_Info membre_info = membre_infos.get(position);



        holder._phonepersoMembre.setText(phone);
        holder._adresse1Membre.setText(membre_info.getAdresse1());

    }

    @Override
    public int getItemCount() {
        return membre_infos.size();
    }
}
