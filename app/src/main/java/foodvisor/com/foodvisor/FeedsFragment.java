package foodvisor.com.foodvisor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import foodvisor.com.foodvisor.adapter.RentItemAdapter;
import foodvisor.com.foodvisor.database.DbManager;
import foodvisor.com.foodvisor.model.BookmarkItem;
import foodvisor.com.foodvisor.model.RentItem;
import foodvisor.com.foodvisor.utils.ItemClickSupport;
import foodvisor.com.foodvisor.utils.NetworkChecking;
import foodvisor.com.foodvisor.utils.PrefManager;
import foodvisor.com.foodvisor.utils.WooCommerceApi;

import static foodvisor.com.foodvisor.Home.mDetailsToolbarLayout;
import static foodvisor.com.foodvisor.Home.mHomeToolbarLayout;
import static foodvisor.com.foodvisor.Home.toolbar;

public class FeedsFragment extends Fragment {
    //Defining Variables
    private static final String TAG = FeedsFragment.class.getSimpleName();
    ArrayList<RentItem> rentItems;
    RecyclerView mRecyclerView;
    StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    RentItemAdapter adapter;

    TextView mRentDetailsName, mRentDetailsCost, mRentDetailsAddress, mRentDescription, mRentUploader, mRentUploadedAt;
    ImageView mRentDetailsImage;
    Button add_to_favorite;

    Snackbar snackbar;
    RelativeLayout mFeedsParent;
    private ProgressDialog mProgressDialog;

    SwipeRefreshLayout refreshLayout;

    FloatingActionButton fab;
    LinearLayout mRentDetailsLayout, mRentPostLayout;

    String RentID, RentName, RentAddress, RentCost, RentDescription, RentUploader, RentUploaderMobile, RentUploadedTime, RentImage;
    String image;

    List<BookmarkItem> dbList;
    DbManager helper;

    // BottomSheetBehavior variable
    RelativeLayout mBottomSheeet;
    public static BottomSheetBehavior bottomSheetBehavior;

    // add post variable
    TextInputLayout inputLayoutPostName, inputLayoutLocation, inputLayoutCost, inputLayoutDescription;
    EditText input_post_name, input_location, input_cost, input_description;
    Button _postButton;
    String PostNameByUser, CostByUser, LocationByUser, DescriptionByUser;

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

        rentItems = new ArrayList<RentItem>();

