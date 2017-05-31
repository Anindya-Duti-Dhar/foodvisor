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
import foodvisor.com.foodvisor.model.RentItem;


public class RentItemAdapter extends RecyclerView.Adapter<RentItemAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView rent_image;
        public TextView rent_name, rent_cost, rent_address;

        public ViewHolder(View itemView) {
            super(itemView);
            rent_name = (TextView) itemView.findViewById(R.id.rent_name);
            rent_cost = (TextView) itemView.findViewById(R.id.rent_cost);
            rent_address = (TextView) itemView.findViewById(R.id.rent_address);
            rent_image = (ImageView) itemView.findViewById(R.id.rent_image);
        }
    }

    private ArrayList<RentItem> _data;
    private Context mContext;

    private Context getContext() {
        return mContext;
    }

    public RentItemAdapter(Context context, ArrayList<RentItem> _data) {
        this._data = _data;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View rentView = inflater.inflate(R.layout.rent_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(rentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final RentItem data = _data.get(position);
        TextView ListTitle = viewHolder.rent_name;
        ListTitle.setText(data.getRentName());

        TextView ListPrice = viewHolder.rent_cost;
        ListPrice.setText(data.getRentCost()+" Taka/Month");

        TextView ListAddress = viewHolder.rent_address;
        ListAddress.setText(data.getRentShortDescription());

        ImageView ListImage = viewHolder.rent_image;
        Picasso.with(getContext())
               .load(data.getImageUrl()).noFade().into(ListImage);
    }

    @Override
    public int getItemCount() {
        return _data.size();
    }

}