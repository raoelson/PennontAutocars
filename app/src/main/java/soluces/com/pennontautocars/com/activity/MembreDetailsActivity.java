package soluces.com.pennontautocars.com.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import library.utils.QuickReturnUtils;
import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.JSON.ParseJSONMembre;
import soluces.com.pennontautocars.com.Model.Contrat;
import soluces.com.pennontautocars.com.Model.Membre;
import soluces.com.pennontautocars.com.Model.Membre_Info;
import soluces.com.pennontautocars.com.adapter.Membre_details_Adapter;
import soluces.com.pennontautocars.com.itemdecorations.CircleTransformation;
import soluces.com.pennontautocars.com.view.RevealBackgroundView;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;

/**
 * Created by RAYA on 15/09/2016.
 */
public class MembreDetailsActivity extends AppCompatActivity {

    private int avatarSize;
    private String profilePhoto,_email,_phone;
    private ImageView _details_email,_details_contatct;
    private Integer theme;
    private com.squareup.picasso.Transformation transformation;
    private TabLayout _tlMembrre;
    private View vMembreStats,vMembreProfileRoot;
    private Toolbar toolbar;
    private RevealBackgroundView _vRevealBackground;
    private RecyclerView _rvUserProfile;
    private ImageView _ivMembreProfilePhoto;
    private View _vUserProfileRoot,_vMembreDetails;
    private TextView _nomMembre,_type_emailMembre,_phoneMembre;
    private HttpManager httpManager;
    private ResquestPackage resquestPackage;
    SharedPreferences preferences;
    private List<Membre_Info> membre_infos;
    Context mContext;
    private String nomTitle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membre_details_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar_);
        _rvUserProfile = (RecyclerView) findViewById(R.id.rvUserProfile);
        _vRevealBackground = (RevealBackgroundView) findViewById(R.id.vRevealBackground);
        _vUserProfileRoot = findViewById(R.id.vMembreProfileRoot);
        _vMembreDetails = findViewById(R.id.vMembreDetails);
        _nomMembre = (TextView) findViewById(R.id.txt_membre_Nom);
        _type_emailMembre = (TextView) findViewById(R.id.txt_membre_type);
        _phoneMembre = (TextView) findViewById(R.id.txt_membre_phone);



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.setExpanded(true);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        resquestPackage = new ResquestPackage();
        httpManager = new HttpManager(this);
        preferences =  PreferenceManager.getDefaultSharedPreferences(this);

        if(getIntent().getExtras().getBoolean("details")){
            String phone_perso = null;
            int id_search =getIntent().getExtras().getInt("id");
            ParseJSONMembre parseJSONMembre = null;
            if(httpManager.isOnline()){
                String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"membre/search_detail";
                resquestPackage.setMethod("GET");
                resquestPackage.setUri(url);
                resquestPackage.setParam("id",String.valueOf(id_search));
                String rep=null;
                if((rep=httpManager.getData(resquestPackage))!=null){
                    //Log.d(this.getClass().getName(),"reponses theme"+parseJSONMembre.getID_membre(rep).getTheme());
                    phone_perso = parseJSONMembre.getID_membre(rep).getTelephone_pro();
                    _email = parseJSONMembre.getID_membre(rep).getEmail();
                    nomTitle = parseJSONMembre.getID_membre(rep).getNom()+" "+
                            parseJSONMembre.getID_membre(rep).getPrenom();

                    theme = parseJSONMembre.getID_membre(rep).getTheme();

                    profilePhoto= parseJSONMembre.getID_membre(rep).getImage();
                    _phone = parseJSONMembre.getID_membre(rep).getTelephone_pro();
                    _nomMembre.setText(parseJSONMembre.getID_membre(rep).getNom()+" "+
                            parseJSONMembre.getID_membre(rep).getPrenom());
                    _type_emailMembre.setText(parseJSONMembre.getID_membre(rep).getStaff_type()+" / "+
                            parseJSONMembre.getID_membre(rep).getEmail());
                    _phoneMembre.setText(parseJSONMembre.getID_membre(rep).getTelephone_pro());

                }else{
                    Toast.makeText(getApplicationContext(),"Veuillez vérifier votre ip serveur",Toast.LENGTH_LONG).show();
                }

            }

            if(httpManager.isOnline()){
                String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"membre_info/search_info";
                resquestPackage.setMethod("GET");
                resquestPackage.setUri(url);
                resquestPackage.setParam("id",String.valueOf(id_search));
                String rep=null;
                if((rep=httpManager.getData(resquestPackage))!=null){
                    membre_infos = new ArrayList<>();
                    membre_infos = parseJSONMembre.getAllInfo(rep);
                }else{
                    Toast.makeText(getApplicationContext(),"Veuillez vérifier votre ip serveur",Toast.LENGTH_LONG).show();
                }
            }
           /* if(httpManager.isOnline()){
                String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"contrat_travail/search_contrat";
                resquestPackage.setMethod("GET");
                resquestPackage.setUri(url);
                resquestPackage.setParam("id",String.valueOf(id_search));
                String rep=null;
                if((rep=httpManager.getData(resquestPackage))!=null){
                    contrats = new ArrayList<>();
                    contrats = parseJSONMembre.getAllStaff_contrat(rep);
                }else{
                    Toast.makeText(getApplicationContext(),"Veuillez vérifier votre ip serveur",Toast.LENGTH_LONG).show();
                }

            }*/

            else{
                Toast.makeText(getApplicationContext(),"Network is't available",Toast.LENGTH_LONG).show();
            }
            Membre_details_Adapter adapter = new Membre_details_Adapter(membre_infos,phone_perso);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            _rvUserProfile.setLayoutManager(mLayoutManager);
            _rvUserProfile.setItemAnimator(new DefaultItemAnimator());
            _rvUserProfile.setAdapter(adapter);
        }

        _ivMembreProfilePhoto = (ImageView) findViewById(R.id.ivMembreProfilePhoto);
        _details_email  = (ImageView) findViewById(R.id.details_email);
        _details_contatct  = (ImageView) findViewById(R.id.details_contatct);
        _tlMembrre = (TabLayout) findViewById(R.id.tlMembrre);
        vMembreProfileRoot = findViewById(R.id.vMembreProfileRoot);
        vMembreStats= findViewById(R.id.vMembreStats);
        this.avatarSize = getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);



        transformation = new RoundedTransformationBuilder()
