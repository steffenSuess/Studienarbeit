package com.example.steffensuess.price48;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by steffensuess on 07.01.17.
 */

public class SearchQueriesAdapter extends ArrayAdapter<SearchQuery> {
    public SearchQueriesAdapter(Context context, int resource, List<SearchQuery> searchQueries) {
        super(context, resource, searchQueries);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.query_list_item, null);

        SearchQuery searchQuery = getItem(position);

        if(searchQuery != null){
            TextView price = (TextView) view.findViewById(R.id.query_price);
            TextView productName = (TextView) view.findViewById(R.id.query_product_name);
            TextView shop_name = (TextView) view.findViewById(R.id.query_shop_name);
            ImageView productImage = (ImageView)view.findViewById(R.id.query_product_image);
            TextView callDate = (TextView)view.findViewById(R.id.call_date);

            if (price != null && shop_name != null && productImage != null && callDate != null && productName != null){
                price.setText(searchQuery.getPrice() + " €");
                shop_name.setText(searchQuery.getShopName());
                new ImageLoadTask(searchQuery.getImageURL(), productImage).execute();
                callDate.setText(searchQuery.getDate());
                productName.setText(searchQuery.getProductName());
            }
        }


        return view;
    }
}
