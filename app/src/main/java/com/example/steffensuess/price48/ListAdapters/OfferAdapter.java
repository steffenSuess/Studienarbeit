package com.example.steffensuess.price48.ListAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.steffensuess.price48.Models.Offer;
import com.example.steffensuess.price48.R;

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

        if (offer != null) {
            TextView price = (TextView) view.findViewById(R.id.price);
            TextView shop_name = (TextView) view.findViewById(R.id.shop_name);
            TextView shipping = (TextView) view.findViewById(R.id.shipping);
            ImageView availability = (ImageView) view.findViewById(R.id.availability_image);

            if (price != null && shop_name != null && shipping != null && availability != null) {
                price.setText(offer.getPrice() + " " + offer.getCurrency().replace("EUR", "€"));
                shop_name.setText(offer.getShop_Name());
                shipping.setText("Versand ab: " + offer.getCost_For_Shipping() + " " + offer.getCurrency().replace("EUR", "€"));

                switch (offer.getAvailability()) {
                    case "green":
                        availability.setImageDrawable(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.ic_shopping_cart_green, null));
                        break;
                    case "yellow":
                        availability.setImageDrawable(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.ic_shopping_cart_yellow, null));
                        break;
                    default:
                        availability.setImageDrawable(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.ic_remove_shopping_cart, null));
                        break;
                }
            }
        }


        return view;
    }
}
