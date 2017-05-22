package foodvisor.com.foodvisor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.util.ArrayList;

import foodvisor.com.foodvisor.adapter.CategoryItemAdapter;
import foodvisor.com.foodvisor.adapter.RestaurantItemAdapter;
import foodvisor.com.foodvisor.model.CategoryItem;
import foodvisor.com.foodvisor.model.RestaurantItem;
import foodvisor.com.foodvisor.utils.ItemClickSupport;
import foodvisor.com.foodvisor.utils.NetworkChecking;
import foodvisor.com.foodvisor.utils.WooCommerceApi;

public class FeedsFragment extends Fragment {
    //Defining Variables
    private static final String TAG = FeedsFragment.class.getSimpleName();
    ArrayList<RestaurantItem> restaurantItems;
    ArrayList<CategoryItem> categoryItems;
    RecyclerView mRecyclerView, mRecyclerView2;
    StaggeredGridLayoutManager mStaggeredGridLayoutManager, mStaggeredGridLayoutManager2;
    RestaurantItemAdapter adapter;
    CategoryItemAdapter adapter2;

    Snackbar snackbar;
    RelativeLayout mFeedsParent;
    private ProgressDialog mProgressDialog;

    String CategoryImage, CategoryName, RestaurantName, RestaurantImage;

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

        mFeedsParent = (RelativeLayout) view.findViewById(R.id.mFeedsParent);

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
                        Log.d("Restaurant Name: ", name);
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
            String restURL = "http://woocommerce.cloudaccess.host/wp-json/wc/v2/products/?categories";
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
            String subdata = jsonObj.getString("images");
            JSONArray json_data1 = new JSONArray(subdata);
            for (int j = 0; j < json_data1.length(); j++) {
                jsonObj = json_data1.getJSONObject(j);
                CategoryImage = jsonObj.getString("src");
            }
            Log.d("Category Details: ", "ID:: " + id + " Name:: " + CategoryName + " ImageUrl:: " + CategoryImage);
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
            String restURL = "http://woocommerce.cloudaccess.host/wp-json/wc/v2/products?";
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
            String average_rating = jsonObj.getString("average_rating");
            String subdata = jsonObj.getString("images");
            JSONArray json_data1 = new JSONArray(subdata);
            for (int j = 0; j < json_data1.length(); j++) {
                jsonObj = json_data1.getJSONObject(j);
                RestaurantImage = jsonObj.getString("src");
            }
            Log.d("Restaurant Details: ", "ID:: " + id + "\nName:: " + RestaurantName + "\nImageUrl:: " + RestaurantImage+"\nPrice:: "+price+"\nAvarage Rating:: "+average_rating+"\nMinutes:: "+weight);
            RestaurantItem restaurantItem = new RestaurantItem();
            restaurantItem.setProductName(RestaurantName);
            restaurantItem.setProductId(id);
            restaurantItem.setProductDistance(weight);
            restaurantItem.setProductRating(average_rating);
            restaurantItem.setProductPrice(price);
            restaurantItem.setImageUrl(RestaurantImage);
            restaurantItems.add(restaurantItem);
        }
    }


}