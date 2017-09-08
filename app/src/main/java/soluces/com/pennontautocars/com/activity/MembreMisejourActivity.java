package soluces.com.pennontautocars.com.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

/*import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;*/

import fr.ganfra.materialspinner.MaterialSpinner;
import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.JSON.ParseJSONMembre;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;


/**
 * Created by RAYA on 04/09/2016.
 */
public class MembreMisejourActivity extends AppCompatActivity {

    private Toolbar toolbar;
   /* private BootstrapLabel label,label_contrat;
    private ExpandableLayout complementaire,expandableLayout_contrat;*/
    private Integer id_search,id_search_info,id_search_contrat;
    private String nom,prenom,email,type,phone_pro,id_pro,phone_perso;
    /*private BootstrapEditText nom_edit,prenom_edit,
            email_edit,phone_edit,id_edit,phone_perso_edit,
            date_debut_contrat,date_fin_contrat,
    adresse_postal1,adresse_postal2,code_postal,ville,date_naissance,pays,
    phoneperso,nbheure_contrat;*/
    /*private BootstrapButton btsave,btdelete;*/
  /*  private BootstrapButton btsave;
    private MaterialSpinner spinner_type;
    private LovelyStandardDialog lovelyStandardDialog = null;*/
    private HttpManager httpManager;
    private ResquestPackage resquestPackage;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*setContentView(R.layout.activity_misejour_membre);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String[] ITEMS = {"Type staff","administrateur", "conducteur", "operateur"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type = (MaterialSpinner) findViewById(R.id.spinner);
        spinner_type.setAdapter(adapter);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        resquestPackage = new ResquestPackage();
        httpManager = new HttpManager(this);
        preferences =  PreferenceManager.getDefaultSharedPreferences(this);

        nom_edit = (BootstrapEditText) findViewById(R.id.txt_nom);
        prenom_edit = (BootstrapEditText) findViewById(R.id.txt_prenom);
        email_edit = (BootstrapEditText) findViewById(R.id.txt_email_pro);
        phone_edit = (BootstrapEditText) findViewById(R.id.txt_phone_pro);
        phone_perso_edit = (BootstrapEditText) findViewById(R.id.txt_phone_pers);
        id_edit = (BootstrapEditText) findViewById(R.id.txt_id_employe);
        btsave  = (BootstrapButton) findViewById(R.id.bt_save);
        //btdelete  = (BootstrapButton) findViewById(R.id.bt_delete);

        phoneperso = (BootstrapEditText) findViewById(R.id.txt_phone_pers);

        adresse_postal1 = (BootstrapEditText) findViewById(R.id.txt_adresse_postale1);
        adresse_postal2 = (BootstrapEditText) findViewById(R.id.txt_adresse_postale2);
        pays = (BootstrapEditText) findViewById(R.id.txt_pays);
        ville = (BootstrapEditText) findViewById(R.id.txt_ville);
        date_naissance = (BootstrapEditText) findViewById(R.id.txt_date);
        code_postal = (BootstrapEditText) findViewById(R.id.txt_code_postale);


        date_debut_contrat = (BootstrapEditText) findViewById(R.id.txt_date_debut);
        date_fin_contrat = (BootstrapEditText) findViewById(R.id.txt_date_fins);
        nbheure_contrat = (BootstrapEditText) findViewById(R.id.txt_nbr_heures);



        label = (BootstrapLabel) findViewById(R.id.expandableButton);
        label_contrat = (BootstrapLabel) findViewById(R.id.label_contrat);



        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complementaire = (ExpandableLayout) findViewById(R.id.expandableLayout_compl);
                complementaire.toggle();
            }
        });
        label_contrat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout_contrat = (ExpandableLayout) findViewById(R.id.expandableLayout_contrat);
                expandableLayout_contrat.toggle();
            }
        });
        if(getIntent().getExtras().getBoolean("updates")){

            id_search = getIntent().getExtras().getInt("id");
            nom = getIntent().getExtras().getString("nom");
            prenom = getIntent().getExtras().getString("prenom");
            email = getIntent().getExtras().getString("email");
            type = getIntent().getExtras().getString("staff_type");
            phone_pro = getIntent().getExtras().getString("phone_pro");
            phone_perso= getIntent().getExtras().getString("phone_perso");
            id_pro = getIntent().getExtras().getString("id_employe");
            int  position = adapter.getPosition(type);
            nom_edit.setText(nom);
            prenom_edit.setText(prenom);
            email_edit.setText(email);
            spinner_type.setSelection(position);
            phone_edit.setText(phone_pro);
            phone_perso_edit.setText(phone_perso);
            id_edit.setText(id_pro);
            btsave.setText("Mise à jour");
            //btdelete.setVisibility(View.VISIBLE);
            ParseJSONMembre parseJSONMembre = null;
            if(preferences.getString("ipserver","")!=null){
                if(httpManager.isOnline()){
                    String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"membre_info/search_info";
                    resquestPackage.setMethod("GET");
                    resquestPackage.setUri(url);
                    resquestPackage.setParam("id",String.valueOf(id_search));
                    String rep=null;
                    if((rep=httpManager.getData(resquestPackage))!=null){
                        id_search_info = parseJSONMembre.getAllStaff_info(rep).getId();
                        adresse_postal1.setText(parseJSONMembre.getAllStaff_info(rep).getAdresse1());
                        adresse_postal2.setText(parseJSONMembre.getAllStaff_info(rep).getAdresse2());
                        code_postal.setText(parseJSONMembre.getAllStaff_info(rep).getCode_postal());
                        ville.setText(parseJSONMembre.getAllStaff_info(rep).getVille());
                        pays.setText(parseJSONMembre.getAllStaff_info(rep).getPays());
                        date_naissance.setText(parseJSONMembre.getAllStaff_info(rep).getDate_naissance());
                    }else{
                        Toast.makeText(getApplicationContext(),"Veuillez vérifier votre ip serveur",Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Network is't available",Toast.LENGTH_LONG).show();
                }

                if(httpManager.isOnline()){
                    String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"contrat_travail/search_contrat";
                    resquestPackage.setMethod("GET");
                    resquestPackage.setUri(url);
                    resquestPackage.setParam("id",String.valueOf(id_search));
                    String rep=null;
                    if((rep=httpManager.getData(resquestPackage))!=null){
                        *//*id_search_contrat = parseJSONMembre.getAllStaff_contrat(rep).getId();
                        date_debut_contrat.setText(parseJSONMembre.getAllStaff_contrat(rep).getDate_debut());
                        date_fin_contrat.setText(parseJSONMembre.getAllStaff_contrat(rep).getDate_fin());
                        nbheure_contrat.setText(parseJSONMembre.getAllStaff_contrat(rep).getNbr_heure());*//*
                    }else{
                        Toast.makeText(getApplicationContext(),"Veuillez vérifier votre ip serveur",Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Network is't available",Toast.LENGTH_LONG).show();
                }
            }

        }else{
            email_edit.setEnabled(false);
            id_edit.setEnabled(false);
           // btdelete.setVisibility(View.INVISIBLE);
        }
        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               //String nom_membre = nom_edit.getText().toString();
                int  position = adapter.getPosition(spinner_type.getSelectedItem().toString());
                if(nom_edit.getText().toString().equals("") || prenom_edit.getText().toString().equals("")
                         ){
                    new LovelyInfoDialog(MembreMisejourActivity.this)
                            .setTopColorRes(R.color.bootstrap_brand_warning)
                            //.setIcon(android.R.drawable.ic_dialog_alert)
                            .setTopTitle("Erreur ")
                            .setMessage("Veuillez remplir les champs vides")
                            .show();
                }else{
                    Boolean erreur =true;
                    if(!phone_edit.getText().toString().equals("")){
                        if (phone_edit.getText().toString().isEmpty() || !Patterns.PHONE.matcher(phone_edit.getText().toString()).matches()) {
                            phone_edit.setError("Le numéro du téléphone n'est pas au bon format");
                            erreur = false;
                        } else {
                            phone_edit.setError(null);
                        }
                    }
                    if(!phoneperso.getText().toString().equals("")){
                        if (phoneperso.getText().toString().isEmpty() || !Patterns.PHONE.matcher(phoneperso.getText().toString()).matches()) {
                            phoneperso.setError("Le numéro du téléphone n'est pas au bon format");
                            erreur = false;
                        } else {
                            phone_edit.setError(null);
                        }
                    }

                    if(position == 0){
                        spinner_type.setError(" Veuillez choisir un autre type staff");
                        erreur = false;
                    }else {
                        spinner_type.setError(null);
                    }

                    if(erreur){
                        if(btsave.getText().toString().equalsIgnoreCase("Ajouter")){
                            save_data("NULL",nom_edit.getText().toString().trim(),prenom_edit.getText()
                                    .toString(),spinner_type.getSelectedItem().toString(),phone_edit.getText()
                                    .toString(),phoneperso.getText().toString(),"ajouter","NULL",
                                    adresse_postal1.getText().toString(),adresse_postal2.getText().toString(),
                                    code_postal.getText().toString(),ville.getText().toString(),
                                    pays.getText().toString(),date_naissance.getText().toString(),
                                    "NULL","NULL",date_debut_contrat.getText().toString(),
                                    date_fin_contrat.getText().toString(),nbheure_contrat.getText().toString());
                        }else{
                            boolean error = true;
                            if (email_edit.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email_edit.getText().toString()).matches()) {
                                email_edit.setError("L\'adresse email n\'est pas au bon format");
                                error = false;
                            } else {
                                email_edit.setError(null);
                            }
                            if(error){
                                save_data(String.valueOf(id_search),nom_edit.getText().toString().trim(),prenom_edit.getText()
                                        .toString(),spinner_type.getSelectedItem().toString(),phone_edit.getText()
                                        .toString(),phoneperso.getText().toString(),"modifier",
                                        email_edit.getText().toString(),adresse_postal1.getText().toString(),adresse_postal2.getText().toString(),
                                        code_postal.getText().toString(),ville.getText().toString(),
                                        pays.getText().toString(),date_naissance.getText().toString(),
                                        String.valueOf(id_search_info),String.valueOf(id_search_contrat),date_debut_contrat.getText().toString(),
                                        date_fin_contrat.getText().toString(),nbheure_contrat.getText().toString());
                            }
                        }

                    }

                }
            }
        });*/


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

    public void save_data(String id,String nom,String prenom,String type_staff,String phonepro
                          ,String phoneperso,String action,String email,
                          String adresse1,String adresse2,String code_postal,String ville,
                          String pays,String date_naissance,String id_info,
                          String id_search_contrat,String date_debut,String date_fin,
                          String nbheure){

        if(httpManager.isOnline()){
            String url = httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"membre/save";
            resquestPackage.setMethod("POST");
            resquestPackage.setUri(url);
            resquestPackage.setParam("action",action);
            if(!id.equals("NULL")){
                resquestPackage.setParam("id",id);
            }
            if(!id_info.equals("NULL")){
                resquestPackage.setParam("id_info",id_info);
            }
            if(!id_search_contrat.equals("NULL")){
                resquestPackage.setParam("id_search_contrat",id_search_contrat);
            }
            resquestPackage.setParam("nom",nom);
            resquestPackage.setParam("prenom",prenom);
            resquestPackage.setParam("staff_type",type_staff);
            resquestPackage.setParam("type_staff","save");
            resquestPackage.setParam("telephone_pro",phonepro);
            resquestPackage.setParam("telephone_perso",phoneperso);
            resquestPackage.setParam("mail_pro",email);

            resquestPackage.setParam("adresse1",adresse1);
            resquestPackage.setParam("adresse2",adresse2);
            resquestPackage.setParam("code_postal",code_postal);
            resquestPackage.setParam("ville",ville);
            resquestPackage.setParam("pays",pays);
            resquestPackage.setParam("date_naissance",date_naissance);


            resquestPackage.setParam("date_debut",date_debut);
            resquestPackage.setParam("date_fin",date_fin);
            resquestPackage.setParam("nb_heures_hebdo",nbheure);

            String rep=null;
            if((rep=httpManager.getData(resquestPackage))!=null){
               int res = rep.compareToIgnoreCase("succes");
               if(res==1){
                   Toast.makeText(getApplicationContext(),"Nouvel employé ajouté",Toast.LENGTH_LONG).show();

               }else{
                   Toast.makeText(getApplicationContext(),"Mise à jour de la fiche effectuée avec succès",Toast.LENGTH_LONG).show();
               }
                onBackPressed();
                this.finish();
            }else{
                Toast.makeText(getApplicationContext(),"Veuillez vérifier votre ip serveur",Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(getApplicationContext(),"Network is't available",Toast.LENGTH_LONG).show();
        }
    }
}
