package soluces.com.pennontautocars.com.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.utils.QuickReturnUtils;
import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.JSON.ParseJSONMembre;
import soluces.com.pennontautocars.com.Model.Membre;
import soluces.com.pennontautocars.com.Model.Mission;
import soluces.com.pennontautocars.com.activity.MissionDetailsActivity;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;


/**
 * Created by RAYA on 23/09/2016.
 */
public class MissionAncienneAdapter extends RecyclerView.Adapter<MissionAncienneAdapter.ViewHolder> {

    // region Member Variables
    private Context mContext;
    private List<Mission> missionList;
    private Transformation mTransformation;
    private int lastPosition = -1;
    public static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";
    // endregion

    // region Constructors
    public MissionAncienneAdapter(Context context, List<Mission> missions1) {
        mContext = context;
        missionList = missions1;
        mTransformation = new RoundedTransformationBuilder()
//                .borderColor(getContext().getResources().getColor(R.color.white))
                .cornerRadius(QuickReturnUtils.dp2px(mContext, 50))
                .build();
    }
    // endregion

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_ancienne_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Mission post = missionList.get(position);

        if (post != null) {
            setUpClient(holder.titre_client, post);
            setUpLogo(holder.image_gamos, post);
            setUpTitre(holder.titre_mission, post);
            setUpIm(holder.im_vehicule, post);
            setUpIcon(holder.icon_mission);
            setUpContent(holder.content_mission, post);
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.up_from_bottom);
                holder.itemView.startAnimation(animation);
            }
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return missionList.size();
    }

    private void setUpClient(TextView tv, Mission post) {
        String timestamp =post.getClient().getRaison_sociale();
        if (!TextUtils.isEmpty(timestamp)) {
            tv.setText(timestamp);
        }
    }

    private void setUpIm(TextView tv, Mission post) {
        String timestamp =String.valueOf(post.getVehicule().getImmatricule());
        if (!TextUtils.isEmpty(timestamp)) {
            tv.setText(timestamp);
        }
    }

      private void setUpTitre(TextView tv, Mission post) {
        String timestamp =String.valueOf(post.getVehicule().getMarque());
        if (!TextUtils.isEmpty(timestamp)) {
            tv.setText(timestamp);
        }
    }

    private void setUpContent(TextView tv, Mission post) {
        String message = post.getDate_debut()+" à "+post.getHeures_debut();
        if (!TextUtils.isEmpty(message)) {
            tv.setText(Html.fromHtml("<b> Le " + message + "</b> "));
        }
    }
    private void setUpIcon(ImageView tv) {
        Picasso.with(tv.getContext())
                .load(R.drawable.clock)
                .transform(mTransformation)
                .centerCrop()
                .resize(QuickReturnUtils.dp2px(mContext, 34),
                        QuickReturnUtils.dp2px(mContext, 34))
                .error(android.R.drawable.stat_notify_error)
                .into(tv);
    }
    int p;

    private void setUpLogo(ImageView iv,Mission post) {
        String im_logo= null;
        p = post.getVehicule().getLogo().compareToIgnoreCase("null");
        if(p != 0){
            im_logo = "http://autocarspennont.fr/sapmanager/static/images/vehicles/"+post.getVehicule().getLogo();
        }else{
            im_logo = "http://autocarspennont.fr/sapmanager/static/images/vehicles/lock-thumb.jpg";
        }
        Picasso.with(iv.getContext())
                .load(im_logo)
                .transform(mTransformation)
                .centerCrop()
                .resize(QuickReturnUtils.dp2px(mContext, 34),
                        QuickReturnUtils.dp2px(mContext, 34))
                .error(android.R.drawable.stat_notify_error)
                .into(iv);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.textView_client_ancienne)
        TextView titre_client;
        @Bind(R.id.img_vehicule_ancienne)
        ImageView image_gamos;
        @Bind(R.id.display_name_ancienne)
        TextView titre_mission;
        @Bind(R.id.txt_immatricule_ancienne)
        TextView im_vehicule;
        @Bind(R.id.icon_text_ancienne)
        ImageView icon_mission;
        @Bind(R.id.textView_message_ancienne)
        TextView content_mission;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public Mission getItem(int paramInt) {
        // TODO Auto-generated method stub
        return missionList.get(paramInt);
    }
}
