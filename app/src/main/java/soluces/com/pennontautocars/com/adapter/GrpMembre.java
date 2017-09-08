package soluces.com.pennontautocars.com.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import library.utils.QuickReturnUtils;
import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.Model.Membre;

/**
 * Created by RAYA on 20/11/2016.
 */

public class GrpMembre extends RecyclerView.Adapter<GrpMembre.MyViewHolder> {
    private Context mContext;
    private List<Membre> membreList;
    private Transformation mTransformation;
    ColorGenerator generator = ColorGenerator.MATERIAL;
    SharedPreferences preferences;

    public GrpMembre(Context context, List<Membre> membres) {
        this.membreList = membres;
        this.mContext = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        mTransformation = new RoundedTransformationBuilder()
                .borderColor(mContext.getResources().getColor(R.color.blue))
                .cornerRadius(QuickReturnUtils.dp2px(mContext, 50))
                .build();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_newgroup_contact, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
      Membre membre = membreList.get(position);
        if(Integer.parseInt(preferences.getString("id_user","")) != membre.getId()) {
            holder.LinearLayout.setVisibility(View.VISIBLE);
            holder.addgrpNom.setText(membre.getNom());
            setUpUserImage(holder.addgrpImage, membre);
            holder.activeMembre.setChecked(membre.isSelected());
            holder.activeMembre.setTag(membreList.get(position));
            holder.activeMembre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox b = (CheckBox) v;
                    Membre membre1 = (Membre) b.getTag();
                    membre1.setSelected(b.isChecked());
                    membreList.get(position).setSelected(b.isChecked());
                }
            });
        }
    }

    private void setUpUserImage(ImageView iv,Membre membre) {
        String letter = String.valueOf(membre.getNom().charAt(0));
//        Create a new TextDrawable for our image's background
        TextDrawable drawable;
        if(membre.getTheme() == 0){
            drawable = TextDrawable.builder()
                    .buildRound(letter, Integer.parseInt(preferences.getString("theme","")));
        }else{
            drawable = TextDrawable.builder()
                    .buildRound(letter, membre.getTheme());
        }

        Picasso.with(iv.getContext())
                .load(membre.getImage())
                .transform(mTransformation)
                .centerCrop()
                .resize(QuickReturnUtils.dp2px(mContext, 50),
                        QuickReturnUtils.dp2px(mContext, 50))
//                    .placeholder(R.drawable.ic_facebook)
                .error(drawable)
                .into(iv);
    }

    @Override
    public int getItemCount() {
        return membreList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView addgrpImage;
        public TextView addgrpNom;
        public CheckBox activeMembre;
        public View LinearLayout;

        public MyViewHolder(View view) {
            super(view);
            addgrpNom = (TextView) view.findViewById(R.id.addgrpNom);
            addgrpImage = (ImageView) view.findViewById(R.id.addgrpImage);
            activeMembre = (CheckBox) view.findViewById(R.id.activeMembre);
            LinearLayout = view.findViewById(R.id.LinearLayout);
        }
    }

    public List<Membre> getMembreList(){
        return membreList;
    }
}
