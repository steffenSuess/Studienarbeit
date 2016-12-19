package com.example.steffensuess.price48;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by steffensuess on 19.12.16.
 */

public class OfferAdapter extends ArrayAdapter<Offer> {

    public OfferAdapter(Context context, int resource, List<Offer> offers) {
        super(context, resource, offers);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.list_item, null);

            Offer offer = getItem(position);

            if(offer != null){
                TextView price = (TextView) view.findViewById(R.id.price);
                TextView shop_name = (TextView) view.findViewById(R.id.shop_name);

                if (price != null && shop_name != null){
                    price.setText(offer.getPrice());
                    shop_name.setText(offer.getShop_Name());
                }
            }


        return view;
    }
}
