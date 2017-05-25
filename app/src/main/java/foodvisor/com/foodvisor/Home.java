package foodvisor.com.foodvisor;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import static foodvisor.com.foodvisor.FeedsFragment.bottomSheetBehavior;

public class Home extends AppCompatActivity {

    private static final String TAG = Home.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String CurntStatus;
    MaterialSpinner spinner;
    public static Toolbar toolbar;
    public static RelativeLayout mHomeToolbarLayout, mDetailsToolbarLayout;
    private static final String[] LOCATION = {
            "Dhaka, Banani", "Dhaka, Mirpur", "Dhaka, Gulshan"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.home);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#de4256"));


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.account_tab_selector));
        tabLayout.addTab(tabLayout.newTab().setText("Home").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab_selector,null)));
        tabLayout.addTab(tabLayout.newTab().setText("Saved").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.bookmark_tab_selector,null)));
        tabLayout.addTab(tabLayout.newTab().setText("History").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.history_tab_selector,null)));
        tabLayout.addTab(tabLayout.newTab().setText("Account").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.account_tab_selector,null)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mHomeToolbarLayout = (RelativeLayout)findViewById(R.id.home_toolbar_layout);
        mDetailsToolbarLayout = (RelativeLayout)findViewById(R.id.details_toolbar_layout);
        ImageView backButton = (ImageView)findViewById(R.id.back_arrow);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    toolbar.setBackgroundColor(Home.this.getColor(R.color.white));
                }
                mHomeToolbarLayout.setVisibility(View.VISIBLE);
                mDetailsToolbarLayout.setVisibility(View.GONE);
            }
        });

        // Location Spinner
        spinner = (MaterialSpinner) findViewById(R.id.location_spinner);
        spinner.setItems(LOCATION);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

            }
        });
        spinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override public void onNothingSelected(MaterialSpinner spinner) {

            }
        });

        // initialize view pager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);         // for smooth transition between tabs
        // initialize view pager adapter and setting that adapter
        final PagerAdapter adapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        // view pager listener
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // tab listener
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // get the position which tab is selected
                viewPager.setCurrentItem(tab.getPosition());
                int Status = tab.getPosition();
                CurntStatus = String.valueOf(Status);
                Log.d("Home: ", CurntStatus);
                if (CurntStatus.equals("0")||CurntStatus.equals("1")||CurntStatus.equals("2")||CurntStatus.equals("3")){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        toolbar.setBackgroundColor(Home.this.getColor(R.color.white));
                    }
                    mHomeToolbarLayout.setVisibility(View.VISIBLE);
                    mDetailsToolbarLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // get the position which tab is unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // get the position which tab is reselected
            }
        });

    }

    // view pager adapter class
    class PageAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PageAdapter(FragmentManager fm, int numTabs) {
            super(fm);
            this.mNumOfTabs = numTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    FeedsFragment ItemOneFragment = new FeedsFragment();
                    return ItemOneFragment;
                case 1:
                    BookmarkFragment ItemTwoFragment = new BookmarkFragment();
                    return ItemTwoFragment;
                case 2:
                    HistoryFragment ItemThreeFragment = new HistoryFragment();
                    return ItemThreeFragment;
                case 3:
                    AccountFragment ItemFourFragment = new AccountFragment();
                    return ItemFourFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

}