//                .borderColor(getContext().getResources().getColor(R.color.white))
                .cornerRadius(2)
                .build();
        setUpUserImage(_ivMembreProfilePhoto);
        setUpCommenterTwoImage(_details_email);
        setUpCommenterThreeImage(_details_contatct);



        setupTabs();

        _details_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    if(!_phone.isEmpty()){
                        callIntent.setData(Uri.parse("tel:"+_phone));
                        view.getContext().startActivity(callIntent);
                    }else{
                        Toast.makeText(getApplicationContext(),"Aucun numéro",Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception ex){
                    Toast.makeText(mContext,"yourActivity is not founded",Toast.LENGTH_SHORT).show();
                }
            }
        });
        _details_contatct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{""+_email});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    view.getContext().startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(nomTitle);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });

    }
    private void setupTabs() {
        _tlMembrre.addTab(_tlMembrre.newTab().setText("Informations sur l'employé"));
    }
    private void setUpUserImage(ImageView iv) {
        String letter = String.valueOf(nomTitle.charAt(0));
        TextDrawable drawable;
        drawable = TextDrawable.builder()
                .buildRound(letter,theme);
        //Log.d(this.getClass().getName(),"reponses : "+theme);

        Picasso.with(this)
                .load(""+profilePhoto)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .error(drawable)
                .into(iv);
    }


    private void setUpCommenterTwoImage(ImageView iv) {
        Picasso.with(iv.getContext())
                .load(android.R.drawable.ic_menu_call)
                .transform(transformation)
                .centerCrop()
                .resize(34,34)
                .error(android.R.drawable.stat_notify_error)
                .into(iv);
    }

    private void setUpCommenterThreeImage(ImageView iv) {
        Picasso.with(iv.getContext())
                .load(android.R.drawable.ic_dialog_email)
                .transform(transformation)
                .centerCrop()
                .resize(34, 34)
                .error(android.R.drawable.stat_notify_error)
                .into(iv);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
