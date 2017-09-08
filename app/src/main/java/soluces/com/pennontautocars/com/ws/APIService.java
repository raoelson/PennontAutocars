package soluces.com.pennontautocars.com.ws;



import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import soluces.com.pennontautocars.com.Model.Chat;
import soluces.com.pennontautocars.com.Model.Membre;
import soluces.com.pennontautocars.com.Model.ServerResponse;


/**
 * Created by RAYA on 12/02/2017.
 */

public interface APIService {


    @FormUrlEncoded
    @POST("membre")
    Call<JsonObject> getAuth(@Field("email") String email, @Field("password") String password,
             @Field("theme") String theme, @Field("theme1") String theme1);


    @Multipart
    @POST("chat/addPic")
    Call<ServerResponse> getSendFile(@Part MultipartBody.Part file, @Part("file") RequestBody name,
                                     @Part("send") RequestBody send, @Part("receive") RequestBody receive,
                                     @Part("messages") RequestBody messages);
}
