package soluces.com.pennontautocars.com.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.utils.QuickReturnUtils;
import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.JSON.ParseJSONMembre;
import soluces.com.pennontautocars.com.Model.Membre;
import soluces.com.pennontautocars.com.Model.Mission;
import soluces.com.pennontautocars.com.Model.Product;
import soluces.com.pennontautocars.com.activity.MissionDetailsActivity;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;


/**
 * Created by RAYA on 23/09/2016.
 */
public class EncoursAdapter extends RecyclerView.Adapter<EncoursAdapter.ViewHolder> {

    // region Member Variables
    private Context mContext;
    private List<Mission> missionList;
    private Transformation mTransformation;
    private Transformation mTransformation2;
    private int lastPosition = -1;
    public static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";
    private HttpManager httpManager;
    private ResquestPackage resquestPackage;
    SharedPreferences preferences;
    List<Membre> listesMembreAttrib;
    MembreConducteurAdapteur adapteur_;




    // endregion

    // region Constructors
    public EncoursAdapter(Context context, List<Mission> missions1,List<Membre> membres) {
        mContext = context;
        missionList = missions1;
        listesMembreAttrib = membres;
        mTransformation = new RoundedTransformationBuilder()
//                .borderColor(getContext().getResources().getColor(R.color.white))
                .cornerRadius(QuickReturnUtils.dp2px(mContext, 50))
                .build();

        mTransformation2 = new RoundedTransformationBuilder()
//                .borderColor(getContext().getResources().getColor(R.color.white))
                .cornerRadius(QuickReturnUtils.dp2px(mContext, 2))
                .build();
    }
    // endregion

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_encours_row, parent, false);
        resquestPackage = new ResquestPackage();
        httpManager = new HttpManager(mContext);
        preferences =  PreferenceManager.getDefaultSharedPreferences(mContext);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

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
            setUpVoir(holder.voir_encours);
            setUpAttrib(holder.attrib_encours, post,holder);
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.up_from_bottom);
                holder.itemView.startAnimation(animation);
            }
            lastPosition = position;
        }

        holder.voir_encours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,MissionDetailsActivity.class);
                intent.putExtra("id",String.valueOf(getItem(position).getId()));
                intent.putExtra("id_membre",getItem(position).getVehicule().getModele());
                intent.putExtra("id_depart",getItem(position).getDepart());
                intent.putExtra("id_arrivee",getItem(position).getArrivee());
                mContext.startActivity(intent);
            }
        });
        holder.attrib_encours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final Dialog ui = new Dialog(mContext);
                ui.getWindow();
                ui.requestWindowFeature(Window.FEATURE_NO_TITLE);
                ui.setContentView(R.layout.liste_membre_conduteurs);
                ListView listView = (ListView) ui.findViewById(R.id.membre_conducteur);
                Button btsave = (Button) ui.findViewById(R.id.bt_sauvegarde);

                if(preferences.getString("ipserver","")!=null){
                    if(httpManager.isOnline()){
                        ParseJSONMembre parseJSONMembre = new ParseJSONMembre();
                        String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"membre/searchDetail";
                        resquestPackage.setMethod("GET");
                        resquestPackage.setUri(url);
                       adapteur_ = new MembreConducteurAdapteur(mContext,
                               parseJSONMembre.getAllStaff(httpManager.getData(resquestPackage)),
                               getItem(position).getVehicule().getModele());
                    }else{
                        Toast.makeText(mContext,"Network is't available",Toast.LENGTH_LONG).show();
                    }
                }

                //adapteur_= new MembreConducteurAdapteur(mContext,mbres);
                listView.setAdapter(adapteur_);

                btsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String result="";
                        int i=1,compte=0;
                        for (Membre p : adapteur_.getBox()) {
                            if (p.isSelected()){
                                result += ""+p.getId();
                                compte=i++;
                            }
                        }
                        if(compte==0){
                            Toast.makeText(mContext, "Veuillez cocher un élément svp", Toast.LENGTH_LONG).show();
                        }else if(compte==1){
                            if (preferences.getString("ipserver", "") != null) {
                                if (httpManager.isOnline()) {
                                    ParseJSONMembre parseJSONMembre = new ParseJSONMembre();
                                    String url = httpManager.URL_serveur() + "" + preferences
                                            .getString("ipserver", "") + "mission/updateMission";
                                    resquestPackage.setMethod("POST");
                                    resquestPackage.setUri(url);
                                    resquestPackage.setParam("id", String.valueOf(getItem(position).getId()));
                                    resquestPackage.setParam("membre",result);

                                    String rep = httpManager.getData(resquestPackage);

                                    if(rep.isEmpty()){
                                        /*Toast.makeText(mContext.getApplicationContext(),
                                                "Ce conducteur est déjà assigné à une autre mission"
                                                ,Toast.LENGTH_LONG).show();*/
                                    }else{
                                        ParseJSONMembre parseJSONMembre_ = new ParseJSONMembre();
                                        int adapterPosition = holder.getAdapterPosition();
                                        missionList.clear();
                                        missionList = parseJSONMembre_.getAllMission(rep);
                                        notifyItemChanged(adapterPosition, ACTION_LIKE_BUTTON_CLICKED);
                                        notifyDataSetChanged();
                                        ui.dismiss();
                                    }
                                } else {
                                    Toast.makeText(mContext, "Network is't available", Toast.LENGTH_LONG).show();
                                }
                            }
                        }else{
                            Toast.makeText(mContext, "Veuillez cocher un élément svp", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                ui.show();

            }
        });

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

    private void setUpVoir(ImageView iv) {
        Picasso.with(iv.getContext())
                .load(android.R.drawable.ic_menu_view)
                .transform(mTransformation2)
                .centerCrop()
                .resize(QuickReturnUtils.dp2px(mContext, 34),
                        QuickReturnUtils.dp2px(mContext, 34))
                .error(android.R.drawable.stat_notify_error)
                .into(iv);
    }

    private void setUpAttrib(ImageView iv,final  Mission post, final ViewHolder holder) {
        int im_;
        if(post.getVehicule().getModele().equals("null")){
            im_ = R.drawable.desactive;
        }else{
            im_ = R.drawable.active_blue;
            Membre membre = new Membre();
            membre.setId(Integer.parseInt(post.getVehicule().getModele()));
        }
        Picasso.with(iv.getContext())
                .load(im_)
                .transform(mTransformation2)
                .centerCrop()
                .resize(QuickReturnUtils.dp2px(mContext, 34),
                        QuickReturnUtils.dp2px(mContext, 34))
                .error(android.R.drawable.stat_notify_error)
                .into(iv);


        /*if(selected){
            Picasso.with(iv.getContext())
                    .load(android.R.drawable.ic_media_next)
                    .transform(mTransformation2)
                    .centerCrop()
                    .resize(QuickReturnUtils.dp2px(mContext, 34),
                            QuickReturnUtils.dp2px(mContext, 34))
                    .error(android.R.drawable.stat_notify_error)
                    .into(iv);
            selected = false;
        }else{
            Picasso.with(iv.getContext())
                    .load(android.R.drawable.ic_media_pause)
                    .transform(mTransformation2)
                    .centerCrop()
                    .resize(QuickReturnUtils.dp2px(mContext, 34),
                            QuickReturnUtils.dp2px(mContext, 34))
                    .error(android.R.drawable.stat_notify_error)
                    .into(iv);
            selected = true;
        }*/

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.textView_client)
        TextView titre_client;
        @Bind(R.id.img_vehicule)
        ImageView image_gamos;
        @Bind(R.id.display_name_encours)
        TextView titre_mission;
        @Bind(R.id.txt_immatricule)
        TextView im_vehicule;
        @Bind(R.id.icon_text)
        ImageView icon_mission;
        @Bind(R.id.textView_message_encours)
        TextView content_mission;
        @Bind(R.id.image_voir_encours)
        ImageView voir_encours;
        @Bind(R.id.image_attribuer)
        ImageView attrib_encours;

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
