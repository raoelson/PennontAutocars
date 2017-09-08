package soluces.com.pennontautocars.com.ws;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by RAYA on 29/08/2016.
 */
public class HttpManager {
    Context mcontext;
    public HttpManager(Context ct){
        this.mcontext = ct;
    }

    public String URL_serveur(){
        return "http://";
    }

    public Boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnectedOrConnecting()){
            if(networkInfo.getType()!=ConnectivityManager.TYPE_WIFI && networkInfo.getType()!=ConnectivityManager.TYPE_MOBILE){
                Toast.makeText(mcontext,"Veuillez activer votre WIFI svp",Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return false;
    }

    public String getData(ResquestPackage p){
        BufferedReader bufferedReader = null;
        String uri = p.getUri();
        if(p.getMethod().equals("GET")){
            uri += "?" + p.getEncodeParams();
        }
        try {
            URL url = new URL(uri);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(p.getMethod());
            if(p.getMethod().equals("POST")){
                httpURLConnection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
                outputStreamWriter.write(p.getEncodeParams());
                outputStreamWriter.flush();
            }
            StringBuffer stringBuffer = new StringBuffer();
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while ((line= bufferedReader.readLine())!=null){
                stringBuffer.append(line+"\n");
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }


}
