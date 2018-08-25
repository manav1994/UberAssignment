package com.manav.uberassignment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.manav.uberassignment.R;
import com.manav.uberassignment.cache.ImageLoader;
import com.manav.uberassignment.model.AddToAdapter;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {

    private final ImageLoader imgLoader;
    List<AddToAdapter> addToAdapters;
    Context context;


    public GridAdapter(Context context, ArrayList<AddToAdapter> addToAdapters) {
        this.context = context;
        this.addToAdapters = addToAdapters;
        imgLoader = new ImageLoader(context.getApplicationContext());

    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        GridViewHolder gridViewHolder = new GridViewHolder(v);
        return gridViewHolder;
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        if (!TextUtils.isEmpty(addToAdapters.get(position).getName()))
            holder.name.setText(addToAdapters.get(position).getName());
        if (!TextUtils.isEmpty(addToAdapters.get(position).getUrl()))
            imgLoader.DisplayImage(addToAdapters.get(position).getUrl(), R.color.cardview_light_background, holder.image);
    }

    @Override
    public int getItemCount() {
        return addToAdapters.size();
    }

    public void clearList() {
        addToAdapters.clear();
        notifyDataSetChanged();
    }


    class GridViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public GridViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public void setNewData(List<AddToAdapter> newData) {
        for (AddToAdapter a : newData) {
            addToAdapters.add(a);
        }
        notifyDataSetChanged();
    }
}
