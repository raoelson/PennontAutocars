package soluces.com.pennontautocars.com.ws;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RAYA on 12/02/2017.
 */

public class Connexion {

    public String base_url;

    public Connexion(String url) {
        this.base_url = url;
    }

    public APIService getInterfaceService() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService apiService = retrofit.create(APIService.class);
        return apiService;
    }
}
