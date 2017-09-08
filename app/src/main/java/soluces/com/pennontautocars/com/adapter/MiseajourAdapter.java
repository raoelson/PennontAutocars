package soluces.com.pennontautocars.com.adapter;

import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.Model.Membre;

/**
 * Created by etiennelawlor on 7/17/14.
 */
public class MiseajourAdapter extends RecyclerView.Adapter<MiseajourAdapter.MyViewHolder> {

    private List<Membre> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView_phone);
            //genre = (TextView) view.findViewById(R.id.textView_phone_perso);
           // year = (TextView) view.findViewById(R.id.year__);
        }
    }


    public MiseajourAdapter(List<Membre> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_details_membre, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Membre movie = moviesList.get(position);
        holder.title.setText(movie.getEmail());
        holder.genre.setText(movie.getNom());
        /*holder.year.setText(movie.getYear());*/
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}