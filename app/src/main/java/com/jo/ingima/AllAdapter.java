package com.jo.ingima;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by jo on 05/07/2017.
 */

public class AllAdapter extends RecyclerView.Adapter<AllViewHolder> {
    private Context mContext;
    private ListCallback mListCallback;

    public AllAdapter(Context context, ListCallback listCallback) {
        this.mContext = context;
        this.mListCallback = listCallback;
    }

    @Override
    public AllViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //each row is the same type
        return new AllViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(AllViewHolder holder, int position) {
        ViewModel itemModel = ModelSingleton.getInstance(mContext).getItemPosition(position);
        holder.bind(itemModel.hour, itemModel.city, itemModel.temp, mListCallback);
    }

    /*@Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof AllViewHolder) {
            ((AllViewHolder) holder).bind();
        }
    }*/

    @Override
    public int getItemCount() {
        return ModelSingleton.getInstance(mContext).getItemsSize();
    }
}
