package soluces.com.pennontautocars.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.utils.QuickReturnUtils;
import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.Model.Client;
import soluces.com.pennontautocars.com.Model.Membre;
import soluces.com.pennontautocars.com.activity.MembreDetailsActivity;

/**
 * Created by RAYA on 21/09/2016.
 */
public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {

    // region Member Variables
    private Context mContext;
    private List<Client> mClient;
    private Transformation mTransformation;
    private Transformation mTransformation2;
    private int lastPosition = -1;
    // endregion

    // region Constructors
    public ClientAdapter(Context context, List<Client> clients) {
        mContext = context;
        mClient = clients;
        mTransformation = new RoundedTransformationBuilder()
//                .borderColor(getContext().getResources().getColor(R.color.white))
                .cornerRadius(QuickReturnUtils.dp2px(mContext, 50))
                .build();

        mTransformation2 = new RoundedTransformationBuilder()
//                .borderColor(getContext().getResources().getColor(R.color.white))
                .cornerRadius(QuickReturnUtils.dp2px(mContext, 2))
                .build();
    }
    // endregion

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_client_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Client post = mClient.get(position);

        if (post != null) {
            /*setUpUserImage(holder.mUserImageView, post);*/
            setUpDisplayName(holder.mDisplayNameTextView, post);
            setUpTimestamp(holder.mTimestampTextView, post);
            setUpMessage(holder.mMessageTextView, post);
            setUpCommenterOneImage(holder.mCommenterOneImageView);
            setUpCommenterTwoImage(holder.mCommenterTwoImageView);
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.up_from_bottom);
                holder.itemView.startAnimation(animation);
            }
            lastPosition = position;
        }
        holder.mCommenterOneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String number =getItem(position).getTelephone();
                    if(!number.isEmpty()){
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:"+number));
                        view.getContext().startActivity(callIntent);
                    }else{
                        Toast.makeText(mContext,"Aucun numéro",Toast.LENGTH_LONG).show();
                    }

                }

                catch (Exception ex){
                    Toast.makeText(mContext,"yourActivity is not founded",Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.mCommenterTwoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{""+getItem(position).getEmail()});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    view.getContext().startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*holder.mCommenterThreeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    //callIntent.setData(Uri.parse("tel:"+getItem(position).getTelephone_pro()));
                    view.getContext().startActivity(callIntent);
                }

                catch (Exception ex){
                    Toast.makeText(mContext,"yourActivity is not founded",Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mClient.size();
    }

    private void setUpUserImage(ImageView iv, Membre post) {
        String avatarUrl = post.getImage();
        if (!TextUtils.isEmpty(avatarUrl)) {
            Picasso.with(iv.getContext())
                    .load(avatarUrl)
                    .transform(mTransformation)
                    .centerCrop()
                    .resize(QuickReturnUtils.dp2px(mContext, 50),
                            QuickReturnUtils.dp2px(mContext, 50))
//                    .placeholder(R.drawable.ic_facebook)
                    .error(R.drawable.profiles)
                    .into(iv);
        }
    }

    private void setUpDisplayName(TextView tv, Client post) {
        String displayName = post.getRaison_sociale();
        if (!TextUtils.isEmpty(displayName)) {
            tv.setText(Html.fromHtml("<b>"+displayName + "</b> "));
        }
    }


    private void setUpTimestamp(TextView tv, Client post) {
        String timestamp = post.getEmail();
        if (!TextUtils.isEmpty(timestamp)) {
            tv.setText(timestamp);
        }
    }

    private void setUpMessage(TextView tv, Client post) {
        String message = post.getTelephone();
        if (!TextUtils.isEmpty(message)) {
            tv.setText(Html.fromHtml("<b>" + message + "</b> "));
        }
    }

    private void setUpCommenterOneImage(ImageView iv) {
        Picasso.with(iv.getContext())
                .load(android.R.drawable.ic_menu_call)
                .transform(mTransformation2)
                .centerCrop()
                .resize(QuickReturnUtils.dp2px(mContext, 34),
                        QuickReturnUtils.dp2px(mContext, 34))
                .error(android.R.drawable.stat_notify_error)
                .into(iv);
    }

    private void setUpCommenterTwoImage(ImageView iv) {

        Picasso.with(iv.getContext())
                .load(android.R.drawable.ic_dialog_email)
                .transform(mTransformation2)
                .centerCrop()
                .resize(QuickReturnUtils.dp2px(mContext, 34),
                        QuickReturnUtils.dp2px(mContext, 34))
                .error(android.R.drawable.stat_notify_error)
                .into(iv);
    }

   /* private void setUpCommenterThreeImage(ImageView iv) {
        Picasso.with(iv.getContext())
                .load(android.R.drawable.ic_menu_call)
                .transform(mTransformation2)
                .centerCrop()
                .resize(QuickReturnUtils.dp2px(mContext, 34),
                        QuickReturnUtils.dp2px(mContext, 34))
                .error(android.R.drawable.stat_notify_error)
                .into(iv);

    }*/
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /*@Bind(R.id.user_iv)
        ImageView mUserImageView;*/
        @Bind(R.id.display_name_tv_)
        TextView mDisplayNameTextView;
        @Bind(R.id.timestamp_tv_)
        TextView mTimestampTextView;
        @Bind(R.id.message_tv_)
        TextView mMessageTextView;
        @Bind(R.id.image_voir_)
        ImageView mCommenterOneImageView;
        @Bind(R.id.image_editer_)
        ImageView mCommenterTwoImageView;
        /*@Bind(R.id.image_email_)
        ImageView mCommenterThreeImageView;*/
        @Bind(R.id.indicator_v_)
        View mIndicatorView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public Client getItem(int paramInt) {
        // TODO Auto-generated method stub
        return mClient.get(paramInt);
    }

}
