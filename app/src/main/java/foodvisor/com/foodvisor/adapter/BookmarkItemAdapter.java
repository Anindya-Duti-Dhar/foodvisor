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
import java.util.List;

import foodvisor.com.foodvisor.R;
import foodvisor.com.foodvisor.model.BookmarkItem;

public class BookmarkItemAdapter extends RecyclerView.Adapter<BookmarkItemAdapter.ViewHolder> {

    static List<BookmarkItem> dbList;
    private Context mContext;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView rent_image;
        public TextView rent_name, rent_cost, rent_address;

        public ViewHolder(View itemView) {
            super(itemView);
            rent_name = (TextView) itemView.findViewById(R.id.bookmark_name);
            rent_cost = (TextView) itemView.findViewById(R.id.bookmark_cost);
            rent_address = (TextView) itemView.findViewById(R.id.bookmark_address);
            rent_image = (ImageView) itemView.findViewById(R.id.bookmark_image);
        }
    }

    private Context getContext() {
        return mContext;
    }

    public BookmarkItemAdapter(Context context, List<BookmarkItem> dbList) {
        this.dbList = new ArrayList<BookmarkItem>();
        this.mContext = context;
        mContext = context;
        this.dbList = dbList;
    }

    @Override
    public BookmarkItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View rentView = inflater.inflate(R.layout.bookmark_item, parent, false);
        BookmarkItemAdapter.ViewHolder viewHolder = new BookmarkItemAdapter.ViewHolder(rentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BookmarkItemAdapter.ViewHolder viewHolder, int position) {
        final BookmarkItem data = dbList.get(position);
        TextView ListTitle = viewHolder.rent_name;
        ListTitle.setText(data.getRentName());

        TextView ListPrice = viewHolder.rent_cost;
        ListPrice.setText(data.getRentCost()+" Taka/Month");

        TextView ListAddress = viewHolder.rent_address;
        ListAddress.setText(data.getRentAddress());

        ImageView ListImage = viewHolder.rent_image;
        Picasso.with(getContext())
                .load(data.getImageUrl()).noFade().into(ListImage);
    }

    @Override
    public int getItemCount() {
        return dbList.size();
    }

}