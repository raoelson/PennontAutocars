package soluces.com.pennontautocars.com.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.utils.QuickReturnUtils;
import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.JSON.ParseJSONMembre;
import soluces.com.pennontautocars.com.Model.Client;
import soluces.com.pennontautocars.com.Model.Groupe;
import soluces.com.pennontautocars.com.Model.Membre;
import soluces.com.pennontautocars.com.activity.ChatGroupesActivity;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;

/**
 * Created by RAYA on 21/09/2016.
 */
public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Groupe> groupeList;
    private Transformation mTransformation;
    SharedPreferences preferences;
    private HttpManager httpManager;
    private ResquestPackage resquestPackage;
    GroupesListesAdapter adapteur_;
    public static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //public TextView title, count;
        public ImageView groupes_icon;
        public TextView textView_titre;
        public ImageButton btnComments_,addUser,btnComments;



        public MyViewHolder(View view) {
            super(view);
            /*title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);*/
            groupes_icon = (ImageView) view.findViewById(R.id.image_groups);
            textView_titre = (TextView) view.findViewById(R.id.textView_titre);
            /*tsLikesCounter_ = (TextSwitcher) view.findViewById(R.id.tsLikesCounter_);*/
            btnComments_ = (ImageButton) view.findViewById(R.id.btnComments_);
            btnComments = (ImageButton) view.findViewById(R.id.btnComments);
            addUser = (ImageButton) view.findViewById(R.id.add_user);
        }
    }


    public GroupsAdapter(Context mContext, List<Groupe> groupes) {
        this.mContext = mContext;
        this.groupeList = groupes;
        mTransformation = new RoundedTransformationBuilder()
                .borderColor(mContext.getResources().getColor(R.color.blue))
                .cornerRadius(QuickReturnUtils.dp2px(mContext, 50))
                .build();
        preferences =  PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_group, parent, false);

        resquestPackage = new ResquestPackage();
        httpManager = new HttpManager(mContext);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        return new MyViewHolder(itemView);
    }

    private void setUpUserImage(ImageView iv,Groupe groupe) {
        String letter = String.valueOf(groupe.getNomGroupe().charAt(0));
//        Create a new TextDrawable for our image's background
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(letter, groupe.getTheme());
        //Log.d("reponses","reponses"+groupe.getTheme());

        Picasso.with(iv.getContext())
                .load(groupe.getImg())
                .transform(mTransformation)
                .centerCrop()
                .resize(QuickReturnUtils.dp2px(mContext, 34),
                        QuickReturnUtils.dp2px(mContext, 34))
                .error(drawable)
                .into(iv);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Groupe grp = groupeList.get(position);


        holder.textView_titre.setText(grp.getNomGroupe());
        setUpUserImage(holder.groupes_icon,grp);

        if(preferences.getString("id_user","").equals(String.valueOf(grp.getAdmin()))){
            holder.btnComments_.setVisibility(View.VISIBLE);
            holder.btnComments_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(holder.btnComments_,grp.getId(),position);
                }
            });
        }
        holder.btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ChatGroupesActivity.class);
                intent.putExtra("idgrps",getItem(position).getId());
                intent.putExtra("nomgrps",getItem(position).getNomGroupe());
                mContext.startActivity(intent);
            }
        });

        holder.addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog ui = new Dialog(mContext);
                ui.getWindow();
                ui.requestWindowFeature(Window.FEATURE_NO_TITLE);
                ui.setContentView(R.layout.liste_membre_conduteurs);
                TextView titre_c = (TextView) ui.findViewById(R.id.titre_c);
                titre_c.setText("Liste(s) de(s) membre(s)");
                ListView listView = (ListView) ui.findViewById(R.id.membre_conducteur);
                Button btsave = (Button) ui.findViewById(R.id.bt_sauvegarde);
                btsave.setText("Ajouter");
                if(preferences.getString("ipserver","")!=null){
                    if(httpManager.isOnline()){
                        ParseJSONMembre parseJSONMembre = new ParseJSONMembre();
                        String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"membre/searchDetails";
                        resquestPackage.setMethod("GET");
                        resquestPackage.setUri(url);

                        adapteur_ = new GroupesListesAdapter(mContext,
                                parseJSONMembre.getAllStaff(httpManager.getData(resquestPackage)),
                                getItem(position).getTmps(),getItem(position).getAdmin());
                    }else{
                        Toast.makeText(mContext,"Network is't available",Toast.LENGTH_LONG).show();
                    }
                }

                if(preferences.getString("id_user","").equals(String.valueOf(grp.getAdmin()))){
                    btsave.setOnClickListener(new View.OnClickListener() {
                        List<Membre> groupeList_ ;
                        @Override
                        public void onClick(View v) {
                            groupeList_= new ArrayList<Membre>();
                            for (Membre p : adapteur_.getBox()) {

                                Membre membre = new Membre();
                                if (p.isSelected()){

                                    membre.setId(p.getId());
                                    //groupe.setMembre(membre);
                                }
                                groupeList_.add(membre);
                            }

                            if (preferences.getString("ipserver", "") != null) {
                                if (httpManager.isOnline()) {
                                    ParseJSONMembre parseJSONMembre = new ParseJSONMembre();
                                    String url = httpManager.URL_serveur() + "" + preferences
                                            .getString("ipserver", "") + "groupes/updateGroupes";
                                    resquestPackage.setMethod("POST");
                                    resquestPackage.setUri(url);
                                    resquestPackage.setParam("id", String.valueOf(getItem(position).getId()));
                                    resquestPackage.setParam("id_user", String.valueOf(preferences.getString("id_user","")));
                                    resquestPackage.setParam("membres",new Gson().toJson(groupeList_));

                                    String rep = httpManager.getData(resquestPackage);
                                    ParseJSONMembre parseJSONMembre_ = new ParseJSONMembre();
                                    int adapterPosition = holder.getAdapterPosition();

                                    groupeList.clear();
                                    groupeList = parseJSONMembre_.getGroupes(rep);
                                    notifyItemChanged(adapterPosition, ACTION_LIKE_BUTTON_CLICKED);
                                    notifyDataSetChanged();
                                    ui.dismiss();

                                } else {
                                    Toast.makeText(mContext, "Network is't available", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }else{
                    btsave.setVisibility(View.GONE);
                }

                //adapteur_= new MembreConducteurAdapteur(mContext,mbres);
                listView.setAdapter(adapteur_);
                ui.show();
            }
        });

    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view,Integer id,Integer position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_admin, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(id,position));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        Integer id,position;

        public MyMenuItemClickListener(Integer id,Integer position)
        {
            this.id = id;
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_delete:

                    if(preferences.getString("ipserver","")!=null){
                        if(httpManager.isOnline()){
                            String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"groupes/deleteGroupe";
                            resquestPackage.setMethod("GET");
                            resquestPackage.setUri(url);
                            resquestPackage.setParam("id",String.valueOf(id));
                            String rep = httpManager.getData(resquestPackage);
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(rep);
                                if (obj.getBoolean("reponse") == false) {
                                    groupeList.remove(groupeList.get(position));
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position,groupeList.size());
                                    notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            }else{
                            Toast.makeText(mContext,"Network is't available",Toast.LENGTH_LONG).show();
                        }
                    }
                    return true;
                case R.id.action_desactiver:
                    Toast.makeText(mContext, "En cours ", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return groupeList.size();
    }
    public Groupe getItem(int paramInt) {
        // TODO Auto-generated method stub
        return groupeList.get(paramInt);
    }
}
