package soluces.com.pennontautocars.com.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.JSON.ParseJSONMembre;
import soluces.com.pennontautocars.com.Model.Membre;
import soluces.com.pennontautocars.com.adapter.GroupesListesAdapter;
import soluces.com.pennontautocars.com.adapter.GrpMembre;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;

/**
 * Created by RAYA on 08/11/2016.
 */

public class NewGroupActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView contactList;
    SharedPreferences preferences;
    private HttpManager httpManager;
    private ResquestPackage resquestPackage;
    private ImageButton takephoto,browseimage;
    private  ImageView imageprofile;

    GrpMembre adapteur_;

    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;

    private EditText input_nom_grp;
    private CoordinatorLayout Coordinatorlayout;
    private String photoSelect ="vide";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_newgroups);
        toolbar = (Toolbar) findViewById(R.id.toolbar_newgroupe);

        takephoto = (ImageButton) findViewById(R.id.takephoto);
        browseimage = (ImageButton) findViewById(R.id.browseimage);
        imageprofile = (ImageView) findViewById(R.id.imageprofile);
        input_nom_grp = (EditText) findViewById(R.id.input_nom_grp);
        Coordinatorlayout = (CoordinatorLayout) findViewById(R.id.CoordinatorLayout);

       /* cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());*/

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Nouveau groupe");
        resquestPackage = new ResquestPackage();
        httpManager = new HttpManager(this);
        preferences =  PreferenceManager.getDefaultSharedPreferences(this);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        contactList = (RecyclerView) findViewById(R.id.contactList);
        getDataMembre();

        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                   /* startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
                    cameraPhoto.addToGallery();*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        browseimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivityForResult(galleryPhoto.openGalleryIntent(),GALLERY_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(requestCode == CAMERA_REQUEST){
            String photopath = cameraPhoto.getPhotoPath();
            photoSelect = photopath;
            try {
                Bitmap bitmap = ImageLoader.init().from(photopath).requestSize(512,512).getBitmap();
                imageprofile.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else if(requestCode == GALLERY_REQUEST){
            Uri uri= data.getData();
            galleryPhoto.setPhotoUri(uri);
            String photopath = galleryPhoto.getPath();
            photoSelect = photopath;
            try {
                Bitmap bitmap = ImageLoader.init().from(photopath).requestSize(512,512).getBitmap();
                imageprofile.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"image no",Toast.LENGTH_LONG);
            }
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menugroups, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.menu_saves:
                save();
                break;
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void save()
    {

        if(!input_nom_grp.getText().toString().equals("")) {
            List<Membre> membreList = adapteur_.getMembreList();
            String imageEncode =null;
            String photoenvoye = null;
            List<Membre> addMembre = new ArrayList<>();
           /* try {
                if(!photoSelect.equalsIgnoreCase("vide")){
                    Bitmap bitmap = ImageLoader.init().from(photoSelect).requestSize(1024,1024).getBitmap();
                    if(bitmap.sameAs(bitmap)){
                        imageEncode = ImageBase64.encode(bitmap);
                        photoenvoye = imageEncode;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }*/


            for (int i = 0; i < membreList.size(); i++) {
                Membre membre = membreList.get(i);
                Membre membre_ = new Membre();
                if (membre.isSelected() == true) {
                    membre_.setId(membre.getId());
                    addMembre.add(membre_);
                }
                if (i == 0) {
                    Membre membres = new Membre();
                    membres.setId(Integer.parseInt(preferences.getString("id_user", "")));
                    addMembre.add(membres);
                }
            }
            if(preferences.getString("ipserver","")!=null){
                if(httpManager.isOnline()){
                    ColorGenerator generator = ColorGenerator.MATERIAL;
                    String url = httpManager.URL_serveur() + "" + preferences
                            .getString("ipserver", "") + "groupes/AddGroupes";
                    resquestPackage.setMethod("POST");
                    resquestPackage.setUri(url);
                    resquestPackage.setParam("nom_groupe",input_nom_grp.getText().toString());
                    resquestPackage.setParam("id_admin", String.valueOf(preferences.getString("id_user","")));
                    resquestPackage.setParam("membres",new Gson().toJson(addMembre));
                    resquestPackage.setParam("themes",String.valueOf(generator.getRandomColor()));
                    //resquestPackage.setParam("img_grp",photoenvoye);
                    asynchrone_data asynchroneData = new asynchrone_data();
                    asynchroneData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,resquestPackage);
                }else{
                    Toast.makeText(this,"Network is't available",Toast.LENGTH_LONG).show();
                }
            }
        }

    }
    private class asynchrone_data extends AsyncTask<ResquestPackage,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(ResquestPackage... strings) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return(httpManager.getData(strings[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            displayData(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            displayData(values[0]);
        }
    }
    public void displayData(String param){

        if(param!=null){
            JSONObject obj = null;
            try {
                obj = new JSONObject(param);
                if (obj.getBoolean("error") == false) {
                    onBackPressed();
                }else{
                    Snackbar snackbar = Snackbar.make(Coordinatorlayout,"Ce Nom du groupe est déjà pris",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(),""+data,Toast.LENGTH_LONG);
        if(resultCode == RESULT_OK){
            if(resultCode == CAMERA_REQUEST){
                String path = cameraPhoto.getPhotoPath();
                try {
                    Bitmap bitmap = ImageLoader.init().from(path).requestSize(512,512).getBitmap();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                }
                Log.d(this.getClass().getName(),path);
            }
        }else if(requestCode == GALLERY_REQUEST){
            Uri uri = data.getData();
            galleryPhoto.setPhotoUri(uri);
            String photopath = galleryPhoto.getPath();
            try {
                Toast.makeText(getApplicationContext(),photopath,Toast.LENGTH_LONG);
                Bitmap bitmap = ImageLoader.init().from(photopath).requestSize(512,512).getBitmap();
                imageprofile.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"image no",Toast.LENGTH_LONG);
            }
        }
    }*/

    public void getDataMembre(){
        if(preferences.getString("ipserver","")!=null){
            if(httpManager.isOnline()){
                ParseJSONMembre parseJSONMembre = new ParseJSONMembre();
                String url =httpManager.URL_serveur()+""+preferences.getString("ipserver","")+"membre/searchDetails";
                resquestPackage.setMethod("GET");
                resquestPackage.setUri(url);

                 adapteur_ = new GrpMembre(getBaseContext(),
                        parseJSONMembre.getAllStaff(httpManager.getData(resquestPackage)));
                contactList.setAdapter(adapteur_);
                contactList.setLayoutManager(new LinearLayoutManager(this));
            }else{
                Toast.makeText(this,"Network is't available",Toast.LENGTH_LONG).show();
            }
        }
    }
}
