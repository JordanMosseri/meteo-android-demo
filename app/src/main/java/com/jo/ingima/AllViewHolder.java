package com.jo.ingima;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jo on 05/07/2017.
 */

public class AllViewHolder extends RecyclerView.ViewHolder {

    protected Context context;
    private TextView itemHour;
    private TextView itemCity;
    private TextView itemTemp;

    public AllViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false));

        context = itemView.getContext();
        itemHour = (TextView) itemView.findViewById(R.id.item_hour);
        itemCity = (TextView) itemView.findViewById(R.id.item_city);
        itemTemp = (TextView) itemView.findViewById(R.id.item_temp);
    }

    public void bind(String hour, final String city, int temperature, final ListCallback listCallback) {
        itemHour.setText(hour);
        itemCity.setText(city);
        itemTemp.setText(temperature+"°");

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listCallback.onItemClicked(city);
            }
        });
    }

}
