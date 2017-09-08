package soluces.com.pennontautocars.com.ws;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soluces.com.pennontautocars.com.Model.Membre;

/**
 * Created by RAYA on 29/08/2016.
 */
public class ResquestPackage {
    private String uri;
    private String method="GET";
    private Map<String,String> params = new HashMap<>();


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
    public void setParam(String key,String value){
        params.put(key,value);
    }



    public String getEncodeParams(){
        StringBuilder sb = new StringBuilder();
        String value = null;
        for (String key : params.keySet()){
            try {
                 value = URLEncoder.encode(params.get(key),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if(sb.length()>0){
                sb.append("&");
            }
            sb.append(key+ "=" +value);
        }
       return sb.toString();
    }


}
