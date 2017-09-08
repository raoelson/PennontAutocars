package soluces.com.pennontautocars;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sinch.android.rtc.SinchError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import soluces.com.pennontautocars.com.Model.Membre;
import soluces.com.pennontautocars.com.Model.ServerResponse;
import soluces.com.pennontautocars.com.app.MyApplication;
import soluces.com.pennontautocars.com.gcm.GcmIntentService;
import soluces.com.pennontautocars.com.gcm.SinchService;
import soluces.com.pennontautocars.com.video.BaseActivity;
import soluces.com.pennontautocars.com.video.PlaceCallActivity;
import soluces.com.pennontautocars.com.ws.Connexion;
import soluces.com.pennontautocars.com.ws.HttpManager;
import soluces.com.pennontautocars.com.ws.ResquestPackage;

public class MainActivity extends BaseActivity implements SinchService.StartFailedListener {


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private final String TAG_ = this.getClass().getName();
    private static final int REQUEST_SIGNUP = 0;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private String ipserveur = null;
    private String email, password;
    private Boolean checkFlag;

    EditText _emailText;
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;
    CheckBox _remember;
    private HttpManager httpManager;
    private ResquestPackage resquestPackage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resquestPackage = new ResquestPackage();
        httpManager = new HttpManager(this);
        /*editor.putString("ipserver", "autocarspennont.fr/ws/");*/


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        _remember = (CheckBox) findViewById(R.id.remember_);
        checkFlag = _remember.isChecked();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        editor = preferences.edit();


        //editor.putString("ipserver", "10.0.3.2/codeigniter/");
        editor.putString("ipserver", "autocarspennont.fr/ws/dev/autocarspennont/");

        editor.apply();
        editor.commit();

        String login_ = preferences.getString("login","");
        String password_= preferences.getString("password","");
        if(!login_.equals("") && !password_.equals("")){
            _emailText.setText(login_);
            _passwordText.setText(password_);
            email = _emailText.getText().toString();
            password = _passwordText.getText().toString();
            onLoginSuccess();
        }
        
        ButterKnife.bind(this);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        _remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkFlag = b;
                /*Log.e(this.getClass().getName(),""+checkFlag);*/
            }
        });
    }

    public void login() {

        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        // TODO: Implement your own authentication logic here.

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        _loginButton.setEnabled(true);
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }
    String reponse = null;
    public void onLoginSuccess() {

        if(preferences.getString("ipserver","")!=null){
            if(httpManager.isOnline()){
                doInBackground(httpManager.URL_serveur()+""+preferences.getString("ipserver",""));
            }else{
                Toast.makeText(getApplicationContext(),"Network is't available",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onLoginFailed() {
        //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("L\'adresse email n\'est pas au bon format");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            _passwordText.setError("between 4 and 20 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }



    private void registerGCM() {
        Intent intent = new Intent(getApplicationContext(), GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog((Activity) getApplicationContext(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();

            }
            return false;
        }
        return true;
    }

    protected void doInBackground(String p) {
        Connexion connexion = new Connexion(p);
        ColorGenerator generator = ColorGenerator.MATERIAL;
        String theme1 = "";
        if(!preferences.getString("theme","").isEmpty()){
            theme1 =  preferences.getString("theme","");
        }
        String theme = String.valueOf(generator.getRandomColor());

        Call<JsonObject > mService = connexion.getInterfaceService().getAuth(email,password,
                theme,theme1);
        mService.enqueue(new Callback<JsonObject  >() {
            @Override
            public void onResponse(Call<JsonObject > call, retrofit2.Response<JsonObject > response) {
                try {

                    if(response.body() == null){
                        Log.d(this.getClass().getName(),"reponses "+response.body());
                    }else{
                        JSONObject obj = new JSONObject(response.body().toString());
                        if(obj.getBoolean("color") == false){
                            editor.remove("theme");
                            editor.apply();
                            editor.commit();
                        }
                        // check for error flag
                        if (obj.getBoolean("error") == false) {
                            JSONObject userObj = obj.getJSONObject("user");
                            editor.putString("id_user", userObj.getString("id"));
                            editor.putString("nom_user", userObj.getString("nom")+" "+userObj.getString("prenom"));
                            //editor.putString("nom_user_appel", userObj.getString("nom"));
                            editor.putString("mail_pro", userObj.getString("mail_pro"));
                            editor.apply();
                            editor.commit();
                            if (checkPlayServices()) {
                                registerGCM();

                            }
                            if(checkFlag){
                                editor.putString("login", email);
                                editor.putString("password", password);
                                editor.apply();
                                editor.commit();

                            }else{

                                if(!(preferences.getString("login","").equals("") &&
                                        preferences.getString("password","").equals(""))){
                                    editor.remove("login");
                                    editor.remove("password");
                                    editor.apply();
                                    editor.commit();
                                }
                                _emailText.setText("");
                                _passwordText.setText("");
                            }
                            if (!getSinchServiceInterface().isStarted()) {
                                getSinchServiceInterface().startClient(userObj.getString("nom"));
                            }else {
                                Intent mainActivity = new Intent(getApplicationContext(), soluces.com.pennontautocars.com.activity.MainActivity.class);
                                startActivity(mainActivity);
                            }
                            /*Intent mainActivity = new Intent(getApplicationContext(), PlaceCallActivity.class);
                            startActivity(mainActivity);*/

                            // start main activity
                            startActivity(new Intent(getApplicationContext(), soluces.com.pennontautocars.com.activity.MainActivity.class));
                           // finish();

                        } else {
                            // login error - simply toast the message
                            Toast.makeText(getApplicationContext(), "Votre Email/Mot de passe incorrect", Toast.LENGTH_LONG).show();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject > call, Throwable t) {
                Log.d(this.getClass().getName(),"error");
            }
        });

    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }


    @Override
    public void onStartFailed(SinchError error) {
        Log.e(this.getClass().getName(),"reponses "+error);
    }

    @Override
    public void onStarted() {

    }
}


