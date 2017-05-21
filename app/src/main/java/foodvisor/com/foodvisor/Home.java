package foodvisor.com.foodvisor;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class Home extends AppCompatActivity {

    private static final String TAG = Home.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String CurntStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.home);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#de4256"));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home_tab_selector));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.bookmark_tab_selector));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.history_tab_selector));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.account_tab_selector));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

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
