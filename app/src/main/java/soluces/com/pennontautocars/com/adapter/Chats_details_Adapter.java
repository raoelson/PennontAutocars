package soluces.com.pennontautocars.com.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import library.utils.QuickReturnUtils;
import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.Model.Chat;
import soluces.com.pennontautocars.com.app.Config;
import soluces.com.pennontautocars.com.ws.HttpManager;

/**
 * Created by RAYA on 15/09/2016.
 */
public class Chats_details_Adapter extends RecyclerView.Adapter<Chats_details_Adapter.MyViewHolder> {

    private List<Chat> mList;

    private int SELF = 100,color = -1710619;
    private int test_message = 0;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    View activity_;

    SharedPreferences preferences;

    Context mcontext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView chatDate_chat, chatMessage_chat;
        ImageView image_chat,attach_chat;


        public MyViewHolder(View view) {
            super(view);
            chatMessage_chat = (TextView) itemView.findViewById(R.id.chatMessage_chat);
            chatDate_chat = (TextView) itemView.findViewById(R.id.chatDate_chat);
            image_chat = (ImageView) itemView.findViewById(R.id.image_chat);
            attach_chat = (ImageView) itemView.findViewById(R.id.img_chat);

        }
    }
    private LayoutInflater mLayoutInflater;
    String url;

    public Chats_details_Adapter( Context context, List<Chat> mLists, String url) {
        this.mcontext = context;
        this.mList = mLists;
        this.url = url;
        preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView ;
               /* = mLayoutInflater
                .inflate(R.layout.item_message, parent, false);*/
        if (viewType == SELF) {

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_gauche, parent, false);
        } else {

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_droite, parent, false);
        }
        return new MyViewHolder(itemView);
    }
    @Override
    public int getItemViewType(int position) {
        Chat message = mList.get(position);
        if (message.getMembre().getId() == Integer.parseInt(preferences.getString("id_user",""))) {
            return SELF;
        }

        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Chat chat = mList.get(position);
        String nomSR = chat.getMembre().getNom();
        String imageSR = chat.getMembre().getImage();
        int i=-1710619;
        if(chat.getMessage().equalsIgnoreCase("Objet") && !chat.getAttach().equalsIgnoreCase("")){
            holder.attach_chat.setVisibility(View.VISIBLE);
            //holder.attach_chat.setImageBitmap(BitmapFactory.decodeFile(url+"uploads/"+chat.getAttach()));

            try {
                File mediaStorageDir = new File(
                        Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                        "autocarspennont");
                File fichier_image = new File(mediaStorageDir+"/"+chat.getAttach());
                String  newurl = null;
                if(fichier_image.exists()){
                    newurl = mediaStorageDir+"/"+chat.getAttach();
                }else{
                    this.downloadFile(url+"uploads/"+chat.getAttach(),mediaStorageDir+"/"+chat.getAttach());
                    newurl = mediaStorageDir+"/"+chat.getAttach();
                }
                holder.attach_chat.setImageBitmap(BitmapFactory.decodeFile(newurl));

            } catch (Exception e) {
                e.printStackTrace();
            }

            /*setUpAttach(holder.attach_chat,url+"uploads/"+chat.getAttach());*/
            holder.chatMessage_chat.setVisibility(View.GONE);
        }else{
            holder.attach_chat.setVisibility(View.GONE);
            holder.chatMessage_chat.setVisibility(View.VISIBLE);
            holder.chatMessage_chat.setText(chat.getMessage());
        }

        holder.chatDate_chat.setText(Config.getTimeAgo(Integer.parseInt(chat.getCreatedAt())));

        if(chat.getMembre().getId() == Integer.parseInt(preferences.getString("id_user",""))){
            i=-1337522;
        }

        setImage(holder.image_chat,nomSR,imageSR,i);
    }

    private void setImage(ImageView iv, String nom,String image,int color) {
        String letter = String.valueOf(nom.charAt(0));
        TextDrawable  drawable;
        drawable = TextDrawable.builder()
                .buildRound(letter,color);
        Picasso.with(iv.getContext())
                .load(image)
                //.transform(mTransformation2)
                .centerCrop()
                .resize(QuickReturnUtils.dp2px(mcontext, 34),
                        QuickReturnUtils.dp2px(mcontext, 34))
                .error(drawable)
                .into(iv);
    }
    public void setAnimationsLocked(boolean animationsLocked) {
        this.animationsLocked = animationsLocked;
    }

    public void setDelayEnterAnimation(boolean delayEnterAnimation) {
        this.delayEnterAnimation = delayEnterAnimation;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void downloadFile(String source,String dest){
        try {
            int count;
            URL url = new URL(source);
            URLConnection conection = url.openConnection();
            conection.connect();
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            OutputStream output = new FileOutputStream(dest);
            byte data[] = new byte[1024];
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
    }
}
