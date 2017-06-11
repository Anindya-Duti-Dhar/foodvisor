package foodvisor.com.foodvisor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import foodvisor.com.foodvisor.adapter.BookmarkItemAdapter;
import foodvisor.com.foodvisor.database.DbManager;
import foodvisor.com.foodvisor.model.BookmarkItem;
import foodvisor.com.foodvisor.utils.ItemClickSupport;
import foodvisor.com.foodvisor.utils.PrefManager;

import static foodvisor.com.foodvisor.Home.mDetailsToolbarLayout;
import static foodvisor.com.foodvisor.Home.mHomeToolbarLayout;
import static foodvisor.com.foodvisor.Home.toolbar;

public class BookmarkFragment extends Fragment {
    private static final String TAG = BookmarkFragment.class.getSimpleName();
    List<BookmarkItem> dbList;
    DbManager helper;
    RecyclerView mRecyclerView;
    StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    BookmarkItemAdapter adapter;

    TextView mBookmarkDetailsName, mBookmarkDetailsCost, mBookmarkDetailsAddress, mBookmarkDescription, mBookmarkUploader, mBookmarkUploadedAt;
    ImageView mBookmarkDetailsImage;
    Button delete_from_favorite;

    RelativeLayout mBookmarkParent;

    LinearLayout mBookmarkDetailsLayout;

    private BroadcastReceiver mBroadcastReceiver;

    TextView mNoBookmark;

    // BottomSheetBehavior variable
    RelativeLayout mBottomSheet;
    public static BottomSheetBehavior bookmarkBottomSheetBehavior;


