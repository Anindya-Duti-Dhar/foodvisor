package foodvisor.com.foodvisor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import foodvisor.com.foodvisor.adapter.RestaurantItemAdapter;
import foodvisor.com.foodvisor.model.RestaurantItem;
import foodvisor.com.foodvisor.utils.ItemClickSupport;

public class FeedsFragment extends Fragment {
    //Defining Variables
    ArrayList<RestaurantItem> restaurantItems;
    RecyclerView mRecyclerView;
    RestaurantItemAdapter adapter;
    String mCategoryName;
    StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    ProgressBar mProgress;
    RelativeLayout mLoadingProgress;
    ImageView mNoInternet;

    public static FeedsFragment newInstance() {
        FeedsFragment fragment = new FeedsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_one, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restaurantItems = new ArrayList<RestaurantItem>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.restaurantItemRecycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RestaurantItemAdapter(getActivity(), restaurantItems);
        mRecyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        RestaurantItem data = restaurantItems.get(position);
                        String name = data.getProductName();
                        Log.d("Restaurant Name: ", name);
                    }
                }
        );
    }
}
