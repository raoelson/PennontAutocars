package soluces.com.pennontautocars.com.adapter;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;

import java.util.List;

import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.Model.MessagesGroupes;
import soluces.com.pennontautocars.com.app.Config;


/**
 * Created by RAYA on 16/11/2016.
 */

public class GroupsCommentAdapter  extends RecyclerView.Adapter<GroupsCommentAdapter.MyViewHolder> {

    private List<MessagesGroupes> messagesGroupesList;
    private Context context;
    private int itemsCount = 0;
    private int lastAnimatedPosition = -1;
    private int avatarSize;

    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    ColorGenerator generator = ColorGenerator.MATERIAL;
    SharedPreferences preferences;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView message,title,icNewCount;
        public ImageView groupesDimage;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tvComment_);
            icNewCount = (TextView) view.findViewById(R.id.icNewCount_);
            message = (TextView) view.findViewById(R.id.chatListItemHints);
            groupesDimage = (ImageView) view.findViewById(R.id.groupesDimage);

        }
    }
    public GroupsCommentAdapter(Context context,List<MessagesGroupes> groupes) {
        this.messagesGroupesList = groupes;
        this.context = context;
        avatarSize = context.getResources().getDimensionPixelSize(R.dimen.comment_avatar_size);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_groups_chat, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        MessagesGroupes messagesGroupe = messagesGroupesList.get(position);
        holder.title.setText(messagesGroupe.getMembre().getNom()+" "
                +messagesGroupe.getMembre().getPrenom());
        holder.message.setText(messagesGroupe.getMessages());
        holder.icNewCount.setText(Config.getTimeAgo(messagesGroupe.getTimeAt()));

        //        Get the first letter of list item
        String letter = String.valueOf(messagesGroupe.getMembre().getNom().charAt(0));
//        Create a new TextDrawable for our image's background
        TextDrawable drawable;
        if(messagesGroupe.getMembre().getTheme() == 0){
            drawable = TextDrawable.builder()
                    .buildRound(letter, Integer.parseInt(preferences.getString("theme","")));
        }else{
            drawable = TextDrawable.builder()
                    .buildRound(letter, messagesGroupe.getMembre().getTheme());
        }


        Picasso.with(context)
                .load(messagesGroupe.getMembre().getImage())
                .centerCrop()
                .resize(avatarSize, avatarSize)
                //.transform(new RoundedTransformation())
                .error(drawable)
                .into(holder.groupesDimage);

    }

    @Override
    public int getItemCount() {
        return messagesGroupesList.size();
    }

    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }

    public void setAnimationsLocked(boolean animationsLocked) {
        this.animationsLocked = animationsLocked;
    }

    public void setDelayEnterAnimation(boolean delayEnterAnimation) {
        this.delayEnterAnimation = delayEnterAnimation;
    }
}