    public static BookmarkFragment newInstance() {
        BookmarkFragment fragment = new BookmarkFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bookmark_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");

        mBottomSheet = (RelativeLayout) view.findViewById(R.id.bookmarkBottomSheetLayout);
        bookmarkBottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bookmarkBottomSheetLayout));
        mBookmarkDetailsLayout = (LinearLayout) view.findViewById(R.id.bookmark_details_layout);

        mBookmarkDetailsName = (TextView) view.findViewById(R.id.bookmark_details_name);
        mBookmarkDetailsAddress = (TextView) view.findViewById(R.id.bookmark_Details_address_data);
        mBookmarkUploader = (TextView) view.findViewById(R.id.bookmark_uploader_name);
        mBookmarkUploadedAt = (TextView) view.findViewById(R.id.bookmark_post_time);
        mBookmarkDetailsCost = (TextView) view.findViewById(R.id.bookmark_details_cost_data);
        mBookmarkDescription = (TextView) view.findViewById(R.id.bookmark_description_data);
        mBookmarkDetailsImage = (ImageView) view.findViewById(R.id.bookmark_details_image);
        delete_from_favorite = (Button) view.findViewById(R.id.delete_from_favorite);

        mBookmarkParent = (RelativeLayout) view.findViewById(R.id.mBookmarkParent);

        initSheet();

        bookmarkBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        mNoBookmark = (TextView) view.findViewById(R.id.no_bookmark_text);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.bookmarkRecycler);
        mRecyclerView.setNestedScrollingEnabled(false);

        helper = DbManager.getInstance(getActivity());
        dbList = new ArrayList<BookmarkItem>();
        dbList = helper.getDataFromDB();
        if (dbList.size() < 1) {
            mNoBookmark.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mNoBookmark.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            adapter = new BookmarkItemAdapter(getActivity(), dbList);
            mRecyclerView.setAdapter(adapter);
            mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        }

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals("deleteBookmarkItem") || intent.getAction().equals("addBookmarkItem")) {
                    Log.d(TAG, "broadcast message receive");
                    dbList = helper.getDataFromDB();
                    if (dbList.size() < 1) {
                        mNoBookmark.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    } else {
                        mNoBookmark.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        adapter = new BookmarkItemAdapter(getActivity(), dbList);
                        mRecyclerView.setAdapter(adapter);
                        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL);
                        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
                    }
                }
            }
        };

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        BookmarkItem data = dbList.get(position);
                        String id = data.getRentId();
                        String name = data.getRentName();
                        String address = data.getRentAddress();
                        String cost = data.getRentCost();
                        String description = data.getRentDescription();
                        String uploader_name = data.getRentUploader();
                        String uploaded_at = data.getRentUploadedAt();
                        String imageURL = data.getImageUrl();
                        Log.d("Bookmark Rent Name: ", name);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            toolbar.setBackgroundColor(getActivity().getColor(R.color.marun));
                        }
                        mHomeToolbarLayout.setVisibility(View.GONE);
                        mDetailsToolbarLayout.setVisibility(View.VISIBLE);
                        bookmarkBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        PrefManager.setItemOpened(getActivity(), "Yes");
                        setBookmarkData(id, name, address, cost, description, uploader_name, uploaded_at, imageURL);
                    }
                }
        );

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            dbList = helper.getDataFromDB();
            if (dbList.size() < 1) {
                mNoBookmark.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                mNoBookmark.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                adapter = new BookmarkItemAdapter(getActivity(), dbList);
                mRecyclerView.setAdapter(adapter);
                mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
            }
        }
        else {
        }
    }

    private void initSheet() {
        // Capturing the callbacks for bottom sheet
        bookmarkBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    Log.d(TAG, "collapse me");
                    mBottomSheet.setBackgroundColor(getResources().getColor(R.color.white));
                } else {
                    Log.d(TAG, "expand me");
                    mBottomSheet.setBackgroundColor(getResources().getColor(R.color.transparent));
                }

                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bookmarkBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }

                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.d(TAG, "STATE_COLLAPSED");
                        mBottomSheet.setBackgroundColor(getResources().getColor(R.color.transparent));
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.d(TAG, "STATE_DRAGGING");
                        bookmarkBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            toolbar.setBackgroundColor(getActivity().getColor(R.color.marun));
                        }
                        mHomeToolbarLayout.setVisibility(View.VISIBLE);
                        mDetailsToolbarLayout.setVisibility(View.GONE);
                        mBottomSheet.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.d(TAG, "STATE_EXPANDED");
                        mBottomSheet.setBackgroundColor(getResources().getColor(R.color.white));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            toolbar.setBackgroundColor(getActivity().getColor(R.color.marun));
                        }
                        mHomeToolbarLayout.setVisibility(View.GONE);
                        mDetailsToolbarLayout.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.d(TAG, "STATE_HIDDEN");
                        bookmarkBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        PrefManager.setItemOpened(getActivity(), "No");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            toolbar.setBackgroundColor(getActivity().getColor(R.color.marun));
                        }
                        mHomeToolbarLayout.setVisibility(View.VISIBLE);
                        mDetailsToolbarLayout.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        bookmarkBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        mBottomSheet.setBackgroundColor(getResources().getColor(R.color.white));
                        Log.d(TAG, "STATE_SETTLING");
                        break;
                }
            }


            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });

    }

    public void setBookmarkData(final String id, String rentName, String address, String rentCost, String rentDescription, String uploader, String uploaded_at, String imageURL) {
        mBookmarkDetailsName.setText(rentName);
        mBookmarkDetailsAddress.setText(address);
        mBookmarkDetailsCost.setText(rentCost + " Taka/Month");
        mBookmarkDescription.setText(rentDescription);
        Picasso.with(getContext())
                .load(imageURL).noFade().into(mBookmarkDetailsImage);
        mBookmarkUploader.setText(uploader);
        mBookmarkUploadedAt.setText(uploaded_at + " Days ago");

        final String mID = id;
        delete_from_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete bookmark
                DeleteFromBookmarkList(mID);
                Intent cartItemDeleted = new Intent("deleteBookmarkItem");
                cartItemDeleted.putExtra("bookmarkDeleted", "Yes");
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(cartItemDeleted);
            }
        });

    }

    public void DeleteFromBookmarkList(String id) {
        dbList = new ArrayList<BookmarkItem>();
        helper = DbManager.getInstance(getContext());
        helper.deleteARow(id);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        // register all receiver
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver,
                new IntentFilter("deleteBookmarkItem"));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver,
                new IntentFilter("addBookmarkItem"));
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }

}
