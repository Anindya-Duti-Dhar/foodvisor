package foodvisor.com.foodvisor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import foodvisor.com.foodvisor.R;
import foodvisor.com.foodvisor.model.CategoryItem;
import foodvisor.com.foodvisor.model.RestaurantMenuItem;

public class RestaurantMenuItemAdapter extends RecyclerView.Adapter<RestaurantMenuItemAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView menu_item_image, right_arrow_image;
        public TextView menu_item_name;

        public ViewHolder(View itemView) {
            super(itemView);
            menu_item_image = (ImageView) itemView.findViewById(R.id.menu_item_image);
            right_arrow_image = (ImageView) itemView.findViewById(R.id.right_arrow_image);
            menu_item_name = (TextView) itemView.findViewById(R.id.menu_item_name);
        }
    }

    private ArrayList<RestaurantMenuItem> _data;
    private Context mContext;

    private Context getContext() {
        return mContext;
    }

    public RestaurantMenuItemAdapter(Context context, ArrayList<RestaurantMenuItem> _data) {
        this._data = _data;
        mContext = context;

        _data.add(0, getListItem("Salad", R.drawable.salad));
        _data.add(1, getListItem("Salad", R.drawable.salad));
        _data.add(2, getListItem("Salad", R.drawable.salad));
        _data.add(3, getListItem("Salad", R.drawable.salad));
        _data.add(4, getListItem("Salad", R.drawable.salad));
        _data.add(5, getListItem("Salad", R.drawable.salad));
        _data.add(6, getListItem("Salad", R.drawable.salad));
    }

    public RestaurantMenuItem getListItem(String title, int imageSource) {
        RestaurantMenuItem StaticItem = new RestaurantMenuItem();
        StaticItem.setMenu_item_name(title);
        StaticItem.setImageUrl(imageSource);
        return StaticItem;
    }

    @Override
    public RestaurantMenuItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View menuItemView = inflater.inflate(R.layout.restaurant_menu_item, parent, false);
        RestaurantMenuItemAdapter.ViewHolder viewHolder = new RestaurantMenuItemAdapter.ViewHolder(menuItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RestaurantMenuItemAdapter.ViewHolder viewHolder, int position) {
        final RestaurantMenuItem data = _data.get(position);

        TextView ListName = viewHolder.menu_item_name;
        ListName.setText(data.getMenu_item_name());
        ImageView ListImage = viewHolder.menu_item_image;
        ListImage.setImageResource(data.getImageUrl());
        ImageView ListArrowImage = viewHolder.right_arrow_image;
    }

    @Override
    public int getItemCount() {
        return _data.size();
    }

}