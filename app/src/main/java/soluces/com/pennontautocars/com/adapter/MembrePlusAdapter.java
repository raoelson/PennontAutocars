package soluces.com.pennontautocars.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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

import com.amulyakhare.textdrawable.TextDrawable;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.utils.QuickReturnUtils;
import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.Model.Membre;
import soluces.com.pennontautocars.com.activity.MembreDetailsActivity;
import soluces.com.pennontautocars.com.activity.MembreMisejourActivity;


/**
 * Created by RAYA on 04/09/2016.
 */
public class MembrePlusAdapter extends RecyclerView.Adapter<MembrePlusAdapter.ViewHolder> {

    // region Member Variables
    private Context mContext;
    private List<Membre> mGooglePlusPosts;
    private Transformation mTransformation;
    private Transformation mTransformation2;
    private int lastPosition = -1;
    // endregion


    // region Constructors
    public MembrePlusAdapter(Context context, List<Membre> googlePlusPosts) {
        mContext = context;
        mGooglePlusPosts = googlePlusPosts;



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
                .inflate(R.layout.items_membres_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    String nom_selected = null;

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Membre post = mGooglePlusPosts.get(position);

        if (post != null) {
            setUpUserImage(holder.mUserImageView, post);
            setUpDisplayName(holder.mDisplayNameTextView, post);
            setUpTimestamp(holder.mTimestampTextView, post);
            setUpMessage(holder.mMessageTextView, post);
            setUpCommenterOneImage(holder.mCommenterOneImageView, post);
            setUpCommenterTwoImage(holder.mCommenterTwoImageView, post);
            setUpCommenterThreeImage(holder.mCommenterThreeImageView, post);
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.up_from_bottom);
                holder.itemView.startAnimation(animation);
            }
            lastPosition = position;
        }
        holder.mCommenterOneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MembreDetailsActivity.class);
                intent.putExtra("id",getItem(position).getId());
                intent.putExtra("details",true);
                mContext.startActivity(intent);

            }
        });

        holder.mCommenterTwoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{""+getItem(position).getEmail()});
                    i.putExtra(Intent.EXTRA_SUBJECT, "");
                    i.putExtra(Intent.EXTRA_TEXT   , "");
                    try {
                        view.getContext().startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        holder.mCommenterThreeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+getItem(position).getTelephone_pro()));
                    view.getContext().startActivity(callIntent);
                }

                catch (Exception ex){
                    Toast.makeText(mContext,"yourActivity is not founded",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGooglePlusPosts.size();
    }
    private void setUpUserImage(ImageView iv, Membre post) {
        String avatarUrl = post.getImage();
        String letter = String.valueOf(post.getNom().charAt(0));

        TextDrawable drawable;
        drawable = TextDrawable.builder()
                .buildRound(letter,post.getTheme());
        if (!TextUtils.isEmpty(avatarUrl)) {
            Picasso.with(iv.getContext())
                    .load(avatarUrl)
                    .transform(mTransformation)
                    .centerCrop()
                    .resize(QuickReturnUtils.dp2px(mContext, 50),
                            QuickReturnUtils.dp2px(mContext, 50))
//                    .placeholder(R.drawable.ic_facebook)
                    .error(drawable)
                    .into(iv);
        }
    }

    private void setUpDisplayName(TextView tv, Membre post) {
        String displayName = post.getNom();
        if (!TextUtils.isEmpty(displayName)) {
            tv.setText(Html.fromHtml("<b>"+displayName +" "+ post.getPrenom() + "</b> "));
        }
    }


    private void setUpTimestamp(TextView tv, Membre post) {
        String timestamp = post.getStaff_type();
        if (!TextUtils.isEmpty(timestamp)) {
            tv.setText(timestamp);
        }
    }

    private void setUpMessage(TextView tv, Membre post) {
        String message = post.getEmail();
        if (!TextUtils.isEmpty(message)) {
            tv.setText(Html.fromHtml("<b>" + message + "</b> "));
        }
    }

    private void setUpCommenterOneImage(ImageView iv, Membre post) {
        Picasso.with(iv.getContext())
                .load(android.R.drawable.ic_menu_view)
                .transform(mTransformation2)
                .centerCrop()
                .resize(QuickReturnUtils.dp2px(mContext, 34),
                        QuickReturnUtils.dp2px(mContext, 34))
                .error(android.R.drawable.stat_notify_error)
                .into(iv);
    }

    private void setUpCommenterTwoImage(ImageView iv, Membre post) {

            Picasso.with(iv.getContext())
                    .load(android.R.drawable.ic_dialog_email)
                    .transform(mTransformation2)
                    .centerCrop()
                    .resize(QuickReturnUtils.dp2px(mContext, 34),
                            QuickReturnUtils.dp2px(mContext, 34))
                    .error(android.R.drawable.stat_notify_error)
                    .into(iv);
    }

    private void setUpCommenterThreeImage(ImageView iv, Membre post) {
            Picasso.with(iv.getContext())
                    .load(android.R.drawable.ic_menu_call)
                    .transform(mTransformation2)
                    .centerCrop()
                    .resize(QuickReturnUtils.dp2px(mContext, 34),
                            QuickReturnUtils.dp2px(mContext, 34))
                    .error(android.R.drawable.stat_notify_error)
                    .into(iv);

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.user_iv)
        ImageView mUserImageView;
        @Bind(R.id.display_name_tv)
        TextView mDisplayNameTextView;
        @Bind(R.id.timestamp_tv)
        TextView mTimestampTextView;
        @Bind(R.id.message_tv)
        TextView mMessageTextView;
        @Bind(R.id.image_voir)
        ImageView mCommenterOneImageView;
        @Bind(R.id.image_editer)
        ImageView mCommenterTwoImageView;
        @Bind(R.id.image_email)
        ImageView mCommenterThreeImageView;
        @Bind(R.id.indicator_v)
        View mIndicatorView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public Membre getItem(int paramInt) {
        // TODO Auto-generated method stub
        return mGooglePlusPosts.get(paramInt);
    }

}
