package foodvisor.com.foodvisor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.lang.reflect.Method;
import java.util.ArrayList;

import foodvisor.com.foodvisor.adapter.CategoryItemAdapter;
import foodvisor.com.foodvisor.adapter.RestaurantItemAdapter;
import foodvisor.com.foodvisor.adapter.RestaurantMenuItemAdapter;
import foodvisor.com.foodvisor.model.CategoryItem;
import foodvisor.com.foodvisor.model.RestaurantItem;
import foodvisor.com.foodvisor.model.RestaurantMenuItem;
import foodvisor.com.foodvisor.utils.ItemClickSupport;
import foodvisor.com.foodvisor.utils.NetworkChecking;
import foodvisor.com.foodvisor.utils.WooCommerceApi;

import static foodvisor.com.foodvisor.Home.mDetailsToolbarLayout;
import static foodvisor.com.foodvisor.Home.mHomeToolbarLayout;
import static foodvisor.com.foodvisor.Home.toolbar;

public class FeedsFragment extends Fragment {
    //Defining Variables
    private static final String TAG = FeedsFragment.class.getSimpleName();
    ArrayList<RestaurantItem> restaurantItems;
    ArrayList<CategoryItem> categoryItems;
    ArrayList<RestaurantMenuItem> restaurantMenuItems;
    RecyclerView mRecyclerView, mRecyclerView2, mRecyclerView3;
    StaggeredGridLayoutManager mStaggeredGridLayoutManager, mStaggeredGridLayoutManager2, mStaggeredGridLayoutManager3;
    RestaurantItemAdapter adapter;
    CategoryItemAdapter adapter2;
    RestaurantMenuItemAdapter adapter3;

    TextView mRestaurantDetailsName, mRestaurantDetailsDuration;
    ImageView mRestaurantDetailsImage;
    RatingBar mRestaurantDetailsRatingsBar;

    Snackbar snackbar;
    RelativeLayout mFeedsParent;
    private ProgressDialog mProgressDialog;

    String CategoryImage, CategoryName, RestaurantName, RestaurantImage;

