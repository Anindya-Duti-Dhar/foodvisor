package foodvisor.com.foodvisor.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import foodvisor.com.foodvisor.R;
import foodvisor.com.foodvisor.model.CategoryItem;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView category_image;

        public ViewHolder(View itemView) {
            super(itemView);
            category_image = (ImageView) itemView.findViewById(R.id.category_image);
        }
    }

    private ArrayList<CategoryItem> _data;
    private Context mContext;

    private Context getContext() {
        return mContext;
    }

    public CategoryItemAdapter(Context context, ArrayList<CategoryItem> _data) {
        this._data = _data;
        mContext = context;

    }

    @Override
    public CategoryItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View categoryView = inflater.inflate(R.layout.category_item, parent, false);
        CategoryItemAdapter.ViewHolder viewHolder = new CategoryItemAdapter.ViewHolder(categoryView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CategoryItemAdapter.ViewHolder viewHolder, int position) {
        final CategoryItem data = _data.get(position);

        ImageView ListImage = viewHolder.category_image;
        //ListImage.setImageResource(R.drawable.kfc);
        Picasso.with(getContext())
        .load(data.getImageUrl()).noFade().into(ListImage);
    }

    @Override
    public int getItemCount() {
        return _data.size();
    }

}