        mBottomSheeet = (RelativeLayout) view.findViewById(R.id.bottomSheetLayout);
        bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottomSheetLayout));
        mRentDetailsLayout = (LinearLayout) view.findViewById(R.id.rent_details_layout);
        mRentPostLayout = (LinearLayout) view.findViewById(R.id.add_post_layout);

        mRentDetailsName = (TextView) view.findViewById(R.id.rent_details_name);
        mRentDetailsAddress = (TextView) view.findViewById(R.id.rent_Details_address_data);
        mRentUploader = (TextView) view.findViewById(R.id.rent_uploader_name);
        mRentUploadedAt = (TextView) view.findViewById(R.id.rent_post_time);
        mRentDetailsCost = (TextView) view.findViewById(R.id.rent_details_cost_data);
        mRentDescription = (TextView) view.findViewById(R.id.rent_description_data);
        mRentDetailsImage = (ImageView) view.findViewById(R.id.rent_details_image);

        add_to_favorite = (Button) view.findViewById(R.id.add_to_favorite);

        mFeedsParent = (RelativeLayout) view.findViewById(R.id.mFeedsParent);

        initSheet();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        inputLayoutPostName = (TextInputLayout) view.findViewById(R.id.inputLayoutPostName);
        inputLayoutCost = (TextInputLayout) view.findViewById(R.id.inputLayoutCost);
        inputLayoutLocation = (TextInputLayout) view.findViewById(R.id.inputLayoutLocation);
        inputLayoutDescription = (TextInputLayout) view.findViewById(R.id.inputLayoutDescription);

        input_post_name = (EditText) view.findViewById(R.id.input_post_name);
        input_cost = (EditText) view.findViewById(R.id.input_cost);
        input_location = (EditText) view.findViewById(R.id.input_location);
        input_description = (EditText) view.findViewById(R.id.input_description);

        _postButton = (Button) view.findViewById(R.id.btn_post);
        _postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                // go to post method
                post();
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rentRecycler);
        mRecyclerView.setNestedScrollingEnabled(false);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);

        // progress dialog during loading data
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.getting_ready));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        // pull to refresh method
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // load available list
                // Initializing Internet Check
                if (NetworkChecking.hasConnection(getActivity())) {
                    //clear the array list first
                    rentItems.clear();
                    // auto load available post
                    new RentAsyncTask().execute();
                } else {
                    // if there is no internet
                    // stop pull to refresh
                    refreshLayout.setRefreshing(false);
                    // Create Snack bar message
                    CreateSnackBar();
                }
            }
        });

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        mRentPostLayout.setVisibility(View.GONE);
                        mRentDetailsLayout.setVisibility(View.VISIBLE);
                        RentItem data = rentItems.get(position);
                        String RentID = data.getRentId();
                        String RentName = data.getRentName();
                        String RentAddress = data.getRentAddress();
                        String RentCost = data.getRentCost();
                        String RentDescription = data.getRentDescription();
                        String RentUploader = data.getRentUploader();
                        String RentUploaderMobile = data.getRentUploaderMobile();
                        String RentUploadedTime = data.getRentUploadedAt();
                        String RentImage = data.getImageUrl();
                        Log.d("Rent Name: ", RentName);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            toolbar.setBackgroundColor(getActivity().getColor(R.color.marun));
                        }
                        mHomeToolbarLayout.setVisibility(View.GONE);
                        mDetailsToolbarLayout.setVisibility(View.VISIBLE);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        PrefManager.setItemOpened(getActivity(), "Yes");
                        setRentData(RentID, RentName, RentAddress, RentCost, RentDescription, RentUploader, RentUploaderMobile, RentUploadedTime, RentImage);
                    }
                }
        );

        // initializing floating action button
        fab = (FloatingActionButton) view.findViewById(R.id.post_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRentPostLayout.setVisibility(View.VISIBLE);
                mRentDetailsLayout.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    toolbar.setBackgroundColor(getActivity().getColor(R.color.marun));
                }
                mHomeToolbarLayout.setVisibility(View.GONE);
                mDetailsToolbarLayout.setVisibility(View.VISIBLE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                PrefManager.setItemOpened(getActivity(), "Yes");
            }
        });

    }

    public void post() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onPostFailed();
            return;
        }
        PostNameByUser = input_post_name.getText().toString();
        CostByUser = input_cost.getText().toString();
        LocationByUser = input_location.getText().toString();
        DescriptionByUser = input_description.getText().toString();
        // add new post
        new PostAsyncTask().execute();
    }

    public void onPostFailed() {
        mProgressDialog.hide();
        toast(getString(R.string.post_failed_message));
        _postButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String postName = input_post_name.getText().toString();
        String cost = input_cost.getText().toString();
        String location = input_location.getText().toString();
        String description = input_description.getText().toString();

        if (postName.isEmpty() || postName.length() > 256) {
            input_post_name.setError(getString(R.string.valid_postName));
            valid = false;
        } else {
            input_post_name.setError(null);
        }

        if (cost.isEmpty() || cost.length() > 6) {
            input_cost.setError(getString(R.string.valid_cost));
            valid = false;
        } else {
            input_cost.setError(null);
        }

        if (location.isEmpty() || location.length() > 256) {
            input_location.setError(getString(R.string.valid_location));
            valid = false;
        } else {
            input_location.setError(null);
        }

        if (description.isEmpty() || description.length() > 1000) {
            input_description.setError(getString(R.string.valid_description));
            valid = false;
        } else {
            input_description.setError(null);
        }

        return valid;
    }

    private void toast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    private class PostAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            JSONObject requestObject = new JSONObject();
            try {
                requestObject.put("name", PostNameByUser);
                requestObject.put("regular_price", CostByUser);
                requestObject.put("short_description", LocationByUser);
                requestObject.put("description", DescriptionByUser);
                requestObject.put("slug", "Shukdev Dutta");
                requestObject.put("shipping_calss", "01673423452");
                String postTime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date());
                requestObject.put("weight", postTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String restURL = "http://rent.cloudaccess.host/wp-json/wc/v2/products";    //"http://woocommerce.cloudaccess.host/wp-json/wc/v2/products?";
            OAuthService service = new ServiceBuilder()
                    .provider(WooCommerceApi.class)
                    .apiKey("ck_627665256e4d308cd75cab04631110ecfa6d1ecd")  //Your Consumer key   //ck_4e14689b6cb44beec1f2b5fa307c7c131ab54f57
                    .apiSecret("cs_cdcc9613c38ab63c9137afdc7211e3d80b065889")   //Your Consumer secret   //cs_32d6111d7da5082ad5dbb384478d38d2c4f2ea56
                    .scope("API.Public") //fixed
                    .signatureType(SignatureType.QueryString)
                    .build();
            String payload = requestObject.toString();
            OAuthRequest request = new OAuthRequest(Verb.POST, restURL);
            request.addHeader("Content-Type", "application/json");
            request.addPayload(payload);
            Token accessToken = new Token("", ""); //not required for context.io
            service.signRequest(accessToken, request);
            Response response = request.send();
            String data3 = response.getBody();
            Log.d("OAuthTask", response.getBody());
            try {
                parseJSON3(data3);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (response.getCode() != 201) {
                onPostFailed();
            }
            int responseCode = response.getCode();
            Log.d("ResponseCode:: ", String.valueOf(responseCode));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
        }
    }

    public void parseJSON3(String data) throws JSONException {
        JSONArray jsonArr = new JSONArray(data);
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            String id = jsonObj.getString("id").toString();
            String postName = jsonObj.getString("name");
            String location = jsonObj.getString("short_description");
            String cost = jsonObj.getString("price");
            String description = jsonObj.getString("description");
            String uploader = jsonObj.getString("slug");
            String uploaderMobile = jsonObj.getString("shipping_class");
            String uploadedAt = jsonObj.getString("weight");
            Log.d("Post details: ", "ID:: " + id + "\nName:: " + postName + "\nLocation:: " + location + "\nCost:: " + cost + "\nDescription:: " + description+"\nUploader:: "+uploader+"\nUploader Mobile:: "+uploaderMobile+"\nUploaded At:: "+uploadedAt);
        }
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
                        fab.show();
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.d(TAG, "STATE_DRAGGING");
                        fab.hide();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            toolbar.setBackgroundColor(getActivity().getColor(R.color.marun));
                        }
                        mHomeToolbarLayout.setVisibility(View.VISIBLE);
                        mDetailsToolbarLayout.setVisibility(View.GONE);
                        mBottomSheeet.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.d(TAG, "STATE_EXPANDED");
                        fab.hide();
                        mBottomSheeet.setBackgroundColor(getResources().getColor(R.color.white));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            toolbar.setBackgroundColor(getActivity().getColor(R.color.marun));
                        }
                        mHomeToolbarLayout.setVisibility(View.GONE);
                        mDetailsToolbarLayout.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.d(TAG, "STATE_HIDDEN");
                        fab.show();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        PrefManager.setItemOpened(getActivity(), "No");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            toolbar.setBackgroundColor(getActivity().getColor(R.color.marun));
                        }
                        mHomeToolbarLayout.setVisibility(View.VISIBLE);
                        mDetailsToolbarLayout.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        fab.hide();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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

    public void setRentData(final String rentID, final String rentName, final String address, final String rentCost, final String rentDescription, final String uploader, final String uploaderMobile, final String uploaded_at, final String imageURL) {
        mRentDetailsName.setText(rentName);
        mRentDetailsAddress.setText(address);
        mRentDetailsCost.setText(rentCost+" Taka/Month");
        mRentDescription.setText(rentDescription);
        Picasso.with(getContext())
                .load(imageURL).noFade().into(mRentDetailsImage);
        mRentUploader.setText(uploader);
        mRentUploadedAt.setText(uploaded_at);

        add_to_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insert bookmark
                InsertBookmarkList(rentID, rentName, address, rentCost, rentDescription, uploader, uploaderMobile, uploaded_at, imageURL);
                Intent cartItemDeleted = new Intent("addBookmarkItem");
                cartItemDeleted.putExtra("bookmarkAdded", "Yes");
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(cartItemDeleted);
            }
        });

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

    private class RentAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            String restURL = "http://rent.cloudaccess.host/wp-json/wc/v2/products?";      //"http://woocommerce.cloudaccess.host/wp-json/wc/v2/products?";
            OAuthService service = new ServiceBuilder()
                    .provider(WooCommerceApi.class)
                    .apiKey("ck_627665256e4d308cd75cab04631110ecfa6d1ecd")  //Your Consumer key   //ck_4e14689b6cb44beec1f2b5fa307c7c131ab54f57
                    .apiSecret("cs_cdcc9613c38ab63c9137afdc7211e3d80b065889")   //Your Consumer secret   //cs_32d6111d7da5082ad5dbb384478d38d2c4f2ea56
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
            mProgressDialog.hide();
            refreshLayout.setRefreshing(false);
            adapter = new RentItemAdapter(getActivity(), rentItems);
            mRecyclerView.setAdapter(adapter);
        }
    }

    private void parseJSON(String data) throws JSONException {
        JSONArray jsonArr = new JSONArray(data);
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            String id = jsonObj.getString("id").toString();
            String name = jsonObj.getString("name");
            String cost = jsonObj.getString("price");
            String server_short_description = jsonObj.getString("short_description");
            String address = server_short_description.replaceAll("<p>", "").replaceAll("</p>", "");
            String server_description = jsonObj.getString("description");
            String description = server_description.replaceAll("<p>", "").replaceAll("</p>", "");
            String uploader = jsonObj.getString("slug");
            String uploaderMobile = jsonObj.getString("shipping_class");
            String time = jsonObj.getString("weight");
            String subdata = jsonObj.getString("images");
            JSONArray json_data1 = new JSONArray(subdata);
            for (int j = 0; j < json_data1.length(); j++) {
                jsonObj = json_data1.getJSONObject(j);
                image = jsonObj.getString("src");
            }
            Log.d("Rent Details: ", "ID:: " + id + "\nName:: " + name + "\nAddress:: " + address + "\nCost:: " + cost);
            RentItem rentItem = new RentItem();
            rentItem.setRentId(id);
            rentItem.setRentName(name);
            rentItem.setRentAddress(address);
            rentItem.setRentCost(cost);
            rentItem.setRentDescription(description);
            rentItem.setRentUploader(uploader);
            rentItem.setRentUploaderMobile(uploaderMobile);
            rentItem.setRentUploadedAt(time);
            rentItem.setImageUrl(image);
            rentItems.add(rentItem);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Initializing Internet Check
        if (NetworkChecking.hasConnection(getActivity())) {
            new RentAsyncTask().execute();
        } else {
            // if there is no internet
            // Create Snack bar message
            CreateSnackBar();
        }
    }

    public void InsertBookmarkList(String rentID, String rentName, String address, String rentCost, String rentDescription, String uploader, String uploaderMobile, String uploaded_at, String imageURL){
        // insert into cart list
        dbList = new ArrayList<>();
        helper = DbManager.getInstance(getActivity());
        helper.insertIntoDB(rentID, rentName, address, rentCost, rentDescription, uploader, uploaderMobile, uploaded_at, imageURL);
    }
}
