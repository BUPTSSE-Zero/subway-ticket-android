package cn.crepusculo.subway_ticket_android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.ui.adapter.PositionTranslationAdapter;
import cn.crepusculo.subway_ticket_android.ui.fragment.orders.TicketHistoryFragment;
import cn.crepusculo.subway_ticket_android.ui.fragment.orders.TicketNotExtractFragment;
import cn.crepusculo.subway_ticket_android.ui.fragment.orders.TicketNotPayFragment;

/**
 * Including three fragment
 *
 * @see cn.crepusculo.subway_ticket_android.ui.fragment.orders.TicketHistoryFragment
 * @see TicketNotPayFragment
 * @see TicketNotExtractFragment
 */
public class TicketManagerActivity extends BaseActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TicketNotExtractFragment ticketNotExtractFragment;
    private TicketNotPayFragment ticketNotPayFragment;
    private TicketHistoryFragment ticketHistoryFragment;
    private boolean refreshNotPay = false;
    private boolean refreshNotExtract = false;
    private boolean refreshHistory = false;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_ticket;
    }

    @Override
    protected void initView() {
        ticketNotExtractFragment = new TicketNotExtractFragment();
        ticketNotPayFragment = new TicketNotPayFragment();
        ticketHistoryFragment = new TicketHistoryFragment();

        initToolbar();
        initTabLayout();
        initViewPager();
        checkPagePosition();
    }

    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void initTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
    }

    public void setRefreshNotExtract(boolean refreshNotExtract) {
        this.refreshNotExtract = refreshNotExtract;
    }

    public void setRefreshNotPay(boolean refreshNotPay) {
        this.refreshNotPay = refreshNotPay;
    }

    public void setRefreshHistory(boolean refreshHistory) {
        this.refreshHistory = refreshHistory;
    }

    protected void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), 3);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        if(refreshNotExtract) {
                            ticketNotExtractFragment.refreshDataFromServer();
                            refreshNotExtract = false;
                        }
                        break;
                    case 1:
                        if(refreshNotPay) {
                            ticketNotPayFragment.refreshDataFromServer();
                            refreshNotPay = false;
                        }
                        break;
                    case 2:
                        if(refreshHistory) {
                            ticketHistoryFragment.refreshDataFromServer();
                            refreshHistory = false;
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    protected void checkPagePosition(){
        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        int position = bundle.getInt("TYPE",0);
        /* Correspond id type */
        position = PositionTranslationAdapter.DrawerIdtoTablayoutId(position);
        Log.e("Bundle in Ticket",""+position);

        tabLayout.setScrollPosition(position,0f,true);
        viewPager.setCurrentItem(position);
    }


    private class PagerAdapter extends FragmentPagerAdapter {
        private int numOfTabs;

        public PagerAdapter(FragmentManager fm, int numOfTabs){
            super(fm);
            this.numOfTabs = numOfTabs;
        }

        @Override
        public Fragment getItem(int position){
            switch (position) {
                case 0:
                    refreshNotExtract = false;
                    return ticketNotExtractFragment;
                case 1:
                    refreshNotPay = true;
                    return ticketNotPayFragment;
                case 2:
                    refreshHistory = true;
                    return ticketHistoryFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return numOfTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return getString(R.string.ticket_tab1);
                case 1:
                    return getString(R.string.ticket_tab2);
                case 2:
                    return getString(R.string.ticket_tab3);
            }
            return null;
        }
    }
}
