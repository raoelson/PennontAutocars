package soluces.com.pennontautocars.com.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Date;
import java.util.List;

import library.utils.QuickReturnUtils;
import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.Model.Chat;
import soluces.com.pennontautocars.com.app.Config;

/**
 * Created by RAYA on 15/10/2016.
 */

public class DiscussionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Chat> list;
    private LayoutInflater inflater;
    private Transformation mTransformation;
    private Context context;
    Integer idsend;
    String letter= null;
    ColorGenerator generator = ColorGenerator.MATERIAL;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public DiscussionAdapter(Context ctx, List<Chat> list,Integer idsend) {
        this.list = list;
        this.context = ctx;
        this.idsend = idsend;
        mTransformation = new RoundedTransformationBuilder()
//                .borderColor(getContext().getResources().getColor(R.color.white))
                .cornerRadius(QuickReturnUtils.dp2px(ctx, 50))
                .build();
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = preferences.edit();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_discussion, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder= (MyHolder) holder;
        Chat chat = list.get(position);
        if(chat.getMembre().getId() != 0){
//        Get the first letter of list item
            letter = String.valueOf(chat.getMembre().getNom().charAt(0));
//        Create a new TextDrawable for our image's background
            TextDrawable  drawable;
            drawable = TextDrawable.builder()
                    .buildRound(letter, generator.getRandomColor());

            if(chat.getMembre().getTheme() == 0){
                if(preferences.getString("theme","").equals("")){
                    editor.putString("theme", String.valueOf(generator.getRandomColor()));
                    editor.apply();
                    editor.commit();
                    drawable = TextDrawable.builder()
                            .buildRound(letter, Integer.parseInt(preferences.getString("theme","")));
                }else{
                    drawable = TextDrawable.builder()
                            .buildRound(letter, Integer.parseInt(preferences.getString("theme","")));
                }

            }else{
                drawable = TextDrawable.builder()
                        .buildRound(letter,chat.getMembre().getTheme());
            }

            //gmailVH.letter.setImageDrawable(drawable);


            myHolder.imageChat.setImageDrawable(drawable);
            myHolder.imageChat.setVisibility(View.VISIBLE);
            myHolder.itemB.setVisibility(View.GONE);
            myHolder.txtCount.setVisibility(View.GONE);
            ;
            myHolder.nomMembre.setText(chat.getMembre().getNom()+" "+chat.getMembre().getPrenom());
            if(chat.getMembre().getOnline() == 1){
                myHolder.v.setText("Connecté");
            }else{
                if(!(chat.getMembre().getTimeStart() == 0)){
                    myHolder.v.setText("Déconnecté "+Config.getTimeAgo(chat.getMembre().getTimeStart()));
                }else{
                    myHolder.v.setText("Déconnecté");
                }

            }
           /* if(chat.getMembre().getTimeStart()== 0){
            }else{
                holder.v.setText(""+Config.getTimeAgo(chat.getMembre().getTimeStart()));
            }*/

            myHolder.messages.setText(chat.getMessage());



        }
        //holder.messages.setText(chat.getMessage());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{

        ImageView imageChat,itemB;
        TextView nomMembre;
        TextView messages;
        TextView v,txtCount;

        // create constructor to get widget reference
        public MyHolder(View convertView) {
            super(convertView);
            imageChat = (ImageView) convertView.findViewById(R.id.chatimage);
            nomMembre = (TextView) convertView.findViewById(R.id.chatListItemName);
            messages = (TextView) convertView.findViewById(R.id.chatListItemHints);
            v= (TextView)convertView.findViewById(R.id.chatListItemDate); //Or just use Butterknife!
            txtCount = (TextView) convertView.findViewById(R.id.icNewCount);
            itemB = (ImageView) convertView.findViewById(R.id.chatListItemMenuButton);

        }

    }
}