    // BottomSheetBehavior variable
    RelativeLayout mBottomSheeet;
    public static BottomSheetBehavior bottomSheetBehavior;

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
        return inflater.inflate(R.layout.feeds_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restaurantItems = new ArrayList<RestaurantItem>();
        categoryItems = new ArrayList<CategoryItem>();
        restaurantMenuItems = new ArrayList<RestaurantMenuItem>();

        mBottomSheeet = (RelativeLayout)view.findViewById(R.id.bottomSheetLayout);
        bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottomSheetLayout));

        mRestaurantDetailsName = (TextView)view.findViewById(R.id.restaurant_details_name);
        mRestaurantDetailsDuration = (TextView)view.findViewById(R.id.restaurant_details_open_close);
        mRestaurantDetailsImage = (ImageView)view.findViewById(R.id.restaurant_details_image);
        mRestaurantDetailsRatingsBar = (RatingBar)view.findViewById(R.id.restauranDetailstRatingBar);

        mRecyclerView3 = (RecyclerView) view.findViewById(R.id.restaurantItemRecycler);
        mRecyclerView3.setNestedScrollingEnabled(false);
        mStaggeredGridLayoutManager3 = new StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL);
        mRecyclerView3.setLayoutManager(mStaggeredGridLayoutManager3);

        mFeedsParent = (RelativeLayout) view.findViewById(R.id.mFeedsParent);

        initSheet();

        initMenuItemList();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.restaurantRecycler);
        mRecyclerView.setNestedScrollingEnabled(false);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);

        mRecyclerView2 = (RecyclerView) view.findViewById(R.id.categoryRecycler);
        mRecyclerView2.setNestedScrollingEnabled(false);
        mStaggeredGridLayoutManager2 = new StaggeredGridLayoutManager(1, GridLayoutManager.HORIZONTAL);
        mRecyclerView2.setLayoutManager(mStaggeredGridLayoutManager2);

        // progress dialog during loading data
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.getting_ready));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        // Initializing Internet Check
        if (NetworkChecking.hasConnection(getActivity())) {
            new CategoryAsyncTask().execute();
        } else {
            // if there is no internet
            // Create Snack bar message
            CreateSnackBar();
        }

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        RestaurantItem data = restaurantItems.get(position);
                        String name = data.getProductName();
                        String duration = data.getProductDescription();
                        String imageURL = data.getImageUrl();
                        String ratings = data.getProductRating();
                        Log.d("Restaurant Name: ", name);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            toolbar.setBackgroundColor(getActivity().getColor(R.color.marun));
                        }
                        mHomeToolbarLayout.setVisibility(View.GONE);
                        mDetailsToolbarLayout.setVisibility(View.VISIBLE);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        setRestaurantData(name, duration, imageURL, ratings);
                    }
                }
        );

        ItemClickSupport.addTo(mRecyclerView2).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        CategoryItem data = categoryItems.get(position);
                        String name = data.getCategoryName();
                        Log.d("Category Name: ", name);
                    }
                }
        );

        ItemClickSupport.addTo(mRecyclerView3).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        RestaurantMenuItem data = restaurantMenuItems.get(position);
                        String name = data.getMenu_item_name();
                        Log.d("Menu Item Name: ", name);
                        Toast.makeText(getActivity(), name, Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void initSheet() {
        // Capturing the callbacks for bottom sheet
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    Log.d(TAG, "collapse me");
                    mBottomSheeet.setBackgroundColor(getResources().getColor(R.color.white));
                } else {
                    Log.d(TAG, "expand me");
                    mBottomSheeet.setBackgroundColor(getResources().getColor(R.color.transparent));
                }

                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }

                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.d(TAG, "STATE_COLLAPSED");
                        mBottomSheeet.setBackgroundColor(getResources().getColor(R.color.transparent));
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.d(TAG, "STATE_DRAGGING");
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            toolbar.setBackgroundColor(getActivity().getColor(R.color.white));
                        }
                        mHomeToolbarLayout.setVisibility(View.VISIBLE);
                        mDetailsToolbarLayout.setVisibility(View.GONE);
                        mBottomSheeet.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.d(TAG, "STATE_EXPANDED");
                        mBottomSheeet.setBackgroundColor(getResources().getColor(R.color.white));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            toolbar.setBackgroundColor(getActivity().getColor(R.color.marun));
                        }
                        mHomeToolbarLayout.setVisibility(View.GONE);
                        mDetailsToolbarLayout.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.d(TAG, "STATE_HIDDEN");
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            toolbar.setBackgroundColor(getActivity().getColor(R.color.white));
                        }
                        mHomeToolbarLayout.setVisibility(View.VISIBLE);
                        mDetailsToolbarLayout.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        mBottomSheeet.setBackgroundColor(getResources().getColor(R.color.white));
                        Log.d(TAG, "STATE_SETTLING");
                        break;
                }
            }


            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });

    }

    public void initMenuItemList(){
        adapter3 = new RestaurantMenuItemAdapter(getActivity(), restaurantMenuItems);
        mRecyclerView3.setAdapter(adapter3);
    }

    public void setRestaurantData(String restaurantName, String duration, String imageURL, String ratings){
        mRestaurantDetailsName.setText(restaurantName);
        mRestaurantDetailsDuration.setText(duration);
        Picasso.with(getContext())
                .load(imageURL).noFade().into(mRestaurantDetailsImage);
        mRestaurantDetailsRatingsBar.setRating(Float.parseFloat(ratings));
    }


    // create snack bar
    public void CreateSnackBar() {
        mProgressDialog.hide();
        snackbar = Snackbar
                .make(mFeedsParent, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // go to wifi settings page tapping on retry button
                        startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    private class CategoryAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            String restURL = "http://woocommerce.cloudaccess.host/wp-json/wc/v2/products/categories";
            OAuthService service = new ServiceBuilder()
                    .provider(WooCommerceApi.class)
                    .apiKey("ck_4e14689b6cb44beec1f2b5fa307c7c131ab54f57")  //Your Consumer key
                    .apiSecret("cs_32d6111d7da5082ad5dbb384478d38d2c4f2ea56")   //Your Consumer secret
                    .scope("API.Public") //fixed
                    .signatureType(SignatureType.QueryString)
                    .build();
            OAuthRequest request = new OAuthRequest(Verb.GET, restURL);
            Token accessToken = new Token("", ""); //not required for context.io
            service.signRequest(accessToken, request);
            Response response = request.send();
            String data = response.getBody();
            Log.d("OAuthTask", response.getBody());
            try {
                parseJSON(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //update your listView adapter here
            //Dismiss your dialog
            adapter2 = new CategoryItemAdapter(getActivity(), categoryItems);
            mRecyclerView2.setAdapter(adapter2);
            new CategoryAsyncTask2().execute();
        }
    }

    public void parseJSON(String data) throws JSONException {
        JSONArray jsonArr = new JSONArray(data);
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            String id = jsonObj.getString("id").toString();
            CategoryName = jsonObj.getString("name");
            String subdata = jsonObj.getString("image");
            JSONObject jsonObj2 = new JSONObject(subdata);
            CategoryImage = jsonObj2.getString("src");
            Log.d("Category Details: ", "ID:: " + id + "\nName:: " + CategoryName + "\nImageUrl:: " + CategoryImage);
            CategoryItem categoryItem = new CategoryItem();
            categoryItem.setCategoryName(CategoryName);
            categoryItem.setImageUrl(CategoryImage);
            categoryItems.add(categoryItem);
        }
    }

    private class CategoryAsyncTask2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            String restURL = "http://woocommerce.cloudaccess.host/wp-json/wc/v2/products";
            OAuthService service = new ServiceBuilder()
                    .provider(WooCommerceApi.class)
                    .apiKey("ck_4e14689b6cb44beec1f2b5fa307c7c131ab54f57")  //Your Consumer key
                    .apiSecret("cs_32d6111d7da5082ad5dbb384478d38d2c4f2ea56")   //Your Consumer secret
                    .scope("API.Public") //fixed
                    .signatureType(SignatureType.QueryString)
                    .build();
            OAuthRequest request = new OAuthRequest(Verb.GET, restURL);
            Token accessToken = new Token("", ""); //not required for context.io
            service.signRequest(accessToken, request);
            Response response = request.send();
            String data = response.getBody();
            Log.d("OAuthTask", response.getBody());
            try {
                parseJSON2(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //update your listView adapter here
            //Dismiss your dialog
            mProgressDialog.hide();
            adapter = new RestaurantItemAdapter(getActivity(), restaurantItems);
            mRecyclerView.setAdapter(adapter);
        }
    }

    public void parseJSON2(String data) throws JSONException {
        JSONArray jsonArr = new JSONArray(data);
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            String id = jsonObj.getString("id").toString();
            RestaurantName = jsonObj.getString("name");
            String price = jsonObj.getString("price");
            String weight = jsonObj.getString("weight");
            String server_description = jsonObj.getString("description");
            String description = server_description.replaceAll("<p>", "").replaceAll("</p>", "");
            String average_rating = jsonObj.getString("average_rating");
            String subdata = jsonObj.getString("images");
            JSONArray json_data1 = new JSONArray(subdata);
            for (int j = 0; j < json_data1.length(); j++) {
                jsonObj = json_data1.getJSONObject(j);
                RestaurantImage = jsonObj.getString("src");
            }
            Log.d("Restaurant Details: ", "ID:: " + id + "\nName:: " + RestaurantName + "\nDescription:: " + description + "\nImageUrl:: " + RestaurantImage + "\nPrice:: " + price + "\nAvarage Rating:: " + average_rating + "\nMinutes:: " + weight);
            RestaurantItem restaurantItem = new RestaurantItem();
            restaurantItem.setProductName(RestaurantName);
            restaurantItem.setProductId(id);
            restaurantItem.setProductDescription(description);
            restaurantItem.setProductDistance(weight);
            restaurantItem.setProductRating(average_rating);
            restaurantItem.setProductPrice(price);
            restaurantItem.setImageUrl(RestaurantImage);
            restaurantItems.add(restaurantItem);
        }
    }


}
