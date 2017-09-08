package soluces.com.pennontautocars.com.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.Model.ItemAttach;

/**
 * Created by RAYA on 10/02/2017.
 */

public class AttachAdapter extends ArrayAdapter<ItemAttach> {

    private final List<ItemAttach> list;
    private final Activity context;

    static class ViewHolder {
        protected TextView name;
        protected ImageView flag;
    }

    public AttachAdapter(Activity context, List<ItemAttach> list) {
        super(context, R.layout.content_file, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.content_file, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.flag = (ImageView) view.findViewById(R.id.flag);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).getName());
        holder.flag.setImageDrawable(list.get(position).getFlag());
        return view;
    }

    @Nullable
    @Override
    public ItemAttach getItem(int position) {
        return list.get(position);
    }
}