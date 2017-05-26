package foodvisor.com.foodvisor.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import foodvisor.com.foodvisor.R;
import foodvisor.com.foodvisor.model.RestaurantItem;


public class RestaurantItemAdapter extends RecyclerView.Adapter<RestaurantItemAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView restaurant_image, restaurant_online;
        public TextView restaurant_name, user_restaurant_rating, restaurant_distance, restaurant_cost;
        public RatingBar restaurantRatingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            restaurant_name = (TextView) itemView.findViewById(R.id.restaurant_name);
            user_restaurant_rating = (TextView) itemView.findViewById(R.id.user_restaurant_rating);
            restaurant_image = (ImageView) itemView.findViewById(R.id.restaurant_image);
            restaurant_online = (ImageView) itemView.findViewById(R.id.restaurant_online);
            restaurant_distance = (TextView) itemView.findViewById(R.id.restaurant_distance);
            restaurant_cost = (TextView) itemView.findViewById(R.id.restaurant_cost);
            restaurantRatingBar = (RatingBar) itemView.findViewById(R.id.restaurantRatingBar);
        }
    }

    private ArrayList<RestaurantItem> _data;
    private Context mContext;

    private Context getContext() {
        return mContext;
    }

    public RestaurantItemAdapter(Context context, ArrayList<RestaurantItem> _data) {
        this._data = _data;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View restaurantView = inflater.inflate(R.layout.restaurant_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(restaurantView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final RestaurantItem data = _data.get(position);
        TextView ListTitle = viewHolder.restaurant_name;
        ListTitle.setText(data.getProductName());

        TextView ListPrice = viewHolder.restaurant_cost;
        ListPrice.setText("$"+data.getProductPrice());

        TextView ListDistance = viewHolder.restaurant_distance;
        ListDistance.setText(data.getProductDistance()+"min");

        TextView ListRating = viewHolder.user_restaurant_rating;
        ListRating.setText(data.getProductRating());

        RatingBar ListRatingBar = viewHolder.restaurantRatingBar;
        ListRatingBar.setRating(Float.parseFloat(data.getProductRating()));

        final ImageView ListFavourite = viewHolder.restaurant_online;
        ListFavourite.setImageResource(R.drawable.ic_online);

        ImageView ListImage = viewHolder.restaurant_image;
        Picasso.with(getContext())
               .load(data.getImageUrl()).noFade().into(ListImage);
    }

    @Override
    public int getItemCount() {
        return _data.size();
    }

}